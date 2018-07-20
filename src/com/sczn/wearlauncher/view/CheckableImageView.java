package com.sczn.wearlauncher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

public class CheckableImageView extends ImageView implements Checkable{  
	  
    /** @param context 
     * @param attrs */  
    public CheckableImageView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
      
    private boolean mChecked = false;  
    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };  
  
  
    @Override  
    public int[] onCreateDrawableState(int extraSpace) {  
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);  
        if (isChecked()) {  
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);  
        }  
        return drawableState;  
    }  
      
    @Override  
    public void setChecked(boolean checked) {  
        // TODO Auto-generated method stub  
        if (mChecked != checked) {  
            mChecked = checked;  
            refreshDrawableState();  
        }  
    }  
  
    @Override  
    public boolean isChecked() {  
        // TODO Auto-generated method stub  
        return mChecked;  
    }  
  
    @Override  
    public void toggle() {  
        // TODO Auto-generated method stub  
        setChecked(!mChecked);  
    }  
  
}  
