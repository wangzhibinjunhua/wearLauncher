package com.sczn.wearlauncher.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.db.bean.SleepState;
import com.sczn.wearlauncher.util.AppListUtil;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.util.SysServices;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;


public class AppMenu implements Parcelable{
	private static final String TAG = AppMenu.class.getSimpleName();
	public static final int RES_INVALUED = Integer.MIN_VALUE;
	
	public static final int MENU_TYPE_NORMAL = 0;
	public static final int MENU_TYPE_FOLDER = 1;
	public static final int MENU_TYPE_BTREMOTE = 2;
	public static final int MENU_TYPE_MORE= 3;
	
	public static final int MENU_TYPE_ANDROID_ASSIST= 4;
	public static final int MENU_TYPE_IOS_ASSIST = 5;
	
	private String custonName;
	private String customImageName;
	private String intent;
	private int intentData = RES_INVALUED;    // for sport
	private ResolveInfo info;
	private ArrayList<AppMenu> childrenList;
	private int menuType = MENU_TYPE_NORMAL;

	private int bgImageRes = RES_INVALUED;
	private int bgImagesBwRes = RES_INVALUED;
	private int customNameRes = RES_INVALUED;
	private int customImageRes = RES_INVALUED;
	
	private ArrayList<String> filtChildren;		// fot assist
	
	public AppMenu(){
	}
	
	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}
	
	public ArrayList<String> getFiltChildren() {
		return filtChildren;
	}

	public void setFiltChildren(ArrayList<String> filtChildren) {
		this.filtChildren = filtChildren;
	}
	
	public int getIntentData() {
		return intentData;
	}

	public void setIntentData(int intentData) {
		this.intentData = intentData;
	}

	public void setCustomNameRes(int customNameRes) {
		this.customNameRes = customNameRes;
	}

	public void setCustomImageRes(int customImageRes) {
		this.customImageRes = customImageRes;
	}
	
	public int getBgImageRes() {
		return bgImageRes;
	}

	public void setBgImageRes(int bgImageRes) {
		this.bgImageRes = bgImageRes;
	}
	public int getBgImageBwRes() {
		return bgImagesBwRes;
	}

	public void setBgImageBwRes(int bgImageBwRes) {
		this.bgImagesBwRes = bgImageBwRes;
	}


	public String getName(Context context){
		if(customNameRes != RES_INVALUED){
			return context.getString(customNameRes);
		}
		if(getCustonName() != null){
			return getCustonName();
		}
		if(getInfo() != null){
			//MxyLog.d(this, (String) getInfo().loadLabel(SysServices.getPkMgr(context)));
			return (String) getInfo().loadLabel(SysServices.getPkMgr(context));
		}
		return context.getString(R.string.Unkown);
	}
	
	public Drawable getIconWithoutCache(Context context){

		if(customImageRes != RES_INVALUED){
			return context.getResources().getDrawable(customImageRes);
		}
		
		if(getCustomImageName() != null){
			InputStream in;
			Bitmap bitmap = null;

				try {
					in = context.getAssets().open(AppListUtil.getMenuSkinPath(context, getCustomImageName()));
					bitmap = BitmapFactory.decodeStream(in);
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					MxyLog.e(TAG, "getDrawable" + "--IOException=" + e.toString());
				}
			if(bitmap == null){
				return context.getResources().getDrawable(R.drawable.ic_launcher);
			}
			return new BitmapDrawable(context.getResources(),bitmap);
		}
		
		if(getInfo() != null){
			return getInfo().loadIcon(SysServices.getPkMgr(context));
		}
		
		return context.getResources().getDrawable(R.drawable.ic_launcher);
	
	}
	public Drawable getIcon(Context context){
		if(customImageRes != RES_INVALUED){
			return context.getResources().getDrawable(customImageRes);
		}
		
		if(getCustomImageName() != null){
			Drawable drawable= AppListUtil.getInctance().getMenuIcon(customImageName);
			if(drawable != null){
				return drawable;
			}
			InputStream in;
			Bitmap bitmap = null;

				try {
					in = context.getAssets().open(AppListUtil.getMenuSkinPath(context, getCustomImageName()));
					bitmap = BitmapFactory.decodeStream(in);
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					MxyLog.e(TAG, "getDrawable" + "--IOException=" + e.toString());
				}
			if(bitmap == null){
				return context.getResources().getDrawable(R.drawable.ic_launcher);
			}
			drawable = new BitmapDrawable(context.getResources(),bitmap);
			AppListUtil.getInctance().cachetMenuIcon(customImageName, drawable);
			return drawable;
		}
		
		if(getInfo() != null){
			return getInfo().loadIcon(SysServices.getPkMgr(context));
		}
		
		return context.getResources().getDrawable(R.drawable.ic_launcher);
	}

	public String getCustonName() {
		return custonName;
	}

	public void setCustonName(String custonName) {
		this.custonName = custonName;
	}

	public String getCustomImageName() {
		return customImageName;
	}

	public void setCustomImageName(String customImageName) {
		this.customImageName = customImageName;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public ArrayList<AppMenu> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(ArrayList<AppMenu> childrenList) {
		this.childrenList = childrenList;
	}
	public void addChildren(AppMenu childrenMenu){
		if(childrenList == null){
			childrenList = new ArrayList<AppMenu>();
		}
		childrenList.add(childrenMenu);
	}

	public ResolveInfo getInfo() {
		return info;
	}

	public void setInfo(ResolveInfo info) {
		this.info = info;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(custonName);
		dest.writeString(customImageName);
		dest.writeString(intent);
		dest.writeParcelable(info, flags);
		dest.writeTypedList(childrenList);
		/*
		if(childrenList != null && childrenList.size() > 0){
			final AppMenu[] childrems = new AppMenu[childrenList.size()];
			dest.writeParcelableArray(childrenList.toArray(childrems), flags);
		}else{
			dest.writeParcelableArray(null, flags);
		}*/
		dest.writeStringList(filtChildren);
	}
	
	public static final Creator<AppMenu> CREATOR = new Creator<AppMenu>() {
		public AppMenu createFromParcel(Parcel source) {
			final AppMenu menu = new AppMenu();
			menu.setCustonName(source.readString());
			menu.setCustomImageName(source.readString());
			menu.setIntent(source.readString());
			
			menu.setInfo((ResolveInfo) source.readParcelable(ResolveInfo.class.getClassLoader()));
			
			final ArrayList<AppMenu> childrens = new ArrayList<AppMenu>();
			source.readTypedList(childrens, AppMenu.CREATOR);
			
			if(childrens != null && childrens.size() > 0){
				menu.setChildrenList(childrens);
			}
			/*
			final AppMenu[] childrens = (AppMenu[]) source.readParcelableArray(AppMenu.class.getClassLoader());
			for(AppMenu child : childrens){
				menu.addChildren(child);
			}*/
			final ArrayList<String> filts = new ArrayList<String>();
			source.readStringList(filts);
			if(filts != null && filts.size() > 0){
				menu.setFiltChildren(filts);
			}
		    return menu;
		}
		public AppMenu[] newArray(int size) {
		    return new AppMenu[size];
		}
	};
	
	
}
