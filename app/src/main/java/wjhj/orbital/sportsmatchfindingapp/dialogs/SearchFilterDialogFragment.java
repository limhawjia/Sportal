package wjhj.orbital.sportsmatchfindingapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.SearchGameFiltersDialogBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.TimeOfDay;

public class SearchFilterDialogFragment extends DialogFragment {
    private List<TimeOfDay> timeOfDayFilters;
    private List<Difficulty> skillLevelFilters;
    private SearchGameFiltersDialogBinding binding;

    public SearchFilterDialogFragment() {
        timeOfDayFilters = new ArrayList<>();
        skillLevelFilters = new ArrayList<>();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(requireActivity().getLayoutInflater(),
                        R.layout.search_game_filters_dialog,null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.DurationDialogTheme)
                .setView(binding.getRoot())
                .setPositiveButton(R.string.apply, (view, which) -> {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 0, null);
                }).setNegativeButton(R.string.cancel, (view, which) -> {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 1, null);
                });

        binding.morningButton.setOnClickListener(view -> {
            if (timeOfDayFilters.contains(TimeOfDay.MORNING)) {
                timeOfDayFilters.remove(TimeOfDay.MORNING);
                binding.morningButton.getBackground().setAlpha(255);
            } else {
                timeOfDayFilters.add(TimeOfDay.MORNING);
                binding.morningButton.getBackground().setAlpha(128);
            }
        });
        binding.afternoonButton.setOnClickListener(view -> {
            if (timeOfDayFilters.contains(TimeOfDay.AFTERNOON)) {
                timeOfDayFilters.remove(TimeOfDay.AFTERNOON);
                binding.afternoonButton.getBackground().setAlpha(255);
            } else {
                timeOfDayFilters.add(TimeOfDay.AFTERNOON);
                binding.afternoonButton.getBackground().setAlpha(128);
            }
        });
        binding.eveningButton.setOnClickListener(view -> {
            if (timeOfDayFilters.contains(TimeOfDay.NIGHT)) {
                timeOfDayFilters.remove(TimeOfDay.NIGHT);
                binding.eveningButton.getBackground().setAlpha(255);
            } else {
                timeOfDayFilters.add(TimeOfDay.NIGHT);
                binding.eveningButton.getBackground().setAlpha(128);
            }
        });

        binding.beginnerButton.setOnClickListener(view -> {
            if (skillLevelFilters.contains(Difficulty.BEGINNER)) {
                skillLevelFilters.remove(Difficulty.BEGINNER);
                binding.beginnerButton.getBackground().setAlpha(255);
            } else {
                skillLevelFilters.add(Difficulty.BEGINNER);
                binding.beginnerButton.getBackground().setAlpha(128);
            }
        });
        binding.intermediateButton.setOnClickListener(view -> {
            if (skillLevelFilters.contains(Difficulty.INTERMEDIATE)) {
                skillLevelFilters.remove(Difficulty.INTERMEDIATE);
                binding.intermediateButton.getBackground().setAlpha(255);
            } else {
                skillLevelFilters.add(Difficulty.INTERMEDIATE);
                binding.intermediateButton.getBackground().setAlpha(128);
            }
        });
        binding.advancedButton.setOnClickListener(view -> {
            if (skillLevelFilters.contains(Difficulty.ADVANCED)) {
                skillLevelFilters.remove(Difficulty.ADVANCED);
                binding.advancedButton.getBackground().setAlpha(255);
            } else {
                skillLevelFilters.add(Difficulty.ADVANCED);
                binding.advancedButton.getBackground().setAlpha(128);
            }
        });

        return builder.create();
    }
}
