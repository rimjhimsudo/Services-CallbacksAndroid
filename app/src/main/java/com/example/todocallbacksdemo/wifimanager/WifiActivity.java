package com.example.todocallbacksdemo.wifimanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.todocallbacksdemo.R;
import com.example.todocallbacksdemo.wifimanager.service.WifiService;
import com.example.todocallbacksdemo.wifimanager.utils.WiFiP2PManagerUtil;

import java.util.ArrayList;
import java.util.List;


/*
NOTE :
this activity is demo for service implementation
 */
public class WifiActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent serviceIntent;
    private WifiService mywifiService;
    private boolean isServiceBound; //to check whther service is bound or not
    private ServiceConnection serviceConnection; //needs to be initialsed and this happens on line 53
    List<WifiP2pDevice> peers=new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] devicesArray;
    ListView devicesList;
    WiFiP2PManagerUtil wiFiP2PManagerUtil;


    //views
    Button btnGen , btnBoundService, btnUnboundService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        Log.d("Line15","onCreate in mainact2 : "+Thread.currentThread().getId());

        //views
        btnGen=findViewById(R.id.btn_generatenum);
        btnBoundService=findViewById(R.id.btn_bound);
        btnUnboundService=findViewById(R.id.btn_unbound);
        btnGen.setOnClickListener(this);
        btnUnboundService.setOnClickListener(this);
        btnBoundService.setOnClickListener(this);
        devicesList=findViewById(R.id.listview);
        setclickitem();

        //android recommends always creating explcit intents
        serviceIntent=new Intent(getApplicationContext(),WifiService.class);
        serviceIntent.putExtra("hello",1);
        //later put it onclick of button
        //startService(serviceIntent);
        startForegroundService(serviceIntent); // now for foregrouung create notification
        // stopService(serviceIntent);

    }

    private void setclickitem() {
        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mywifiService.setitemDevice(devicesArray[i]);
               /* final WifiP2pDevice itemDevice=devicesArray[i];
                WifiP2pConfig config=new WifiP2pConfig();
                config.deviceAddress=itemDevice.deviceAddress;
                if(ActivityCompat.checkSelfPermission(WifiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    wiFiP2PManagerUtil.wifiP2PManager().connect(wiFiP2PManagerUtil.getChannel(), config, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            Log.d("TAGTAG1","aagaya");
                            Toast.makeText(getApplicationContext(),"device connnected to"+itemDevice.deviceName,Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int i) {
                            Toast.makeText(getApplicationContext(),"device disconnected",Toast.LENGTH_LONG).show();
                        }
                    });
                }*/
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_generatenum : getpeerList();
                break;
            case R.id.btn_bound : bindService();
                break;
            case R.id.btn_unbound : unbindservice();
                break;

        }
    }

    private void bindService(){
        if(serviceConnection==null){
            serviceConnection=new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    //now we got service instance in IBinder so we initilaised myservice here using IBinder
                    WifiService.MyWifiServiceBinder mywifiServiceBinder= (WifiService.MyWifiServiceBinder) iBinder;
                    mywifiService= mywifiServiceBinder.getService(); //myService got initialised
                    wiFiP2PManagerUtil=mywifiService.getmWiFiP2PManagerUtil();
                    isServiceBound=true;
                    Log.d("Line64","OnserviceConnected"+isServiceBound);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound=false;
                }
            };
        }
        //now bindservice
        bindService(serviceIntent,serviceConnection, Context.BIND_AUTO_CREATE); //if  service is not created then it creates and bind

    }
    private  void unbindservice(){
        if(isServiceBound){
            unbindService(serviceConnection);
            isServiceBound=false;
            Log.d("Line64","unbound"+isServiceBound);

        }
    }
    private  void getpeerList(){
        WifiP2pDeviceList peerList;
        if(isServiceBound){
            peerList =mywifiService.getmPeerList();
            Toast.makeText(this,""+peerList,Toast.LENGTH_LONG).show(); //got the list,hurray
            //peers=peerList.getDeviceList();
            if(peerList==null){
                Log.d("TAGTAG","line156");
                return;
            }
            if(!peerList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(peerList.getDeviceList());
                deviceNameArray=new String[peerList.getDeviceList().size()];
                devicesArray=new WifiP2pDevice[peerList.getDeviceList().size()];
                int index=0;
                for(WifiP2pDevice device: peerList.getDeviceList()){
                    deviceNameArray[index]=device.deviceName;
                    devicesArray[index]=device;
                    index++;
                }
                ArrayAdapter<String> peeradapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceNameArray);
                devicesList.setAdapter(peeradapter);

            }
        }
        else{ Toast.makeText(this,"Services in not bound",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceIntent);
    }
}
