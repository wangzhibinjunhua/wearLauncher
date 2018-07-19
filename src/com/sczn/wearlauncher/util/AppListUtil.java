package com.sczn.wearlauncher.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sczn.wearlauncher.LauncherApp;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.model.AppMenu;
import com.sczn.wearlauncher.receivers.AbsBroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Xml;

public class AppListUtil extends Observable implements Parcelable{
	private final static String TAG = AppListUtil.class.getSimpleName();
	
	public static final String SETTING_KEY_STYLE = "menu_style";
	public static final String SETTING_KEY_SKIN= "menu_skin";
	
	public static final String SKIN_PATH1 = "menuskin/skin1/";
	public static final String SKIN_PATH2 = "menuskin/skin2/";
	
	public static final int MENU_STYLE_VERTICAL = 1; 
	public static final int MENU_STYLE_SQUARE = 2; 
	public static final int MENU_STYLE_CIRCLE = 3; 
	
	public static final int OBSERVER_DATA_STYLE = 0;
	public static final int OBSERVER_DATA_MENU_SKIN = 1;
	public static final int OBSERVER_DATA_MENU_MORE = 2;
	public static final int OBSERVER_DATA_MENU = 3;
	
	private static ArrayList<String> mAppHides = new ArrayList<String>();
	
	static{
		mAppHides.add("com.sczn.wearlauncher.activity.WearLauncherActivity");
		mAppHides.add("com.sohu.inputmethod.sogou.SogouIMELauncher");
		mAppHides.add("com.android.stk.StkLauncherActivity");
		mAppHides.add("com.android.inputmethod.latin.setup.SetupActivity");
		mAppHides.add("com.google.android.googlequicksearchbox.SearchActivity");
		mAppHides.add("com.google.android.gms.app.settings.GoogleSettingsActivity");
		
		mAppHides.add("com.baidu.input.ImeMainConfigActivity");
		mAppHides.add("com.mediatek.StkSelection.StkSelection");
	}
	
	private static AppListUtil instance;
	public static AppListUtil getInctance(){
		if(instance == null){
			synchronized (AppListUtil.class) {
				instance = new AppListUtil();
			}
		}
		return instance;
	}
	public static void startMgr(Context context){
		getInctance().startFresh(context.getApplicationContext());
	}
	public static void stopMgr(Context context){
		if(instance != null){
			instance.stopFresh(context);
		}
	}
	
	public static String getMenuSkinPath(Context context,String menuImage){
		return SysServices.getSystemSettingString(context, SETTING_KEY_SKIN,SKIN_PATH1) + menuImage;
	}
	
	public static int getMenuStyle(Context context){
		return SysServices.getSystemSettingInt(context, SETTING_KEY_STYLE, MENU_STYLE_VERTICAL);
	}
	
	public static String getMenuSkin(Context context){
		return SysServices.getSystemSettingString(context, SETTING_KEY_SKIN,SKIN_PATH1);
	}
	
	public static void setMenuSkin(Context context,String value){
		SysServices.setSystemSettingString(context, SETTING_KEY_SKIN, value);
	}
	public static void setMenuStyle(Context context,int value){
		SysServices.setSystemSettingInt(context, SETTING_KEY_STYLE, value);
	}
	
	private AppListTask mAppListTask;
	private AppListReceive mAppListReceive;
	private AppListObserve mAppListObserve;
	private ArrayList<AppMenu> mAppList;
	private AppMenu mMoreMenu;
	private AppMenu mAssistantMenu;
	private ArrayList<String> mAndroidAssistFilter;
	private ArrayList<String> mIosAssistFilter;
	private HashMap<String, Drawable> mMenuIconCache;
	
	private AppListUtil(){
		mAppListReceive = new AppListReceive();
		mAppListObserve = new AppListObserve(new Handler());
		mAppList = new ArrayList<AppMenu>();
		mMenuIconCache = new HashMap<String, Drawable>();
	}
	
	private Context getAppContext(){
		return LauncherApp.appContext;
	}
	
	public void startFresh(Context context){
		mAppListReceive.register(context);
		mAppListObserve.register(context);
		executeTask();
		
	}
	public void stopFresh(Context context){
		stopTask();
		mAppListReceive.unRegister(context);
		mAppListObserve.unRegister(context);
	}
	public void executeTask(){
		stopTask();
		mAppListTask = new AppListTask();
		mAppListTask.execute();
	}
	private void stopTask(){
		if(mAppListTask != null){
			mAppListTask.cancel(true);
			mAppListTask = null;
		}
	}
	public ArrayList<AppMenu> getAppList(){
		return mAppList;
	}
	public AppMenu getMoreMenu(){
		return mMoreMenu;
	}
	public AppMenu getAssistantMenu(){
		return mAssistantMenu;
	}
	public ArrayList<String> getIosAssistFilter(){
		return mIosAssistFilter;
	}
	public ArrayList<String> getAndroidAssistFilter(){
		return mAndroidAssistFilter;
	}
	public Drawable getMenuIcon(String intent){
		return mMenuIconCache.get(intent);
	}
	public void cachetMenuIcon(String intent, Drawable drawable){
		mMenuIconCache.put(intent, drawable);
	}
	public void clearMenuIconCache(){
		mMenuIconCache.clear();
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
	private void applistChanged(ArrayList<AppMenu> result) {
		// TODO Auto-generated method stub
		//MxyLog.d(TAG, "applistChanged");
		final boolean oldHasMore = (mMoreMenu == null
				|| mMoreMenu.getChildrenList() == null) ? false : true;
		final boolean newHasMore;
		if(result != null){
			mMoreMenu = result.get(result.size() - 1);
			if(mMoreMenu.getChildrenList() == null){
				result.remove(result.size() - 1);
				newHasMore = false;
			}else{
				newHasMore = true;
			}
			mAppList.clear();
			mAppList.addAll(result);
			setChanged();
			if(oldHasMore != newHasMore){
				notifyObservers(Integer.valueOf(OBSERVER_DATA_MENU));
			}else{
				//notifyObservers(Integer.valueOf(OBSERVER_DATA_MENU_MORE));
				notifyObservers(Integer.valueOf(OBSERVER_DATA_MENU));
			}
		}
	}
	
	private void appStyleChanged(){
		setChanged();
		notifyObservers(Integer.valueOf(OBSERVER_DATA_STYLE));
	}
	private void appSkinChanged(){
		//MxyLog.d(TAG, "appSkinChanged");
		setChanged();
		notifyObservers(Integer.valueOf(OBSERVER_DATA_MENU_SKIN));
	}
	
	private class AppListObserve extends ContentObserver{

		private Uri styleUri;
		private Uri skinUri;
		public AppListObserve(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		
		public void register(Context context){
			styleUri = getStyleUri(context);
			skinUri = getSkinUri(context);
			if(styleUri != null){
				context.getContentResolver().registerContentObserver(styleUri, true,this);
			}
			if(skinUri != null){
				context.getContentResolver().registerContentObserver(skinUri, true,this);
			}
		}
		
		public void unRegister(Context context){
			context.getContentResolver().unregisterContentObserver(this);
		}
		private Uri getStyleUri(Context context){
			final Uri uri = Settings.System.getUriFor(SETTING_KEY_STYLE);
			if(uri == null){
				SysServices.setSystemSettingInt(context, SETTING_KEY_STYLE, MENU_STYLE_VERTICAL);
			}
			return Settings.System.getUriFor(SETTING_KEY_STYLE);
		}
		private Uri getSkinUri(Context context){
			final Uri uri = Settings.System.getUriFor(SETTING_KEY_SKIN);
			if(uri == null){
				SysServices.setSystemSettingString(context, SETTING_KEY_SKIN, SKIN_PATH1);
			}
			return Settings.System.getUriFor(SETTING_KEY_SKIN);
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			
			//MxyLog.d(TAG, "onChange uri=" + uri);
			if(styleUri.equals(uri)){
				appStyleChanged();
				return;
			}
			if(skinUri.equals(uri)){
				appSkinChanged();
				return;
			}
		}
		
		
	}
	private class AppListReceive extends AbsBroadcastReceiver{
		
		@Override
		public void register(Context context) {
			// TODO Auto-generated method stub
			context.registerReceiver(this, getIntentFilter());
		}
		
		@Override
		public void unRegister(Context context) {
			// TODO Auto-generated method stub
			context.unregisterReceiver(this);
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())
					|| Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())
					//|| Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())
					|| Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
				MxyLog.d(TAG, "intent.getAction()=" + intent.getAction());
				executeTask();
			}
		}
		@Override
		public IntentFilter getIntentFilter() {
			// TODO Auto-generated method stub
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_PACKAGE_ADDED);
			//filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
			filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
			//filter.addAction(Intent.ACTION_LOCALE_CHANGED);
			filter.addDataScheme("package");
			return filter;
		}
		
	}
	private class AppListTask extends AsyncTask<Void, Void, ArrayList<AppMenu>>{
		private static final String XML_PARSER_TAG_MENU = "menu";
		private static final String XML_PARSER_TAG_CHILDREN = "children";
		private  HashMap<String, ResolveInfo> activityMap;
		private ArrayList<AppMenu> tempList;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			activityMap = new HashMap<String, ResolveInfo>();
			tempList = new ArrayList<AppMenu>();
		}

		@Override
		protected ArrayList<AppMenu> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			initActivityMap();

            try {
            	AppMenu menu = null;
    			int type;
    			String tagName;
    			if(getAppContext() == null){
    				cancel(true);
    				return null;
    			}
    			XmlResourceParser parser = getAppContext().getResources().getXml(R.xml.menu_app_list);
                AttributeSet attrs = Xml.asAttributeSet(parser);
                
				do {
					type = parser.getEventType();
					switch (type) {
						case XmlPullParser.START_DOCUMENT:
							if(tempList == null){
								tempList = new ArrayList<AppMenu>();
		                    }else {
		                    	tempList.clear();
		                    }
							break;
						case XmlResourceParser.START_TAG:
							tagName = parser.getName();
							if(XML_PARSER_TAG_MENU.equals(tagName)){
								menu = newMenu(attrs);
							}else if(XML_PARSER_TAG_CHILDREN.equals(tagName)){
								if(menu != null){
									menu.addChildren(newMenu(attrs));
								}
							}
							break;
						case XmlResourceParser.END_TAG:
							tagName = parser.getName();
							if(XML_PARSER_TAG_MENU.equals(tagName)){
								if(menu != null){
									addMenu(menu);
									menu = null;
								}
							}
							break;
						default:
							break;
					}
				} while (!isCancelled() && parser.next() != XmlPullParser.END_DOCUMENT);
				customAppMenu();
				
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return tempList;
		}
		
		private AppMenu newMenu(AttributeSet attrs){
			final AppMenu menu = new AppMenu();
			if(getAppContext() == null){
				cancel(true);
				return menu;
			}
			TypedArray a = getAppContext().obtainStyledAttributes(attrs, R.styleable.Menu);
			menu.setCustomImageName(a.getString(R.styleable.Menu_CustomIconName));
			menu.setCustonName(a.getString(R.styleable.Menu_CustomName));
			menu.setIntent(a.getString(R.styleable.Menu_Intent));
			menu.setMenuType(a.getInteger(R.styleable.Menu_Type, AppMenu.MENU_TYPE_NORMAL));
			a.recycle();
			return menu;
		}
		
		private void addMenu(AppMenu menu){
			if(isCancelled()){
				return;
			}
			if(menu.getIntent() != null){
				final ResolveInfo info = activityMap.get(menu.getIntent());
				//MxyLog.i(TAG, "addMenu--menu.getIntent()=" + menu.getIntent());
				if(info != null){
					menu.setInfo(info);
					//MxyLog.i(TAG, "addMenu " + menu.getIntent());
					activityMap.remove(menu.getIntent());
				}else{
					
					if(AppMenu.MENU_TYPE_NORMAL == menu.getMenuType()){
						//����δ��װapp
						return;
					}
				}
			}
			
			if(menu.getChildrenList() != null){
				for(AppMenu childmenu:menu.getChildrenList()){
					//MxyLog.i(TAG, "addMenu--childrenmenu.getIntent()=" + childmenu.getIntent());
					final ResolveInfo childInfo = activityMap.get(childmenu.getIntent());

					if(childInfo != null){
						childmenu.setInfo(childInfo);
						//MxyLog.i(TAG, "addchildren " + icon.getIntent());
						activityMap.remove(childmenu.getIntent());
					}
				}
				if(AppMenu.MENU_TYPE_BTREMOTE == menu.getMenuType()){
					mAssistantMenu = menu;      // only for filter,donnot add in list to show
					return;
				}
				
				if(AppMenu.MENU_TYPE_ANDROID_ASSIST == menu.getMenuType()){
					if(mAndroidAssistFilter == null){
						mAndroidAssistFilter = new ArrayList<String>();
					}
					mAndroidAssistFilter.clear();
					for(AppMenu childmenu:menu.getChildrenList()){
						mAndroidAssistFilter.add(childmenu.getIntent());
					}
				}
				if(AppMenu.MENU_TYPE_IOS_ASSIST == menu.getMenuType()){
					if(mIosAssistFilter == null){
						mIosAssistFilter = new ArrayList<String>();
					}
					mIosAssistFilter.clear();
					for(AppMenu childmenu:menu.getChildrenList()){
						mIosAssistFilter.add(childmenu.getIntent());
					}
				}
			}
			tempList.add(menu);
		}

		@Override
		protected void onPostExecute(ArrayList<AppMenu> result) {
			// TODO Auto-generated method stub
			if(isCancelled()){
				return;
			}
			applistChanged(result);
		}

		private void initActivityMap(){
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
	        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	        if(getAppContext() == null){
	        	cancel(true);
	        	return;
	        }
	        final List<ResolveInfo> appList = SysServices.getPkMgr(getAppContext()).queryIntentActivities(mainIntent, 0);
	       // MxyLog.d(TAG, "initActivityMap--start");
	        for(ResolveInfo info:appList){
	        	//MxyLog.i(TAG, "initActivityMap--info.activityInfo.name=" + info.activityInfo.name);
	        	activityMap.put(info.activityInfo.name, info);
	        }
	        //MxyLog.d(TAG, "initActivityMap--end");
		}
		private void customAppMenu(){
			AppMenu moreMenu = new AppMenu();
			for(ResolveInfo info : activityMap.values()){
				//MxyLog.i(TAG, "customAppMenu--" + info.activityInfo.name);
				if(mAppHides.contains(info.activityInfo.name)){
					continue;
				}
				final AppMenu menu = new AppMenu();
				MxyLog.i(TAG,"tempList=" + tempList.size() +  "name=" + info.activityInfo.name);
				menu.setInfo(info);
				moreMenu.addChildren(menu);
			}

			moreMenu.setCustomImageName("more.png");
			moreMenu.setMenuType(AppMenu.MENU_TYPE_MORE);
			moreMenu.setCustomNameRes(R.string.menu_more);
			tempList.add(moreMenu);
		}
	}
}
