package wjhj.orbital.sportsmatchfindingapp.game;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.databinding.FragmentGameDetailsBinding;
import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;
import wjhj.orbital.sportsmatchfindingapp.user.DisplayUserProfileFragment;
import wjhj.orbital.sportsmatchfindingapp.user.FriendProfilesAdapter;
import wjhj.orbital.sportsmatchfindingapp.user.UserProfile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameDetailsFragment extends Fragment implements FriendProfilesAdapter.UserProfileClickListener {

    public static final String GAME_UID = "game_uid";

    private String mGameUid;

    private GameDetailsViewModel viewModel;
    private FragmentGameDetailsBinding binding;
    private SportalRepo repo;


    public GameDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gameUid Parameter 1.
     * @return A new instance of fragment GameDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameDetailsFragment newInstance(String gameUid) {
        GameDetailsFragment fragment = new GameDetailsFragment();
        Bundle args = new Bundle();
        args.putString(GAME_UID, gameUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGameUid = getArguments().getString(GAME_UID);
        }
        repo = SportalRepo.getInstance();
        initViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentGameDetailsBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setUpParticipantsRecyclerView(binding.participantsRecycler);

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onUserProfileClick(String uid) {
        FragmentManager fm = requireFragmentManager();
        DisplayUserProfileFragment fragment = DisplayUserProfileFragment.newInstance(uid);

        fm.beginTransaction().replace(R.id.game_activity_secondary_container, fragment).commit();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initViewModel() {
        GameUidViewModelFactory factory = new GameUidViewModelFactory(mGameUid);

        viewModel = ViewModelProviders.of(this, factory).get(GameDetailsViewModel.class);
        viewModel.getResult().observe(this, bool -> {
            if (!bool) {
                Toast.makeText(requireContext(), "Process failed, please try again later", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpParticipantsRecyclerView(RecyclerView recyclerView) {
        FriendProfilesAdapter adapter = new FriendProfilesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        LiveData<List<UserProfile>> participants = repo.getParticipatingUsers(mGameUid);
        participants.observe(this, adapter::updateFriends);
    }
}
