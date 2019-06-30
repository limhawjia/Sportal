package wjhj.orbital.sportsmatchfindingapp.homepage;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGamesSwipeViewBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesSwipeViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesSwipeViewFragment extends Fragment {
    private static String GAMES_PAGE_DEBUG = "gamesSwipeView";
    private static String GAME_STATUSES_TAG = "gameStatuses";

    private FragmentGamesSwipeViewBinding binding;
    private String[] mTabNames;

    public GamesSwipeViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment GamesSwipeViewFragment.
     */
    public static GamesSwipeViewFragment newInstance(String[] tabNames) {
        GamesSwipeViewFragment gamesSwipeViewFragment = new GamesSwipeViewFragment();
        Bundle args = new Bundle();
        args.putStringArray(GAME_STATUSES_TAG, tabNames);
        gamesSwipeViewFragment.setArguments(args);
        return gamesSwipeViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(GAMES_PAGE_DEBUG, "Created fragment");
        if (getArguments() != null) {
            mTabNames = getArguments().getStringArray(GAME_STATUSES_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(GAMES_PAGE_DEBUG, "Created view");
        binding = FragmentGamesSwipeViewBinding.inflate(inflater, container, false);

        GamesSwipeViewPagerAdapter adapter = new GamesSwipeViewPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                mTabNames);
        binding.gamesSwipeView.setAdapter(adapter);
        binding.gamesTabLayout.setupWithViewPager(binding.gamesSwipeView);

        return binding.getRoot();
    }
}
