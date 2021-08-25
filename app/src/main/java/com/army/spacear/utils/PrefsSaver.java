package com.army.spacear.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class PrefsSaver {
    
    SharedPreferences saver;
    public boolean fst, rewrite;
    public String mine, ad_id;
    public int go;
    JSONObject data;

    public PrefsSaver(Context context){
        saver = context.getSharedPreferences("FILE", Context.MODE_PRIVATE);
        unpackData();
    }

    public String getData(){
        return saver.getString("SETTINGS", "{\"first_st\":\"true\",\"rewrite\":\"true\",\"mine\":\"\",\"go\":\"0\",\"advid\":\"\"}");
    }

    public void setData(String file){
        saver.edit().putString("SETTINGS", file).apply();
    }

    public void unpackData(){
        try{
            data = new JSONObject(getData());
            fst = data.optBoolean("first_st");
            rewrite = data.optBoolean("rewrite");
            mine = data.optString("mine");
            go = data.optInt("go");
            ad_id = data.optString("advid");
        } catch (JSONException e) {}
    }

    public void packData(boolean st, boolean rewriteing, String mine, int going, String advid){
        try {
            JSONObject packData = new JSONObject();
            packData.put("first_st", st);
            packData.put("rewrite", rewriteing);
            packData.put("mine", mine);
            packData.put("go", going);
            packData.put("advid", advid);
            setData(packData.toString());
        }catch (JSONException e){}
    }

    public boolean isFirstRef() {
        return fst;
    }

    public void setFirstRef(boolean firstRef){
        this.fst = firstRef;
        packData(firstRef, rewrite, mine, go, ad_id);
    }

    public boolean isRewrite() {
        return rewrite;
    }

    public void setRewrite(boolean rewrite){
        this.rewrite = rewrite;
        packData(fst, rewrite, mine, go, ad_id);
    }

    public String getMine() {
        return mine;
    }

    public void setMine(String mine){
        this.mine = mine;
        packData(fst, rewrite, mine, go, ad_id);
    }

    public int getGo(){
        unpackData();
        return go;
    }

    public void setGo(int go){
        this.go = go;
        packData(fst, rewrite, mine, go, ad_id);
    }

    public String getAdId() {
        return ad_id;
    }

    public void setAdId(String ad_id){
        this.ad_id = ad_id;
        packData(fst, rewrite, mine, go, ad_id);
    }
}
