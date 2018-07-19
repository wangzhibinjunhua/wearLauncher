package com.sczn.wearlauncher.fragment.clockmenu;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.fragment.absDialogFragment;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.ScrollerTextView;
import com.sczn.wearlauncher.view.menu.MenuIconImage;

public class MenuChildrenTwoFragment extends absDialogFragment implements OnClickListener{
	private static final String TAG = MenuChildrenTwoFragment.class.getSimpleName();
	
	private static final String ARG_KEY_PATENT= "parent_menu";
	public static MenuChildrenTwoFragment newInstance(AppMenu parenrMenu){
		MenuChildrenTwoFragment f = new MenuChildrenTwoFragment();
		Bundle bdl = new Bundle();
		bdl.putParcelable(ARG_KEY_PATENT, parenrMenu);
		f.setArguments(bdl);
		return f;
	}
	
	private AppMenu mParentMenu;
	private ArrayList<AppMenu> mChildrenMenus;
	private String mParentName;
	
	private ScrollerTextView mTitle;
	private ScrollerTextView mLeftName;
	private ScrollerTextView mRightName;
	private MenuIconImage mLeftIcon;
	private MenuIconImage mRightIcon;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		if(bdl != null){
			mParentMenu = bdl.getParcelable(ARG_KEY_PATENT);
			if(mParentMenu != null){
				mChildrenMenus = mParentMenu.getChildrenList();
				mParentName = mParentMenu.getName(getActivity());
			}
			mChildrenMenus = mParentMenu.getChildrenList();
		}
		if(mChildrenMenus == null){
			mChildrenMenus = new ArrayList<AppMenu>();
		}
		if(mParentName == null){
			mParentName = "";
		}
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_clockmenu_menu_children_two;
	}

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTitle = findViewById(R.id.menu_children_two_title);
		mLeftIcon = findViewById(R.id.menu_child_left_icon);
		mLeftName = findViewById(R.id.menu_child_left_text);
		mRightIcon = findViewById(R.id.menu_child_right_icon);
		mRightName = findViewById(R.id.menu_child_right_text);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mTitle.setText(mParentName);
		
		if(mChildrenMenus.size() == 0){
			return;
		}
		if(mChildrenMenus.size() >= 1){
			final AppMenu leftMenu = mChildrenMenus.get(0);
			mLeftName.setText(leftMenu.getName(mLeftName.getContext()));
			mLeftIcon.setImageDrawable(leftMenu.getIcon(mLeftName.getContext()));
			mLeftIcon.setTag(leftMenu);
			mLeftIcon.setOnClickListener(this);
		}
		if(mChildrenMenus.size() >= 2){
			final AppMenu rightMenu = mChildrenMenus.get(1);
			mRightName.setText(rightMenu.getName(mLeftName.getContext()));
			mRightIcon.setImageDrawable(rightMenu.getIcon(mLeftName.getContext()));
			mRightIcon.setTag(rightMenu);
			mRightIcon.setOnClickListener(this);
		}
	}

	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menu_child_left_icon:
		case R.id.menu_child_right_icon:
			doMenuClick(v);
			break;

		default:
			break;
		}
	}

	private void doMenuClick(View v) {
		// TODO Auto-generated method stub
		if(!(v.getTag() instanceof AppMenu)){
			return;
		}
		AppMenu menu = (AppMenu) v.getTag();

		try {
			if(menu.getInfo() != null){
				Intent i = new Intent();
				i.setClassName(menu.getInfo().activityInfo.packageName,
						menu.getInfo().activityInfo.name);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			}else{
				final ResolveInfo info = SysServices.getPkMgr(getActivity()).
						resolveActivity(new Intent(menu.getIntent()), PackageManager.GET_INTENT_FILTERS);
				Intent i = new Intent();
				i.setClassName(info.activityInfo.packageName,
						info.activityInfo.name);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MxyToast.showShort(getActivity(), e.toString());
		}
	
	
	}

}
