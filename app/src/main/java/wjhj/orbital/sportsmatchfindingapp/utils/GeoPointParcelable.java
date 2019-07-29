package wjhj.orbital.sportsmatchfindingapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

public class GeoPointParcelable implements Parcelable {
    private double latitude;
    private double longitude;

    public GeoPointParcelable(GeoPoint geoPoint) {
        this.latitude = geoPoint.getLatitude();
        this.longitude = geoPoint.getLongitude();
    }

    public GeoPointParcelable(Parcel parcel) {
        this.latitude = parcel.readDouble();
        this.longitude = parcel.readDouble();
    }

    public static final Parcelable.Creator<GeoPointParcelable> CREATOR
            = new Parcelable.Creator<GeoPointParcelable>() {
        public GeoPointParcelable createFromParcel(Parcel in) {
            return new GeoPointParcelable(in);
        }

        public GeoPointParcelable[] newArray(int size) {
            return new GeoPointParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    public GeoPoint toGeoPoint() {
        return new GeoPoint(latitude, longitude);
    }
}
