package wjhj.orbital.sportsmatchfindingapp.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.PreferencesActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

public class PreferencesActivity extends AppCompatActivity {
    public static String PREFERENCES_DEBUG = "preferences";
    private PreferencesActivityBinding binding;
    private UserPreferencesViewModel userPreferencesViewModel;
    private RecyclerView sportsPreferenceRecyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(PREFERENCES_DEBUG, "Preferences created");
        userPreferencesViewModel = ViewModelProviders.of(this).get(UserPreferencesViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.preferences_activity);
        binding.setUserPreferences(userPreferencesViewModel);
        binding.setActivity(this);
        userPreferencesViewModel.getBirthday().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.birthdayField.setText(s);
                Log.d(PreferencesActivity.PREFERENCES_DEBUG, "Birthday changed");
            }
         });
        sportsPreferenceRecyclerView = binding.sportsPreferenceRecyclerView;
        sportsPreferenceRecyclerView.setHasFixedSize(true);
        sportsPreferenceRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        sportsPreferenceRecyclerView.setAdapter(new SportPreferencesAdapter());
    }

    public void selectDate(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Log.d(PreferencesActivity.PREFERENCES_DEBUG, "clicked");

        View dialogView = getLayoutInflater().inflate(R.layout.date_dialog, null);

        builder.setView(dialogView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker datePicker = dialogView.findViewById(R.id.dialog_datepicker);
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();

                        String date = day + "/" + month + "/" + year;
                        userPreferencesViewModel.setBirthday(date);
                        Log.d(PreferencesActivity.PREFERENCES_DEBUG, userPreferencesViewModel.getBirthday().getValue());
                    }
                })
                .setTitle(R.string.birthday_dialog)
                .create()
                .show();
    }

    public void selectSport(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ArrayList<Sport> selectedItems = new ArrayList<>();
        String[] string = Sport.getAllSportsString();
        Sport[] sports = Sport.getAllSports();
        builder.setMultiChoiceItems(Sport.getAllSportsString(), null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedItems.add(sports[which]);
                } else {
                    selectedItems.remove(sports[which]);
                }
            }
        })
                .setTitle(R.string.sport_dialog)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Sport> sports = userPreferencesViewModel.getSportPreferences();
                        for (Sport selectedItem : selectedItems) {
                            if (!sports.contains(selectedItem)) {
                                sports.add(selectedItem);
                            }
                        }
                        sportsPreferenceRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                })
                .create()
                .show();
    }


    private class SportsPreferenceHolder extends RecyclerView.ViewHolder {
        wjhj.orbital.sportsmatchfindingapp.game.Sport sport;
        ImageView sportIcon;
        TextView sportName;

        SportsPreferenceHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sports_preference_item, parent, false));
            sportIcon = itemView.findViewById(R.id.sport_icon);
            sportName = itemView.findViewById(R.id.sport_name);
        }

        void bind(wjhj.orbital.sportsmatchfindingapp.game.Sport sport) {
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

        @Override
        public SportsPreferenceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SportsPreferenceHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(SportsPreferenceHolder holder, int position) {
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
