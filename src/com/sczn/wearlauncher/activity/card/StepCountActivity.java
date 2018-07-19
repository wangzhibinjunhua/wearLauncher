package com.sczn.wearlauncher.activity.card;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.db.provider.Provider.StepCountDetailColumns;
import com.sczn.wearlauncher.view.card.ChartView;

/**
 * Created by mengmeng on 2016/12/5.
 */
public class StepCountActivity extends Activity {

    private ChartView mDataLineChartView;
    private TextView mTodaySteps;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private final static float[] defValues = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
            300.f, 200.0f, 600.0f, 900.0f, 3500.0f, 300.0f, 1100.0f, 1500.0f,
            1000.0f, 600.0f, 800.0f, 2000.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};

    private float[] mStepsValues = new float[25];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_steps);
        mDataLineChartView = (ChartView) findViewById(R.id.chart_line);
        mTodaySteps = (TextView) findViewById(R.id.today_steps);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataBase();
    }

    private void getDataBase(){
        int allSteps = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        for(int i=0; i<=24; i++){
            calendar.set(Calendar.HOUR_OF_DAY,i);
            long time = calendar.getTimeInMillis();
            String selection = StepCountDetailColumns.COLUMNS_GET_TIME +" == "+time;
            Cursor cursor = getContentResolver().query(StepCountDetailColumns.CONTENT_URI,null,selection,null,null);
            if(cursor == null){
                mStepsValues[i] = 0.0f;
            }else {
                cursor.moveToFirst();
                try {
                    float steps = cursor.getInt(cursor.getColumnIndex(StepCountDetailColumns.COLUMNS_SETP_COUNT));
                    Log.e("dmm step apk","time:"+sdf.format(cursor.getLong(cursor.getColumnIndex(StepCountDetailColumns.COLUMNS_GET_TIME)))
                                        +"\n step:"+steps);
                    mStepsValues[i] = steps;
                }catch (Exception e){

                }
            }
            allSteps += mStepsValues[i];
            cursor.close();
        }
        mDataLineChartView.setIsHistogram(true);
        //获取数据后，绘制图形
        mDataLineChartView.setValues(mStepsValues);
        //today all steps
        mTodaySteps.setText(String.valueOf(allSteps));
    }

}
