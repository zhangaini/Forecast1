package com.forecast.forecast.stepUtils.utils;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class XValueUtils implements IAxisValueFormatter {
    private final int mChart;

    public XValueUtils(int chart) {
        this.mChart = chart;

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Log.i("RNG", "getFormattedValue-------------" + value);

        Log.i("RNG", "mChart.getVisibleXRange()-------------" );
        if (value == 0.0 || value == 6.0) {
            return "";
        } else {
            return "13685624925";
        }
    }

}
