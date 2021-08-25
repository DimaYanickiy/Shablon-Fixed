package com.army.spacear.utils;

public class BuildPoint {

    private String mainPoint;
    public String destination(String destinationPoint, String campaign, String appsflyerUID, String gid) {
        mainPoint = destinationPoint
                + "?nmg=" + campaign
                + "&dv_id=" + appsflyerUID
                + "&avr=" + gid;
        return mainPoint;
    }
}
