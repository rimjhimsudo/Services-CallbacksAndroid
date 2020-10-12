package com.example.todocallbacksdemo.wifimanager.service;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WifiPeersListener implements WifiP2pManager.PeerListListener {
    private static final String TAG = "WiFiPeersListener";
    private List<WifiP2pDevice> oldPeerList;
    private MyListListener1 listener;
    //interface
    public interface MyListListener1 {
        void onRetrievelist(WifiP2pDeviceList peerslist);
    }

   /* WifiPeersListener(List<WifiP2pDevice> peers, MyListListener1 listener){
        oldPeerList = new ArrayList<>(peers);
        this.listener=listener;
    }*/
   public WifiPeersListener( MyListListener1 listener){
       //oldPeerList = new ArrayList<>(peers);
       this.listener=listener;
       Log.d(TAG,"inside constructor");
   }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerslist) {
        if(peerslist.getDeviceList().size() == 0){
            Log.d(TAG, "No devices found");
        }
        if (!peerslist.getDeviceList().equals(oldPeerList) && peerslist!=null) {
            //Log.d("line33",""+oldPeerList.size()+"peerlist size : "+peerslist.getDeviceList().size());
            Log.d("line33",""+"peerlist size : "+peerslist.getDeviceList().size());
           // oldPeerList.clear();
           // oldPeerList.addAll(peerslist.getDeviceList());
            listener.onRetrievelist(peerslist);
            //oldPeerList=peerslist.getDeviceList();
			/*WifiP2pDevice deviceOne=oldPeerList.get(0);
			WifiP2pConfig config=new WifiP2pConfig();
			config.deviceAddress=deviceOne.deviceAddress;
			config.wps.setup= WpsInfo.PBC;
			Log.d("INSIDE",""+deviceOne.deviceName +"config"+config.wps+config.deviceAddress);*/
        }

    }
}

