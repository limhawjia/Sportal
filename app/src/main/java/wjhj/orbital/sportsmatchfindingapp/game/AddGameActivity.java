package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.AddGameActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.dialogs.DatePickerFragment;
import wjhj.orbital.sportsmatchfindingapp.dialogs.DurationPickerFragment;
import wjhj.orbital.sportsmatchfindingapp.dialogs.TimePickerFragment;

public class AddGameActivity extends AppCompatActivity implements DatePickerFragment.DatePickerListener,
        TimePickerFragment.TimePickerListener, DurationPickerFragment.DurationPickerListener {

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
}
