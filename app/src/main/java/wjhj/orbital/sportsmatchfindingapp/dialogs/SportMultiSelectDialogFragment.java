package wjhj.orbital.sportsmatchfindingapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java9.util.stream.StreamSupport;
import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

public class SportMultiSelectDialogFragment extends DialogFragment {
    public interface  SportMultiSelectDialogListener {
        public void onSportMultiSelectDialogPositiveButtonSelected(List<Sport> selection);
    }
    List<Sport> sports;
    List<Sport> selected;
    SportMultiSelectDialogListener listener;
    public SportMultiSelectDialogFragment(List<Sport> sports) {
        this.sports = Arrays.asList(Sport.values());
        this.selected = new ArrayList<>(sports);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] sportNames = StreamSupport.stream(sports).map(Sport::toString).toArray(String[]::new);
        boolean[] select = new boolean[sportNames.length];
        for (int i = 0; i < sportNames.length; i++) {
            if (selected.contains(Sport.from(sportNames[i]))) {
                select[i] = true;
            } else {
                select[i] = false;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.PopupDialogTheme)
                .setTitle(R.string.select_sports)
                .setMultiChoiceItems(sportNames, select, this::onClick)
                .setPositiveButton(R.string.apply, (view, which) -> {
                    listener.onSportMultiSelectDialogPositiveButtonSelected(selected);
                });

        return builder.create();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SportMultiSelectDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getSimpleName() +
                    "must implement SportMultiSelectDialogListener");
        }
    }

    public void onClick (DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked) {
            selected.add(sports.get(which));
        } else {
            selected.remove(sports.get(which));
        }
    }
}
