package com.example.todocallbacksdemo.interfaces;

import android.content.Context;
//this interface acting as bridge between
public interface ItemClickInterface {
    void deleteitem(int position);
    void showToast();

}
