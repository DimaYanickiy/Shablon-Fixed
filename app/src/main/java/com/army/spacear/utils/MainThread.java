package com.army.spacear.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.onesignal.OneSignal;

public class MainThread extends Thread{

    Checker checker;
    PrefsSaver prefs;
    Context context;

    public MainThread(Context context){
        this.context = context;
        prefs = new PrefsSaver(context);
        checker = new Checker(context);
        checker.start();
    }

    @Override
    public void run() {
        if(prefs.isFirstRef()){
            ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
            if(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()){
                while(checker.isAlive()){
                    try {
                        Thread.sleep(1000);
                        Log.e("kkk", "....");
                    } catch (InterruptedException e) {
                    }
                }
                if(checker.dev || !checker.loc || (checker.plugged && (checker.batLvl == 90 || checker.batLvl == 100))){
                    prefs.setGo(2);
                    prefs.setFirstRef(false);
                    Log.e("kkk", "bot");
                } else {
                    OneSignal.initWithContext(context);
                    OneSignal.setAppId("bc1d7fc4-3c35-440f-9a1c-ecb99eaa8fba");
                    FacebookSdk.setAutoInitEnabled(true);
                    FacebookSdk.fullyInitialize();
                    AppsflyerConnection apps = new AppsflyerConnection(context);
                    apps.start();
                    Log.e("kkk", "appsConection");
                }
            }else{
                prefs.setGo(2);
            }
        } else{
            if(prefs.getMine().equals("") || prefs.getMine().isEmpty()){
                prefs.setGo(2);
            } else {
                prefs.setGo(1);
            }
        }
    }


}
