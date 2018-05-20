package tv.hooq.sampleapp;

import android.app.Application;

public class SampleApplication extends Application {

    private static SampleApplication instance;

    public static SampleApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SampleApplication.instance = this;
    }
}
