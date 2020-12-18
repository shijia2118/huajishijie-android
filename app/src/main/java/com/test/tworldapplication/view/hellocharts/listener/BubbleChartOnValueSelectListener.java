package com.test.tworldapplication.view.hellocharts.listener;


import com.test.tworldapplication.view.hellocharts.model.BubbleValue;

public interface BubbleChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int bubbleIndex, BubbleValue value);

}
