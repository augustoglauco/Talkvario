package de.oganisyan.paraglidervario.device;

import android.content.*;
import android.hardware.*;

public abstract class PressureDevice implements SensorEventListener
{
    Context context;
    SensorManager sensorManager;
    
    public PressureDevice(final Context context) {
        super();
        this.context = context;
    }
    
    public void onAccuracyChanged(final Sensor sensor, final int n) {
    }
    
    public void start() {
        (this.sensorManager = (SensorManager)this.context.getSystemService(Context.SENSOR_SERVICE)).registerListener(this, this.sensorManager.getDefaultSensor(6), 0);
        //(this.sensorManager = (SensorManager)this.context.getSystemService("sensor")).registerListener((SensorEventListener)this, this.sensorManager.getDefaultSensor(6), 0);
    }
    
    public void stop() {
        if (this.sensorManager != null) {
            this.sensorManager.unregisterListener(this);
        }
        this.sensorManager = null;
    }
}
