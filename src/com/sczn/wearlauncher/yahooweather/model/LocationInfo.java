package com.sczn.wearlauncher.yahooweather.model;

/**
 * 接口内的定位城市信息
 * Created by mxy on 2016/10/24.
 */
public class LocationInfo {
    private String city;
    private String country;
    private String region;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}

