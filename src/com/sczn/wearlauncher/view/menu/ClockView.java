package com.sczn.wearlauncher.view.menu;

import com.sczn.wearlauncher.model.ClockSkin;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ClockView extends View implements Runnable{
	private static final String TAG = ClockView.class.getSimpleName();
	
	private static final int FRESH_FREQUENCY = 50;
	private Paint mPaint;
    private ClockSkin mClockSkin;
    private Thread mThread;
    private int sleepTime = FRESH_FREQUENCY;
    private boolean isRunning = false;
    
    public ClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setColor(Color.BLACK);
    
	}
    
    public void setFreshFreQuency(int duration){
    	this.sleepTime = duration;
    }
    
    public void setClockSkin(ClockSkin paramSkin){
        this.mClockSkin = paramSkin;
        //startDraw();
    }
    
    public void startDraw(){
    	isRunning = true;
    	if(mThread != null && mThread.isAlive()){
    		return;
    	}
    	mThread = new Thread(this);
    	mThread.start();
    }
    public void stopDraw(){
    	isRunning = false;
    }

    private void DrawByType(Canvas paramCanvas){
        if (this.mClockSkin == null) return;
        this.mClockSkin.drawClock(paramCanvas, getContext());
    }

    protected void onDraw(Canvas paramCanvas){
        //super.onDraw(paramCanvas);
        paramCanvas.drawPaint(this.mPaint);
        try{
            DrawByType(paramCanvas);
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        while (isRunning) {
            try {
            	if(isAttachedToWindow()){
            		postInvalidate();
            	}
                Thread.sleep(this.sleepTime);
            } catch (InterruptedException localInterruptedException) {

            }
        }
    }

}
