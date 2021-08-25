package com.army.spacear.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimeZone;

public class Checker extends Thread{

    Context context;

    public int finished = 0;
    public int batLvl;
    public boolean plugged;
    public boolean dev;
    public boolean loc;

    public Checker(Context context){
        this.context = context;
    }

    public void isPhonePlugged(){
        final Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharge = status==BatteryManager.BATTERY_STATUS_CHARGING;
        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        plugged = batteryCharge || usbCharge || acCharge;
        Log.e("kkk", "plugged");
        finished++;
    }

    public void isDev(){
        dev = android.provider.Settings.Secure.getInt(context.getContentResolver(),
                android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0) != 0;
        Log.e("kkk", "developer");
        finished++;
    }

    public void battaryLevel(){
        BatteryManager bm = (BatteryManager)context.getSystemService(context.BATTERY_SERVICE);
        batLvl = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.e("kkk", "batLvl");
        finished++;
    }

    public void getLocale(){
        try {
            URLConnection url = new URL("http://ip-api.com/json/").openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String resp;

            while ((resp = in.readLine()) != null)
                sb.append(resp);

            JSONObject jsonObject = new JSONObject(sb.toString());

            String timezoneInet = jsonObject.optString("timezone");
            String timezoneLocsl = TimeZone.getDefault().getID();

            loc = timezoneInet.equals(timezoneLocsl);

            in.close();
        } catch (Exception e) {
            loc = false;
        }
        Log.e("kkk", "timezone");
        finished++;
    }

    @Override
    public void run() {
        getLocale();
        isDev();
        battaryLevel();
        isPhonePlugged();
        while(finished != 4){
            try {
                Thread.sleep(1000);
                Log.e("kkk", "sleep..................");
            } catch (InterruptedException e) {
            }
        }

    }
}
