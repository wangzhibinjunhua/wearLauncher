/*
 * Copyright (C) 2014 Pixplicity
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sczn.wearlauncher.view;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.transformer.ZoomOutPageTransformerSecondMenu;
import com.sczn.wearlauncher.util.MxyLog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.CallSuper;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class  MultiViewPager extends HorizalViewPager{
	private final static String TAG = MultiViewPager.class.getSimpleName();
	
	private static final float MIN_SCALE = 0.5f;
    private static final float MIN_ALPHA = 0.5f;
    
    /**
     * Maximum size.
     */
    private int mMaxWidth = -1;
    /**
     * Maximum size.
     */
    private int mMaxHeight = -1;
    /**
     * Child view inside a page to match the page size against.
     */
    private int mMatchWidthChildResId;

    /**
     * Internal state to schedule a new measurement pass.
     */
    private boolean mNeedsMeasurePage;
    private final Point size;
    private final Point maxSize;

    private static void constrainTo(Point size, Point maxSize) {
        if (maxSize.x >= 0) {
            if (size.x > maxSize.x) {
                size.x = maxSize.x;
            }
        }
        if (maxSize.y >= 0) {
            if (size.y > maxSize.y) {
                size.y = maxSize.y;
            }
        }
    }

    public MultiViewPager(Context context) {
        this(context, null);
    }

    public MultiViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        size = new Point();
        maxSize = new Point();
    }

    private void init(Context context, AttributeSet attrs) {
    	if(attrs == null){
    		return;
    	}
        setClipChildren(false);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MultiViewPager);
        setMaxWidth(ta.getDimensionPixelSize(R.styleable.MultiViewPager_android_maxWidth, -1));
        setMaxHeight(ta.getDimensionPixelSize(R.styleable.MultiViewPager_android_maxHeight, -1));
        setMatchChildWidth(ta.getResourceId(R.styleable.MultiViewPager_matchChildWidth, 0));
        ta.recycle();
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        size.set(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
        if (mMaxWidth >= 0 || mMaxHeight >= 0) {
            maxSize.set(mMaxWidth, mMaxHeight);
            constrainTo(size, maxSize);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    size.x,
                    MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    size.y,
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        onMeasurePage(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onMeasurePage(int widthMeasureSpec, int heightMeasureSpec) {
        // Only measure if a measurement pass was scheduled
        if (!mNeedsMeasurePage) {
            return;
        }
        if (mMatchWidthChildResId == 0) {
            mNeedsMeasurePage = false;
        } else if (getChildCount() > 0) {
            View child = getChildAt(0);
            child.measure(widthMeasureSpec, heightMeasureSpec);
            int pageWidth = child.getMeasuredWidth();
            View match = child.findViewById(mMatchWidthChildResId);
            if (match == null) {
                throw new NullPointerException(
                        "MatchWithChildResId did not find that ID in the first fragment of the ViewPager; "
                                + "is that view defined in the child view's layout? Note that MultiViewPager "
                                + "only measures the child for nextCall 0.");
            }
            int childWidth = match.getMeasuredWidth();
            // Check that the measurement was successful
           // MxyLog.d(TAG, "onMeasurePage" + "--childWidth=" + childWidth + "--pageWidth=" + pageWidth);
            if (childWidth > 0) {
                mNeedsMeasurePage = false;
                int difference = pageWidth - childWidth;
                setPageMargin(-difference);
                int offscreen = (int) Math.ceil((float) pageWidth / (float) childWidth) + 1;
                setOffscreenPageLimit(offscreen);
                requestLayout();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Schedule a new measurement pass as the dimensions have changed
        mNeedsMeasurePage = true;
    }

    /**
     * Sets the child view inside a page to match the page size against.
     *
     * @param matchChildWidthResId the child id
     */
    public void setMatchChildWidth(int matchChildWidthResId) {
        if (mMatchWidthChildResId != matchChildWidthResId) {
            mMatchWidthChildResId = matchChildWidthResId;
            mNeedsMeasurePage = true;
        }
    }

    /**
     * Sets the maximum size.
     *
     * @param width in pixels
     */
    public void setMaxWidth(int width) {
        mMaxWidth = width;
    }

    /**
     * Sets the maximum size.
     *
     * @param height in pixels
     */
    public void setMaxHeight(int height) {
        mMaxHeight = height;
    }
    
    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    	// TODO Auto-generated method stub
    	super.onLayout(arg0, arg1, arg2, arg3, arg4);
    	
    	final int scrollX = getScrollX();
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (lp.isDecor) continue;

            final float transformPos = (float) (child.getLeft() - scrollX) / getClientWidth();
            reSizeChildren(child, transformPos);
        }
    }
    
    private void reSizeChildren(View view, float position){

        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1)
        { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) //a椤垫粦鍔ㄨ嚦b椤� 锛� a椤典粠 0.0 -1 锛沚椤典粠1 ~ 0.0
        { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0)
            {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else
            {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        } else
        { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    
    }
    
    private int getClientWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }
}
