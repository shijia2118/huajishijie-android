package com.test.tworldapplication.view.hellocharts.formatter;

import com.test.tworldapplication.view.hellocharts.model.BubbleValue;

public interface BubbleChartValueFormatter {

    public int formatChartValue(char[] formattedValue, BubbleValue value);
}
