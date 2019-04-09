package com.sf.DarkCalculator;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

public class MyApplicaiton extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this,"p2HxRyJcAscfVindyhAGDlyp-gzGzoHsz", "xwVfjHMXbtzIyReqVUF4Dswt");
        AVOSCloud.setDebugLogEnabled(true);
        AVAnalytics.enableCrashReport(this, true);
    }
}
