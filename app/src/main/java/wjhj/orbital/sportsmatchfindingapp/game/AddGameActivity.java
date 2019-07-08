package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.Calendar;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.AddGameActivityBinding;

public class AddGameActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

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
        binding.setLifecycleOwner(this);

        ArrayAdapter<Sport> sportAdapter = new ArrayAdapter<>(this,
                R.layout.dropdown_menu_popup_item, Sport.values());
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.addGameSelectSport.setAdapter(sportAdapter);

        binding.addGameDatePicker.setOnClickListener(view -> openDatePicker());

        binding.addGameTimePicker.setOnClickListener(view -> openTimePicker());

        binding.addGameDurationPicker.setOnClickListener(view -> openDurationPicker());



//        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, new String[]{"North", "South", "East", "West"});
//        binding.addGameLocationInput.setAdapter(locationAdapter);
//
//        ArrayAdapter<String> skillAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, new String[]{"Beginner", "Intermediate", "Advanced"});
//        binding.addGameSkillInput.setAdapter(skillAdapter);
//
//        binding.startDate.setOnClickListener((View v) -> getDate(viewModel.getStartDate()));
//        binding.endDate.setOnClickListener((View v) -> getDate(viewModel.getEndDate()));
//
//
//        binding.addGamePickStartTime.setOnClickListener((View v) -> getTime(viewModel.getStartTime()));
//        binding.addGamePickEndTime.setOnClickListener((View v) -> getTime(viewModel.getEndTime()));
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this,
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        dialog.show();
    }

    private void openDurationPicker() {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        LocalDate date = LocalDate.of(year, month, dayOfMonth);
        viewModel.setDate(date);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        LocalTime time = LocalTime.of(hourOfDay, minute);
        viewModel.setTime(time);
    }


}
