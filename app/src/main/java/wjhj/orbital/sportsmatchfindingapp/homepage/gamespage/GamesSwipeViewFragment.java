package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGamesSwipeViewBinding;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesSwipeViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesSwipeViewFragment extends Fragment {
    private static String GAMES_PAGE_DEBUG = "games_swipe_view";

    private FragmentGamesSwipeViewBinding binding;
    private UserProfileViewModel userProfileViewModel;

    public GamesSwipeViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment GamesSwipeViewFragment.
     */
    public static GamesSwipeViewFragment newInstance() {
        return new GamesSwipeViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(GAMES_PAGE_DEBUG, "Created fragment");
        userProfileViewModel = ViewModelProviders.of(getActivity()).get(UserProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(GAMES_PAGE_DEBUG, "Created view");
        binding = FragmentGamesSwipeViewBinding.inflate(inflater, container, false);

        GamesSwipeViewPagerAdapter adapter = new GamesSwipeViewPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        setUpSwipeView(adapter);

        userProfileViewModel.getGameIds().observe(getActivity(), adapter::updateGameIds);

        return binding.getRoot();
    }

    private void setUpSwipeView(GamesSwipeViewPagerAdapter adapter) {
        binding.gamesSwipeView.setAdapter(adapter);
        binding.gamesTabLayout.setupWithViewPager(binding.gamesSwipeView);
    }
}
