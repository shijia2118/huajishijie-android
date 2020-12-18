package com.test.tworldapplication.view.hellocharts.listener;


import com.test.tworldapplication.view.hellocharts.model.SliceValue;

public interface PieChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int arcIndex, SliceValue value);

}
