package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGamesTabBinding;
import wjhj.orbital.sportsmatchfindingapp.game.Game;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesTabFragment extends Fragment {

    private static String GAMES_PAGE_DEBUG = "games_tab_page";
    private static String TAB_NAME_TAG = "tab_name";
    private static String GAME_IDS_TAG = "game_ids";

    private String mTabName;
    private GamesTabViewModel gamesTabViewModel;
    private FragmentGamesTabBinding binding;
    private GamesCardAdapter mGamesCardAdapter;
    private ArrayAdapter<CharSequence> mArrayAdapter;

    public GamesTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param gameIds ArrayList of the ids of the games to be displayed in this fragment.
     * @return A new instance of fragment GamesTabFragment.
     */
    public static GamesTabFragment newInstance(String gameStatus, ArrayList<String> gameIds) {
        GamesTabFragment gamesTabFragment = new GamesTabFragment();
        Bundle args = new Bundle();
        args.putString(TAB_NAME_TAG, gameStatus);
        args.putStringArrayList(GAME_IDS_TAG, gameIds);
        gamesTabFragment.setArguments(args);
        return gamesTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(GAMES_PAGE_DEBUG, "Created tab");
        List<String> gameIds = new ArrayList<>();

        if (getArguments() != null) {
            mTabName = getArguments().getString(TAB_NAME_TAG);
           gameIds = getArguments().getStringArrayList(GAME_IDS_TAG);
        }
        gamesTabViewModel= ViewModelProviders.of(this).get(GamesTabViewModel.class);
        gamesTabViewModel.loadGames(gameIds);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGamesTabBinding.inflate(inflater, container, false);
        binding.setGamesTab(gamesTabViewModel);
        binding.setLifecycleOwner(this);

        setUpRecyclerView(binding.gamesTabRecyclerView);
        setUpDropdownSpinner(binding.gamesTabSpinner);

        return binding.getRoot();
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mGamesCardAdapter = new GamesCardAdapter();
        recyclerView.setAdapter(mGamesCardAdapter);

        gamesTabViewModel.getGames().observe(getViewLifecycleOwner(), newGames -> {
            List<Game> oldGames = mGamesCardAdapter.getGames();
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new GamesDiffCallback(oldGames, newGames));
            mGamesCardAdapter.updateGames(newGames);
            result.dispatchUpdatesTo(mGamesCardAdapter);
        });
    }

    private void setUpDropdownSpinner(AppCompatSpinner spinner) {
        mArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.games_search_options,
                android.R.layout.simple_spinner_item);

        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mArrayAdapter);
    }


    public String getTabName() {
        return mTabName;
    }

    public void updateIds(List<String> newIds) {
        gamesTabViewModel.loadGames(newIds);
    }
}
