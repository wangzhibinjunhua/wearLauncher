package com.sczn.wearlauncher.db.util;

import java.math.BigDecimal;

import com.sczn.wearlauncher.db.bean.UserInfoConstant;

import android.content.Context;
import android.provider.Settings;

public class SportsUtil {

	public static double getDistance(Context context, int step){
        double distance = 0;    //单位公里
        double oneStepDistance;
        int height;
        height = Settings.System.getInt(context.getContentResolver(), UserInfoConstant.USER_HEIGHT, UserInfoConstant.DEFAULT_HEIGHT_VALUE);
        if(height<165){
            oneStepDistance = UserInfoConstant.USER_ONE_STEP_DISTANCE_SMALL;
        }else if(height<185){
            oneStepDistance = UserInfoConstant.USER_ONE_STEP_DISTANCE_NORMAL;
        }else {
            oneStepDistance = UserInfoConstant.USER_ONE_STEP_DISTANCE_BIG;
        }

        distance = step * oneStepDistance * 0.001;
        return new BigDecimal(distance).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

	public static double getCal(Context context, double distance){
        double cal = 0;

        int weight = Settings.System.getInt(context.getContentResolver(), UserInfoConstant.USER_WEIGHT,UserInfoConstant.DEFAULT_WEIGHT_VALUE);

        cal = weight * distance * 1.036;

        return new BigDecimal(cal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
