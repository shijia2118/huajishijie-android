package com.test.tworldapplication.view.hellocharts.formatter;

import com.test.tworldapplication.view.hellocharts.model.SubcolumnValue;

public interface ColumnChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SubcolumnValue value);

}
