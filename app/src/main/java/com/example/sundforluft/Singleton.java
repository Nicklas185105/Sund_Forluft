package com.example.sundforluft;

import android.app.Application;

public class Singleton extends Application {
    //constructor
    private Singleton() {

    }

    private static Singleton mSingleton;

    public static Singleton get(){
        if(mSingleton ==  null){
            mSingleton = new Singleton();
        }
        return mSingleton;
    }
}
