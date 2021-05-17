/*
 * GeofenceBroadcastReciever
 * Author: Andrew Iliescu
 * Date: 4/27/21
 * This file creates a custom broadcast receiver action for when the user is within the designated
 * geographical location
 */
package edu.msoe.andrewiliescu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    /**
     * This method needs to be implemented when extending BroadcastReceiver
     * because it handles when a message is sent
     *
     * @param context application context
     * @param intent  intent information sent from MainActivity
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Toast.makeText(context, "Secret Found", Toast.LENGTH_LONG).show();
        }
    }
}