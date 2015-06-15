package de.oganisyan.geo;

import android.location.*;

public class Point extends Location
{
    public Point() {
        super("gps");
    }
    
    public Point(final double latitude, final double longitude) {
        super("gps");
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }
    
    public Point(final Point point) {
        super(point);
    }
}
