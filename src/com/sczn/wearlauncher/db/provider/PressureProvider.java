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
public class PressureProvider extends ContentProvider {

    private DBUtil DBdatas = null;
    private DatabaseHelper mHelper = null;

    //判断Uri是否匹配
    private static final UriMatcher sUriMatcher;
    private static final int COLLECTION_INDICATOR = 1;
    private static final int SINGLE_INDICATOR = 2;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.PressureDetailColumns.AUTHORITY, "pressure_detail", COLLECTION_INDICATOR);
        sUriMatcher.addURI(Provider.PressureDetailColumns.AUTHORITY, "pressure_detail/#", SINGLE_INDICATOR);//更精确查询的标识#
    }
    //查询映射，抽象的字段映射到数据库中真实存在的字段
    private static HashMap<String,String> mPressureProjectionMap;
    static {
        mPressureProjectionMap = new HashMap<String,String >();
        mPressureProjectionMap.put(Provider.PressureDetailColumns._ID, Provider.PressureDetailColumns._ID);
        mPressureProjectionMap.put(Provider.PressureDetailColumns.COLUMNS_TIME, Provider.PressureDetailColumns.COLUMNS_TIME);
        mPressureProjectionMap.put(Provider.PressureDetailColumns.COLUMNS_PRESSURE, Provider.PressureDetailColumns.COLUMNS_PRESSURE);
        mPressureProjectionMap.put(Provider.PressureDetailColumns.DEFAULT_SORT_ORDER, Provider.PressureDetailColumns.DEFAULT_SORT_ORDER);
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
                qb.setTables(Provider.PressureDetailColumns.TABLE_NAME);
                // 设置投影映射
                qb.setProjectionMap(mPressureProjectionMap);
                break;
            case SINGLE_INDICATOR:
                qb.setTables(Provider.PressureDetailColumns.TABLE_NAME);
                qb.setProjectionMap(mPressureProjectionMap);
                qb.appendWhere(Provider.PressureDetailColumns._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }

        String orderBy;
        if(StringUtils.isEmpty(sortOrder))
        {
            orderBy = Provider.PressureDetailColumns.DEFAULT_SORT_ORDER;
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
