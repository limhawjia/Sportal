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
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.SearchGameFiltersDialogBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.TimeOfDay;
import wjhj.orbital.sportsmatchfindingapp.repo.GameSearchFilter;

public class SearchFilterDialogFragment extends DialogFragment {
    public interface SearchFilterDialogListener {
        public void onSearchFilterDialogPositiveButtonClicked(GameSearchFilter filter);
    }

    GameSearchFilter mGameSearchFilter;

    public SearchFilterDialogFragment(GameSearchFilter filter) {
        mGameSearchFilter = filter;
    }

    private SearchGameFiltersDialogBinding binding;
    private SearchFilterDialogViewModel searchFilterDialogViewModel;
    private SearchFilterDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initViewModel();
        binding = DataBindingUtil.inflate(requireActivity().getLayoutInflater(),
                        R.layout.search_game_filters_dialog,null, false);
        binding.setLifecycleOwner(this);
        binding.setGameFilters(searchFilterDialogViewModel);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.PopupDialogTheme)
                .setView(binding.getRoot())
                .setPositiveButton(R.string.apply, (view, which) -> {
                    listener.onSearchFilterDialogPositiveButtonClicked(searchFilterDialogViewModel.getFilters());
                }).setNeutralButton(R.string.cancel, (view, which) -> {});

        return builder.create();
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SearchFilterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getSimpleName() +
                    "must implement SearchDialogFilterListener");
        }
    }

    private void initViewModel() {
        SearchFilterDialogViewModelFactory factory = new SearchFilterDialogViewModelFactory(mGameSearchFilter);
        searchFilterDialogViewModel = ViewModelProviders.of(this, factory).get(SearchFilterDialogViewModel.class);
    }
}
