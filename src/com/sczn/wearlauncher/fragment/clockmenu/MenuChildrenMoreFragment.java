package com.sczn.wearlauncher.fragment.clockmenu;


import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sczn.wearlauncher.Config;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.activity.BtStyleChooseActivity;
import com.sczn.wearlauncher.adapter.MenuChildrenMoreAdapter;
import com.sczn.wearlauncher.fragment.absDialogFragment;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.transformer.ZoomOutPageTransformerSecondMenu;
import com.sczn.wearlauncher.util.AppListUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.MultiViewPager;
import com.sczn.wearlauncher.view.ViewPagerIndicator;
import com.sczn.wearlauncher.view.HorizalViewPager.IHorizalViewPagerSelected;
import com.sczn.wearlauncher.view.menu.MenuMore;

public class MenuChildrenMoreFragment extends absDialogFragment implements IHorizalViewPagerSelected{
	private static final String TAG = MenuChildrenMoreFragment.class.getSimpleName();
	
	public static final int BT_STYLE_REQUSET_CODE = 0;
	private static final String ARG_KEY_PATENT = "parent_menu";
	
	public static MenuChildrenMoreFragment newInstance(AppMenu parentMenu){
		MenuChildrenMoreFragment f = new MenuChildrenMoreFragment();
		Bundle bdl = new Bundle();
		bdl.putParcelable(ARG_KEY_PATENT, parentMenu);
		f.setArguments(bdl);
		return f;
	}
	
	private AppMenu mParentMenu;
	private ArrayList<AppMenu> mChildrenMenus;
	private ArrayList<AppMenu> mEnsureMenus;
	private String mParentName;
	private MultiViewPager mMenuChildViewPager;
	private MenuChildrenMoreAdapter mMenuChildrenMoreAdapter;
	private TextView mTitle;
	private ViewPagerIndicator mMenuViewPageIndicator;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		mChildrenMenus = new ArrayList<AppMenu>();
		if(bdl != null){
			mParentMenu = bdl.getParcelable(ARG_KEY_PATENT);
			if(mParentMenu != null){
				mChildrenMenus.addAll(mParentMenu.getChildrenList());
				mParentName = mParentMenu.getName(getActivity());
			}
		}
		if(mParentMenu == null){
			MxyLog.e(TAG, "mParentMenu == null");
		}
		if(mParentName == null){
			mParentName = "";
		}
		ensureMenus(getBtStyle());
	}
	
	public AppMenu getParentMenu(){
		return mParentMenu;
	}
	
	private int getBtStyle(){
		return BtStyleChooseActivity.BT_STYLE_UNKNOW;
	}
	
	private void ensureMenus(int style){
		if(mEnsureMenus == null){
			mEnsureMenus = new ArrayList<AppMenu>();
		}
		mEnsureMenus.clear();
		
		if(mParentMenu.getFiltChildren() != null){
			final ArrayList<String> filt = mParentMenu.getFiltChildren();
			for(AppMenu menu:mChildrenMenus){
				if(filt.contains(menu.getIntent())){
					mEnsureMenus.add(menu);
				}
			}
			return;
		}
		
		if(!(AppMenu.MENU_TYPE_BTREMOTE == mParentMenu.getMenuType()) 
				|| (!Config.isBleVersion(getActivity()))){
			mEnsureMenus.addAll(mChildrenMenus);
			initMenus();
			return;
		}
		
		switch (style) {
			case BtStyleChooseActivity.BT_STYLE_UNKNOW:
				Intent i = new Intent(getActivity(), BtStyleChooseActivity.class);
				startActivityForResult(i, BT_STYLE_REQUSET_CODE);
				return;
			case BtStyleChooseActivity.BT_STYLE_ANDROID:
				mEnsureMenus.addAll(mChildrenMenus);
				break;
			case BtStyleChooseActivity.BT_STYLE_IOS:
				for(AppMenu menu:mChildrenMenus){
					if("com.example.bluetoothwat.RemoteMusicActivity".equals(menu.getIntent())
							|| "com.example.bluetoothwat.WatchLockPhoneActivity".equals(menu.getIntent())
							|| "com.example.bluetoothwat.QRcodeAcitivity".equals(menu.getIntent())){
						continue;
					}
					mEnsureMenus.add(menu);
				}
				break;
			default:
				break;
		}

		initMenus();
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_clockmenu_menu_children_more;
	}
	
	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		mMenuChildViewPager = findViewById(R.id.menu_children_more);
		mTitle = findViewById(R.id.menu_children_more_title);
		mMenuViewPageIndicator = findViewById(R.id.menu_child_page_ind);
		
		mTitle.setText(mParentName);
		mMenuChildViewPager.setHorizalViewPagerSelected(this);
		mMenuChildrenMoreAdapter = new MenuChildrenMoreAdapter(getActivity());
		mMenuChildViewPager.setPageTransformer(true, new ZoomOutPageTransformerSecondMenu());
		mMenuChildViewPager.setAdapter(mMenuChildrenMoreAdapter);

		initMenus();
	}
	
	private void initMenus(){
		
		if(mMenuChildViewPager == null){
			return;
		}
		
		
		final ArrayList<MenuMore> menus = new ArrayList<MenuMore>();
		for(AppMenu menu:mEnsureMenus){
			final MenuMore view = (MenuMore) LayoutInflater.from(mMenuChildViewPager.getContext()).inflate(
					R.layout.item_menu_children_more, null);
			view.setMenu(menu);
			menus.add(view);
		}
		
		setMenus(menus);
	}
	
	private void initIndicator(){
		mMenuViewPageIndicator.init(mMenuChildrenMoreAdapter.getCount(),ViewPagerIndicator.TYPE_NORMAL);
		mMenuViewPageIndicator.setSelect(mMenuChildViewPager.getCurrentItem());
	}
	
	private void setMenus(ArrayList<MenuMore> items){
		if(mMenuChildrenMoreAdapter != null){
			mMenuChildrenMoreAdapter.setItems(items);
			mMenuChildViewPager.setCurrentItem(getDefaultPagerCurrIndex());
			initIndicator();
		}
	}
	
	public void freshMoreMenus(){
		if(mParentMenu != null && (AppMenu.MENU_TYPE_MORE == mParentMenu.getMenuType())){
			mParentMenu = AppListUtil.getInctance().getMoreMenu();
			if(mParentMenu != null){
				if(mParentMenu.getChildrenList() == null || mParentMenu.getChildrenList().size() == 0){
					dismissAllowingStateLoss();
				}else{
					getArguments().putParcelable(ARG_KEY_PATENT, mParentMenu);

					if(mEnsureMenus == null){
						mEnsureMenus = new ArrayList<AppMenu>();
					}
					mEnsureMenus.clear();
					mEnsureMenus.addAll(mParentMenu.getChildrenList());
					
					initMenus();
				}
			}
		}
	}
	
	private int getDefaultPagerCurrIndex() {
		// TODO Auto-generated method stub
		return mMenuChildViewPager.getCurrentItem();
	}

	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void horizalViewPageSelected(int index) {
		// TODO Auto-generated method stub
		mMenuViewPageIndicator.setSelect(index);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case BT_STYLE_REQUSET_CODE:
				if(BtStyleChooseActivity.RESULT_CODE_OK == resultCode){
					ensureMenus(SysServices.getSystemSettingInt(getActivity(),
							BtStyleChooseActivity.SETTING_KEY_BT_STYLE, BtStyleChooseActivity.BT_STYLE_ANDROID));
				}else{
					dismissAllowingStateLoss();
					
				}
				break;
	
			default:
				break;
		}
	}
}
