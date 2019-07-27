package wjhj.orbital.sportsmatchfindingapp.game;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import wjhj.orbital.sportsmatchfindingapp.repo.SportalRepo;

public class GameDetailsViewModel extends ViewModel {
    public LiveData<List<String>> getmParticipants() {
        return mParticipants;
    }

    private LiveData<Game> mGame;
    private LiveData<List<String>> mParticipants;
    private LiveData<Integer> mNumParticipating;
    private LiveData<Integer> mMaxPlayers;
    private LiveData<Integer> mMinPlayers;
    private MediatorLiveData<Boolean> mReady;

    public GameDetailsViewModel(String gameUid) {
        mGame = SportalRepo.getInstance().getGame(gameUid);
        mParticipants = Transformations.map(mGame, Game::getParticipatingUids);
        mNumParticipating = Transformations.map(mParticipants, List::size);
        mMaxPlayers = Transformations.map(mGame, Game::getMaxPlayers);
        mMinPlayers = Transformations.map(mGame, Game::getMinPlayers);
        mReady = new MediatorLiveData<>();
        mReady.addSource(mMinPlayers, min -> {
            Integer numParticipating = mNumParticipating.getValue();
            if (numParticipating != null && numParticipating < min) {
                mReady.setValue(false);
            } else {
                mReady.setValue(true);
            }
        });
        mReady.addSource(mNumParticipating, num -> {
            Integer minPlayers = mMinPlayers.getValue();
            if (minPlayers != null && num >= minPlayers) {
                mReady.setValue(true);
            } else {
                mReady.setValue(false);
            }
        });
    }

    public LiveData<Game> getGame() {
        return mGame;
    }

    public LiveData<List<String>> getParticipants() {
        return mParticipants;
    }

    public LiveData<Integer> getParticipating() {
        return mNumParticipating;
    }

    public LiveData<Integer> getMaxPlayers() {
        return mMaxPlayers;
    }

    public LiveData<Integer> getMinPlayers() {
        return mMinPlayers;
    }

    public LiveData<Boolean> getReady() {
        return mReady;
    }
}
