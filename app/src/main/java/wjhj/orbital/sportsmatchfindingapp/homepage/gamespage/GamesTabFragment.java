package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGamesTabBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Difficulty;
import wjhj.orbital.sportsmatchfindingapp.game.GameStatus;
import wjhj.orbital.sportsmatchfindingapp.game.Sport;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesTabFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static String GAMES_PAGE_DEBUG = "games_tab_page";
    private static String TAB_NAME_TAG = "tab_name";

    private GameStatus mGameStatus;
    private GamesTabViewModel gamesTabViewModel;
    private FragmentGamesTabBinding binding;

    public GamesTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gameStatus Status of the games this Fragment will display.
     * @return A new instance of fragment GamesTabFragment.
     */
    public static GamesTabFragment newInstance(String gameStatus) {
        GamesTabFragment gamesTabFragment = new GamesTabFragment();
        Bundle args = new Bundle();
        args.putString(TAB_NAME_TAG, gameStatus);
        gamesTabFragment.setArguments(args);
        return gamesTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(GAMES_PAGE_DEBUG, "Created tab");

        if (getArguments() != null) {
            mGameStatus = GameStatus.fromString(getArguments().getString(TAB_NAME_TAG));
        }

        initViewModel();
    }

    private void initViewModel() {
        UserProfileViewModel userProfileViewModel = ViewModelProviders.of(getParentFragment().getActivity())
                        .get(UserProfileViewModel.class);

        GamesTabViewModelFactory factory = new GamesTabViewModelFactory(mGameStatus, userProfileViewModel);
        gamesTabViewModel = ViewModelProviders.of(this, factory).get(GamesTabViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGamesTabBinding.inflate(inflater, container, false);
        binding.setGamesTab(gamesTabViewModel);
        binding.setLifecycleOwner(this);

        setUpRecyclerView(binding.gamesTabRecyclerView);
        setUpMainSpinner(binding.gamesTabSpinnerMain);

        return binding.getRoot();
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GamesCardAdapter mGamesCardAdapter = new GamesCardAdapter();
        recyclerView.setAdapter(mGamesCardAdapter);

        gamesTabViewModel.getGamesLiveData().observe(getViewLifecycleOwner(), mGamesCardAdapter::updateGames);
        gamesTabViewModel.getFilteredGames().observe(getViewLifecycleOwner(), mGamesCardAdapter::updateGames);
    }

    private void setUpMainSpinner(AppCompatSpinner spinner) {
        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.games_search_options, android.R.layout.simple_spinner_item);

        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sortByAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private <T> void addSecondarySpinner(T[] objects, int viewId) {
        AppCompatSpinner spinner = new AppCompatSpinner(getContext());
        ArrayAdapter<T> secondaryAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, objects);
        secondaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(secondaryAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setId(viewId);
        binding.gamesTabSpinnerSecondaryContainer.addView(spinner);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.games_tab_spinner_main:
                mainSpinnerOnItemSelected(position);
                break;
            case R.id.games_tab_sport_spinner:
                sportSpinnerOnItemSelected(position);
                break;
            case R.id.games_tab_difficulty_spinner:
                difficultySpinnerOnItemSelected(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void mainSpinnerOnItemSelected(int position) {
        binding.gamesTabSpinnerSecondaryContainer.removeAllViewsInLayout();
        switch (position) {
            case 0: // Date
                gamesTabViewModel.sortGames((game1, game2)
                        -> game1.getStartTime().compareTo(game2.getStartTime()));
                break;
            case 1: // Players needed
                gamesTabViewModel.sortGames((game1, game2)
                        -> Integer.compare(game1.numExtraPlayersNeeded(), game2.numExtraPlayersNeeded()));
                break;
            case 2: // Sport
                addSecondarySpinner(Sport.values(), R.id.games_tab_sport_spinner);
                break;
            case 3: // Skill Level
                addSecondarySpinner(Difficulty.values(), R.id.games_tab_difficulty_spinner);
        }
    }

    private void sportSpinnerOnItemSelected(int position) {
        Sport sportSelected = Sport.values()[position];
        gamesTabViewModel.filterSports(sportSelected);
    }

    private void difficultySpinnerOnItemSelected(int position) {
        Difficulty difficultySelected = Difficulty.values()[position];
        gamesTabViewModel.filterDifficulty(difficultySelected);
    }

}
