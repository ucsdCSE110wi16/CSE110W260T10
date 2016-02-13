package com.cse110.app;

import android.app.Application;
import com.parse.Parse;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "pQv3yaW2Dkl8x22xNO2Kam0iAloBUZKSRjPsSWuq", "kVJE6SqJt3zPlho2p66NEYWwSBBIfLfdDyonolSH");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}