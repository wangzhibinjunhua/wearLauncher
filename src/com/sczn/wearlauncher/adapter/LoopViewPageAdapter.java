package com.sczn.wearlauncher.adapter;

import java.util.ArrayList;

import com.sczn.wearlauncher.fragment.absFragment;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.view.HorizalViewPager;
import com.sczn.wearlauncher.view.VerticalViewPager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;


public class LoopViewPageAdapter extends PagerAdapter {
	
	private static final String TAG = LoopViewPageAdapter.class.getSimpleName();
	
	private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    
    private ArrayList<absFragment> mList;
    
    private boolean needLoop = false;
    private boolean isLoop = false;
    private ViewGroup mViewPage;

    public LoopViewPageAdapter(FragmentManager fm,boolean needLoop,ViewGroup mViewPage) {
		// TODO Auto-generated constructor stub
    	mList = new ArrayList<absFragment>();

    	mFragmentManager = fm;
    	this.mViewPage = mViewPage;
    	this.needLoop = needLoop;
	}
	public void setList(ArrayList<absFragment> list){

		if(needLoop && list.size() > 1){
			
			isLoop = true;
		}
		
		mList = list;
		
    	notifyDataSetChanged();
    }
	
	public boolean isLoop(){
		return isLoop;
	}

    /**
     * Return the Fragment associated with a specified position.
     */
    public Fragment getItem(int position){
    	return mList.get(position);
    }

    @Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
    public void startUpdate(ViewGroup container) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            //MxyLog.d(TAG , "Attaching item #" + itemId + ": f=" + fragment);
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            //MxyLog.d(TAG , "Adding item #" + itemId + ": f=" + fragment);

            mCurTransaction.add(container.getId(), fragment,
                        makeFragmentName(container.getId(), itemId));
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        //MxyLog.d(TAG , "Detaching item #" + getItemId(position) + ": f=" + object+ " v=" + ((Fragment)object).getView());
        mCurTransaction.detach((Fragment)object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment)object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    /**
     * Return a unique identifier for the item at the given position.
     *
     * <p>The default implementation returns the given position.
     * Subclasses should override this method if the positions of items can change.</p>
     *
     * @param position Position within this adapter
     * @return Unique identifier for the item at position
     */
    public long getItemId(int position) {
        return position;
    }

    public static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
