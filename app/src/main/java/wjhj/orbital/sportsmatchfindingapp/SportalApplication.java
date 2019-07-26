package wjhj.orbital.sportsmatchfindingapp;

import android.app.Application;

import com.sendbird.android.SendBird;

import timber.log.Timber;

public class SportalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        SendBird.init(getString(R.string.sendbird_app_id), this);
    }
}
