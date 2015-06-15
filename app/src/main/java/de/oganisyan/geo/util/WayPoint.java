package de.oganisyan.geo.util;

import android.location.*;

public class WayPoint
{
    private Location center;
    private int radius;
    
    public WayPoint(final Location center, final int radius) {
        super();
        this.center = center;
        this.radius = radius;
    }
    
    public Location getCenter() {
        return this.center;
    }
    
    public int getRadius() {
        return this.radius;
    }
}
