package de.oganisyan.paraglidervario.util;

public interface VarioIfc
{
    public static final float[] DEFAULT_AUDIO_SETTINGS = { 1000.0f, 0.5f, 4.0f, 400.0f, 2.0f, 4.0f };
    public static final int[] DEFAULT_VIEW_SETTINGS = { 0, 0, 0, 4, 3, 2, 1, 5, 0, 0, 0, 0, 0, 0, 0 };
    //public static final float R0 = 287.05286f;
    public static final int[] RADIXS = { 0, 1, 1, 0, 1, 1 };
    //public static final float T0 = 288.15f;
    public static final int defaultBaroDamping = 4;
    public static final int defaultBaroInterval = 250;
    public static final int defaultCalibInterval = 5000;
    public static final int defaultHeight = 0;
    public static final int defaultKalmanDumping = 5;
    public static final float defaultLiftBarrier = 1.0f;
    public static final int defaultLiftMaxDist = 500;
    public static final boolean defaultUseAcceleraton = false;
    public static final boolean defaultUseBaroQfe = true;
    public static final boolean defaultUseKalmanFilter = true;
    public static final boolean defaultUseStorageFilter = true;
    //public static final float gn = 9.80665f;
    //public static final float k = -0.0065f;
    //public static final float m2f = 3.28084f;
    public static final float mConst1 = 0.19026309f;
    public static final float mConst2 = 2.2557697E-5f;
    public static final float p0 = 1013.25f;
    public static float defaultTomRadio = 19000f;
}
