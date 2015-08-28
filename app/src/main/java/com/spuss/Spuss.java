package com.spuss;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;


public class Spuss extends Application {

    Intent intent;

    public static final String[] logTypesText = {"Activity at home", "Dangerous activity",
            "ALARM!!!", "SPUSS Module error","SPUSS ARMED"};

    public static final int[] logTypesIcon = {R.drawable.log1, R.drawable.log2,
            R.drawable.log3, R.drawable.log4,  R.drawable.log5};

    Context context;

    String TAG = "Application";

    private static Spuss singleton;


    public Spuss getInstance(){
        return singleton;
    }
    public Spuss(){}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        Log.e(TAG, "OnCreate");
        intent = new Intent(this, SpussService.class);
        startService(intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        stopService(intent);
        super.onTerminate();
    }

}
