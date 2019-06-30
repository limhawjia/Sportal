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

import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGamesTabBinding;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesTabFragment extends Fragment {

    private static String GAMES_PAGE_DEBUG = "games_page";
    private static String GAME_STATUS_TAG = "game_status";

    private UserProfileViewModel userProfileViewModel;
    private FragmentGamesTabBinding binding;
    private String mTabName;

    public GamesTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment GamesTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GamesTabFragment newInstance(String tabName) {
        GamesTabFragment gamesTabFragment = new GamesTabFragment();
        Bundle args = new Bundle();
        args.putString(GAME_STATUS_TAG, tabName);
        return gamesTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(GAMES_PAGE_DEBUG, "Created tab");
        if (getArguments() != null) {
            mTabName = getArguments().getString(GAME_STATUS_TAG);
        }
        userProfileViewModel = ViewModelProviders.of(getActivity()).get(UserProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGamesTabBinding.inflate(inflater, container, false);
        binding.setUserProfile(userProfileViewModel);
        binding.setLifecycleOwner(getActivity());

        RecyclerView recyclerView = binding.confirmedGamesRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GamesCardAdapter adapter = new GamesCardAdapter(userProfileViewModel.getConfirmedGames().getValue());
        recyclerView.setAdapter(adapter);

        binding.testFilterButton.setOnClickListener(view -> adapter.remove());

        return binding.getRoot();
    }
}
