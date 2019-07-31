package wjhj.orbital.sportsmatchfindingapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.SearchGameFiltersDialogBinding;
import wjhj.orbital.sportsmatchfindingapp.repo.GameSearchFilter;
import wjhj.orbital.sportsmatchfindingapp.utils.GeoPointParcelable;
import wjhj.orbital.sportsmatchfindingapp.utils.LocationPickerActivity;

import static android.app.Activity.RESULT_OK;

public class SearchFilterDialogFragment extends DialogFragment {
    public interface SearchFilterDialogListener {
        void onSearchFilterDialogPositiveButtonClicked(GameSearchFilter filter);
    }

    private static final int LOCATION_RESULT_RC = 22;

    private GameSearchFilter mGameSearchFilter;
    private SearchGameFiltersDialogBinding binding;
    private SearchFilterDialogViewModel searchFilterDialogViewModel;
    private SearchFilterDialogListener listener;

    public SearchFilterDialogFragment(GameSearchFilter filter) {
        mGameSearchFilter = filter;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SearchFilterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getSimpleName()
                    + " must implement SearchFilterDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initViewModel();
        binding = DataBindingUtil.inflate(requireActivity().getLayoutInflater(),
                        R.layout.search_game_filters_dialog,null, false);
        binding.setLifecycleOwner(this);
        binding.setGameFilters(searchFilterDialogViewModel);
        binding.searchLocationPicker.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), LocationPickerActivity.class);
            startActivityForResult(intent, LOCATION_RESULT_RC);
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.PopupDialogTheme)
                .setView(binding.getRoot())
                .setPositiveButton(R.string.apply, (view, which) ->
                        listener.onSearchFilterDialogPositiveButtonClicked(searchFilterDialogViewModel.getFilters()))
                .setNeutralButton(R.string.cancel, (view, which) -> {});

        return builder.create();
    }

    private void initViewModel() {
        SearchFilterDialogViewModelFactory factory = new SearchFilterDialogViewModelFactory(mGameSearchFilter);
        searchFilterDialogViewModel = ViewModelProviders.of(this, factory).get(SearchFilterDialogViewModel.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_RESULT_RC && resultCode == RESULT_OK && data != null) {
            GeoPointParcelable geoPoint = data.getParcelableExtra(LocationPickerActivity.GEOPOINT);
            String locationName = data.getStringExtra(LocationPickerActivity.LOCATION_NAME);

            if (geoPoint != null && locationName != null) {
                GameSearchFilter newFilter = searchFilterDialogViewModel.getFilters();
                newFilter.setLocationQuery(geoPoint.toGeoPoint());
                newFilter.setLocationName(locationName);
                searchFilterDialogViewModel.setFilters(newFilter);
                searchFilterDialogViewModel.setLocationName(locationName);
                Log.d("hi", "update");
            }
        }
    }
}
