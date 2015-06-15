package de.oganisyan.paraglidervario.device;

import android.content.*;
import android.hardware.*;

public abstract class OrentationDevice implements SensorEventListener
{
    Context context;
    private float[] mRotationMatrix;
    Sensor sensor;
    SensorManager sensorManager;
    float[] values;
    
    public OrentationDevice(final Context context) {
        super();
        this.mRotationMatrix = new float[] { 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f };
        this.values = new float[3];
        this.context = context;
        //this.sensorManager = (SensorManager)context.getSystemService("sensor");
        this.sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        this.sensor = this.sensorManager.getDefaultSensor(11);
        this.sensorManager.registerListener(this, this.sensor, 10000);
    }
    
    public void destroy() {
        if (this.sensorManager != null) {
            this.sensorManager.unregisterListener(this);
        }
    }
    
    public void onAccuracyChanged(final Sensor sensor, final int n) {
    }
    
    public abstract void onOrentationChanged(final float p0);
    
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 11) {
            SensorManager.getRotationMatrixFromVector(this.mRotationMatrix, sensorEvent.values);
            SensorManager.getOrientation(this.mRotationMatrix, this.values);
            this.onOrentationChanged((float)(180.0 * (-this.values[0] / 3.141592653589793)));
        }
    }
}
