package com.example.todocallbacksdemo.wifimanager.utils;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;

import static android.content.Context.WIFI_SERVICE;
//copied
public class WiFiP2PManagerUtil {
    private static WiFiP2PManagerUtil INSTANCE;
    private WifiP2pManager mWiFiP2PManager;
    private WifiManager mWifiManager;
    private WifiP2pManager.Channel mChannel;

    private WiFiP2PManagerUtil(Application application) {
        mWiFiP2PManager = (WifiP2pManager) application
                .getSystemService(Context.WIFI_P2P_SERVICE);

        mChannel = mWiFiP2PManager
                .initialize(application, Looper.getMainLooper(), null);

        mWifiManager = (WifiManager) application.getSystemService(WIFI_SERVICE);
    }

    public static WiFiP2PManagerUtil getInstance() {
        return INSTANCE;
    }

    public static void init(Application application) {
        INSTANCE = new WiFiP2PManagerUtil(application);
    }

    public WifiManager getWifiManager() {
        return mWifiManager;
    }

    public WifiP2pManager wifiP2PManager() {
        return mWiFiP2PManager;
    }

    public WifiP2pManager.Channel getChannel() {
        return mChannel;
    }
}

