package com.sczn.wearlauncher.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mengmeng on 2016/4/12.
 */
public class DINCondTextView extends ScrollerTextView {
    public DINCondTextView(Context context) {
        super(context);
    }
    public DINCondTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        changeTypeFace(context, attrs);
    }
    /**
     * 改变字体类型
     * @param context
     * @param attrs
     */
    private void changeTypeFace(Context context, AttributeSet attrs){
        if (attrs != null){
            //TypedArray a = context.obtainStyledAttributes(attrs,
            //R.styleable.TextView_Typefaces);
            //            tf = a.getInt(R.styleable.TextView_Typefaces_tf, tf);
            Typeface mtf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/DINCond-Medium.otf");
            super.setTypeface(mtf);
        }
    }
}
