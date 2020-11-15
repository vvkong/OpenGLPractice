package com.godot.mooc;

import android.app.Application;
import android.content.Context;

/**
 * Created by AllenWang on 2020/11/15.
 */
public class App extends Application {

    public static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getApp() {
        return instance;
    }
}
