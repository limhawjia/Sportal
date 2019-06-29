package wjhj.orbital.sportsmatchfindingapp.homepage;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGamesSwipeViewBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesSwipeViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesSwipeViewFragment extends Fragment {

    private static String GAMES_PAGE_DEBUG = "games page";

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(GAMES_PAGE_DEBUG, "Created fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(GAMES_PAGE_DEBUG, "Created view");
        binding = FragmentGamesSwipeViewBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_games_swipe_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
