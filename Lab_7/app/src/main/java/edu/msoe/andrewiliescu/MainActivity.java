/*
 * MainActivity
 * Author: Andrew Iliescu
 * Date: 4/27/21
 * This file is the main Activity that greets the user upon launching the app
 * it has a simple UI that displays the users current latitude and longitude
 * and has options for displaying the accuracy and altitude as well
 */
package edu.msoe.andrewiliescu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private CoordAdapter adapter;
    private final ArrayList<CoordClass> coordList = new ArrayList<>();
    private CoordClass coords;
    private String accuracyVal;
    private String altitudeVal;
    private LocationCallback locationCallback;
    private boolean locUpdates = true;
    private PendingIntent gfPendingIntent;
    private static final int PERMISSIONS_ALL = 1;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET, Manifest.permission.ACCESS_BACKGROUND_LOCATION};

    /**
     * This method is run when the app is first launched and sets everything up
     *
     * @param savedInstanceState the current saved state of the app
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView listView = findViewById(R.id.listview);
        FusedLocationProviderClient flpc = LocationServices.getFusedLocationProviderClient(this);

        //Check to ensure necessary permissions provided
        if (!hasPermissions()) {
            Toast.makeText(this, "Please allow permissions", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_ALL);
        }

        adapter = new CoordAdapter(this, coordList);
        listView.setAdapter(adapter);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        setupGeofence();
        startLocationUpdates();
        //Permissions not missing, just checking for multiple at once above
        flpc.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    /**
     * This helper method triggers the LocationCallback feature that is used to get the current location
     * and update the ListView with the up to date coordinates
     */
    private void startLocationUpdates() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locUpdates) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        coords = new CoordClass("Lat: " + location.getLatitude(),
                                "Long: " + location.getLongitude());
                        accuracyVal = "Accuracy: " + location.getAccuracy();
                        altitudeVal = "Altitude: " + location.getAltitude();
                    } else {
                        coords = new CoordClass("Lat: Unavailable", "Long: Unavailable");
                    }
                    coordList.add(coords);
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    /**
     * This is a helper method to set up the geofence functionality
     */
    @SuppressLint("MissingPermission")
    private void setupGeofence() {
        GeofencingClient gfc = LocationServices.getGeofencingClient(this);
        Geofence geofence = new Geofence.Builder().setRequestId("mGeoFence")
                .setCircularRegion(37.3866, -122.0208, 40).setExpirationDuration(500)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build();
        GeofencingRequest gfRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER).addGeofence(geofence).build();
        if (gfPendingIntent == null) {
            Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
            gfPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        gfc.addGeofences(gfRequest, gfPendingIntent);
    }

    /**
     * This method is called to create the overflow menu for the app
     *
     * @param menu reference to overflow menu
     * @return true for completion
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * This method handles what happens when a MenuItem is pressed
     *
     * @param item the MenuItem pressed
     * @return true for completion
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_getAlt) {
            Toast.makeText(getApplicationContext(), altitudeVal, Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.item_getAcc) {
            Toast.makeText(getApplicationContext(), accuracyVal, Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.item_toggleLocUpdates) {
            locUpdates = !locUpdates;
            startLocationUpdates();
        }
        return true;
    }

    /**
     * Helper method to check whether necessary permissions are provided
     *
     * @return boolean expression based on whether permissions are provided
     */
    private boolean hasPermissions() {
        if (getApplicationContext() != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}