package de.oganisyan.geo;

public class Util
{
    static double calcDDMMSS(final String s) {
        final int intValue = Integer.valueOf(s);
        final int n = intValue / 10000;
        final int n2 = intValue - n * 10000;
        final int n3 = n2 / 100;
        return n + n3 / 60.0 + (n2 - n3 * 100) / 3600.0;
    }
    
    static double calcFromGWS84(final String s) {
        final double floor = Math.floor(Double.valueOf(s) / 10000.0);
        return floor + 100.0 * (Double.valueOf(s) / 10000.0 - floor) / 60.0;
    }
    
    public static Point getPoint(final String s, final String s2) {
        double n;
        if (s.startsWith("N")) {
            n = 1.0;
        }
        else {
            n = -1.0;
        }
        double n2;
        if (s2.startsWith("W")) {
            n2 = -1.0;
        }
        else {
            n2 = 1.0;
        }
        return new Point(n * calcDDMMSS(s.substring(1)), n2 * calcDDMMSS(s2.substring(1)));
    }
}
