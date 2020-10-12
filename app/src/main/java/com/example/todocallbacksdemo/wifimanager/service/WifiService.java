package com.example.todocallbacksdemo.wifimanager.service;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.todocallbacksdemo.Extraclass;
import com.example.todocallbacksdemo.R;
import com.example.todocallbacksdemo.wifimanager.broadcast.WifiDirectBroadcast;
import com.example.todocallbacksdemo.wifimanager.utils.WiFiP2PManagerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// generates random number every 1 sec in service and returns random number when invoked by activity
public class WifiService extends Service implements WifiPeersListener.MyListListener1{
    private static final String TAG = WifiService.class.getSimpleName()+"TAG";
    //it is the method that starts whenevr sevice starts
    //service runs on same thread by default (proof: i got same thread id in logs)
    private int randomNum;
    private boolean isRandomGeneratorOn;
    private final int MIN=0;
    private final int MAX=100;
    //

    private static final String CHANNEL_ID = "1000";
    private WiFiP2PManagerUtil mWiFiP2PManagerUtil;
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;
    private WifiP2pDeviceList mPeerList;


   @Override
   public void onRetrievelist(WifiP2pDeviceList peerslist) {
        this.mPeerList=peerslist;
        Log.d(TAG,"inside onRetrievelist ");
       Log.d(TAG, "" + peerslist.getDeviceList().size());
       /*if(peerslist.size()!=0){
           for(WifiP2pDevice device: peerslist){
               Log.d(TAG,"deviceAddress :"+device.deviceAddress+"deviceName : "+device.deviceName);
           }
       }*/

   }

    //to return  Service instance we neeed IBinder
    //implement IBinder or extend Binder(abstract class) is same
    public class MyWifiServiceBinder extends Binder{
        public WifiService getService(){
            return WifiService.this;
        }
    }
    private IBinder iBinder=new MyWifiServiceBinder();
    //comes in act when we make boundservice
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        mWiFiP2PManagerUtil = WiFiP2PManagerUtil.getInstance();
        mWiFiP2PManagerUtil.getWifiManager().setWifiEnabled(true);
        broadcastReceiver=new WifiDirectBroadcast(mWiFiP2PManagerUtil,this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("WiFi Direct")
                        .setContentText("P2P connection service running")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .build();
        Log.d("Line14","in start command"+Thread.currentThread().getId());

        // stopSelf(); //one way of stopping service
        //another is to invoke stopService() froom other component
        //start of service
        isRandomGeneratorOn=true;
        //code for getting extra from intent
        //int num=intent.hasExtra("hello");
        int num=intent.getIntExtra("hello",0);
        Log.d("Line55",""+"intent extra "+num);
        //code for interface implementation
        //doing it on different thrrrad as service runs on same thread as activity so to avoid "app not responding"
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomGenNum();
            }
        }).start();
        //stop self or stop sevice from somewhere - IMPORTANT
        startForeground(1, notification);
        registerReceiver(broadcastReceiver, intentFilter);
        //mPeerList= new ArrayList<>();
        //wifiPeersListener=new WifiPeersListener(mPeerList,this);
        return START_STICKY; //means yes auto restart and no intent delivery
        //return super.onStartCommand(intent, flags, startId);
    }



    //start random generator method
    public void startRandomGenNum(){
        while(isRandomGeneratorOn){
            try {
                Thread.sleep(1000); //so that it gnearted after 1 sec and we slow down random numbers  generation
                randomNum=new Random().nextInt(MAX)+MIN;
                Log.d("Line41","inside service"+randomNum+"thread id : "+Thread.currentThread().getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    private void stopRandomGenNum(){
        isRandomGeneratorOn=false;
    }
    //methods return randomenum to activity
    /*public int getRandomNum(){
        return randomNum;
    }*/
    public WifiP2pDeviceList getmPeerList(){
        return mPeerList;
    }
    public WiFiP2PManagerUtil getmWiFiP2PManagerUtil(){
        return mWiFiP2PManagerUtil;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomGenNum(); //if not called then just destroy app and service stops
        unregisterReceiver(broadcastReceiver);
        Log.d("Line29","destroyed");
    }

    private void createNotificationChannel(){
        NotificationChannel channel =
                new NotificationChannel(CHANNEL_ID, "WIFI Manager " + "service",
                        NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager =
                getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

}

// 2 part implementations
/*
service needs to impemnt the OnBind() method and returns IBinder instance-for communicating data from service to activity
activity needs to use ServiceconnectionAPi-

 */
