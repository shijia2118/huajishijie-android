package com.test.tworldapplication.view.hellocharts.formatter;

import com.test.tworldapplication.view.hellocharts.model.SliceValue;

public interface PieChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SliceValue value);
}
