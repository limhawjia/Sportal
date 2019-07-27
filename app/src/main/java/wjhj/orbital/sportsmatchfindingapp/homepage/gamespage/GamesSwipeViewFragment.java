package wjhj.orbital.sportsmatchfindingapp.homepage.gamespage;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import timber.log.Timber;
import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGamesSwipeViewBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesSwipeViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesSwipeViewFragment extends Fragment {

    private FragmentGamesSwipeViewBinding binding;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Timber.d("Created games swipe view");
        binding = FragmentGamesSwipeViewBinding.inflate(inflater, container, false);

        GamesSwipeViewPagerAdapter adapter = new GamesSwipeViewPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        setUpSwipeView(adapter);

        return binding.getRoot();
    }

    private void setUpSwipeView(GamesSwipeViewPagerAdapter adapter) {
        binding.gamesSwipeView.setAdapter(adapter);
        binding.gamesTabLayout.setupWithViewPager(binding.gamesSwipeView);
    }
}
