package com.sczn.wearlauncher.model;

import android.graphics.drawable.Drawable;

/**
 * Created by wuzhiyi on 2016/2/3.
 */
public class SnowInfo {
    private Drawable drawable;
    private float scale;
    private int x;
    private float y;
    private float speed;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
