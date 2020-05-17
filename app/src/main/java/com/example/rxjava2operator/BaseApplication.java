package com.example.rxjava2operator;

import android.app.Application;
import android.widget.Toast;

public class BaseApplication extends Application {

    private static String baseUrl;
    private static BaseApplication mInstance = null;
    private Toast mToast;

    public static BaseApplication getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("Application is not created.");
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
