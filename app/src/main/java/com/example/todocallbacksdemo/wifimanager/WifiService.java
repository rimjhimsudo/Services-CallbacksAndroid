package com.example.todocallbacksdemo.wifimanager;



import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todocallbacksdemo.Extraclass;

import java.util.Random;

// generates random number every 1 sec in service and returns random number when invoked by activity
public class WifiService extends Service implements Extraclass.MyListener{
    //it is the method that starts whenevr sevice starts
    //service runs on same thread by default (proof: i got same thread id in logs)
    private int randomNum;
    private boolean isRandomGeneratorOn;
    private final int MIN=0;
    private final int MAX=100;
    //
    Extraclass extraclass;

    @Override
    public void getnums(int number) {
        Log.d("NUMBER","reached");
    }

    //to return  Service instance we neeed IBinder
    //implement IBinder or extend Binder(abstract class) is same
    class MyWifiServiceBinder extends Binder{
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


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        extraclass=new Extraclass(this);
        extraclass.hello();
        //doing it on different thrrrad as service runs on same thread as activity so to avoid "app not responding"
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomGenNum();
            }
        }).start();
        //stop self or stop sevice frrom somewhere - IMPORTANT
        return START_STICKY;
        //means yes auto restart and no intent delivery ??
        //return super.onStartCommand(intent, flags, startId);
    }

    //start random generator method
    private void startRandomGenNum(){
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
    public int getRandomNum(){
        return randomNum;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomGenNum(); //if not called then just destroy app and service stops
        Log.d("Line29","destroyed");

    }

}

// 2 part implementations
/*
service needs to impemnt the OnBind() method and returns IBinder instance-for communicating data from service to activity
activity needs to use ServiceconnectionAPi-

 */
