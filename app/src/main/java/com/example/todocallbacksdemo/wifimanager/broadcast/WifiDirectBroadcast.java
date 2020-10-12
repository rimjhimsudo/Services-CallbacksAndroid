package com.example.todocallbacksdemo.wifimanager.broadcast;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.todocallbacksdemo.wifimanager.service.WifiPeersListener;
import com.example.todocallbacksdemo.wifimanager.service.WifiService;
import com.example.todocallbacksdemo.wifimanager.utils.WiFiP2PManagerUtil;
//wifi direct works on radiowaves

public class WifiDirectBroadcast extends BroadcastReceiver{
    private static final String TAG = WifiDirectBroadcast.class.getSimpleName()+"TAG";
    //private static final int PERMSSION = 2;
    private WiFiP2PManagerUtil wiFiP2PManagerUtil;
    private WifiPeersListener.MyListListener1 myListListener1;
    //WifiService.PeersListenerToBroadcast peersListenerToBroadcast;
   // private MainActivity mainActivity;

    public WifiDirectBroadcast(WiFiP2PManagerUtil wiFiP2PManagerUtil,WifiPeersListener.MyListListener1 myListListener1) {
        this.wiFiP2PManagerUtil=wiFiP2PManagerUtil;
        this.myListListener1=myListListener1;
        //this.peersListenerToBroadcast=peersListenerToBroadcast;

        /*this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;*/
       // this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            Log.d("TAG_State" ,"state : "+state);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "WIFI direct is on", Toast.LENGTH_LONG).show();
                //mainActivity.wifiDirectstatus.setText("wifi direct is ON");g.d
                Log.d(TAG,"wifi direct is on");
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Needed location permission!", Toast.LENGTH_SHORT).show();
                    return;
                }
                wiFiP2PManagerUtil.wifiP2PManager().discoverPeers(wiFiP2PManagerUtil.getChannel(), new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"on success discovery called");
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.d(TAG,"on failure discovery called");
                    }
                });
            } else {
                Toast.makeText(context, "WIFI direct is off", Toast.LENGTH_LONG).show();
               // mainActivity.wifiDirectstatus.setText("wifi direct is OFF");
                Log.d(TAG,"wifi direct is off");

            }
        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (wiFiP2PManagerUtil.wifiP2PManager()!=null) {
                if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                   // wiFiP2PManagerUtil.wifiP2PManager().requestPeers(wiFiP2PManagerUtil.getChannel(), mainActivity.peerListListener);
                    Log.d(TAG,"discovering peer and wifipeers ");
                    wiFiP2PManagerUtil.wifiP2PManager().requestPeers(wiFiP2PManagerUtil.getChannel(),new WifiPeersListener(myListListener1));
                }

            }
        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            if(wiFiP2PManagerUtil.wifiP2PManager()==null){
                Log.d(TAG,"iside wifip2p manger null");
                return;
            }
            NetworkInfo networkInfo=intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            Log.d(TAG, " line no 60 : "+networkInfo.getExtraInfo()); //coming null
            Log.d(TAG, " line no 61 : "+networkInfo.isConnected());
            if(networkInfo.isConnected()){
              //  wiFiP2PManagerUtil.wifiP2PManager().requestConnectionInfo(channel,mainActivity.connectionInfoListener);
                //mainActivity.deviceConnectionStatus.setText("status changed-device connected");
                Log.d(TAG,"status changed-device connected");
            }
            else{
                //mainActivity.deviceConnectionStatus.setText("status changed-device disconnected");
                Log.d(TAG,"status changed-device disconnected");
            }

        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            //
        }

    }

    //@Override
   /* public void getListener(WifiPeersListener wifiPeersListener) {
        this.wifiPeersListener=wifiPeersListener;
    }*/
}



 /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMSSION,);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

                    return;
                }
                else{
                    wifiP2pManager.requestPeers(channel, mainActivity.peerListListener);
                }
              /*  if (ActivityCompat.checkSelfPermission(this.mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                        }
                        */


