package com.nimbl3.having.exchange.ui;

import android.app.Application;

public class HavingApplication extends Application {
    private static HavingApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static HavingApplication getInstance() {
        return mInstance;
    }
}
