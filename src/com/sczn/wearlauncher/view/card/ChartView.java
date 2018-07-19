package com.sczn.wearlauncher.view.card;

import java.util.ArrayList;
import java.util.Arrays;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mengmeng on 2016/9/22.
 */
public class ChartView extends View {
    //鐢绘í绾佃酱
    private Paint mBorderPaint;

    //鐢绘姌绾垮浘
    protected Paint mPathPaint;

    protected Path mPath;

    protected float fontHeight;

    //鐢昏櫄绾�
    private Path mDottedLinePath;

    //绾佃酱鏈�澶у��
    protected int maxValue;

    //绾佃酱鍒嗗壊鏁伴噺
    protected int dividerCount;

    //绾佃酱姣忎釜鍗曚綅鍊�
    protected int perValue;

    //搴曢儴鏄剧ずString(24灏忔椂鐨勬暟鎹巻鍙诧紝姣忓皬鏃朵竴涓棿闅斻��0锛氬綋鍓嶆椂闂达紝4锛�4灏忔椂鍓嵚仿仿仿仿蜂互姝ょ被鎺�)
    protected String[] bottomStr = {"0","4","8","12","16","20","24"};
    
    protected String[] leftStr;

    //鍏蜂綋鐨勫��
    private float[] values = {};

    //搴曢儴妯酱鍗曚綅闂磋窛
    protected float bottomGap;

    //宸﹁竟绾佃酱闂磋窛
    protected float leftGap;

    //鐢籝杞存枃瀛�
    private TextPaint textPaint;

    protected float padding = 20;

    private Shader mShader = null;

    //鏇茬嚎娓愬彉寮�濮嬮鑹�
    private int mStartColor;

    //鏇茬嚎娓愬彉涓棿棰滆壊
    private int mMidColor;

    //鏇茬嚎娓愬彉缁撴潫棰滆壊
    private int mEndColor;

    //鏌辩姸鍥�
    private boolean isHistogram = false;

    //鏌辩姸鐢荤瑪
    private Paint mStepsPaint = null;
    
    private int mPathEffect;

    //璁版鏉℃渶浣庢暟鍊�
    private static final int MIN_STEP_DISPLAY = 80;

    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    protected void init(Context context, AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChartView);
        
        try {
        	final CharSequence[] xValues = array.getTextArray(R.styleable.ChartView_xString);

        	if(xValues != null){
        		bottomStr = new String[xValues.length];
        		for(int i = 0; i < xValues.length; i++){
        			bottomStr[i] = xValues[i].toString();
        		}
            }
		} catch (Exception e) {
			// TODO: handle exception
			MxyLog.e(this, e.toString());
		}
        
        try {
        	final CharSequence[] yValues = array.getTextArray(R.styleable.ChartView_yString);

        	if(yValues != null){
        		leftStr = new String[yValues.length];
        		for(int i = 0; i < yValues.length; i++){
        			leftStr[i] = yValues[i].toString();
        		}
            }
		} catch (Exception e) {
			// TODO: handle exception
			MxyLog.e(this, e.toString());
		}
        
        mPathEffect = array.getInt(R.styleable.ChartView_pathEffect,6);

        maxValue = array.getInt(R.styleable.ChartView_maxValue,100);
        dividerCount = array.getInt(R.styleable.ChartView_dividerCount,10);

        mStartColor = array.getColor(R.styleable.ChartView_pathStartColor,Color.WHITE);
        mMidColor = array.getColor(R.styleable.ChartView_pathMidColor,-1);
        mEndColor = array.getColor(R.styleable.ChartView_pathEndColor,Color.RED);

        int lineColor = array.getColor(R.styleable.ChartView_lineColor, Color.argb(88,88,88,88));
        int textColor = array.getColor(R.styleable.ChartView_textColor, Color.WHITE);

        mBorderPaint = new Paint();
        mPathPaint = new Paint();
        textPaint = new TextPaint();
        mDottedLinePath = new Path();
        mPath = new Path();
        mStepsPaint = new Paint();

        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(lineColor);
        mBorderPaint.setStrokeWidth(2.0f);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setPathEffect(new DashPathEffect(new float[]{1.0f, 1.0f}, 1));

        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(3);
        mPathPaint.setPathEffect(new CornerPathEffect(mPathEffect));
        mPathPaint.setColor(Color.WHITE);

        mStepsPaint.setAntiAlias(true);
        mStepsPaint.setStrokeWidth(5);
        mStepsPaint.setStyle(Paint.Style.STROKE);
        mStepsPaint.setStrokeCap(Paint.Cap.ROUND);


        textPaint.setColor(textColor);
        textPaint.setTextSize(dip2px(getContext(), 13));

        perValue = maxValue/dividerCount;
        
        if(leftStr == null){
        	leftStr = new String[dividerCount + 1];
        	for(int i = 0; i < leftStr.length; i++){
        		leftStr[i] = String.valueOf(perValue*i);
        	}
        }

        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode==MeasureSpec.EXACTLY&&heightMode==MeasureSpec.EXACTLY){
            setMeasuredDimension(widthSize,heightSize);
        }else if (widthMeasureSpec==MeasureSpec.EXACTLY){
            setMeasuredDimension(widthSize,widthSize);
        }else if (heightMeasureSpec==MeasureSpec.EXACTLY){
            setMeasuredDimension(heightSize,heightSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        fontHeight =(textPaint.getFontMetrics().descent-textPaint.getFontMetrics().ascent);
        bottomGap = (getWidth() - padding * 2)/(bottomStr.length + 1);
        leftGap = (getHeight() - fontHeight * 2) / dividerCount;
        if(mMidColor != -1){
            mShader = new LinearGradient(0,0,0,getHeight(),new int[]{mStartColor,mMidColor,mEndColor},new float[]{0.0f,0.75f,1.0f},Shader.TileMode.CLAMP);
        }else {
            mShader = new LinearGradient(0,0,0,getHeight(),new int[]{mStartColor,mEndColor},new float[]{0.5f,0.5f},Shader.TileMode.CLAMP);
        }
        super.onLayout(changed, left, top, right, bottom);
    }
    
    protected void drawValues(Canvas canvas){
    	if(isHistogram){
            /**
             * 灏忔椂鏁版嵁鏌辩姸鏉�
             */
            for(int i = 0;i<values.length;i++){
                if(values[i] <= MIN_STEP_DISPLAY){
                    canvas.drawLine(i*bottomGap / 4 + bottomGap + padding + padding / 2,
                            dividerCount*leftGap + fontHeight - fontHeight / 2,
                            i*bottomGap / 4 + bottomGap + padding + padding / 2,
                            dividerCount*leftGap + fontHeight - MIN_STEP_DISPLAY*leftGap/perValue,
                            mStepsPaint);
                }else {
                    canvas.drawLine(i*bottomGap / 4 + bottomGap + padding + padding / 2,
                            dividerCount*leftGap + fontHeight - fontHeight / 2,
                            i*bottomGap / 4 + bottomGap + padding + padding / 2,
                            dividerCount*leftGap + fontHeight - (values[i]*leftGap/perValue),
                            mStepsPaint);
                }
            }
        }else {
            /**
             * 鐢昏建杩�
             * y鐨勫潗鏍囩偣鏍规嵁 y/leftGap = values[i]/perValue 璁＄畻
             *
             */
            for (int i = 0; i < values.length;i++){
                if (i==0){
                    mPath.moveTo(bottomGap + padding + padding / 2,dividerCount*leftGap + fontHeight - (values[i]*leftGap/perValue));
                }else{
                    mPath.lineTo((i+1)*bottomGap + padding + padding / 2,dividerCount*leftGap + fontHeight - (values[i]*leftGap/perValue));
                }
            }
            canvas.drawPath(mPath, mPathPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bottomStr==null||bottomStr.length==0){
            return;
        }

        mPathPaint.setShader(mShader);
        mStepsPaint.setShader(mShader);
        for (int i = 0;i<leftStr.length;i++){
            //鐢诲乏杈圭殑瀛�
            canvas.drawText(leftStr[i], bottomGap / 2 + padding - (textPaint.measureText(perValue * (i) + "") / 2), getHeight() - i * leftGap - fontHeight / 6 * 5, textPaint);
            //鐢绘í绾�
            mDottedLinePath.moveTo(bottomGap + padding, getHeight() - fontHeight - i * leftGap);
            mDottedLinePath.quadTo(bottomGap + padding, getHeight() - fontHeight - i * leftGap, getWidth() - bottomGap / 2 - padding, getHeight() - i * leftGap - fontHeight);
            canvas.drawPath(mDottedLinePath, mBorderPaint);
            mDottedLinePath.reset();
        }

        for(int i = 0; i<bottomStr.length; i++){
            //鐢荤旱绾�
            mDottedLinePath.moveTo(bottomGap * (i + 1) + padding + padding / 2, 0);
            mDottedLinePath.quadTo(bottomGap * (i + 1) + padding + padding / 2, 0, bottomGap * (i + 1) + padding + padding / 2, getHeight() - fontHeight);
            canvas.drawPath(mDottedLinePath, mBorderPaint);
            mDottedLinePath.reset();

            textPaint.setTextSize(dip2px(getContext(), 10));
            canvas.drawText(bottomStr[i] + "", bottomGap*(i+1) + padding + padding / 4, getHeight(), textPaint);
        }
        drawValues(canvas);
    }

    public void setValues(float[] values) {
        this.values = values;
        invalidate();
    }

    public void setIsHistogram(boolean isHistogram) {
        this.isHistogram = isHistogram;
    }
    
    public void setMaxValue(int maxvalue){
    	this.maxValue = maxvalue;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
