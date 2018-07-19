package com.sczn.wearlauncher.fragment.clockmenu;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.adapter.MenuVirticalAdapter;
import com.sczn.wearlauncher.adapter.MenuVirticalAdapter.IMenuVerticalClickListen;
import com.sczn.wearlauncher.fragment.absDialogFragment;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.util.MxyToast;
import com.sczn.wearlauncher.util.SysServices;
import com.sczn.wearlauncher.view.menu.MenuVirticalRecyleView;

public class MenuChildrenFragment extends absDialogFragment implements IMenuVerticalClickListen{
	private final static String TAG = MenuChildrenFragment.class.getSimpleName();
	public static final String ARG_KEY_CHILDREN_LIST = "children_list";
	public static MenuChildrenFragment newInstance(ArrayList<AppMenu> childrenList){
		MenuChildrenFragment f = new MenuChildrenFragment();
		Bundle bdl = new Bundle();
		bdl.putParcelableArrayList(ARG_KEY_CHILDREN_LIST, childrenList);
		f.setArguments(bdl);
		return f;
	}

	private ArrayList<AppMenu> mChildrenList;
	
	private MenuVirticalRecyleView menuChildRecyleView;
	private MenuVirticalAdapter mMenuChildAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bdl = getArguments();
		if(bdl != null){
			mChildrenList = bdl.getParcelableArrayList(ARG_KEY_CHILDREN_LIST);
		}
		if(mChildrenList == null){
			mChildrenList = new ArrayList<AppMenu>();
		}
	}
	
	@Override
	protected int getLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_clockmenu_menu_children;
	}

	@Override
	protected void creatView() {
		// TODO Auto-generated method stub
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		menuChildRecyleView = findViewById(R.id.menu_child);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mMenuChildAdapter = new MenuVirticalAdapter(true, false);
		mMenuChildAdapter.setMenuClickListem(this);
		mMenuChildAdapter.setMenus(mChildrenList);
		menuChildRecyleView.initLayoutManager(LinearLayoutManager.VERTICAL, false);
		menuChildRecyleView.setAdapter(mMenuChildAdapter);
		menuChildRecyleView.setVelocityScale(4);
	}

	@Override
	protected void destorytView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVerticalMenuClick(View view) {

		// TODO Auto-generated method stub
		if(!(view.getTag() instanceof AppMenu)){
			return;
		}
		AppMenu menu = (AppMenu) view.getTag();

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
