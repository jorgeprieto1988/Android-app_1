package com.appadia.beacon.app;

/**
 * Created by Jorge on 29/03/2016.
 */
import android.app.Application;


import com.appadia.beacon.helper.MyPreferenceManager;

//Need to be added inside the manifest
public class MyApplication extends Application{
    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;

    private MyPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }

}
