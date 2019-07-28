package wjhj.orbital.sportsmatchfindingapp.utils;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.GeoPoint;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;

import wjhj.orbital.sportsmatchfindingapp.R;
import wjhj.orbital.sportsmatchfindingapp.maps.LocationPickerMapFragment;

public class LocationPickerActivity extends AppCompatActivity implements
        LocationPickerMapFragment.OnMapFragmentCancelledListener,
        LocationPickerMapFragment.LocationPickerListener {

    public static final String LOCATION_NAME = "name";
    public static final String GEOPOINT = "point";
    private static String LOCATION_PICKER_TAG = "location_picker";

    GeoPoint geoPoint;
    String locationName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_picker_activity);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        MapboxMapOptions options = MapboxMapOptions.createFromAttributes(this, null);
        options.camera(new CameraPosition.Builder()
                .zoom(13.3)
                .build());

        LocationPickerMapFragment mapFragment = LocationPickerMapFragment.newInstance(options);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.location_picker_fragment_container, mapFragment, LOCATION_PICKER_TAG)
                .commit();
    }

    @Override
    public void onMapCancelled(LocationPickerMapFragment locationPickerMapFragment) {}

    @Override
    public void onLocationPicked(LocationPickerMapFragment locationPickerMapFragment, Point selectedPoint, String selectedPlaceName) {
        geoPoint = new GeoPoint(selectedPoint.latitude(), selectedPoint.longitude());
        locationName = selectedPlaceName;
        Intent intent = new Intent();
        intent.putExtra(LOCATION_NAME, locationName);
        intent.putExtra(GEOPOINT, new GeoPointParcelable(geoPoint));
        setResult(RESULT_OK, intent);
        finish();
    }
}
