package wjhj.orbital.sportsmatchfindingapp.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.auth.Authentications;
import wjhj.orbital.sportsmatchfindingapp.databinding.PreferencesActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.dialogs.DatePickerFragment;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;

public class PreferencesActivity extends AppCompatActivity implements DatePickerFragment.DatePickerListener {

    public static String PREFERENCES_DEBUG = "preferences";
    private static final String DISPLAY_NAME_TAG = "display_name;";
    public static final int PICK_DISPLAY_IMAGE_RC = 1;

    private FirebaseUser currUser;
    private PreferencesActivityBinding binding;
    private UserPreferencesViewModel userPreferencesViewModel;
    private RecyclerView sportsPreferenceRecyclerView;

    private String displayName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(PREFERENCES_DEBUG, "Preferences created");

        currUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle args = getIntent().getExtras();
        if (args != null) {
            displayName = args.getString(DISPLAY_NAME_TAG, "default_name");
        }

        userPreferencesViewModel = ViewModelProviders.of(this).get(UserPreferencesViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.preferences_activity);
        binding.setUserPreferences(userPreferencesViewModel);
        binding.setActivity(this);
        binding.setLifecycleOwner(this);

        binding.addDisplayPicButton.setOnClickListener(view -> {
            Intent pickImageIntent = new Intent();
            pickImageIntent.setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(pickImageIntent, PICK_DISPLAY_IMAGE_RC);
        });

        sportsPreferenceRecyclerView = binding.sportsPreferenceRecyclerView;
        sportsPreferenceRecyclerView.setHasFixedSize(true);
        sportsPreferenceRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        sportsPreferenceRecyclerView.setAdapter(new SportPreferencesAdapter());


        findViewById(R.id.preferences_done_button).setOnClickListener(view ->
                userPreferencesViewModel.updatePreferences(displayName,"abcd"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DISPLAY_IMAGE_RC && resultCode == RESULT_OK) {
            if (data != null) {
                userPreferencesViewModel.setDisplayPicUri(data.getData());
            }
        }
    }

    public void selectDate(View v) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "birthday picker");
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

//    public void updatePreferences(View v) {
//        UserProfile user;
//        try {
//            user = UserProfile.builder().withDisplayName(displayName)
//                    .withGender(userPreferencesViewModel.getGender())
//                    .withBirthday(userPreferencesViewModel.getBirthday().getInput())
//                    .withUid(currUser.getUid())
//                    .addAllPreferences(userPreferencesViewModel.getSportPreferencesToUpdate())
//                    .build();
//
//            SportalRepo repo = SportalRepo.getInstance();
//            repo.addUser(currUser.getUid(), user);
//            Intent intent = new Intent(this, HomepageActivity.class);
//            intent.putExtra(HomepageActivity.CURR_USER_TAG, currUser);
//            startActivity(intent);
//
//        } catch (NullPointerException e) {
//            if (userPreferencesViewModel.getGender() == null) {
//                Toast.makeText(this, R.string.gender_reminder, Toast.LENGTH_SHORT).show();
//            }
//            if (userPreferencesViewModel.getBirthday().getInput() == null) {
//                binding.preferencesBirthdayInput.setError("Please enter a valid birthday");
//            }
//        }
//    }

    @Override
    public void onDialogDateSet(DatePickerFragment datePickerFragment, LocalDate dateSet) {
        userPreferencesViewModel.setBirthday(dateSet);
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
