package com.test.tworldapplication.view.hellocharts.listener;


import com.test.tworldapplication.view.hellocharts.model.PointValue;
import com.test.tworldapplication.view.hellocharts.model.SubcolumnValue;

public interface ComboLineColumnChartOnValueSelectListener extends OnValueDeselectListener {

    public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value);

    public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value);

}
