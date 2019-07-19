package wjhj.orbital.sportsmatchfindingapp.maps;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.MapFragmentUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wjhj.orbital.sportsmatchfindingapp.R;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;


public class LocationPickerMapFragment extends SupportMapFragment implements PermissionsListener {

    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    private static final String PLACES_AUTOCOMPLETE_TAG = "places_autocomplete";
    private static final String LOCATION_PICKER_TAG = "location_picker";

    private MapboxMap mapboxMap;
    private MapView mapView;
    private PlaceAutocompleteFragment autocompleteFragment;
    private OnMapFragmentCancelledListener closeListener;
    private LocationPickerListener locationPickerListener;
    private PermissionsManager permissionsManager;
    private ImageView hoveringMarker;
    private Button selectLocationButton;

    private Point selectedPoint;
    private String selectedPlaceName;

    public static LocationPickerMapFragment newInstance() {
        return new LocationPickerMapFragment();
    }

    @NonNull
    public static LocationPickerMapFragment newInstance(@Nullable MapboxMapOptions mapboxMapOptions) {
        LocationPickerMapFragment mapFragment = new LocationPickerMapFragment();
        mapFragment.setArguments(MapFragmentUtils.createFragmentArgs(mapboxMapOptions));
        return mapFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            closeListener = (OnMapFragmentCancelledListener) context;
            locationPickerListener = (LocationPickerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getSimpleName()
                    + " must implement OnMapFragmentCancelledListener and LocationPickerListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mapView = (MapView) super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState != null) {
            autocompleteFragment = (PlaceAutocompleteFragment)
                    requireActivity().getSupportFragmentManager().findFragmentByTag(PLACES_AUTOCOMPLETE_TAG);
        }
        return mapView;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        super.onMapReady(mapboxMap);
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            enableLocationPlugin(style);

            initHoveringMarker();
            // Initialize a hidden marker to be shown when the location is selected.
            initDroppedMarker(style);
            initSelectButton(style);
            initSearchBar(style);

            // If hovering marker is invisible and camera is moved, reset to default select location mode.
            mapboxMap.addOnCameraMoveStartedListener(reason -> {
                if (reason == MapboxMap.OnCameraMoveStartedListener.REASON_API_GESTURE) {

                    if (hoveringMarker.getVisibility() == View.INVISIBLE) {
                        // Set hovering marker to visible again.
                        hoveringMarker.setVisibility(View.VISIBLE);

                        // Hide the selected location SymbolLayer
                        if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                            style.getLayer(DROPPED_MARKER_LAYER_ID).setProperties(visibility(Property.NONE));
                        }

                        // Reset button
                        selectLocationButton.setText(requireActivity().getString(R.string.add_game_select_location));
                        selectLocationButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                        selectLocationButton.setEnabled(true);
                    }
                }

            });

        });
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationPlugin(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request.
        if (PermissionsManager.areLocationPermissionsGranted(requireActivity())) {

            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            LocationComponentOptions options = LocationComponentOptions
                    .builder(requireActivity())
                    .backgroundTintColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimaryVariant))
                    .bearingTintColor(ContextCompat.getColor(requireActivity(), R.color.mapbox_blue))
                    .build();

            LocationComponentActivationOptions activationOptions = LocationComponentActivationOptions
                    .builder(requireActivity(), loadedMapStyle)
                    .locationComponentOptions(options)
                    .build();

            locationComponent.activateLocationComponent(activationOptions);
            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    private void initHoveringMarker() {
        hoveringMarker = new ImageView(getActivity());
        hoveringMarker.setImageResource(R.drawable.ic_location_on_red_36dp);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        hoveringMarker.setLayoutParams(params);

        mapView.addView(hoveringMarker);
    }

    private void initDroppedMarker(@NonNull Style loadedMapStyle) {
        Drawable image = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_location_on_blue_36dp);
        if (image != null) {
            loadedMapStyle.addImage("dropped-icon-image", image);
        }

        loadedMapStyle.addSource(new GeoJsonSource("dropped-marker-source-id"));
        loadedMapStyle.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id").withProperties(
                iconImage("dropped-icon-image"),
                visibility(Property.NONE),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        ));
    }

    private void initSelectButton(@NonNull Style loadedMapStyle) {
        selectLocationButton = new Button(new ContextThemeWrapper(getActivity(),
                R.style.Widget_MaterialComponents_Button));
        selectLocationButton.setText(requireActivity().getString(R.string.add_game_select_location));
        selectLocationButton.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER
        );
        params.setMargins(0, 0, 0, 16);
        selectLocationButton.setLayoutParams(params);

        selectLocationButton.setOnClickListener(view -> {
            if (hoveringMarker.getVisibility() == View.VISIBLE) {
                final LatLng selectedLatLng = mapboxMap.getCameraPosition().target;
                final Point point = Point.fromLngLat(selectedLatLng.getLongitude(),
                        selectedLatLng.getLatitude());

                // Perform reverse geocoding search
                reverseGeocode(loadedMapStyle, point);

            } else { // Button will be showing confirm
                locationPickerListener.onLocationPicked(this,
                        selectedPoint, selectedPlaceName);
            }
        });

        mapView.addView(selectLocationButton);
    }

    private void initSearchBar(Style loadedMapStyle) {
        if (autocompleteFragment == null) {
            PlaceOptions options = PlaceOptions.builder()
                    .limit(4)
                    .historyCount(0)
                    .build(PlaceOptions.MODE_CARDS);
            autocompleteFragment = PlaceAutocompleteFragment
                    .newInstance(getString(R.string.mapbox_access_token), options);
        }

        // Create button to open search window
        FloatingActionButton searchButton = new FloatingActionButton(requireActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.START);
        params.setMargins(20, 20, 20, 20);

        searchButton.setLayoutParams(params);
        searchButton.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireActivity(), R.color.white)));
        searchButton.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_search_black_24dp));

        searchButton.setOnClickListener(v -> {
            searchButton.setVisibility(View.INVISIBLE);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(CarmenFeature carmenFeature) {
                    Point center = carmenFeature.center();
                    LatLng latLng = new LatLng(center.latitude(), center.longitude());
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    selectLocation(carmenFeature, center, loadedMapStyle);

                    closeFragment();
                }

                @Override
                public void onCancel() {
                    closeFragment();
                }

                private void closeFragment() {
                    searchButton.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mapView.getWindowToken(), 0);
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            });

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.add_game_page_container, autocompleteFragment, PLACES_AUTOCOMPLETE_TAG)
                    .addToBackStack(null)
                    .commit();
        });

        mapView.addView(searchButton);
    }

    /**
     * This method is used to reverse geocode where the user has dropped the marker.
     *
     * @param style style
     * @param point The location to use for the search
     */
    private void reverseGeocode(@NonNull final Style style, final Point point) {
        try {
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.mapbox_access_token))
                    .query(point)
                    .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                    .build();

            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(@NonNull Call<GeocodingResponse> call,
                                       @NonNull Response<GeocodingResponse> response) {
                    List<CarmenFeature> results = response.body().features();
                    if (results.size() > 0) {
                        CarmenFeature feature = results.get(0);
                        if (style.isFullyLoaded() && style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                            selectLocation(feature, point, style);
                        }

                    } else {
                        selectLocationButton.setText(getActivity().getString(R.string.add_game_try_again));
                        selectLocationButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
                        selectLocationButton.setEnabled(false);

                        Toast failureToast = Toast.makeText(getActivity(),
                                "No results for selection.", Toast.LENGTH_SHORT);
                        failureToast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 200);
                        failureToast.show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable t) {
                    Log.d(LOCATION_PICKER_TAG, "Geocoding failed.", t);
                }
            });

        } catch (ServicesException e) {
            Log.d(LOCATION_PICKER_TAG, "Services exception.", e);
            e.printStackTrace();
        }
    }

    private void selectLocation(CarmenFeature feature, Point point, Style loadedMapStyle) {
        // Transform appearance of button to become confirm button.
        selectLocationButton.setText(requireActivity().getString(R.string.add_game_confirm));
        selectLocationButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));

        // Hide the red hovering marker.
        hoveringMarker.setVisibility(View.INVISIBLE);

        // Show dropped marker to represent selected location
        Layer layer = loadedMapStyle.getLayer(DROPPED_MARKER_LAYER_ID);
        if (layer != null) {
            GeoJsonSource source = loadedMapStyle.getSourceAs("dropped-marker-source-id");
            if (source != null) {
                source.setGeoJson(point);
            }
            loadedMapStyle.getLayer(DROPPED_MARKER_LAYER_ID).setProperties(visibility(Property.VISIBLE));
        }

        Toast successToast = Toast.makeText(getActivity(),
                "Place name: " + feature.placeName(), Toast.LENGTH_LONG);
        successToast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 200);
        successToast.show();

        selectedPoint = point;
        selectedPlaceName = feature.placeName();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), "User permissions are needed to pick a location.", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted && mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                enableLocationPlugin(style);
            }
        } else {
            Toast.makeText(getActivity(), "Permissions not granted.", Toast.LENGTH_LONG).show();
            closeListener.onMapCancelled(this);
        }
    }

    public interface OnMapFragmentCancelledListener {
        void onMapCancelled(LocationPickerMapFragment locationPickerMapFragment);
    }

    public interface LocationPickerListener {
        void onLocationPicked(LocationPickerMapFragment locationPickerMapFragment,
                              Point selectedPoint, String selectedPlaceName);
    }
}
