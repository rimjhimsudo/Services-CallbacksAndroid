package com.example.todocallbacksdemo.wifimanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.todocallbacksdemo.R;
import com.example.todocallbacksdemo.wifimanager.service.WifiService;


/*
NOTE :
this activity is demo for service implementation
 */
public class WifiActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent serviceIntent;
    private WifiService mywifiService;
    private boolean isServiceBound; //to check whther service is bound or not
    private ServiceConnection serviceConnection; //needs to be initialsed and this happens on line 53


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

        //android recommends always creating explcit intents
        serviceIntent=new Intent(getApplicationContext(),WifiService.class);
        serviceIntent.putExtra("hello",1);
        //later put it onclick of button
        //startService(serviceIntent);
        startForegroundService(serviceIntent); // now for foregrouung create notification
        // stopService(serviceIntent);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_generatenum : getandShowRandomNum();
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
    private  void getandShowRandomNum(){
        if(isServiceBound){
            int num=mywifiService.getRandomNum();
            Toast.makeText(this,"number is : "+num,Toast.LENGTH_LONG).show();
        }
        else{ Toast.makeText(this,"Services in not bound",Toast.LENGTH_LONG).show();
        }
    }

}
