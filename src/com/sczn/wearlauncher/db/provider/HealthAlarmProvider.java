package com.sczn.wearlauncher.db.provider;

import java.util.HashMap;

import com.sczn.wearlauncher.db.DBUtil;
import com.sczn.wearlauncher.db.DatabaseHelper;
import com.sczn.wearlauncher.db.provider.Provider.HealthAlarmcolumns;
import com.sczn.wearlauncher.util.StringUtils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class HealthAlarmProvider extends ContentProvider {
	
	private DBUtil DBdatas = null;
    private DatabaseHelper mHelper = null;

    //判断Uri是否匹配
    private static final UriMatcher sUriMatcher;
    private static final int COLLECTION_INDICATOR = 1;
    private static final int SINGLE_INDICATOR = 2;
    
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Provider.HealthAlarmcolumns.AUTHORITY, HealthAlarmcolumns.TABLE_NAME, COLLECTION_INDICATOR);
        sUriMatcher.addURI(Provider.HealthAlarmcolumns.AUTHORITY, HealthAlarmcolumns.TABLE_NAME + "/#", SINGLE_INDICATOR);//更精确查询的标识#
    }
    //查询映射，抽象的字段映射到数据库中真实存在的字段
    private static HashMap<String,String> mHealthAlarmProjectionMap;
    static {
    	mHealthAlarmProjectionMap = new HashMap<String,String >();
    	mHealthAlarmProjectionMap.put(Provider.HealthAlarmcolumns._ID,Provider.HealthAlarmcolumns._ID);
    	mHealthAlarmProjectionMap.put(Provider.HealthAlarmcolumns.COLUMNS_TIME,Provider.HealthAlarmcolumns.COLUMNS_TIME);
    	mHealthAlarmProjectionMap.put(Provider.HealthAlarmcolumns.COLUMNS_TYPE,Provider.HealthAlarmcolumns.COLUMNS_TYPE);
    	mHealthAlarmProjectionMap.put(Provider.HealthAlarmcolumns.COLUMNS_REPEAT,Provider.HealthAlarmcolumns.COLUMNS_REPEAT);
    	mHealthAlarmProjectionMap.put(Provider.HealthAlarmcolumns.COLUMNS_EBABLE,Provider.HealthAlarmcolumns.COLUMNS_EBABLE);
    	mHealthAlarmProjectionMap.put(Provider.HealthAlarmcolumns.DEFAULT_SORT_ORDER,Provider.HealthAlarmcolumns.DEFAULT_SORT_ORDER);
    }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
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
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case COLLECTION_INDICATOR:
                // 设置查询的表
                qb.setTables(Provider.HealthAlarmcolumns.TABLE_NAME);
                // 设置投影映射
                qb.setProjectionMap(mHealthAlarmProjectionMap);
                break;
            case SINGLE_INDICATOR:
                qb.setTables(Provider.HealthAlarmcolumns.TABLE_NAME);
                qb.setProjectionMap(mHealthAlarmProjectionMap);
                qb.appendWhere(Provider.HealthAlarmcolumns._ID + "=" + uri.getPathSegments().get(1));
                
                break;
            default:
                throw new IllegalArgumentException("Unknow URI: " + uri);
        }

        String orderBy;
        if(StringUtils.isEmpty(sortOrder))
        {
            orderBy = Provider.HealthAlarmcolumns.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = qb.query(db,projection,selection,null,null,null,orderBy);

        return c;
    }

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
