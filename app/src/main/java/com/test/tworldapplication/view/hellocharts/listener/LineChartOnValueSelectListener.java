package com.test.tworldapplication.view.hellocharts.listener;


import com.test.tworldapplication.view.hellocharts.model.PointValue;

public interface LineChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int lineIndex, int pointIndex, PointValue value);

}
