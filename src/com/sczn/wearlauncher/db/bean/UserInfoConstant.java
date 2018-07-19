package com.sczn.wearlauncher.db.bean;

import java.math.BigDecimal;

/**
 * Created by mengmeng on 2016/3/28.
 */
public class UserInfoConstant {
    public static final String  USER_SEX = "user_sex";
    public static final String  USER_AGE = "user_age";
    public static final String  USER_HEIGHT = "user_height";
    public static final String  USER_WEIGHT = "user_weight";
    public static final String  USER_STEP_TARGET = "suggest_steps";
    public static final String  USER_KCAL_TARGET = "suggest_kcal";

    public static final double USER_ONE_STEP_DISTANCE_SMALL = 0.6; //单位m
    public static final double USER_ONE_STEP_DISTANCE_NORMAL = 0.7;
    public static final double USER_ONE_STEP_DISTANCE_BIG = 0.8;

    public static final int DEFAULT_SEX_VALUE = 0;
    public static final int DEFAULT_AGE_VALUE = 24;
    public static final int DEFAULT_HEIGHT_VALUE = 172;
    public static final int DEFAULT_WEIGHT_VALUE = 62;
    public static final int DEFAULT_STEP_TARGET = 10000;
    public static final double DEFAULT_KCAL_TARGET = new BigDecimal(
    		DEFAULT_STEP_TARGET * 0.7 * 0.001)
    		.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()* DEFAULT_WEIGHT_VALUE * 1.036;
}
