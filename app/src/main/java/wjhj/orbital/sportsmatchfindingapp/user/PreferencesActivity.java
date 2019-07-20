package wjhj.orbital.sportsmatchfindingapp.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.PreferencesActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.homepage.HomepageActivity;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class PreferencesActivity extends AppCompatActivity {
    public static String PREFERENCES_DEBUG = "preferences";
    private FirebaseUser currUser;
    private PreferencesActivityBinding binding;
    private UserPreferencesViewModel userPreferencesViewModel;
    private RecyclerView sportsPreferenceRecyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currUser = (FirebaseUser) getIntent().getExtras().get(HomepageActivity.CURR_USER_TAG);
        Log.d(PREFERENCES_DEBUG, "Preferences created");
        userPreferencesViewModel = ViewModelProviders.of(this).get(UserPreferencesViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.preferences_activity);
        binding.setUserPreferences(userPreferencesViewModel);
        binding.setActivity(this);
        userPreferencesViewModel.getBirthdayLiveData().observe(this, s -> {
            binding.birthdayField.setText(s);
            Log.d(PreferencesActivity.PREFERENCES_DEBUG, "Birthday changed");
        });
        sportsPreferenceRecyclerView = binding.sportsPreferenceRecyclerView;
        sportsPreferenceRecyclerView.setHasFixedSize(true);
        sportsPreferenceRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        sportsPreferenceRecyclerView.setAdapter(new SportPreferencesAdapter());
        findViewById(R.id.button2).setOnClickListener(this::updatePreferences);
    }

    public void selectDate(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Log.d(PreferencesActivity.PREFERENCES_DEBUG, "clicked");

        View dialogView = getLayoutInflater().inflate(R.layout.date_dialog, null);

        builder.setView(dialogView)
                .setPositiveButton("Ok", (dialog, which) -> {
                    DatePicker datePicker = dialogView.findViewById(R.id.dialog_datepicker);
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth();
                    int year = datePicker.getYear();

                    String date = day + "/" + month + "/" + year;
                    userPreferencesViewModel.setBirthday(date);
                })
                .setTitle(R.string.birthday_dialog)
                .create()
                .show();
    }

    public void selectSport(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ArrayList<Sport> selectedItems = new ArrayList<>();
        String[] string = Sport.getAllSportsString();
        Sport[] sports = Sport.values();
        boolean[] checked = new boolean[string.length];
        for (int i = 0; i < sports.length; i++) {
            if (userPreferencesViewModel.getSportPreferences().contains(sports[i])) {
                checked[i] = true;
            }
        }
        builder.setMultiChoiceItems(string, checked, (dialog, which, isChecked) -> {
            if (isChecked) {
                selectedItems.add(sports[which]);
            } else {
                selectedItems.remove(sports[which]);
            }
        })
                .setTitle(R.string.sport_dialog)
                .setPositiveButton("Ok", (dialog, which) -> {
                    List<Sport> sports1 = userPreferencesViewModel.getSportPreferences();
                    for (Sport selectedItem : selectedItems) {
                        if (!sports1.contains(selectedItem)) {
                            sports1.add(selectedItem);
                        }
                    }
                    sportsPreferenceRecyclerView.getAdapter().notifyDataSetChanged();
                })
                .create()
                .show();
    }

    public void updatePreferences(View v) {
        Toast.makeText(this, "blah", Toast.LENGTH_SHORT).show();
        UserProfile user;
        try {
            user = UserProfile.builder().withDisplayName(userPreferencesViewModel.getDisplayName())
                    .withGender(userPreferencesViewModel.getGender())
                    .withBirthday(userPreferencesViewModel.getBirthday())
                    .withUid(currUser.getUid())
                    .addAllPreferences(userPreferencesViewModel.getSportPreferencesToUpdate())
                    .build();
            SportalRepo repo = new SportalRepo();
            repo.addUser(currUser.getUid(), user);
            Intent intent = new Intent(this, HomepageActivity.class);
            intent.putExtra(HomepageActivity.CURR_USER_TAG, currUser);
            startActivity(intent);
        } catch (NullPointerException e) {
            if (userPreferencesViewModel.displayName.getValue() == null) {
                binding.displayName.setError("Please enter a display name");
            }
            if (userPreferencesViewModel.gender.getValue() == null) {
                Toast.makeText(this, R.string.gender_reminder, Toast.LENGTH_SHORT).show();
            }
            if (userPreferencesViewModel.birthday.getValue() == null) {
                binding.birthday.setError("Please enter a valid birthday");
            }
        }
    }

    private class SportsPreferenceHolder extends RecyclerView.ViewHolder {
        Sport sport;
        ImageView sportIcon;
        TextView sportName;

        SportsPreferenceHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sports_preference_item, parent, false));
            sportIcon = itemView.findViewById(R.id.sport_icon);
            sportName = itemView.findViewById(R.id.sport_name);
        }

        void bind(Sport sport) {
            this.sport = sport;
            this.sportIcon.setImageResource(sport.getIconResourceId());
            this.sportName.setText(this.sport.toString());
        }

        void bindAddButton() {
            this.sport = null;
            this.sportIcon.setImageResource(R.drawable.ic_add_box_black_24dp);
            this.sportName.setText(R.string.click_to_add);
            itemView.setOnClickListener(PreferencesActivity.this::selectSport);
        }
    }

    private class SportPreferencesAdapter extends RecyclerView.Adapter<SportsPreferenceHolder> {
        private List<Sport> sportPreferences;

        SportPreferencesAdapter() {
            sportPreferences = userPreferencesViewModel.getSportPreferences();
            sportPreferences.add(0, null);
        }

        @NonNull
        @Override
        public SportsPreferenceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SportsPreferenceHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SportsPreferenceHolder holder, int position) {
            if (position == 0) {
                holder.bindAddButton();
            } else {
                holder.bind(sportPreferences.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return sportPreferences.size();
        }
    }
}
