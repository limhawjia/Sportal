package wjhj.orbital.sportsmatchfindingapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.firestore.GeoPoint;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.SearchGameFiltersDialogBinding;
import wjhj.orbital.sportsmatchfindingapp.repo.GameSearchFilter;
import wjhj.orbital.sportsmatchfindingapp.utils.GeoPointParcelable;
import wjhj.orbital.sportsmatchfindingapp.utils.LocationPickerActivity;

import static android.app.Activity.RESULT_OK;

public class SearchFilterDialogFragment extends DialogFragment {
    public interface SearchFilterDialogListener {
        public void onSearchFilterDialogPositiveButtonClicked(GameSearchFilter filter);
    }

    private static final int RESULT_RC = 22;

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
            startActivityForResult(intent, RESULT_RC);
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.PopupDialogTheme)
                .setView(binding.getRoot())
                .setPositiveButton(R.string.apply, (view, which) -> {
                    listener.onSearchFilterDialogPositiveButtonClicked(searchFilterDialogViewModel.getFilters());
                }).setNeutralButton(R.string.cancel, (view, which) -> {});

        return builder.create();
    }

    private void initViewModel() {
        SearchFilterDialogViewModelFactory factory = new SearchFilterDialogViewModelFactory(mGameSearchFilter);
        searchFilterDialogViewModel = ViewModelProviders.of(this, factory).get(SearchFilterDialogViewModel.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_RC && resultCode == RESULT_OK) {
            Log.d("hi", "blah");
            GeoPointParcelable geopoint = data.getParcelableExtra(LocationPickerActivity.GEOPOINT);
            String locationName = data.getStringExtra(LocationPickerActivity.LOCATION_NAME);

            if (geopoint != null && locationName != null) {
                GameSearchFilter newFilter = searchFilterDialogViewModel.getFilters();
                newFilter.setLocationQuery(geopoint.toGeoPoint());
                newFilter.setLocationName(locationName);
                searchFilterDialogViewModel.setFilters(newFilter);
                searchFilterDialogViewModel.setLocationName(locationName);
                Log.d("hi", "update");
            }
        }
    }
}
