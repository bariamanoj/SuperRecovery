package com.slbdeveloper.superrecovery;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.facebook.ads.AudienceNetworkAds;

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    public SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AudienceNetworkAds.initialize(this);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

}
