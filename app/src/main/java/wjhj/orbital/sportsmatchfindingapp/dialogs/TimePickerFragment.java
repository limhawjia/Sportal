package wjhj.orbital.sportsmatchfindingapp.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.threeten.bp.LocalTime;

import java.util.Calendar;

import wjhj.orbital.sportsmatchfindingapp.R;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePickerListener listener;

    public interface TimePickerListener {
        void onDialogTimeSet(TimePickerFragment timePickerFragment, LocalTime timeSet);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        return new TimePickerDialog(getActivity(),
                R.style.PopupDialogTheme,
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        LocalTime time = LocalTime.of(hourOfDay, minute);
        listener.onDialogTimeSet(this, time);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (TimePickerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    "must implement TimePickerListener");
        }
    }
}
