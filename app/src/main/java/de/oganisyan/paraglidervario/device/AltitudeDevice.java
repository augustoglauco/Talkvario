package de.oganisyan.paraglidervario.device;

import android.content.Context;
import android.hardware.SensorEvent;

import java.util.ArrayList;

import de.oganisyan.paraglidervario.model.PressureDataModel;
import de.oganisyan.paraglidervario.util.KalmanFilter;
import de.oganisyan.paraglidervario.util.Pair;
import de.oganisyan.paraglidervario.util.VarioIfc;
import de.oganisyan.paraglidervario.util.VarioUtil;

public class AltitudeDevice extends PressureDevice implements VarioIfc
{
    private int damping;
    private PressureDataModel data;
    private KalmanFilter filter;
    private int intervall;
    private Store store;
    private long time;
    private boolean useStorageFilter;
    
    public AltitudeDevice(final Context context) {
        super(context);
        this.store = new Store();
        this.time = 0L;
        this.data = new PressureDataModel();
        this.useStorageFilter = true;
        this.intervall = 1000;
        this.damping = 3;
        this.filter = null;
    }
    
    public PressureDataModel getData() {
        return this.data;
    }
    
    public void onSensorChanged(final SensorEvent sensorEvent) {
        float valorSensorBaro = sensorEvent.values[0];
        if (this.filter != null) {
            valorSensorBaro = this.filter.update(valorSensorBaro);
        }
        this.upadteData(System.currentTimeMillis(), valorSensorBaro);
    }
    
    public void setHeight(final float aitfeldHeight, final float qfe) {
        this.data.setAitfeldHeight(aitfeldHeight);
        this.data.setQfe(qfe);
    }
    
    public void setIntervall(final int intervall) {
        this.intervall = intervall;
    }
    
    public void setKalmanFilter(final boolean b, final int n) {
        KalmanFilter filter;
        if (b) {
            filter = new KalmanFilter(0.01f, n);
        }
        else {
            filter = null;
        }
        this.filter = filter;
    }
    
    public void setStorageFilter(final boolean useStorageFilter, final int damping) {
        this.damping = damping;
        this.useStorageFilter = useStorageFilter;
    }
    
    public void upadteData(final long time, final float valorSensorEntrada) {
        if (this.useStorageFilter) {
            this.store.add(time, valorSensorEntrada);
        }
        if (time - this.time > this.intervall) {
            float vSensor2 = valorSensorEntrada;
            if (this.useStorageFilter) {
                float vSensorComFiltroKalman = 0.0f;
                for (Pair aStore : this.store) {
                    vSensorComFiltroKalman += (Float) (aStore.getSecond());
                }
                vSensor2 = vSensorComFiltroKalman / this.store.size();
            }
            final float aitfeldHeight = this.data.getAitfeldHeight();
            float qfe = this.data.getQfe();
            if (Float.isNaN(qfe)) {
                qfe = vSensor2;
            }
            final float calcHeight = VarioUtil.calcHeight(vSensor2, p0);
            float vericalSpeed;
            if (this.data == null) {
                vericalSpeed = 0.0f;
            }
            else {
                vericalSpeed = 1000.0f * (calcHeight - this.data.getFl()) / (time - this.time);
            }
            final float calcQNH = VarioUtil.calcQNH(qfe, aitfeldHeight);
            final float calcHeight2 = VarioUtil.calcHeight(vSensor2, calcQNH);
            //PressureDataModel(float verticalSpeed, final float fl, final float a1, final float a2, final float qnh, final float qfe, final float airfieldHeight)
            //verticalSpeed = vericalSpeed;
            //fl = calcHeight;  a altitude padrao acima dos 3000 ft (QNE)
            //a1 = calcHeight2; altitude em relacao ao QNH (nivel medio mar)
            //a2 = calcHeight2 - aitfeldHeight; altitude acima da decolagem ou aerodromo
            //qnh = calcQNH;
            //qfe = qfe; (ajuste a zero)
            //airfieldHeight = aitfeldHeight;
            
            
            this.data = new PressureDataModel(vericalSpeed, calcHeight, calcHeight2, calcHeight2 - aitfeldHeight, calcQNH, qfe, aitfeldHeight);
            this.time = time;
            if (this.useStorageFilter) {
                this.store.rmUntil(time - this.damping * this.intervall);
            }
        }
    }
    
    class Store extends ArrayList<Pair>
    {
        private static final long serialVersionUID = 1431438550034819779L;
        
        public void add(final long n, final float n2) {
            this.add(new Pair(n, n2));
        }
        
        public void rmUntil(final long n) {
            int n2 = 0;
            for (int n3 = 0; n3 < this.size() && (Long) (this.get(n3)).getFirst() < n; ++n3) {
                n2 = n3;                
            }
            this.removeRange(0, n2);
        }
        
/*        public void toHalve() {
            this.removeRange(0, this.size() / 2);
        }*/
    }
}
