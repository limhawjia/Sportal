package wjhj.orbital.sportsmatchfindingapp.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.AddGameActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.homepage.SearchGameViewModel;

public class AddGameActivity extends AppCompatActivity {

    private AddGameActivityBinding binding;
    private SearchGameViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.add_game_activity);

        viewModel = ViewModelProviders.of(this).get(SearchGameViewModel.class);
        binding.setSearchGameViewModel(viewModel);
        binding.setLifecycleOwner(this);

        ArrayAdapter<String> sportAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, Sport.getAllSportsString());
        binding.sports.setAdapter(sportAdapter);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, new String[]{"North", "South", "East", "West"});
        binding.addGameLocationInput.setAdapter(locationAdapter);

        ArrayAdapter<String> skillAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, new String[]{"Beginner", "Intermediate", "Advanced"});
        binding.addGameSkillInput.setAdapter(skillAdapter);

        binding.startDate.setOnClickListener((View v) -> getDate(viewModel.getStartDate()));
        binding.endDate.setOnClickListener((View v) -> getDate(viewModel.getEndDate()));


        binding.addGamePickStartTime.setOnClickListener((View v) -> getTime(viewModel.getStartTime()));
        binding.addGamePickEndTime.setOnClickListener((View v) -> getTime(viewModel.getEndTime()));
    }

    private void getTime(MutableLiveData<LocalTime> liveData) {
        new TimePickerDialog(this, (view, hourOfDay, minute)
                -> liveData.setValue(LocalTime.of(hourOfDay, minute)),
                0, 0, false).show();
    }

    private void getDate(MutableLiveData<LocalDate> liveData) {
        new DatePickerDialog(this, (view, year, month, dayOfMonth)
                -> liveData.setValue(LocalDate.of(year, month, dayOfMonth)),
                2000, 1, 1).show();
    }

}
