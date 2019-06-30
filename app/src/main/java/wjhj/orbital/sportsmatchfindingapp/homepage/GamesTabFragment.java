package wjhj.orbital.sportsmatchfindingapp.homepage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGamesTabBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesTabFragment extends Fragment {

    private static String GAMES_PAGE_DEBUG = "games_tab_page";
    private static String GAME_IDS_TAG = "game_ids";

    private GamesTabViewModel gamesTabViewModel;
    private FragmentGamesTabBinding binding;
    private GamesCardAdapter mGamesCardAdapter;
    private List<String> mGameIds;

    public GamesTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param gameIds ArrayList of the ids of the games to be displayed in this fragment.
     * @return A new instance of fragment GamesTabFragment.
     */
    public static GamesTabFragment newInstance(ArrayList<String> gameIds) {
        GamesTabFragment gamesTabFragment = new GamesTabFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(GAME_IDS_TAG, gameIds);
        gamesTabFragment.setArguments(args);
        return gamesTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(GAMES_PAGE_DEBUG, "Created tab");
        if (getArguments() != null) {
           mGameIds = getArguments().getStringArrayList(GAME_IDS_TAG);
        }
        gamesTabViewModel= ViewModelProviders.of(this).get(GamesTabViewModel.class);
        gamesTabViewModel.loadGames(mGameIds);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGamesTabBinding.inflate(inflater, container, false);
        binding.setGamesTab(gamesTabViewModel);
        binding.setLifecycleOwner(this);

        setUpRecyclerView(binding.gamesTabRecyclerView);

        binding.gamesTabSearch.setOnClickListener(view -> mGamesCardAdapter.updateGames(new ArrayList<>()));

        return binding.getRoot();
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mGamesCardAdapter = new GamesCardAdapter();
        recyclerView.setAdapter(mGamesCardAdapter);

        gamesTabViewModel.getGames().observe(getViewLifecycleOwner(), newGames -> {
            mGamesCardAdapter.updateGames(newGames);
            Log.d("games_swipe_view", "games: " + newGames.toString());
        });
    }
}
