package com.sczn.wearlauncher.db.bean;

import com.sczn.wearlauncher.util.StringUtils;


/**
 * Created by wuzhiyi on 2016/1/19.
 */
public class SportsStepCountInfo {
    private long getTime;
    private long stepCount;
    private double distance;
    private double kcal;

    public SportsStepCountInfo(){

    }

    public long getStepCount() {
        return stepCount;
    }

    public void setStepCount(long stepCount) {
        this.stepCount = stepCount;
    }

    public long getGetTime() {
        return getTime;
    }

    public void setGetTime(long getTime) {
        this.getTime = getTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public String toString(){
        return StringUtils.getStrDateTime(getTime)+"|"+String.valueOf(stepCount)+"|"+String.valueOf(distance)+"|"+String.valueOf(kcal);
    }
}
