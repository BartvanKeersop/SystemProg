package com.example.bartvankeersop.lifecycledemo;

import android.app.Application;

/**
 * Created by Bart van Keersop on 11/09/2017.
 */

public class CustomApplication extends Application {

    private static CustomApplication singleton;
    public String message;

    public void setMessage(String value){
         message = value;
    }

    public String getMessage(){
        return message;
    }
    public static CustomApplication getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        message = "";
    }
}
