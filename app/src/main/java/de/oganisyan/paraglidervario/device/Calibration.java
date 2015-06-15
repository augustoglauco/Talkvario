package de.oganisyan.paraglidervario.device;

import java.util.*;

class Calibration
{
    private long duration;
    Float value;
    ArrayList<Float> values;
    
    public Calibration(final long n) {
        super();
        this.values = new ArrayList<Float>();
        this.duration = n + System.currentTimeMillis();
    }
    
    void add(final float n) {
        this.values.add(n);
    }
    
    float getValue() {
        if (!this.isReady()) {
            throw new RuntimeException("Acceleration is not ready");
        }
        if (this.value == null) {
            float n = 0.0f;
            for (Float value1 : this.values) {
                n += value1;
            }
            this.value = n / this.values.size();
        }
        return this.value;
    }
    
    boolean isReady() {
        final boolean b = this.duration < System.currentTimeMillis() && this.values.size() > 0;
        if (!b) {
            this.value = null;
        }
        return b;
    }
}
