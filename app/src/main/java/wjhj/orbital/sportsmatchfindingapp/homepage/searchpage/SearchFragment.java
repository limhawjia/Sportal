package wjhj.orbital.sportsmatchfindingapp.homepage.searchpage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGamesTabBinding;
import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentSearchBinding;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.homepage.gamespage.GamesCardAdapter;

public class SearchFragment extends Fragment {
    private ImmutableList<Sport> mUserPreferences;
    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;

    private static String SPORTS_PREFERENCES_TAG = " sports_preferences";
    private static String SEARCH_PAGE_DEBUG = "search_debug";

    public SearchFragment() {}

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
        return binding.getRoot();
    }

    private void setUpMainSpinner(AppCompatSpinner spinner) {
        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.games_search_options, android.R.layout.simple_spinner_item);

        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sortByAdapter);
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GamesCardAdapter mGamesCardAdapter = new GamesCardAdapter();
        recyclerView.setAdapter(mGamesCardAdapter);

        searchViewModel.getGamesLiveData()
                .observe(getViewLifecycleOwner(), mGamesCardAdapter::updateGames);
    }
}
