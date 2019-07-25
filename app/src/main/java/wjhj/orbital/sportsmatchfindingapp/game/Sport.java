package wjhj.orbital.sportsmatchfindingapp.game;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;

import java9.util.stream.StreamSupport;
import wjhj.orbital.sportsmatchfindingapp.R;

public enum Sport implements Parcelable {
    FOOTBALL("Football", R.drawable.soccer_icon),
    BASKETBALL("Basketball", R.drawable.basketball_icon),
    CRICKET("Cricket", R.drawable.cricket_icon),
    BADMINTON("Badminton", R.drawable.badminton_icon),
    TENNIS("Tennis", R.drawable.squash_icon),
    SQUASH("Squash", R.drawable.squash_icon),
    FRISBEE("Frisbee", R.drawable.frisbee_icon);

    private String str;
    private int iconResourceId;

    public static final Parcelable.Creator<Sport> CREATOR = new Parcelable.Creator<Sport>() {

        @Override
        public Sport createFromParcel(Parcel source) {
            return values()[source.readInt()];
        }

        @Override
        public Sport[] newArray(int size) {
            return new Sport[size];
        }
    };

    Sport(String str, int iconResourceId) {
        this.str = str;
        this.iconResourceId = iconResourceId;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public static String[] getAllSportsString() {
        String[] strings = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            strings[i] = values()[i].toString();
        }
        return strings;
    }

    @NonNull
    @Override
    public String toString() {
        return str;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    public static Sport from(String str) {
        return StreamSupport.stream(Arrays.asList(Sport.values())).filter(sport -> sport.toString() == str)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(str + " is not a valid sport"));
    }
}
