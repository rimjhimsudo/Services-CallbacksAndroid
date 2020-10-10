package com.example.todocallbacksdemo.wifimanager;

import android.app.Application;

import com.example.todocallbacksdemo.wifimanager.utils.WiFiP2PManagerUtil;

//copied
public class WiFiApplication
        extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WiFiP2PManagerUtil.init(this); //object initialised after creation
    }
}

/*
IMPORTANT
https://github.com/sowmen/Android-P2P-Chat-Messenger-using-Java-TCP-IP-Socket-Programming/tree/master/app/src/main/java/com/example/chatfull
 */

