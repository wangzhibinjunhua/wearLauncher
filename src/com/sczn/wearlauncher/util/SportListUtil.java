package com.sczn.wearlauncher.util;

import java.util.ArrayList;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.model.AppMenu;

public class SportListUtil {
	public static final String SETTING_KEY_MENU_SPORT_BG = "sczb_menu_sport_bg";
	public static final int MENU_SPORT_BG = 0;
	public static final int MENU_SPORT_BG_BW = 1;
	private static ArrayList<AppMenu> mSportList = new ArrayList<AppMenu>();
	private static int[] images = {
		R.drawable.sport_icon_walking,
		R.drawable.sport_run_outdoor,
		R.drawable.sport_run_indoor,
		R.drawable.sport_icon_mountain,
		R.drawable.sport_icon_cross_country,
		R.drawable.sport_icon_marathon_half,
		R.drawable.sport_icon_marathon_full,
		R.drawable.sport_icon_history,
		R.drawable.sport_icon_setting,	
	};
	private static int[] bg_images = {
		R.drawable.sport_bg_walking,
		R.drawable.sport_bg_outdoor,
		R.drawable.sport_bg_indoor,
		R.drawable.sport_bg_mountain,
		R.drawable.sport_bg_cross_country,
		R.drawable.sport_bg_marathon_half,
		R.drawable.sport_bg_marathon_full,
		R.drawable.sport_bg_history,
		R.drawable.sport_bg_setting,
	};
	private static int[] bg_images_bw = {
		R.drawable.sport_bg_walking_bw,
		R.drawable.sport_bg_outdoor_bw,
		R.drawable.sport_bg_indoor_bw,
		R.drawable.sport_bg_mountain_bw,
		R.drawable.sport_bg_cross_country_bw,
		R.drawable.sport_bg_marathon_half_bw,
		R.drawable.sport_bg_marathon_full_bw,
		R.drawable.sport_bg_history_bw,
		R.drawable.sport_bg_setting_bw,
	};
	private static int[] names = {
		R.string.sport_walking,
		R.string.sport_run_outdoor,
		R.string.sport_run_indoor,
		R.string.sport_mountain,
		R.string.sport_cross_country,
		R.string.sport_marathon_half,
		R.string.sport_marathon_full,
		R.string.sport_history,
		R.string.sport_setting,
	};
	private static String[] intents = {
		"com.sczn.action.SearchGPSActivity#0",
		"com.sczn.action.SearchGPSActivity#1",
		"com.sczn.action.SearchGPSActivity#2",
		"com.sczn.action.SearchGPSActivity#3",
		"com.sczn.action.SearchGPSActivity#4",
		"com.sczn.action.SearchGPSActivity#5",
		"com.sczn.action.SearchGPSActivity#6",
		"com.sczn.action.sports.History.HistoryMainActivity",
		"com.sczn.action.sports.setting.SportsSettings"
	};
	
	
	static{
		for(int i=0;i< images.length;i++){
			final AppMenu menu = new AppMenu();
			menu.setCustomImageRes(images[i]);
			menu.setCustomNameRes(names[i]);
			menu.setBgImageRes(bg_images[i]);
			menu.setBgImageBwRes(bg_images_bw[i]);
			final String[] intent = intents[i].split("#");
			menu.setIntent(intent[0]);
			if(intent.length == 2){
				menu.setIntentData(Integer.parseInt(intent[1]));
			}
			mSportList.add(menu);
		}
	}

	
	public static ArrayList<AppMenu> getSportList(){
		if(mSportList == null){
			mSportList = new ArrayList<AppMenu>();
			
		}
		return mSportList;
	}
}
