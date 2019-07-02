package wjhj.orbital.sportsmatchfindingapp.homepage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.util.DataUtils;
import com.google.firebase.auth.FirebaseUser;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.function.Consumer;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.GameSearchActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.homepage.HomepageActivity;
import wjhj.orbital.sportsmatchfindingapp.homepage.SearchGameViewModel;

public class GameSearchActivity extends AppCompatActivity {
    private SearchGameViewModel viewModel;
    private GameSearchActivityBinding binding;
    private FirebaseUser currUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currUser = (FirebaseUser) getIntent().getExtras().get(HomepageActivity.CURR_USER_TAG);

        viewModel = ViewModelProviders.of(this).get(SearchGameViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.game_search_activity);
        binding.setSearchGameViewModel(viewModel);
        binding.setLifecycleOwner(this);

        ArrayAdapter<String> sportAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, Sport.getAllSportsString());
        binding.sports.setAdapter(sportAdapter);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, new String[]{"North", "South", "East", "West"});
        binding.addGameLocationInput.setAdapter(locationAdapter);

        ArrayAdapter<String> skillAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, new String[]{"Beginner", "Intermediate", "Advanced"});
        binding.skill.setAdapter(skillAdapter);

        binding.startDate.setOnClickListener((View v) -> getDate(viewModel.getStartDate()));
        binding.endDate.setOnClickListener((View v) -> getDate(viewModel.getEndDate()));


        binding.startTime.setOnClickListener((View v) -> getTime(viewModel.getStartTime()));
        binding.endTime.setOnClickListener((View v) -> getTime(viewModel.getEndTime()));
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
