package com.sczn.wearlauncher.model;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Calendar;
import java.util.List;

import com.sczn.wearlauncher.db.bean.UserInfoConstant;

/**
 * Created by wuzhiyi on 2016/1/28.
 */
public abstract class ClockSkin implements Serializable {
    public static final int SCREEN_HEIGHT;
    public static final int SCREEN_WIDTH;
    private static final long serialVersionUID = 1L;
    public int scaleWidth = 400;
    public String mPreview;
    private Calendar moonPhaseCalendar;
    private int moonPhaseType = -1;
    public int position;
    public int preview;

    static {
        int j = 400;
        /*if (Build.BOARD.equals("w812a_kk"))
        {
            i = 360;
            label19: SCREEN_WIDTH = i;
            if (!Build.BOARD.equals("w812a_kk"))
                break label48;
        }
        for (int i = j; ; i = 400)
        {
            SCREEN_HEIGHT = i;
            return;
            i = 400;
            label48: break label19:
        }*/

        SCREEN_HEIGHT = SCREEN_WIDTH = j;
    }
    
    protected int mClockWidth = SCREEN_WIDTH;
    protected int mClockHeight = SCREEN_HEIGHT;

    public int getScaleWidth() {
        return scaleWidth;
    }

    public void setScaleWidth(int scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    protected int adjustResolution(int paramInt) {
        int i = paramInt;
        //if (Build.BOARD.equals("w812a_kk"))
        i = paramInt * mClockWidth / scaleWidth;
        return i;
    }

    protected float adjustResolutionFloat(float paramInt) {
        float i = paramInt;
        //if (Build.BOARD.equals("w812a_kk"))
        i = paramInt * mClockWidth / scaleWidth;
        return i;
    }

//    protected void drawBatteryPicture(Context paramContext, Canvas paramCanvas, int[] paramArrayOfInt, int centerX, int centerY, int batteryLevel, boolean shouldAdjust) {
//        int b, t, n;
//        if (batteryLevel >= 100) {
//            b = 1;
//            t = 0;
//            n = 0;
//        } else if (batteryLevel < 10) {
//            b = 10;
//            t = 10;
//            n = batteryLevel;
//        } else {
//            b = 10;
//            t = batteryLevel / 10;
//            n = batteryLevel % 10;
//        }
//        Drawable drawable1 = paramContext.getResources().getDrawable(paramArrayOfInt[b], null);
//        Drawable drawable2 = paramContext.getResources().getDrawable(paramArrayOfInt[t], null);
//        Drawable drawable3 = paramContext.getResources().getDrawable(paramArrayOfInt[n], null);
//        int width = drawable1.getIntrinsicWidth();
//        int height = drawable2.getIntrinsicHeight();
//        int adjustWidth = width;
//        int adjustHeight = height;
//        if (shouldAdjust) {
//            adjustWidth = adjustResolution(width);
//            adjustHeight = adjustResolution(height);
//        }
//        centerX -= adjustWidth * 3 / 2;
//        centerY -= adjustHeight / 2;
//        drawable1.setBounds(centerX, centerY, centerX + adjustWidth, centerY + adjustHeight);
//        drawable1.draw(paramCanvas);
//        centerX += adjustWidth;
//        drawable2.setBounds(centerX, centerY, centerX + adjustWidth, centerY + adjustHeight);
//        drawable2.draw(paramCanvas);
//        centerX += adjustWidth;
//        drawable3.setBounds(centerX, centerY, centerX + adjustWidth, centerY + adjustHeight);
//        drawable3.draw(paramCanvas);
//    }

    protected void drawBatteryPicture(Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, int batteryLevel, boolean shouldAdjust) {
        int b = batteryLevel / 100;
        int t = (batteryLevel / 10) % 10;
        int n = batteryLevel % 10;
        if (batteryLevel < 10) {
            t = 10;
            b = 10;
        }
        Drawable drawable1 = paramList.get(b);
        Drawable drawable2 = paramList.get(t);
        Drawable drawable3 = paramList.get(n);
        Drawable drawable4 = paramList.size() > 11 ? paramList.get(11) : null;
        int width = drawable1.getIntrinsicWidth();
        int width2 = drawable2.getIntrinsicWidth();
        int width3 = drawable3.getIntrinsicWidth();
        int height = drawable3.getIntrinsicHeight();
        int width4 = 0;
        if (drawable4 != null) {
            width4 = drawable4.getIntrinsicWidth();
        }
        if (shouldAdjust) {
            width = adjustResolution(width);
            width2 = adjustResolution(width2);
            width3 = adjustResolution(width3);
            width4 = adjustResolution(width4);
            height = adjustResolution(height);
        }
        if(b==10 || b==0){
            width = 0;
        }
        centerX -= (width + width2 + width3 + width4) / 2;
        centerY -= height / 2;
        if (b > 0) {
            drawable1.setBounds(centerX, centerY, centerX + width, centerY + height);
            drawable1.draw(paramCanvas);
            centerX += width;
        }
        drawable2.setBounds(centerX, centerY, centerX + width2, centerY + height);
        drawable2.draw(paramCanvas);
        centerX += width2;
        drawable3.setBounds(centerX, centerY, centerX + width3, centerY + height);
        drawable3.draw(paramCanvas);

        if (drawable4 != null) {
            centerX += width3;
            drawable4.setBounds(centerX, centerY, centerX + width4, centerY + height);
            drawable4.draw(paramCanvas);
        }
    }

    protected void drawBatteryPictureWithCirclePic(Canvas paramCanvas, List<Drawable> paramList, int centerX,
                                                   int centerY, int batteryLevel, boolean shouldAdjust){
        Drawable batteryPic;
        int index = 0;
        if(batteryLevel == 100){
            index = 10;
        }else if(batteryLevel >= 90){
            index = 9;
        }else if(batteryLevel >= 80){
            index = 8;
        }else if(batteryLevel >= 70){
            index = 7;
        }else if(batteryLevel >= 60){
            index = 6;
        }else if(batteryLevel >= 50){
            index = 5;
        }else if(batteryLevel >= 40){
            index = 4;
        }else if(batteryLevel >= 30){
            index = 3;
        }else if(batteryLevel >= 20){
            index = 2;
        }else if(batteryLevel >= 10){
            index = 1;
        }else if(batteryLevel > 0){
            index = 0;
        }
        batteryPic = paramList.get(index);
        int width = batteryPic.getIntrinsicWidth();
        int height = batteryPic.getIntrinsicHeight();
        int adjustWidth = width;
        int adjustHeight = height;
        if (shouldAdjust) {
            adjustWidth = adjustResolution(width);
            adjustHeight = adjustResolution(height);
        }
        centerX -= (adjustWidth) / 2;
        centerY -= adjustHeight / 2;
        batteryPic.setBounds(centerX,centerY,centerX+adjustWidth,centerY+adjustHeight);
        batteryPic.draw(paramCanvas);
        paramCanvas.restore();
    }

    protected void drawBatteryPictureWithCircle(Canvas paramCanvas, List<Drawable> paramList, int centerX,
                                                int centerY, int batteryLevel, String colorsInfo, boolean shouldAdjust) {

        batteryLevel = Math.min(100, batteryLevel);
        batteryLevel = Math.max(0, batteryLevel);
        int h = batteryLevel / 100;
        int t = batteryLevel / 10 % 10;
        int g = batteryLevel % 10;
        int center = centerX;
        int highColor = 0xFF000000;
        int normalColor = 0xFF000000;
        Paint paint = null;
        if (batteryLevel < 10) {
            h = 10;
            t = 10;
        }
        Drawable drawable1 = paramList.get(h);
        Drawable drawable2 = paramList.get(t);
        Drawable drawable3 = paramList.get(g);
        Drawable drawable4 = paramList.size() > 11 ? paramList.get(11) : null;
        int width = drawable1.getIntrinsicWidth();
        int height = drawable1.getIntrinsicHeight();
        int width2 = drawable2.getIntrinsicWidth();
        int width3 = drawable3.getIntrinsicWidth();
        int width4 = 0;
        if (drawable4 != null) {
            width4 = drawable4.getIntrinsicWidth();
        }
        if (shouldAdjust) {
            width = adjustResolution(width);
            width2 = adjustResolution(width2);
            width3 = adjustResolution(width3);
            width4 = adjustResolution(width4);
            height = adjustResolution(height);
        }
        if (h == 0) {
            width = 0;
        }
        centerX -= (width + width2 + width3 + width4) / 2;
        centerY -= height / 2;
        if (h > 0) {
            drawable1.setBounds(centerX, centerY, centerX + width, centerY + height);
            drawable1.draw(paramCanvas);
        }
        centerX += width;
        drawable2.setBounds(centerX, centerY, centerX + width2, centerY + height);
        drawable2.draw(paramCanvas);
        centerX += width2;
        drawable3.setBounds(centerX, centerY, centerX + width3, centerY + height);
        drawable3.draw(paramCanvas);

        if (drawable4 != null) {
            centerX += width3;
            drawable4.setBounds(centerX, centerY, centerX + width4, centerY + height);
            drawable4.draw(paramCanvas);
        }

        centerY += height / 2;
        if (colorsInfo.contains(",")) {
            normalColor = 0xFF000000 | Integer.valueOf(colorsInfo.split(",")[0], 16).intValue();
            highColor = 0xFF000000 | Integer.valueOf(colorsInfo.split(",")[1], 16).intValue();
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(adjustResolution(15));
            paint.setStyle(Paint.Style.STROKE);
            paint.setAlpha(255);
            paramCanvas.save();
            paramCanvas.translate(center, centerY);
            paramCanvas.scale(adjustResolutionFloat(0.28f), adjustResolutionFloat(0.28f));
            paramCanvas.rotate(180.0F);
            //centerY-=adjustResolution(50);
            for (int i = 0; i < 20; i++) {
                paint.setColor((i < batteryLevel / 5) ? highColor : normalColor);
                paramCanvas.drawLine(adjustResolution(7), centerY, adjustResolution(7), centerY + adjustResolution(25), paint);
                paramCanvas.rotate(18.0F, 0.0F, 0.0F);
            }
        }
        paramCanvas.restore();
    }

    protected void drawChargingInfo(Context paramContext, Canvas paramCanvas, Drawable paramDrawable, int centerX, int centerY, int paramInt3, boolean shouldAdjust) {
        int batteryLevel = getBatteryLevel(paramContext);
        int width = paramDrawable.getIntrinsicWidth();
        int height = paramDrawable.getIntrinsicHeight();
        if (shouldAdjust) {
            width = adjustResolution(width);
            height = adjustResolution(height);
        }
        paramDrawable.setBounds(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
        paramDrawable.draw(paramCanvas);
        String battery = batteryLevel + "%";
        Paint localPaint = new Paint();
        localPaint.setTextSize(adjustResolution(20));
        localPaint.setAntiAlias(true);
        localPaint.setColor(paramInt3);
        paramCanvas.drawText(battery, paramDrawable.getBounds().right + adjustResolution(5), paramDrawable.getBounds().bottom - adjustResolution(6), localPaint);
    }

    protected void drawClockQuietPicture(Canvas paramCanvas, Drawable paramDrawable, int centerX, int centerY, boolean shouldAdjust) {
        int width = paramDrawable.getIntrinsicWidth();
        int height = paramDrawable.getIntrinsicHeight();
        int adjustWidth = width;
        int adjustHeight = height;
        if (shouldAdjust) {
            adjustWidth = adjustResolution(width);
            adjustHeight = adjustResolution(height);
        }
        if (adjustWidth <= 3) {
            paramDrawable.setBounds(centerX - adjustWidth / 2 + 1, centerY - adjustHeight / 2, centerX + adjustWidth / 2 + 1, centerY + adjustHeight / 2);
        } else {
            paramDrawable.setBounds(centerX - adjustWidth / 2, centerY - adjustHeight / 2, centerX + adjustWidth / 2, centerY + adjustHeight / 2);
        }
        paramDrawable.draw(paramCanvas);
    }

    protected void drawClockRotatePicture(Canvas paramCanvas, Drawable drawable, int centerX,
                                          int centerY, float rotateAngle, boolean shouldAdjust) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (shouldAdjust) {
            width = adjustResolution(width);
            height = adjustResolution(height);
        }
        paramCanvas.save();
        paramCanvas.rotate(rotateAngle, centerX, centerY);
        drawable.setBounds(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
        drawable.draw(paramCanvas);
        paramCanvas.restore();
    }

    protected void drawDigitalHourAndMinute(Canvas canvas, Drawable drawableHour1, Drawable drawableHour2,
                                            Drawable drawableColon, Drawable drawableMinute1, Drawable drawableMinute2,
                                            Drawable drawableAMPM, int centerX, int centerY, int second, boolean shouldAdjust) {

        int width = drawableHour1.getIntrinsicWidth();
        int height = drawableHour1.getIntrinsicHeight();
        int adjustWidthHour1 = width;
        int adjustHeightHour1 = height;
        if (shouldAdjust) {
            adjustWidthHour1 = adjustResolution(width);
            adjustHeightHour1 = adjustResolution(height);
        }
        width = drawableColon.getIntrinsicWidth();
        height = drawableColon.getIntrinsicHeight();
        int adjustWidthColon = width;
        int adjustHeightColon = height;
        if (shouldAdjust) {
            adjustWidthColon = adjustResolution(width);
            adjustHeightColon = adjustResolution(height);
        }
        int adjustAMPMWidth = 0;
        int adjustAMPMHeight = 0;
        if (drawableAMPM != null) {
            width = drawableAMPM.getIntrinsicWidth();
            height = drawableAMPM.getIntrinsicHeight();
            if (shouldAdjust) {
                adjustAMPMWidth = adjustResolution(width);
                adjustAMPMHeight = adjustResolution(height);
            }
        }
        centerX -= (adjustWidthHour1 * 2 + adjustWidthColon / 2 + adjustAMPMWidth / 2);
        drawableHour1.setBounds(centerX, centerY - adjustHeightHour1 / 2, centerX + adjustWidthHour1, centerY + adjustHeightHour1 / 2);
        drawableHour1.draw(canvas);
        centerX += adjustWidthHour1;
        drawableHour2.setBounds(centerX, centerY - adjustHeightHour1 / 2, centerX + adjustWidthHour1, centerY + adjustHeightHour1 / 2);
        drawableHour2.draw(canvas);
        centerX += adjustWidthHour1;
        if (second % 2 == 0) {
            drawableColon.setBounds(centerX, centerY - adjustHeightColon / 2, centerX + adjustWidthColon, centerY + adjustHeightColon / 2);
            drawableColon.draw(canvas);
        }
        centerX += adjustWidthColon;
        drawableMinute1.setBounds(centerX, centerY - adjustHeightHour1 / 2, centerX + adjustWidthHour1, centerY + adjustHeightHour1 / 2);
        drawableMinute1.draw(canvas);
        centerX += adjustWidthHour1;
        drawableMinute2.setBounds(centerX, centerY - adjustHeightHour1 / 2, centerX + adjustWidthHour1, centerY + adjustHeightHour1 / 2);
        drawableMinute2.draw(canvas);
        centerX += adjustWidthHour1;
        if (drawableAMPM != null) {
            drawableAMPM.setBounds(centerX, centerY - adjustHeightHour1 / 2, centerX + adjustAMPMWidth, centerY - adjustHeightHour1 / 2 + adjustAMPMHeight);
            drawableAMPM.draw(canvas);
        }
    }

    protected void drawDigitalMonthAndDay(Canvas canvas, Drawable month1, Drawable month2, Drawable day1, Drawable day2,
                                          int centerX, int centerY, boolean shouldAdjust) {
        int width = month1.getIntrinsicWidth();
        int height = month1.getIntrinsicHeight();
        int adjustWidth = width;
        int adjustHeight = height;
        if (shouldAdjust) {
            adjustWidth = adjustResolution(width);
            adjustHeight = adjustResolution(height);
        }

        width = month2.getIntrinsicWidth();
        height = month2.getIntrinsicHeight();
        int adjustWidth2 = width;
        int adjustHeight2 = height;
        if (shouldAdjust) {
            adjustWidth2 = adjustResolution(width);
            adjustHeight2 = adjustResolution(height);
        }

        width = day1.getIntrinsicWidth();
        height = day1.getIntrinsicHeight();
        int adjustWidthDay = width;
        int adjustHeightDay = height;
        if (shouldAdjust) {
            adjustWidthDay = adjustResolution(width);
            adjustHeightDay = adjustResolution(height);
        }
        width = day1.getIntrinsicWidth();
        height = day1.getIntrinsicHeight();
        int adjustWidthDay2 = width;
        int adjustHeightDay2 = height;
        if (shouldAdjust) {
            adjustWidthDay2 = adjustResolution(width);
            adjustHeightDay2 = adjustResolution(height);
        }

        centerX -= (adjustWidth + adjustWidth2 + adjustWidthDay + adjustWidthDay2) / 2;
        month1.setBounds(centerX, centerY - (adjustHeight / 2), centerX + adjustWidth, centerY + adjustHeight / 2);
        month1.draw(canvas);
        centerX += adjustWidth;
        month2.setBounds(centerX, centerY - (adjustHeight2 / 2), centerX + adjustWidth2, centerY + (adjustHeight2 / 2));
        month2.draw(canvas);
        centerX += adjustWidth2;
        day1.setBounds(centerX, centerY - (adjustHeightDay / 2), centerX + adjustWidthDay, centerY + (adjustHeightDay / 2));
        day1.draw(canvas);
        centerX += adjustWidthDay;
        day2.setBounds(centerX, centerY - (adjustHeightDay2 / 2), centerX + adjustWidthDay2, centerY + (adjustHeightDay2 / 2));
        day2.draw(canvas);
    }

    protected void drawDigitalMonthAndDay(Canvas canvas, Drawable month1, Drawable month2, Drawable colon, Drawable day1, Drawable day2,
                                          int centerX, int centerY, boolean shouldAdjust) {
        int width = month1.getIntrinsicWidth();
        int height = month1.getIntrinsicHeight();
        int adjustWidth = width;
        int adjustHeight = height;
        if (shouldAdjust) {
            adjustWidth = adjustResolution(width);
            adjustHeight = adjustResolution(height);
        }

        width = month2.getIntrinsicWidth();
        height = month2.getIntrinsicHeight();
        int adjustWidth2 = width;
        int adjustHeight2 = height;
        if (shouldAdjust) {
            adjustWidth2 = adjustResolution(width);
            adjustHeight2 = adjustResolution(height);
        }

        width = colon.getIntrinsicWidth();
        height = colon.getIntrinsicHeight();
        int adjustWidthColon = width;
        int adjustHeightColon = height;
        if (shouldAdjust) {
            adjustWidthColon = adjustResolution(width);
            adjustHeightColon = adjustResolution(height);
        }

        width = day1.getIntrinsicWidth();
        height = day1.getIntrinsicHeight();
        int adjustWidthDay = width;
        int adjustHeightDay = height;
        if (shouldAdjust) {
            adjustWidthDay = adjustResolution(width);
            adjustHeightDay = adjustResolution(height);
        }
        width = day1.getIntrinsicWidth();
        height = day1.getIntrinsicHeight();
        int adjustWidthDay2 = width;
        int adjustHeightDay2 = height;
        if (shouldAdjust) {
            adjustWidthDay2 = adjustResolution(width);
            adjustHeightDay2 = adjustResolution(height);
        }

        centerX -= (adjustWidth + adjustWidth2 + adjustWidthColon + adjustWidthDay + adjustWidthDay2) / 2;
        month1.setBounds(centerX, centerY - (adjustHeight / 2), centerX + adjustWidth, centerY + adjustHeight / 2);
        month1.draw(canvas);
        centerX += adjustWidth;
        month2.setBounds(centerX, centerY - (adjustHeight2 / 2), centerX + adjustWidth2, centerY + (adjustHeight2 / 2));
        month2.draw(canvas);
        centerX += adjustWidth2;
        colon.setBounds(centerX, centerY - (adjustHeightColon / 2), centerX + adjustWidthColon, centerY + (adjustHeightColon / 2));
        colon.draw(canvas);
        centerX += adjustWidthColon;
        day1.setBounds(centerX, centerY - (adjustHeightDay / 2), centerX + adjustWidthDay, centerY + (adjustHeightDay / 2));
        day1.draw(canvas);
        centerX += adjustWidthDay;
        day2.setBounds(centerX, centerY - (adjustHeightDay2 / 2), centerX + adjustWidthDay2, centerY + (adjustHeightDay2 / 2));
        day2.draw(canvas);
    }

    //画数字图�?
    protected void drawDigitalOnePicture(Canvas canvas, Drawable drawable, int centerX, int centerY, boolean shouldAdjust) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        int adjustWidth = width;
        int adjustHeight = height;
        if (shouldAdjust) {
            adjustWidth = adjustResolution(width);
            adjustHeight = adjustResolution(height);
        }
        centerX -= adjustWidth / 2;
        centerY -= adjustHeight / 2;
        drawable.setBounds(centerX, centerY, centerX + adjustWidth, centerY + adjustHeight);
        drawable.draw(canvas);
    }


    protected void drawDigitalTwoPicture(Canvas canvas, Drawable drawable1, Drawable drawable2, int centerX, int centerY, boolean paramBoolean) {
        int width = drawable1.getIntrinsicWidth();
        int height = drawable1.getIntrinsicHeight();
        int adjustWidth = width;
        int adjustHeight = height;
        if (paramBoolean) {
            adjustWidth = adjustResolution(width);
            adjustHeight = adjustResolution(height);
        }
        int width2 = drawable2.getIntrinsicWidth();
        int height2 = drawable2.getIntrinsicHeight();
        int adjustWidth2 = width;
        int adjustHeight2 = height;
        if (paramBoolean) {
            adjustWidth2 = adjustResolution(width2);
            adjustHeight2 = adjustResolution(height2);
        }
        centerX -= (adjustWidth + adjustWidth2) / 2;
        centerY -= (adjustHeight) / 2;
        drawable1.setBounds(centerX, centerY, centerX + adjustWidth, centerY + adjustHeight);
        drawable1.draw(canvas);
        centerX = centerX + adjustWidth;
        centerY -= (adjustHeight - adjustHeight2) / 2;
        drawable2.setBounds(centerX, centerY, centerX + adjustWidth2, centerY + adjustHeight2);
        drawable2.draw(canvas);
    }

    protected void drawDigitalYear(Canvas canvas, List<Drawable> drawables, int currentYear, int centerX, int centerY, boolean shouldAdjust) {
        int l = (drawables.get(0)).getIntrinsicWidth();
        int k = (drawables.get(0)).getIntrinsicHeight();
        int j = k;
        int i = l;
        if (shouldAdjust) {
            i = adjustResolution(l);
            j = adjustResolution(k);
        }
        k = centerY - j / 2;
        centerY += j / 2;
        j = currentYear / 1000;
        l = currentYear / 100 % 10;
        int i1 = currentYear / 10 % 10;
        currentYear %= 10;
        centerX -= i * 2;
        int i2 = centerX + i;
        int i3 = i2 + i;
        int i4 = i3 + i;
        ((Drawable) drawables.get(j)).setBounds(centerX, k, i2, centerY);
        ((Drawable) drawables.get(j)).draw(canvas);
        ((Drawable) drawables.get(l)).setBounds(i2, k, i3, centerY);
        ((Drawable) drawables.get(l)).draw(canvas);
        ((Drawable) drawables.get(i1)).setBounds(i3, k, i4, centerY);
        ((Drawable) drawables.get(i1)).draw(canvas);
        ((Drawable) drawables.get(currentYear)).setBounds(i4, k, i4 + i, centerY);
        ((Drawable) drawables.get(currentYear)).draw(canvas);
    }
    protected void drawDigitalYearMonthDay(Canvas paramCanvas, List<Drawable> paramList, Calendar calendar, int centerX, int centerY, boolean shouldAdjust)
    {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int width = (paramList.get(0)).getIntrinsicWidth();
        int height = (paramList.get(0)).getIntrinsicHeight();
        int colonHeight = (paramList.get(10)).getIntrinsicHeight();
        if (shouldAdjust)
        {
            width = adjustResolution(width);
            height = adjustResolution(height);
            colonHeight = adjustResolution(colonHeight);
        }
        centerX = centerX - (width * 8 + colonHeight * 2) / 2;
        centerY -= height / 2;

        (paramList.get(year/1000)).setBounds(centerX, centerY, centerX+width, centerY + height);
        (paramList.get(year/1000)).draw(paramCanvas);
        centerX+=width;
        (paramList.get(year/100%10)).setBounds(centerX, centerY, centerX+width, centerY + height);
        (paramList.get(year/100%10)).draw(paramCanvas);
        centerX+=width;
        (paramList.get(year/10%10)).setBounds(centerX, centerY, centerX+width, centerY + height);
        (paramList.get(year/10%10)).draw(paramCanvas);
        centerX+=width;
        (paramList.get(year%10)).setBounds(centerX, centerY, centerX+width, centerY + height);
        (paramList.get(year%10)).draw(paramCanvas);
        centerX+=width;
        (paramList.get(10)).setBounds(centerX, centerY, centerX+width, centerY + colonHeight);
        (paramList.get(10)).draw(paramCanvas);
        centerX+=width;
        (paramList.get(month / 10)).setBounds(centerX, centerY, centerX+width, centerY + height);
        (paramList.get(month / 10)).draw(paramCanvas);
        centerX+=width;
        (paramList.get(month % 10)).setBounds(centerX, centerY, centerX+width, centerY + height);
        (paramList.get(month % 10)).draw(paramCanvas);
        centerX+=width;
        (paramList.get(10)).setBounds(centerX, centerY, centerX+width, centerY + height);
        (paramList.get(10)).draw(paramCanvas);
        centerX+=width;
        (paramList.get(day / 10)).setBounds(centerX, centerY, centerX+width, centerY + height);
        (paramList.get(day / 10)).draw(paramCanvas);
        centerX+=width;
        (paramList.get(day % 10)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(day % 10)).draw(paramCanvas);
    }
    protected void drawHeartRatePicture(Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, int hearRate, boolean shouldAdjust) {
        int b = 0, t = 0, n = 0;
        hearRate = Math.min(hearRate, 999);
        hearRate = Math.max(hearRate, 0);
        if (hearRate > 99) {
            b = hearRate / 100;
            hearRate = hearRate % 100;
        }
        t = hearRate / 10 % 10;
        n = hearRate % 10;
        int width = (paramList.get(0)).getIntrinsicWidth();
        int height = (paramList.get(0)).getIntrinsicHeight();

        if (shouldAdjust) {
            width = adjustResolution(width);
            height = adjustResolution(height);
        }
        centerY -= height / 2;
        if (b > 0) {
            centerX -= width * 3 / 2;
            (paramList.get(b)).setBounds(centerX, centerY, centerX + width, centerY + height);
            (paramList.get(b)).draw(paramCanvas);
            centerX += width;
        } else {
            centerX -= width;
        }
        (paramList.get(t)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(t)).draw(paramCanvas);
        centerX += width;
        (paramList.get(n)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(n)).draw(paramCanvas);


    }
    protected void drawPedometer(Context paramContext, Canvas paramCanvas, int startAngle, int direction, int textSize, int paramInt4) {

    }
    protected void drawSpecialSecond(Canvas paramCanvas, String colorsInfo, int minute, int second) {
        int centerX = mClockWidth / 2;
        int highColor = 0xFF000000;
        int normalColor = 0xFF000000;
        Paint paint = null;

        if (colorsInfo.contains(",")) {
            highColor = 0xFF000000 | Integer.valueOf(colorsInfo.split(",")[0], 16).intValue();
            normalColor = 0xFF000000 | Integer.valueOf(colorsInfo.split(",")[1], 16).intValue();
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(adjustResolution(10));
            paint.setStyle(Paint.Style.STROKE);
            paint.setAlpha(255);
            paramCanvas.save();
            paramCanvas.translate(centerX, centerX);
            float f = -centerX + 5;
            for (int i = 0; i < 60; i++) {
                if (minute % 2 == 0) {
                    paint.setColor((i < second) ? highColor : normalColor);
                } else {
                    paint.setColor((i < second) ? normalColor : highColor);
                }

                paramCanvas.drawLine(adjustResolution(5), f, adjustResolution(5), f + adjustResolution(15), paint);
                paramCanvas.rotate(6.0F, 0.0F, 0.0F);
            }

            paramCanvas.restore();
        }
    }

    protected void drawStepsPicture(Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, int step, boolean shouldAdjust) {
        step = Math.max(step, 0);
        step = Math.min(step, 99999);
        int w = step / 10000;//万位
        int k = step / 1000 % 10;//千位
        int h = step / 100 % 10;//百位
        int t = step / 10 % 10;//十位
        int g = step % 10;//个位

        int widthW = (paramList.get(w)).getIntrinsicWidth();
        int widthK = (paramList.get(k)).getIntrinsicWidth();
        int widthH = (paramList.get(h)).getIntrinsicWidth();
        int widthT = (paramList.get(t)).getIntrinsicWidth();
        int widthG = (paramList.get(g)).getIntrinsicWidth();
        int height = (paramList.get(w)).getIntrinsicHeight();
        if (shouldAdjust) {
            widthW = adjustResolution(widthW);
            widthK = adjustResolution(widthK);
            widthH = adjustResolution(widthH);
            widthT = adjustResolution(widthT);
            widthG = adjustResolution(widthG);
            height = adjustResolution(height);
        }
        centerX -= (widthW + widthK + widthH + widthT + widthG) / 2;
        centerY -= height / 2;
        (paramList.get(w)).setBounds(centerX, centerY, centerX + widthW, centerY + height);
        (paramList.get(w)).draw(paramCanvas);
        centerX += widthW;
        (paramList.get(k)).setBounds(centerX, centerY, centerX + widthK, centerY + height);
        (paramList.get(k)).draw(paramCanvas);
        centerX += widthK;
        (paramList.get(h)).setBounds(centerX, centerY, centerX + widthH, centerY + height);
        (paramList.get(h)).draw(paramCanvas);
        centerX += widthH;
        (paramList.get(t)).setBounds(centerX, centerY, centerX + widthT, centerY + height);
        (paramList.get(t)).draw(paramCanvas);
        centerX += widthT;
        (paramList.get(g)).setBounds(centerX, centerY, centerX + widthG, centerY + height);
        (paramList.get(g)).draw(paramCanvas);

    }

    protected void drawStepsPictureWithCircle(Context paramContext, Canvas paramCanvas, List<Drawable> paramList,
                                              int centerX, int centerY, int step, String colorsInfo, boolean shouldAdjust) {
        step = Math.max(step, 0);
        step = Math.min(step, 99999);
        int center = centerX;
        int w = step / 10000;//万位
        int k = step / 1000 % 10;//千位
        int h = step / 100 % 10;//百位
        int t = step / 10 % 10;//十位
        int g = step % 10;//个位

        int widthW = (paramList.get(w)).getIntrinsicWidth();
        int widthK = (paramList.get(k)).getIntrinsicWidth();
        int widthH = (paramList.get(h)).getIntrinsicWidth();
        int widthT = (paramList.get(t)).getIntrinsicWidth();
        int widthG = (paramList.get(g)).getIntrinsicWidth();
        int height = (paramList.get(w)).getIntrinsicHeight();
        if (shouldAdjust) {
            widthW = adjustResolution(widthW);
            widthK = adjustResolution(widthK);
            widthH = adjustResolution(widthH);
            widthT = adjustResolution(widthT);
            widthG = adjustResolution(widthG);
            height = adjustResolution(height);
        }
        centerX -= (widthW + widthK + widthH + widthT + widthG) / 2;
        centerY -= height / 2;
        (paramList.get(w)).setBounds(centerX, centerY, centerX + widthW, centerY + height);
        (paramList.get(w)).draw(paramCanvas);
        centerX += widthW;
        (paramList.get(k)).setBounds(centerX, centerY, centerX + widthK, centerY + height);
        (paramList.get(k)).draw(paramCanvas);
        centerX += widthK;
        (paramList.get(h)).setBounds(centerX, centerY, centerX + widthH, centerY + height);
        (paramList.get(h)).draw(paramCanvas);
        centerX += widthH;
        (paramList.get(t)).setBounds(centerX, centerY, centerX + widthT, centerY + height);
        (paramList.get(t)).draw(paramCanvas);
        centerX += widthT;
        (paramList.get(g)).setBounds(centerX, centerY, centerX + widthG, centerY + height);
        (paramList.get(g)).draw(paramCanvas);

        centerY += height / 2;
        int highColor = 0xFF000000;
        int normalColor = 0xFF000000;
        Paint paint = null;
        normalColor = Integer.valueOf(colorsInfo.split(",")[0], 16).intValue();
        highColor = Integer.valueOf(colorsInfo.split(",")[1], 16).intValue();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(adjustResolution(10));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(255);
        paramCanvas.save();
        RectF rectF = new RectF();
        rectF.set(center - mClockWidth / 8, centerY - mClockHeight / 8, mClockWidth / 8 + center, mClockHeight / 8 + centerY);
        int j = Settings.System.getInt(paramContext.getContentResolver(), "suggest_steps", UserInfoConstant.DEFAULT_STEP_TARGET);
        paint.setColor(0xFF000000 | normalColor);
        paramCanvas.drawCircle(center, centerY, mClockWidth / 8, paint);
        paint.setColor(0xFF000000 | highColor);
        if (step > j) {
            paramCanvas.drawCircle(center, centerY, mClockWidth / 8, paint);
        }
        paramCanvas.drawArc(rectF, 270.0F, step / Float.valueOf(j).floatValue() * 360.0F, false, paint);

        paramCanvas.restore();
    }

    protected void drawTemperature(Canvas paramCanvas, Drawable minus, Drawable temp1, Drawable temp2,
                                   Drawable tempUnit, int centerX, int centerY, boolean paramBoolean1, boolean shouldAdjust) {
        int widthMinus = minus.getIntrinsicWidth();
        int heightMinus = minus.getIntrinsicHeight();
        if (shouldAdjust) {
            widthMinus = adjustResolution(widthMinus);
            heightMinus = adjustResolution(heightMinus);
        }
        int widthTemp1 = temp1.getIntrinsicWidth();
        int heightTemp1 = temp1.getIntrinsicHeight();
        if (shouldAdjust) {
            widthTemp1 = adjustResolution(widthTemp1);
            heightTemp1 = adjustResolution(heightTemp1);
        }
        int widthTemp2 = temp2.getIntrinsicWidth();
        int heightTemp2 = temp2.getIntrinsicHeight();
        if (shouldAdjust) {
            widthTemp2 = adjustResolution(widthTemp2);
            heightTemp2 = adjustResolution(heightTemp2);
        }
        int widthTempUnit = tempUnit.getIntrinsicWidth();
        int heightTempUnit = tempUnit.getIntrinsicHeight();
        if (shouldAdjust) {
            widthTempUnit = adjustResolution(widthTempUnit);
            heightTempUnit = adjustResolution(heightTempUnit);
        }
        centerX -= (widthMinus + widthTemp1 + widthTemp2 + widthTempUnit) / 2;
        if (paramBoolean1) {
            minus.setBounds(centerX, centerY - heightMinus / 2, centerX + widthMinus, centerY + heightMinus / 2);
            minus.draw(paramCanvas);
            centerX += widthMinus;
        }
        temp1.setBounds(centerX, centerY - heightTemp1 / 2, centerX + widthTemp1, centerY + heightTemp1 / 2);
        temp1.draw(paramCanvas);
        centerX += widthTemp1;
        temp2.setBounds(centerX, centerY - heightTemp2 / 2, centerX + widthTemp2, centerY + heightTemp2 / 2);
        temp2.draw(paramCanvas);
        centerX += widthTemp2;
        tempUnit.setBounds(centerX, centerY - heightTemp2 / 2, centerX + widthTempUnit, centerY- heightTemp2 / 2 + heightTempUnit);
        tempUnit.draw(paramCanvas);
    }

    public static Object String2Object(String paramString)
            throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(paramString.getBytes(), 0)));
        Object localObject = objectInputStream.readObject();
        objectInputStream.close();
        return localObject;
    }

    public static ClockSkin defaultSkin(Context paramContext) {
        return new ClockSkinParse().getChildSkinByPosition(paramContext, 0);
    }

    public static ClockSkin getDefaultSkin(Context paramContext) {
        ClockSkin clockSkin = null;
        Object localObject = PreferenceManager.getDefaultSharedPreferences(paramContext).getString("sp_clock_skin", null);
        if (localObject != null)
            try {
                clockSkin = (ClockSkin) String2Object((String) localObject);
                return clockSkin;
            } catch (StreamCorruptedException localStreamCorruptedException) {
                localStreamCorruptedException.printStackTrace();
                return defaultSkin(paramContext);
            } catch (IOException localIOException) {
                localIOException.printStackTrace();
                return defaultSkin(paramContext);
            } catch (ClassNotFoundException localClassNotFoundException) {
                localClassNotFoundException.printStackTrace();
                return defaultSkin(paramContext);
            }
        return (ClockSkin) defaultSkin(paramContext);
    }

    public ClockSkin getChildSkin(Context paramContext, ClockSkin paramSkin) {
        return getChildSkinByPosition(paramContext, paramSkin.position);
    }

    protected int getBatteryLevel(Context paramContext) {
        Intent intent = paramContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        return intent.getIntExtra("level", -1) * 100 / intent.getIntExtra("scale", -1);
    }

    protected int getMoonPhaseType(Context paramContext) {
        int i = 0;
        /*if (this.moonPhaseCalendar == null) {
            this.moonPhaseCalendar = Calendar.getContext(Locale.getDefault());
            this.moonPhaseCalendar.setTimeInMillis(System.currentTimeMillis());
            this.moonPhaseCalendar.setTimeZone(TimeZone.getDefault());
            i = 1;
        }
        Calendar localCalendar = Calendar.getContext(Locale.getDefault());
        localCalendar.setTimeInMillis(System.currentTimeMillis());
        localCalendar.setTimeZone(TimeZone.getDefault());
        if ((localCalendar.get(Calendar.MONTH) != this.moonPhaseCalendar.get(Calendar.MONTH)) ||
                (localCalendar.get(Calendar.DAY_OF_MONTH) != this.moonPhaseCalendar.get(Calendar.DAY_OF_MONTH))) {
            i = 1;
            this.moonPhaseCalendar = localCalendar;
        }
        if ((this.moonPhaseType == -1) || (i != 0))
            this.moonPhaseType = new MoonPhase(paramContext).searchMoonPhase();*/
        return this.moonPhaseType;
    }

    protected boolean isChargingNow(Context paramContext) {
        boolean isChargingNow = false;
        if (paramContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("plugged", 0) != 0)
            isChargingNow = true;
        return isChargingNow;
    }

    protected boolean isTime24Format(Context paramContext) {
        return "24".equals(Settings.System.getString(paramContext.getContentResolver(), "time_12_24"));
    }

    public abstract void drawClock(Canvas paramCanvas, Context paramContext);

    protected abstract ClockSkin getChildSkinByPosition(Context paramContext, int paramInt);

    protected abstract List<ClockSkin> getSkins(Context paramContext);

    public abstract void recycleDrawable();
}
