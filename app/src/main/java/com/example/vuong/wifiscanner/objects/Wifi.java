package com.example.vuong.wifiscanner.objects;

/**
 * Created by vuong on 22/01/2018.
 */

public class Wifi {

    private String name = "";
    private String ssid = "";
    private boolean isConnected = false;

    public Wifi(String name, String ssid, boolean isConnected) {
        this.name = name;
        this.ssid = ssid;
        this.isConnected = isConnected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
