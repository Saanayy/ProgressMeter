package com.bytebucket1111.progressmeter;

import android.app.Application;

import com.onesignal.OneSignal;

public class ApplicationClass extends Application {
//Class for OneSignal notifications
    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: Add OneSignal initialization here
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
