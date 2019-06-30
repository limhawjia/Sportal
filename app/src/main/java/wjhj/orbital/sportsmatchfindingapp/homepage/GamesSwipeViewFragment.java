package wjhj.orbital.sportsmatchfindingapp.homepage;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGamesSwipeViewBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesSwipeViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesSwipeViewFragment extends Fragment {
    private static String GAMES_PAGE_DEBUG = "games page";
    private static String GAME_STATUSES_TAG = "game statuses";

    private FragmentGamesSwipeViewBinding binding;
    private ArrayList<String> mTabNames;

    public GamesSwipeViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment GamesSwipeViewFragment.
     */
    public static GamesSwipeViewFragment newInstance(ArrayList<String> tabNames) {
        GamesSwipeViewFragment gamesSwipeViewFragment = new GamesSwipeViewFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(GAME_STATUSES_TAG, tabNames);
        return new GamesSwipeViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(GAMES_PAGE_DEBUG, "Created fragment");
        if (getArguments() != null) {
            mTabNames = getArguments().getStringArrayList(GAME_STATUSES_TAG);
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

        return binding.getRoot();
    }
}
