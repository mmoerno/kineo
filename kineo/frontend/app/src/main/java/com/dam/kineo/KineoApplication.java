package com.dam.kineo;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class KineoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
