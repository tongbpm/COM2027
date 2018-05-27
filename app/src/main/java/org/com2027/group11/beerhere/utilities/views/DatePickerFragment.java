package org.com2027.group11.beerhere.utilities.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DatePickerFragment";

    private DatePickerFragmentListener datePickerFragmentListener;

    public static DatePickerFragment newInstance(DatePickerFragmentListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setDatePickerFragmentListener(listener);
        return fragment;
    }

    public DatePickerFragmentListener getDatePickerFragmentListener() {
        return this.datePickerFragmentListener;
    }

    public void setDatePickerFragmentListener(DatePickerFragmentListener datePickerFragmentListener) {
        this.datePickerFragmentListener = datePickerFragmentListener;
    }

    protected void notifyDatePickerListener(Date date) {
        if (this.datePickerFragmentListener != null) {
            this.datePickerFragmentListener.onDateSet(date);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d(TAG, "Year: " + year);
        Log.d(TAG, "Month: " + month);
        Log.d(TAG, "Day of Month: " + day);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();

        //Pass the date to the listener
        notifyDatePickerListener(date);

    }


    /**
     * The listener interface allows us to pass data from the fragment to the activity that created it
     */
    public interface DatePickerFragmentListener {
        public void onDateSet(Date date);
    }
}
