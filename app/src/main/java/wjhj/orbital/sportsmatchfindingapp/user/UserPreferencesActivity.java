package wjhj.orbital.sportsmatchfindingapp.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
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

import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.auth.SignUpActivity;
import wjhj.orbital.sportsmatchfindingapp.databinding.UserPreferencesActivityBinding;
import wjhj.orbital.sportsmatchfindingapp.dialogs.DatePickerFragment;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.homepage.HomepageActivity;
import wjhj.orbital.sportsmatchfindingapp.maps.Country;

public class UserPreferencesActivity extends AppCompatActivity implements DatePickerFragment.DatePickerListener {

    public static final String EDIT_PROFILE_TAG = "edit_profile";
    public static final String DISPLAY_NAME_TAG = "display_name";
    public static final int PICK_DISPLAY_IMAGE_RC = 1;

    private FirebaseUser currUser;
    private UserPreferencesActivityBinding binding;
    private UserPreferencesViewModel viewModel;

    private String displayName = "default_name";
    private String editProfileUid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("Preferences created");

        currUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle args = getIntent().getExtras();
        if (args != null) {
            if (args.getString(DISPLAY_NAME_TAG) != null) {
                displayName = args.getString(DISPLAY_NAME_TAG);
            }
            editProfileUid = args.getString(EDIT_PROFILE_TAG);
        }

        viewModel = ViewModelProviders.of(this).get(UserPreferencesViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.user_preferences_activity);
        binding.setUserPreferences(viewModel);
        binding.setActivity(this);
        binding.setLifecycleOwner(this);


        if (editProfileUid != null) {
            viewModel.linkWithExistingProfile(editProfileUid);
            binding.setupAccountMessage.setVisibility(View.INVISIBLE);
            binding.preferencesWelcomeText.setText(getString(R.string.preferences_edit_profile));
        }

        binding.addDisplayPicButton.setOnClickListener(view -> {
            Intent pickImageIntent = new Intent();
            pickImageIntent.setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(pickImageIntent, PICK_DISPLAY_IMAGE_RC);
        });

        initSportsPreferencePicker(binding.sportsPreferenceRecyclerView);

        binding.preferencesDoneButton.setOnClickListener(view ->
                viewModel.updateProfile(displayName, currUser.getUid()));

        viewModel.getSuccess().observe(this, success -> {
            if (success) {
                if (editProfileUid == null) {
                    Intent intent = new Intent(this, HomepageActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }


    private void initSportsPreferencePicker(RecyclerView sportsPreferenceRecyclerView) {
        sportsPreferenceRecyclerView.setHasFixedSize(true);
        sportsPreferenceRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        SportPreferencesAdapter adapter = new SportPreferencesAdapter();
        sportsPreferenceRecyclerView.setAdapter(adapter);

        viewModel.getSportPreferences().observe(this, adapter::updateList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DISPLAY_IMAGE_RC && resultCode == RESULT_OK) {
            if (data != null) {
                viewModel.setDisplayPicUri(data.getData());
            }
        }
    }

    public void openDatePicker() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "birthday picker");
    }

    public void openCountryPicker() {
        SpinnerDialog dialog = new SpinnerDialog(this,
                Country.getCountryNamesArrList(),
                "Select country",
                R.style.PopupDialogTheme,
                "Close");

        dialog.setCancellable(true);
        dialog.bindOnSpinerListener((item, position) ->
                viewModel.setCountry(Country.values()[position]));

        dialog.showSpinerDialog();
    }


    public void openSportPicker(View v) {
        boolean[] sportSelections = viewModel.getSportSelections();

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMultiChoiceItems(Sport.getAllSportsString(), sportSelections,
                        (dialog, which, isChecked) -> sportSelections[which] = isChecked)
                .setTitle(R.string.sport_dialog)
                .setPositiveButton("Ok", (dialog, id) ->
                        viewModel.updateSportPreferences(sportSelections));

        builder.create()
                .show();
    }

    @Override
    public void onDialogDateSet(DatePickerFragment datePickerFragment, LocalDate dateSet) {
        viewModel.setBirthday(dateSet);
    }


    private class SportPreferencesAdapter extends RecyclerView.Adapter<SportsPreferenceHolder> {
        private List<Sport> sportPreferences;
        private ViewGroup parent;

        SportPreferencesAdapter() {
            sportPreferences = new ArrayList<>();
        }

        void updateList(List<Sport> list) {
            AutoTransition transition = new AutoTransition();
            transition.setDuration(150);
            TransitionManager.beginDelayedTransition(parent, transition);
            sportPreferences = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SportsPreferenceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            this.parent = parent;
            return new SportsPreferenceHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SportsPreferenceHolder holder, int position) {
            if (position == sportPreferences.size()) {
                holder.bindAddButton();
            } else {
                holder.bind(sportPreferences.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return sportPreferences.size() + 1;
        }
    }

    private class SportsPreferenceHolder extends RecyclerView.ViewHolder {
        ImageView sportIcon;
        ImageView plusIcon;
        TextView sportName;

        SportsPreferenceHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sports_preference_item, parent, false));
            sportIcon = itemView.findViewById(R.id.sport_icon);
            plusIcon = itemView.findViewById(R.id.sport_add_icon);
            sportName = itemView.findViewById(R.id.sport_name);
        }

        void bind(Sport sport) {
            sportIcon.setImageResource(sport.getIconResourceId());
            sportName.setText(sport.toString().toUpperCase());
            plusIcon.setVisibility(View.INVISIBLE);
            itemView.setBackgroundResource(R.drawable.bordered_box);
        }

        void bindAddButton() {
            sportIcon.setImageResource(0);
            sportName.setText("");
            plusIcon.setVisibility(View.VISIBLE);
            itemView.setBackgroundResource(R.drawable.dotted_bordered_box);
            itemView.setOnClickListener(UserPreferencesActivity.this::openSportPicker);
        }
    }

}
