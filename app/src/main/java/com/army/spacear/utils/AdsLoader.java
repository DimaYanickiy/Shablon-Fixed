package com.army.spacear.utils;

import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

public class AdsLoader extends Thread{

    Context context;
    String ad_id = "";
    PrefsSaver prefs;
    public int finished = 0;

    public AdsLoader(Context context){
        this.context = context;
    }

    private void loadAd(){
        try {
            AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            ad_id = adInfo != null ? adInfo.getId() : null;
            prefs = new PrefsSaver(context);
            prefs.setAdId(ad_id);
            finished++;
        } catch (Exception ex) {
            finished++;
        }
    }

    @Override
    public void run() {
        loadAd();
        while(finished != 1){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
