package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.AddGameActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.dialogs.DatePickerFragment;
import wjhj.orbital.sportsmatchfindingapp.dialogs.DurationPickerFragment;
import wjhj.orbital.sportsmatchfindingapp.dialogs.TimePickerFragment;
import wjhj.orbital.sportsmatchfindingapp.maps.LocationPickerMapFragment;

public class AddGameActivity extends AppCompatActivity implements
        DatePickerFragment.DatePickerListener,
        TimePickerFragment.TimePickerListener,
        DurationPickerFragment.DurationPickerListener,
        LocationPickerMapFragment.OnMapFragmentCancelledListener,
        LocationPickerMapFragment.LocationPickerListener {

    private static String LOCATION_PICKER_TAG = "location_picker";

    private AddGameActivityBinding binding;
    private AddGameViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.add_game_activity);

        Toolbar toolbar = (Toolbar) binding.topToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.add_game_toolbar_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewModel = ViewModelProviders.of(this).get(AddGameViewModel.class);
        binding.setAddGameViewModel(viewModel);
        binding.setActivity(this);
        binding.setLifecycleOwner(this);

        ArrayAdapter<Sport> sportAdapter = new ArrayAdapter<>(this,
                R.layout.dropdown_menu_popup_item, Sport.values());
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.addGameSelectSport.setAdapter(sportAdapter);
    }

    public void openDatePicker() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    public void openTimePicker() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    public void openDurationPicker() {
        DialogFragment durationPicker = new DurationPickerFragment();
        durationPicker.show(getSupportFragmentManager(), "duration picker");
    }

    public void openLocationPicker() {
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        MapboxMapOptions options = MapboxMapOptions.createFromAttributes(this, null);
        options.camera(new CameraPosition.Builder()
                .target(new LatLng(-52.6885, -70.1395))
                .zoom(9)
                .build());

        LocationPickerMapFragment mapFragment = LocationPickerMapFragment.newInstance(options);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.add_game_page_container, mapFragment, LOCATION_PICKER_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDialogDateSet(DatePickerFragment datePickerFragment, LocalDate dateSet) {
        viewModel.setDate(dateSet);
    }

    @Override
    public void onDialogTimeSet(TimePickerFragment timePickerFragment, LocalTime timeSet) {
        viewModel.setTime(timeSet);
    }

    @Override
    public void onDialogDurationSet(DurationPickerFragment durationPickerFragment, Duration durationSet) {
        viewModel.setDuration(durationSet);
    }

    @Override
    public void onMapCancelled(@NonNull LocationPickerMapFragment locationPickerMapFragment) {
        getSupportFragmentManager().beginTransaction()
                .remove(locationPickerMapFragment)
                .commit();
    }

    @Override
    public void onLocationPicked(LocationPickerMapFragment locationPickerMapFragment,
                                 Point selectedPoint, String selectedPlaceName) {
        viewModel.setLocationPoint(selectedPoint);
        viewModel.setPlaceName(selectedPlaceName);
        getSupportFragmentManager().beginTransaction()
                .remove(locationPickerMapFragment)
                .commit();
    }
}
