package com.test.tworldapplication.view.hellocharts.formatter;


import com.test.tworldapplication.view.hellocharts.model.PointValue;

public interface LineChartValueFormatter {

    public int formatChartValue(char[] formattedValue, PointValue value);
}
