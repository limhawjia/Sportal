package wjhj.orbital.sportsmatchfindingapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;

public class SkillLevelPickerFragment extends DialogFragment {

    private int initialItem = 0;
    private int selectedItem;
    private SkillLevelPickerListener listener;

    public interface SkillLevelPickerListener {
        void onSkillLevelSet(SkillLevelPickerFragment skillLevelPickerFragment, int skillLevelSet);
    }

    public SkillLevelPickerFragment(@Nullable Difficulty currDifficulty) {
        super();
        if (currDifficulty != null) {
            initialItem = currDifficulty.ordinal();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.PopupDialogTheme)
                .setTitle("Select skill level")
                .setSingleChoiceItems(Difficulty.getAllDifficultyStrings(), initialItem,
                        (dialog, which) -> selectedItem = which)
                .setPositiveButton("Ok", (dialog, id) ->
                        listener.onSkillLevelSet(this, selectedItem))
                .setNegativeButton("Cancel", (dialog, id) -> {
                });


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SkillLevelPickerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getSimpleName() +
                    "must implement SkillLevelPickerListener");
        }
    }
}

