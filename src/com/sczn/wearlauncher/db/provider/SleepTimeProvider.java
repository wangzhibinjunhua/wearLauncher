package com.sczn.wearlauncher.db.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

import com.sczn.wearlauncher.db.DBUtil;
import com.sczn.wearlauncher.db.DatabaseHelper;
import com.sczn.wearlauncher.util.StringUtils;

/**
 * Created by mengmeng on 2016/6/29.
 */
public class SleepTimeProvider extends ContentProvider {

    private DBUtil DBdatas = null;
    private DatabaseHelper mHelper = null;

    //判断Uri是否匹配
    private static final UriMatcher sUriMatcher;
    private static final int COLLECTION_INDICATOR = 1;
    private static final int SINGLE_INDICATOR = 2;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.SleepTimeDetailColumns.AUTHORITY, "sleep_time_detail", COLLECTION_INDICATOR);
        sUriMatcher.addURI(Provider.SleepTimeDetailColumns.AUTHORITY, "sleep_time_detail/#", SINGLE_INDICATOR);//更精确查询的标识#
    }
    //查询映射，抽象的字段映射到数据库中真实存在的字段
    private static HashMap<String,String> mSleepTimeProjectionMap;
    static {
        mSleepTimeProjectionMap = new HashMap<String,String >();
        mSleepTimeProjectionMap.put(Provider.SleepTimeDetailColumns._ID, Provider.SleepTimeDetailColumns._ID);
        mSleepTimeProjectionMap.put(Provider.SleepTimeDetailColumns.COLUMNS_START_TIME, Provider.SleepTimeDetailColumns.COLUMNS_START_TIME);
        mSleepTimeProjectionMap.put(Provider.SleepTimeDetailColumns.COLUMNS_END_TIME, Provider.SleepTimeDetailColumns.COLUMNS_END_TIME);
        mSleepTimeProjectionMap.put(Provider.SleepTimeDetailColumns.COLUMNS_SLEEP_STATE, Provider.SleepTimeDetailColumns.COLUMNS_SLEEP_STATE);
        mSleepTimeProjectionMap.put(Provider.SleepTimeDetailColumns.COLUMNS_SLEEP_STATE_STEP, Provider.SleepTimeDetailColumns.COLUMNS_SLEEP_STATE_STEP);
        mSleepTimeProjectionMap.put(Provider.SleepTimeDetailColumns.DEFAULT_SORT_ORDER, Provider.SleepTimeDetailColumns.DEFAULT_SORT_ORDER);
    }

    @Override
    public boolean onCreate() {
        if(DBdatas == null){
            DBdatas = new DBUtil(getContext());
            mHelper = DBdatas.getDbHelper();
        }
        return (DBdatas == null) ? false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case COLLECTION_INDICATOR:
                // 设置查询的表
                qb.setTables(Provider.SleepTimeDetailColumns.TABLE_NAME);
                // 设置投影映射
                qb.setProjectionMap(mSleepTimeProjectionMap);
                break;
            case SINGLE_INDICATOR:
                qb.setTables(Provider.SleepTimeDetailColumns.TABLE_NAME);
                qb.setProjectionMap(mSleepTimeProjectionMap);
                qb.appendWhere(Provider.SleepTimeDetailColumns._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }

        String orderBy;
        if(StringUtils.isEmpty(sortOrder))
        {
            orderBy = Provider.SleepTimeDetailColumns.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = qb.query(db,projection,selection,null,null,null,orderBy);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
