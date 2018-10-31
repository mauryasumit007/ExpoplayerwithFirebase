package com.example.sumitmaurya.mobiotisktask.others;

import android.app.Activity;
import android.app.Application;

import com.example.sumitmaurya.mobiotisktask.models.Output;

import java.util.ArrayList;
import java.util.List;

public class MobiosticApp extends Application {

    private static MobiosticApp CONTEXT;
    private Activity mCurrentActivity = null;


    public List<Output> getSingletonResponse() {
        return SingletonResponse;
    }

    public void setSingletonResponse(List<Output> singletonResponse) {
        SingletonResponse = singletonResponse;
    }

    List<Output> SingletonResponse = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

    }

    public static MobiosticApp getInstance() {
        return CONTEXT;
    }
}
