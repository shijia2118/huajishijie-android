package com.test.tworldapplication.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by dasiy on 16/10/19.
 */

public class TimePickerView extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("onTimeSet", "hourOfDay: "+hourOfDay + "Minute: "+minute);
    }

//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        //处理设置的时间，这里我们作为示例，在日志中输出我们选择的时间
//        Log.d("onTimeSet", "hourOfDay: "+hourOfDay + "Minute: "+minute);
//    }
}
