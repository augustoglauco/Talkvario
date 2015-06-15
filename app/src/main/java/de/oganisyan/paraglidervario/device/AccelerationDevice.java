package de.oganisyan.paraglidervario.device;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import de.oganisyan.paraglidervario.util.VarioIfc;

public class AccelerationDevice implements SensorEventListener, VarioIfc
{
    private Sensor accelerationSensor;
    private Calibration calibration;
    private Context context;
    private float[] rotationMatrix;
    private Sensor rotationSensor;
    private boolean usedAccelerationSensor;
    private VSpeedCalc vSpeedCalc;
    
    public AccelerationDevice(final Context context) {
        super();
        this.rotationMatrix = new float[9];
        this.vSpeedCalc = new VSpeedCalc();
        this.context = context;
        this.accelerationSensor = this.getSensorManager().getDefaultSensor(10);
        this.rotationSensor = this.getSensorManager().getDefaultSensor(11);
        this.getSensorManager().registerListener(this, this.accelerationSensor, 0);
        this.getSensorManager().registerListener(this, this.rotationSensor, 3);
    }
    
    private SensorManager getSensorManager() {
        //TODO: Alterado sugestão
        //return (SensorManager)this.context.getSystemService("sensor");
        return (SensorManager)this.context.getSystemService(Context.SENSOR_SERVICE);
    }
    
    private float[] transorm(final float[] array, final float[] array2) {
        if (array.length != array2.length * array2.length) {
            throw new RuntimeException("Ilegal Dimension");
        }
        final float[] array3 = new float[array2.length];
        int i = 0;
        int n = 0;
        while (i < array2.length) {
            array3[i] = 0.0f;
            for (int j = 0; j < array2.length; ++j, ++n) {
                array3[i] += array[n] * array2[j];
            }
            ++i;
        }
        return array3;
    }
    
/*    public float getVSpeed() {
        return this.vSpeedCalc.getVSpeed();
    }*/
    
/*    public boolean isUsedAccelerationSensor() {
        return this.usedAccelerationSensor;
    }*/
    
    public void onAccuracyChanged(final Sensor sensor, final int n) {
    }
    
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent.accuracy == 0) {
            Log.i(this.getClass().getName(), "accuracy is SENSOR_STATUS_UNRELIABLE, ignore this event!");
        }
        else if (sensorEvent.sensor.getType() == 11) {
            SensorManager.getRotationMatrixFromVector(this.rotationMatrix, sensorEvent.values);
        }
        else if (sensorEvent.sensor.getType() == 10 && this.usedAccelerationSensor) {
            final float[] transorm = this.transorm(this.rotationMatrix, sensorEvent.values);
            if (this.calibration.isReady()) {
                this.vSpeedCalc.calcVSpeed(System.currentTimeMillis(), -(transorm[2] - this.calibration.getValue()));
            }
            else {
                this.calibration.add(transorm[2]);
            }
        }
    }
    
    public void setCalibration(final long n) {
        this.calibration = new Calibration(n);
    }
    
    public void setUsedAccelerationSensor(final boolean usedAccelerationSensor) {
        this.usedAccelerationSensor = usedAccelerationSensor;
    }
    
    public void setVSpeed(final float vSpeed) {
        this.vSpeedCalc.setVSpeed(vSpeed);
    }
    
    public void unregisterListeners() {
        this.getSensorManager().unregisterListener(this, this.accelerationSensor);
        this.getSensorManager().unregisterListener(this, this.rotationSensor);
    }
}
