package de.oganisyan.paraglidervario.util;

import android.location.*;
import android.os.*;

public abstract class LocationAdaptor implements LocationListener
{
    public void onProviderDisabled(final String s) {
    }
    
    public void onProviderEnabled(final String s) {
    }
    
    public void onStatusChanged(final String s, final int n, final Bundle bundle) {
    }
}
