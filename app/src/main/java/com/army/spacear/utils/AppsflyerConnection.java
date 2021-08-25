package com.army.spacear.utils;

import android.content.Context;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.Map;

public class AppsflyerConnection extends Thread{

    Context context;
    PrefsSaver prefs;
    BuildPoint bp;
    AdsLoader ads;

    public AppsflyerConnection(Context context){
        this.context = context;
        prefs = new PrefsSaver(context);
        ads = new AdsLoader(context);
        ads.start();
    }

    @Override
    public void run() {
        while(ads.isAlive()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        AppsFlyerLib.getInstance().init("55cZ5t6haW6R65hVg47MwD", new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                FirebaseRemoteConfig frm = FirebaseRemoteConfig.getInstance();
                FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(3600)
                        .build();
                frm.setConfigSettingsAsync(configSettings);
                frm.fetchAndActivate().addOnCompleteListener(task -> {
                    try {
                        String fireString = frm.getString("manage");
                        JSONObject params = new JSONObject(fireString);
                        JSONObject jsonObject = new JSONObject(conversionData);

                        String campaign = getCampaign(jsonObject);
                        try {
                            OneSignal.sendTag("g_id", prefs.getAdId());
                        } catch (Exception e) {
                        }
                        bp = new BuildPoint();
                        prefs.setMine(bp.destination(params.optString("808tak"),
                                campaign,
                                AppsFlyerLib.getInstance().getAppsFlyerUID(context.getApplicationContext()),
                                prefs.getAdId()));
                        prefs.setGo(1);
                        prefs.setFirstRef(false);
                        AppsFlyerLib.getInstance().unregisterConversionListener();
                    } catch (Exception ex) {
                        prefs.setGo(2);
                        AppsFlyerLib.getInstance().unregisterConversionListener();
                    }
                }).addOnFailureListener(e -> {
                    prefs.setGo(2);
                });
            }
            @Override
            public void onConversionDataFail(String s) {
                prefs.setGo(2);
            }
            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }
            @Override
            public void onAttributionFailure(String s) {

            }
        }, context);
        AppsFlyerLib.getInstance().start(context);
        AppsFlyerLib.getInstance().enableFacebookDeferredApplinks(true);
    }

    private String getCampaign(JSONObject obj){
        String camp = obj.optString("campaign");
        if (camp.isEmpty() || camp.equals("null")) {
            camp = obj.optString("c");
        }
        String[] splitsCampaign = camp.split("_");
        try {
            OneSignal.sendTag("user_id", splitsCampaign[2]);
        } catch (Exception e) {}
        return camp;
    }
}
