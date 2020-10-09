package com.example.todocallbacksdemo;

public class Extraclass  {
    int number=20;
    private MyListener myListener;
    public interface MyListener{
        void getnums(int number);
    }
    public Extraclass(MyListener myListener){
        this.myListener=myListener;
    }
    public void hello(){
        myListener.getnums(number);
    }

}
