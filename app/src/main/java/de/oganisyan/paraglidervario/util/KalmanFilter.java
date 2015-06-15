package de.oganisyan.paraglidervario.util;

public class KalmanFilter
{
    private float k;
    private float p;
    private float q;
    private float r;
    private float x;
    
/*    public KalmanFilter() {
        this(1.0E-5f, 0.01f);
    }*/
    
    public KalmanFilter(final float q, final float r) {
        super();
        this.q = q;
        this.r = r;
        this.p = 1.0f;
        this.x = Float.NaN;
    }
    
    private void measurementUpdate() {
        this.k = 1.0f - this.r / (this.p + this.q + this.r);
        this.p = this.r * this.k;
    }
    
    public float update(final float n) {
        float n2 = n;
        if (Double.isNaN(this.x)) {
            this.x = n2;
        }
        else {
            this.measurementUpdate();
            n2 = this.x + (n - this.x) * this.k;
            this.x = n2;
        }
        return n2;
    }
}
