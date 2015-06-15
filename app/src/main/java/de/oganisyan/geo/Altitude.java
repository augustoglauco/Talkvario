package de.oganisyan.geo;

import java.io.*;

public class Altitude
{
	private static int[] $SWITCH_TABLE$de$oganisyan$geo$Altitude$Reference;
    public static final float m2f = 3.28084f;
    private int altitude;
    private Reference reference;
    
    static /* synthetic */ int[] $SWITCH_TABLE$de$oganisyan$geo$Altitude$Reference() {

    	int[] $switch_TABLE$de$oganisyan$geo$Altitude$Reference = Altitude.$SWITCH_TABLE$de$oganisyan$geo$Altitude$Reference;
        if ($switch_TABLE$de$oganisyan$geo$Altitude$Reference == null) {
            $switch_TABLE$de$oganisyan$geo$Altitude$Reference = new int[Reference.values().length];
            while (true) {
                try {
                    $switch_TABLE$de$oganisyan$geo$Altitude$Reference[Reference.FL.ordinal()] = 1;
                    try {
                        $switch_TABLE$de$oganisyan$geo$Altitude$Reference[Reference.GND.ordinal()] = 3;
                        try {
                            $switch_TABLE$de$oganisyan$geo$Altitude$Reference[Reference.MSL.ordinal()] = 2;
                            Altitude.$SWITCH_TABLE$de$oganisyan$geo$Altitude$Reference = $switch_TABLE$de$oganisyan$geo$Altitude$Reference;
                        }
                        catch (NoSuchFieldError ignored) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError2) {}
                }
                catch (NoSuchFieldError noSuchFieldError3) {
                    continue;
                }
                break;
            }
        }
        return $switch_TABLE$de$oganisyan$geo$Altitude$Reference;
    }
    
    public Altitude(final Reference reference, final int altitude) {
        super();
        this.reference = reference;
        this.altitude = altitude;
    }
    
    public Altitude(final String s, final int altitude) {
        super();
        if (s.compareTo(Reference.FL.name()) == 0) {
            this.reference = Reference.FL;
        }
        else if (s.compareTo(Reference.GND.name()) == 0) {
            this.reference = Reference.GND;
        }
        else {
            this.reference = Reference.MSL;
        }
        this.altitude = altitude;
    }
    
    private static int getAltitude(final String s) {
        String s2 = s.trim();
        float n = 1.0f;
        if (s2.endsWith("F")) {
            s2 = s2.substring(0, -1 + s2.length());
        }
        else if (s2.endsWith("FT")) {
            s2 = s2.substring(0, -2 + s2.length());
        }
        else if (s2.endsWith("M")) {
            s2 = s2.substring(0, -1 + s2.length());
            n = 3.28084f;
        }
        return Math.round(n * Float.valueOf(s2));
    }
    
   /* public static Altitude parser(final String s) throws AirspaceException {
        try {
            final String trim = s.trim();
            Reference reference;
            int n;
            if (trim.contains("FL")) {
                reference = Reference.FL;
                n = Integer.valueOf(trim.substring(2).trim());
            }
            else {
                int n2 = trim.indexOf("GND");
                if (n2 < 0) {
                    n2 = trim.indexOf("AGL");
                    if (n2 < 0) {
                        n2 = trim.indexOf("ASFC");
                        if (n2 < 0) {
                            n2 = trim.indexOf("SFC");
                            if (n2 < 0) {
                                int n3 = trim.indexOf("AMSL");
                                if (n3 < 0) {
                                    n3 = trim.indexOf("MSL");
                                    if (n3 < 0) {
                                        final int index = trim.indexOf("UNL");
                                        if (index < 0) {
                                            reference = Reference.MSL;
                                            n = getAltitude(trim);
                                            return new Altitude(reference, n);
                                        }
                                        reference = Reference.MSL;
                                        n = 99999;
                                        if (index != 0) {
                                            n = getAltitude(trim.substring(0, index));
                                            return new Altitude(reference, n);
                                        }
                                        return new Altitude(reference, n);
                                    }
                                }
                                reference = Reference.MSL;
                                n = 0;
                                if (n3 != 0) {
                                    n = getAltitude(trim.substring(0, n3));
                                    return new Altitude(reference, n);
                                }
                                return new Altitude(reference, n);
                            }
                        }
                    }
                }
                reference = Reference.GND;
                n = 0;
                if (n2 != 0) {
                    n = getAltitude(trim.substring(0, n2));
                }
            }
            return new Altitude(reference, n);
        }
        catch (NumberFormatException ex) {
            throw new AirspaceException("Altitude parser error for: \"" + s + "\"");
        }
    }*/
    
   /* public float comapre(final Altitude altitude, final Altitude altitude2, final Altitude altitude3) throws AirspaceException {
        final int n = $SWITCH_TABLE$de$oganisyan$geo$Altitude$Reference()[this.reference.ordinal()];
        float n2 = 0.0f;
        switch (n) {
            case 1: {
                if (altitude.reference != Reference.FL) {
                    throw new AirspaceException("Parameter 2 reference is not FL");
                }
                n2 = 100.0f * (altitude.altitude - this.altitude) / 3.28084f;
                break;
            }
            case 2: {
                if (altitude2.reference != Reference.MSL) {
                    throw new AirspaceException("Parameter 2 reference is not MSL");
                }
                n2 = (altitude2.altitude - this.altitude) / 3.28084f;
                break;
            }
            case 3: {
                if (altitude3.reference != Reference.GND) {
                    throw new AirspaceException("Parameter 2 reference is not GND");
                }
                n2 = (altitude3.altitude - this.altitude) / 3.28084f;
                break;
            }
        }
        return n2;
    }
*/
    public int getAltitude() {
        return this.altitude;
    }
    
    public Reference getReference() {
        return this.reference;
    }
    
    public String toString() {
        String s = "";
        switch ($SWITCH_TABLE$de$oganisyan$geo$Altitude$Reference()[this.reference.ordinal()]) {
            case 1: {
                s = String.valueOf(s) + this.reference + this.altitude;
                break;
            }
            case 2: {
                s = String.valueOf(s) + this.altitude + this.reference;
                break;
            }
            case 3: {
                final StringBuilder sb = new StringBuilder(String.valueOf(s));
                Serializable s2;
                if (this.altitude == 0) {
                    s2 = this.reference;
                }
                else {
                    s2 = String.valueOf(this.altitude) + this.reference;
                }
                s = sb.append(s2).toString();
                break;
            }
        }
        return s;
    }
    
    public enum Reference
    {
        FL("FL", 0), 
        GND("GND", 2), 
        MSL("MSL", 1);
        
        private String stringValue;
        private Reference(String toString, int value) {
            stringValue = toString;
        }
        @Override
        public String toString() {
            return stringValue;
        }
    }
}
