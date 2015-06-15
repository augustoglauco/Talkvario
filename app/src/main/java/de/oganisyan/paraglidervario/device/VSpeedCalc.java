package de.oganisyan.paraglidervario.device;

class VSpeedCalc
{
/*    static final int CALC = 0;
    static final int GET = 1;
    static final int SET = 2;*/
    private float lastAcc;
    private long lastTime;
    private float vertikalSpeed;
    
    VSpeedCalc() {
        super();
        this.lastAcc = 0.0f;
        this.lastTime = System.currentTimeMillis();
        this.vertikalSpeed = 0.0f;
    }
    
    private float doIt(final int n, final long lastTime, final float n2) {
        // monitorenter(this)
        switch (n) {
            default: {
                //try {
                    throw new RuntimeException("cmd nr:" + n + "not supported!");
                //}
                //finally {}
                // monitorexit(this)
            }
            case 2: {
                this.vertikalSpeed = n2;
            }
            case 0: {
                if (lastTime != this.lastTime) {
                    this.vertikalSpeed += this.lastAcc * (lastTime - this.lastTime) / 1000.0f;
                }
                this.lastTime = lastTime;
                this.lastAcc = n2;
            }
            case 1: {
                // monitorexit(this)
                return this.vertikalSpeed;
            }
        }
    }
    
    void calcVSpeed(final long n, final float n2) {
        this.doIt(0, n, n2);
    }
    
/*    float getVSpeed() {
        return this.doIt(1, 0L, 0.0f);
    }*/
    
    void setVSpeed(final float n) {
        this.doIt(2, 0L, n);
    }
}
