package wjhj.orbital.sportsmatchfindingapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.threeten.bp.Duration;

import wjhj.orbital.sportsmatchfindingapp.R;


public class DurationPickerFragment extends DialogFragment {

    public static final String[] MINUTES = new String[] {"00", "15", "30", "45"};

    private DurationPickerListener listener;
    private NumberPicker mHourPicker;
    private NumberPicker mMinutePicker;

    public interface DurationPickerListener {
        void onDialogDurationSet(DurationPickerFragment durationPickerFragment, Duration durationSet);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LinearLayout pickerLayout = (LinearLayout) requireActivity()
                .getLayoutInflater()
                .inflate(R.layout.duration_picker, null);

        mHourPicker = pickerLayout.findViewById(R.id.duration_picker_hour_picker);
        mMinutePicker = pickerLayout.findViewById(R.id.duration_picker_minute_picker);

        mHourPicker.setMinValue(0);
        mHourPicker.setMaxValue(24);
        mHourPicker.setWrapSelectorWheel(false);

        mMinutePicker.setMinValue(0);
        mMinutePicker.setMaxValue(MINUTES.length - 1);
        mMinutePicker.setDisplayedValues(MINUTES);
        mMinutePicker.setWrapSelectorWheel(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.PopupDialogTheme)
                .setView(pickerLayout)
                .setPositiveButton("Ok", (dialog, id) -> {
                    Duration durationSet = Duration.ofHours(mHourPicker.getValue())
                            .plusMinutes(Integer.valueOf(MINUTES[mMinutePicker.getValue()]));
                    listener.onDialogDurationSet(this, durationSet);
                })
                .setNegativeButton("Cancel", (dialog, id) -> {});

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DurationPickerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    "must implement DurationPickerListener");
        }
    }
}
