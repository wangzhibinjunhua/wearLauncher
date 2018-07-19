package com.sczn.wearlauncher.db.provider;

import java.util.HashMap;

import com.sczn.wearlauncher.db.DBUtil;
import com.sczn.wearlauncher.db.DatabaseHelper;
import com.sczn.wearlauncher.util.StringUtils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class HeartRateProvider extends ContentProvider{
	
	private DBUtil DBdatas = null;
    private DatabaseHelper mHelper = null;

    //判断Uri是否匹配
    private static final UriMatcher sUriMatcher;
    private static final int COLLECTION_INDICATOR = 1;
    private static final int SINGLE_INDICATOR = 2;
    
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.HeartRateDetailColumns.AUTHORITY, "heart_rate_detail", COLLECTION_INDICATOR);
        sUriMatcher.addURI(Provider.HeartRateDetailColumns.AUTHORITY, "heart_rate_detail/#", SINGLE_INDICATOR);//更精确查询的标识#
    }
    //查询映射，抽象的字段映射到数据库中真实存在的字段
    private static HashMap<String,String> mHeartRateProjectionMap;
    static {
        mHeartRateProjectionMap = new HashMap<String,String >();
        mHeartRateProjectionMap.put(Provider.HeartRateDetailColumns._ID,Provider.HeartRateDetailColumns._ID);
        mHeartRateProjectionMap.put(Provider.HeartRateDetailColumns.COLUMNS_TIME,Provider.HeartRateDetailColumns.COLUMNS_TIME);
        mHeartRateProjectionMap.put(Provider.HeartRateDetailColumns.COLUMNS_HEART_RATE,Provider.HeartRateDetailColumns.COLUMNS_HEART_RATE);
        mHeartRateProjectionMap.put(Provider.HeartRateDetailColumns.DEFAULT_SORT_ORDER,Provider.HeartRateDetailColumns.DEFAULT_SORT_ORDER);
    }

    @Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
    	if(DBdatas == null){
            DBdatas = new DBUtil(getContext());
            mHelper = DBdatas.getDbHelper();
        }
        return (DBdatas == null) ? false:true;
	}
    
    @Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case COLLECTION_INDICATOR:
                // 设置查询的表
                qb.setTables(Provider.HeartRateDetailColumns.TABLE_NAME);
                // 设置投影映射
                qb.setProjectionMap(mHeartRateProjectionMap);
                break;
            case SINGLE_INDICATOR:
                qb.setTables(Provider.HeartRateDetailColumns.TABLE_NAME);
                qb.setProjectionMap(mHeartRateProjectionMap);
                qb.appendWhere(Provider.HeartRateDetailColumns._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }

        String orderBy;
        if(StringUtils.isEmpty(sortOrder))
        {
            orderBy = Provider.HeartRateDetailColumns.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = qb.query(db,projection,selection,null,null,null,orderBy);

        return c;
	}
    
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
}
