package com.example.todocallbacksdemo.wifimanager.service;

import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.todocallbacksdemo.wifimanager.model.ClientThread;
import com.example.todocallbacksdemo.wifimanager.model.ServerThread;

import java.net.InetAddress;

public class ConnectionInfoListener implements WifiP2pManager.ConnectionInfoListener {
    InetAddress inetAddress;
    ServerThread serverThread;
    ClientThread clientThread;
    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
        final InetAddress groupOwnerAddress=wifiP2pInfo.groupOwnerAddress;
        inetAddress=groupOwnerAddress;
        if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner){
            // deviceConnectionStatus.setText("host");
            Log.d("NOW", "host"); // 1 TIME
            //thread class instantiated and started
            //start_server=true;
            serverThread=new ServerThread();
            Thread thread = new Thread(serverThread);
            thread.start();
            //btnSocket.setText("Start server");
        }
        else if(wifiP2pInfo.groupFormed){
            //deviceConnectionStatus.setText("client");
            Log.d("NOW", "client"); ///1 TIME
            //connect_server=true;
            clientThread=new ClientThread(inetAddress);
            Thread thread1=new Thread(clientThread);
            thread1.start();
           // btnSocket.setText("Connect server");

        }
    }
}
