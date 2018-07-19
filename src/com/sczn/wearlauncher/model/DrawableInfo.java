package com.sczn.wearlauncher.model;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;


/**
 * Created by wuzhiyi on 2016/1/28.
 */
public class DrawableInfo {
    private int angle;
    private int arrayType;
    private int centerX;
    private int centerY;
    private int color;
    private String colorArray;
    private int direction = 1;
    private Drawable drawable;
    private ArrayList<Drawable> drawables;
    private ArrayList<Integer> durations;
    private ArrayList<SnowInfo> snowInfos;
    private float currentAngle;
    private float rotateSpeed;
    private int duration;
    private int mulRotate = 1;
    private int rotate;
    private int startAngle;
    private int textsize = 19;
    
    private int mDrawableWidth;
    private int mDrawableHeight;
    
    public DrawableInfo(int width,int height){
    	mDrawableWidth = width;
    	mDrawableHeight = height;
    }
    
    public int getAngle(){
        return this.angle;
    }

    public int getArrayType()
    {
        return this.arrayType;
    }

    public int getCenterX()
    {
        return (int)(this.centerX * (mDrawableWidth / 400.0D)) + mDrawableWidth / 2;
    }

    public int getCenterY()
    {
        return (int)(this.centerY * (mDrawableHeight / 400.0D)) + mDrawableHeight / 2;
    }

    public int getColor()
    {
        return this.color;
    }

    public String getColorArray()
    {
        return this.colorArray;
    }

    public int getDirection()
    {
        return this.direction;
    }

    public Drawable getDrawable()
    {
        return this.drawable;
    }

    public ArrayList<Drawable> getDrawableArrays()
    {
        return this.drawables;
    }

    public ArrayList<Integer> getDurationArrays(){
        return this.durations;
    }

    public ArrayList<SnowInfo> getSnowInfos() {
        return snowInfos;
    }

    public int getMulRotate()
    {
        return this.mulRotate;
    }

    public int getRotate()
    {
        return this.rotate;
    }

    public int getStartAngle()
    {
        return this.startAngle;
    }

    public int getTextsize()
    {
        return this.textsize;
    }

    public int getDuration() {
        return duration;
    }

    public float getCurrentAngle() {
        return currentAngle;
    }

    public float getRotateSpeed() {
        return rotateSpeed;
    }

    public void setAngle(int paramInt)
    {
        this.angle = paramInt;
    }

    public void setArrayType(int paramInt)
    {
        this.arrayType = paramInt;
    }

    public void setCenterX(int paramInt)
    {
        this.centerX = paramInt;
    }

    public void setCenterY(int paramInt)
    {
        this.centerY = paramInt;
    }

    public void setColor(int paramInt)
    {
        this.color = paramInt;
    }

    public void setColorArray(String paramString)
    {
        this.colorArray = paramString;
    }

    public void setDirection(int paramInt)
    {
        this.direction = paramInt;
    }

    public void setDrawable(Drawable paramDrawable)
    {
        this.drawable = paramDrawable;
    }

    public void setDrawableArrays(ArrayList<Drawable> paramArrayList) {
        this.drawables = paramArrayList;
    }

    public void setDurationArrays(ArrayList<Integer> paramArrayList){
        this.durations = paramArrayList;
    }

    public void setSnowInfos(ArrayList<SnowInfo> snowInfos) {
        this.snowInfos = snowInfos;
    }

    public void setMulRotate(int paramInt)
    {
        this.mulRotate = paramInt;
    }

    public void setRotate(int paramInt)
    {
        this.rotate = paramInt;
    }

    public void setStartAngle(int paramInt)
    {
        this.startAngle = paramInt;
    }

    public void setTextsize(int paramInt)
    {
        this.textsize = paramInt;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCurrentAngle(float currentAngle) {
        this.currentAngle = currentAngle;
    }

    public void setRotateSpeed(float rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

}
