package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.threeten.bp.LocalDate;

import java.util.Calendar;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.AddGameActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.homepage.SearchGameViewModel;

public class AddGameActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private AddGameActivityBinding binding;
    private SearchGameViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.add_game_activity);

        Toolbar toolbar = (Toolbar) binding.topToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.add_game_toolbar_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewModel = ViewModelProviders.of(this).get(SearchGameViewModel.class);
        binding.setSearchGameViewModel(viewModel);
        binding.setLifecycleOwner(this);

        ArrayAdapter<Sport> sportAdapter = new ArrayAdapter<>(this,
                R.layout.dropdown_menu_popup_item, Sport.values());
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.addGameSelectSport.setAdapter(sportAdapter);

        binding.addGameDatePicker.setOnClickListener(view -> openDatePicker());



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
        DatePickerDialog dialog = DatePickerDialog.newInstance(this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dialog.setVersion(DatePickerDialog.Version.VERSION_1);
        dialog.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        LocalDate date = LocalDate.of(year, monthOfYear, dayOfMonth);
        // todo: update viewmodel and use databinding to set text instead!
        binding.addGameDatePicker.setText(date.toString());
    }
}
