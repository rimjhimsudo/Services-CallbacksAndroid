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
    ClientInterface clientInterface;
    ServerInterface serverInterface;
    public ConnectionInfoListener(ServerInterface serverInterface,ClientInterface clientInterface){
        this.serverInterface=serverInterface;
        this.clientInterface=clientInterface;
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
        final InetAddress groupOwnerAddress=wifiP2pInfo.groupOwnerAddress;
        inetAddress=groupOwnerAddress;
        if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner){
            // deviceConnectionStatus.setText("host");
            //thread class instantiated and started
            //start_server=true;
            serverThread=new ServerThread();
            serverInterface.setServerThread(serverThread);
            Log.d("COOL", "host"+serverThread); // 1 TIME
            Thread thread = new Thread(serverThread);
            thread.start();
            //serverThread.sendMessage("whswhswhj"); //not getting called here
            //btnSocket.setText("Start server");
        }
        else if(wifiP2pInfo.groupFormed){
            //deviceConnectionStatus.setText("client");
            //connect_server=true;
            clientThread=new ClientThread(inetAddress);
            clientInterface.setClientThread(clientThread);
            Log.d("NOW", "client"+clientThread);
            Thread thread1=new Thread(clientThread);
            thread1.start();
            //clientThread.sendMessage("qwertyyyyyyy");
           // btnSocket.setText("Connect server");

        }
    }
    public interface ClientInterface {
        public void setClientThread(ClientThread clientThread);
    }
    public interface ServerInterface {
        public void setServerThread(ServerThread serverThread);
    }
}
