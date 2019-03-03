package com.bytebucket1111.progressmeter.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bytebucket1111.progressmeter.R;
import com.bytebucket1111.progressmeter.helper.Config;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker newMarker;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private String TAG = LocationPickerActivity.class.getSimpleName();
    private float DEFAULT_ZOOM = 15;
    private LatLng MY_LOCATION;
    private LatLng mDefaultLocation;
    private boolean savePlacesFetched = false;
    private String selectedPlace = "";
    private boolean mLocationPermissionGranted = false;
    private int ACCESS_LOCATION_PERMISSIONS_REQUEST = 123;
    private GoogleApiClient googleApiClient;
    private int REQUEST_CHECK_SETTINGS = 111;
    private boolean isMapReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getRequiredPermissions();
        } else {
            mLocationPermissionGranted = true;
        }

        mDefaultLocation = new LatLng(-34, 151);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), Config.API_KEY);
        }

        handleAutoCompletePlaces();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (mLocationPermissionGranted) {
            enableGps();
        }


    }

    private void enableGps() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);


        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                Log.d(TAG, "GpS Success");
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "GpS Failure");
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(LocationPickerActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getRequiredPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                }
            }
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    ACCESS_LOCATION_PERMISSIONS_REQUEST);
        } else {
            Log.d(TAG, "Permissions granted called");
            mLocationPermissionGranted = true;
            if (mMap != null) {
                updateLocationUI();
                getDeviceLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == ACCESS_LOCATION_PERMISSIONS_REQUEST) {
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                    ) {

                //  Toast.makeText(this, " Access location permissions granted", Toast.LENGTH_SHORT).show();
                mLocationPermissionGranted = true;
                if (mMap != null) {
                    updateLocationUI();
                    getDeviceLocation();
                } else {
                    Log.e(TAG, "in permissions map is null");
                }
            } else {
                Toast.makeText(getApplicationContext(), " permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Log.d(TAG, "onMapReady Called");
        isMapReady = true;
        updateLocationUI();
        getDeviceLocation();

    }

    private void handleAutoCompletePlaces() {
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                LatLng latLng = place.getLatLng();
                selectedPlace = place.getName();
                if (latLng != null) {
                    if (newMarker != null) {
                        newMarker.setTitle("Set");
                        newMarker.setPosition(latLng);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(latLng.latitude,
                                        latLng.longitude), DEFAULT_ZOOM));
                    } else {
                        getDeviceLocation();
                        Toast.makeText(getApplicationContext(), "Some error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "latlng is null");
                    Toast.makeText(getApplicationContext(), "Please select a proper location", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void getDeviceLocation() {
        try {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location) task.getResult();
                        if (mLastKnownLocation != null) {
                            MY_LOCATION = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            newMarker = mMap.addMarker(new MarkerOptions()
                                    .position(MY_LOCATION)
                                    .title("Set")); //yaha

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }

                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mLastKnownLocation = null;

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    selectedPlace = "";
                    newMarker.setTitle("Set");
                    newMarker.setPosition(latLng);

                }
            });
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    newMarker.setTitle("Set");
                    setCurrentLocation();
                    return false;
                }
            });

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    if (marker.equals(newMarker)) {
                        if (newMarker.getTitle().charAt(0) == 'S') {

                            String name = selectedPlace;
                            Intent intent = new Intent();
                            com.bytebucket1111.progressmeter.modal.Place place = new com.bytebucket1111.progressmeter.modal.Place(name,
                                    marker.getPosition().latitude, marker.getPosition().longitude);
                            intent.putExtra("place", place);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void setCurrentLocation() {

        try {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location) task.getResult();
                        newMarker.setPosition(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Location turned on");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (mMap != null) {
                            updateLocationUI();
                            getDeviceLocation();
                        }
                    }
                };
                if (isMapReady) {
                    Toast.makeText(this, "Give us a moment", Toast.LENGTH_SHORT).show();
                    new android.os.Handler().postDelayed(runnable, 3000);
                } else {
                    enableGps();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Cancelled");
                enableGps();
                Toast.makeText(this, "Please turn on the location to proceed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
