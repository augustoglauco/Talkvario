package de.oganisyan.paraglidervario.device;

import android.content.*;
import android.location.*;
import android.widget.*;
import android.util.*;

public class LocationService
{
    Context context;
    LocationManager lm;
    
    public LocationService(final Context context) {
        super();
        this.context = context;
    }
    
    public Location getLastKnownGPSLocation() {
        if (this.lm == null) {
            //this.lm = (LocationManager)this.context.getSystemService("location");
            this.lm = (LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);
        }
        Location lastKnownLocation = new Location("gps");
        try {
            if (this.lm == null) {
                lastKnownLocation = null;
            }
            else {
                lastKnownLocation = this.lm.getLastKnownLocation("gps");
            }
        }
        catch (Exception ex) {
            Toast.makeText(this.context.getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(LocationService.class.getName(), ex.getMessage());
        }
        return lastKnownLocation;
    }
}
