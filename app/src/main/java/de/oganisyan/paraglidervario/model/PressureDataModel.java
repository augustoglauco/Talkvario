package de.oganisyan.paraglidervario.model;

public class PressureDataModel
{
    float a1;
    float a2;
    float airfieldHeight;
    float fl;
    float qfe;
    float qnh;
    float verticalSpeed;
    
    public PressureDataModel() {
        super();
        this.verticalSpeed = 0.0f;
        this.fl = 0.0f;
        this.a1 = 0.0f;
        this.a2 = 0.0f;
        this.qnh = 1013.25f;
        this.qfe = 1013.25f;
        this.airfieldHeight = 0.0f;
    }
    
    public PressureDataModel(final float verticalSpeed, final float fl, final float a1, final float a2, final float qnh, final float qfe, final float airfieldHeight) {
        super();
        this.verticalSpeed = 0.0f;
        this.fl = 0.0f;
        this.a1 = 0.0f;
        this.a2 = 0.0f;
        this.qnh = 1013.25f;
        this.qfe = 1013.25f;
        this.airfieldHeight = 0.0f;
        this.verticalSpeed = verticalSpeed;
        this.fl = fl;
        this.a1 = a1;
        this.a2 = a2;
        this.qnh = qnh;
        this.qfe = qfe;
        this.airfieldHeight = airfieldHeight;
    }
    
    public float getA1() {
        return this.a1;
    }
    
    public float getA2() {
        return this.a2;
    }
    
    public float getAitfeldHeight() {
        return this.airfieldHeight;
    }
    
    public float getFl() {
        return this.fl;
    }
    
    public float getQfe() {
        return this.qfe;
    }
    
    public float getQnh() {
        return this.qnh;
    }
    
    public float getSpeed() {
        return this.verticalSpeed;
    }
    
    public void setAitfeldHeight(final float airfieldHeight) {
        this.airfieldHeight = airfieldHeight;
    }
    
    public void setQfe(final float qfe) {
        this.qfe = qfe;
    }
}
