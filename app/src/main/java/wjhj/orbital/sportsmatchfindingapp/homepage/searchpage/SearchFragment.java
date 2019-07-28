package wjhj.orbital.sportsmatchfindingapp.homepage.searchpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ImmutableList;
import com.google.firebase.firestore.GeoPoint;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentSearchBinding;
import wjhj.orbital.sportsmatchfindingapp.dialogs.SearchFilterDialogFragment;
import wjhj.orbital.sportsmatchfindingapp.dialogs.SportMultiSelectDialogFragment;
import wjhj.orbital.sportsmatchfindingapp.game.GameActivity;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.homepage.gamespage.GamesCardAdapter;
import wjhj.orbital.sportsmatchfindingapp.maps.LocationPickerMapFragment;
import wjhj.orbital.sportsmatchfindingapp.repo.GameSearchFilter;

public class SearchFragment extends Fragment implements
        AdapterView.OnItemSelectedListener, SearchFilterDialogFragment.SearchFilterDialogListener {
    private ImmutableList<Sport> mUserPreferences;
    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;
    private MutableLiveData<String> locationName;

    private static String SPORTS_PREFERENCES_TAG = " sports_preferences";
    private static String SEARCH_PAGE_DEBUG = "search_debug";
    public static int START_FILTER_DIALOG = 1;
    public static String LOCATION_PICKER_TAG = "location";

    public SearchFragment() {
    }

    public static SearchFragment newInstance(ImmutableList<Sport> sportsPreferences) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        if (sportsPreferences != null) {
            args.putParcelableArrayList(SPORTS_PREFERENCES_TAG, new ArrayList<>(sportsPreferences));
        }
        searchFragment.setArguments(args);
        return searchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(SEARCH_PAGE_DEBUG, "Created search page");

        if (getArguments() != null) {
            ArrayList<Sport> sports = getArguments().getParcelableArrayList(SPORTS_PREFERENCES_TAG);
            if (sports != null) {
                mUserPreferences = ImmutableList.<Sport>builder().addAll(sports).build();
            } else {
                mUserPreferences = ImmutableList.<Sport>builder().build();
            }
        }

        initViewModel();
    }

    private void initViewModel() {
        SearchViewModelFactory searchViewModelFactory = new SearchViewModelFactory(mUserPreferences);
        searchViewModel = ViewModelProviders.of(this, searchViewModelFactory).get(SearchViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        binding.setSearch(searchViewModel);
        binding.setLifecycleOwner(this);

        setUpMainSpinner(binding.sortSpinner);
        setUpRecyclerView(binding.searchRecyclerView);

        binding.filtersButton.setOnClickListener(view -> {
            SearchFilterDialogFragment dialog = new SearchFilterDialogFragment(searchViewModel.getSearchFilters().getValue());
            dialog.setTargetFragment(this, START_FILTER_DIALOG);
            dialog.show(requireFragmentManager(), "filter");
        });

        binding.sportFilterButton.setOnClickListener(view -> {
            DialogFragment dialog = new SportMultiSelectDialogFragment(searchViewModel
                    .getSearchFilters().getValue().getSportQuery());
            dialog.show(requireFragmentManager(), "sport_filter");
        });

        return binding.getRoot();
    }

    private void setUpMainSpinner(AppCompatSpinner spinner) {
        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.games_search_options, android.R.layout.simple_spinner_item);

        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sortByAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GamesCardAdapter mGamesCardAdapter = new GamesCardAdapter(game -> {
            Intent intent = new Intent(requireContext(), GameActivity.class);
            intent.putExtra(GameActivity.GAME_UID, game.getUid());
            startActivity(intent);
        });
        recyclerView.setAdapter(mGamesCardAdapter);

        searchViewModel.getGamesData()
                .observe(getViewLifecycleOwner(), mGamesCardAdapter::updateGames);
    }

    public void updateFilterFromSearchFilterDialog(GameSearchFilter filter) {
        GameSearchFilter oldFilters = searchViewModel.getSearchFilters().getValue();
        oldFilters.setTimeOfDayQuery(filter.getTimeOfDayQuery());
        oldFilters.setSkillLevelQuery(filter.getSkillLevelQuery());
        searchViewModel.postNewFilters(oldFilters);
    }

    public void updateSports(List<Sport> sports) {
        searchViewModel.updateSports(sports);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sort_spinner) {
            switch(position) {
                case 0:
                    searchViewModel.sortGames((game1, game2)
                            -> game1.getStartDateTime().compareTo(game2.getStartDateTime()));
                    break;
                case 1:
                    searchViewModel.sortGames((game1, game2)
                            -> Integer.compare(game1.numExtraPlayersNeeded(), game2.numExtraPlayersNeeded()));
                    break;
                case 2:
                    searchViewModel.sortGames((game1, game2)
                            -> Integer.compare(game1.getSport().ordinal(), game2.getSport().ordinal()));
                    break;
                case 3:
                    searchViewModel.sortGames((game1, game2)
                            -> Integer.compare(game1.getSkillLevel().ordinal(), game2.getSkillLevel().ordinal()));
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSearchFilterDialogPositiveButtonClicked(GameSearchFilter filters) {
        this.updateFilterFromSearchFilterDialog(filters);
    }
}
