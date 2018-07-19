package com.sczn.wearlauncher.activity.card;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.view.card.ChartView;

import android.app.Activity;
import android.os.Bundle;

public class AltActivity extends Activity {

    private ChartView mDataLineChartView;

    private final static float[] defValues = {90.0f, 68.7f, 85.0f, 5.9f,
            125.5f, 102.4f, 194.5f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_alt);
        mDataLineChartView = (ChartView) findViewById(R.id.chart_line);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        //获取数据后，绘制图形
        mDataLineChartView.setValues(defValues);
    }
}
