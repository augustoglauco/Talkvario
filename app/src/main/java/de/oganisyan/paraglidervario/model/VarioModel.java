package de.oganisyan.paraglidervario.model;

import android.location.*;
import de.oganisyan.paraglidervario.widgets.*;
import de.oganisyan.paraglidervario.util.*;

public class VarioModel
{
    ServiceDataModel data;
    public LabelModel[] labelModels;
    //MapModel mapModel;
    float orentation;
    ImageButton satView;
    private float thermalLiftBarrier;
    float thermalLiftDirection;
    float thermalLiftDistance;
    private Location thermalLiftLocation;
    private int thermalLiftMaxDist;
    private float thermalLiftVerticalSpeed;
    TrackingStatus trackingStatus;
    VarioView view;
    float windDirection;
    private static int[] SWITCH_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus;
    
    
    static int[] SWITCH_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus() {
        int[] switch_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus = VarioModel.SWITCH_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus;
        if (switch_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus == null) {
        	switch_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus = new int[TrackingStatus.values().length];
            while (true) {
                try {
                	switch_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus[TrackingStatus.TR_DOWN.ordinal()] = 3;
                    try {
                    	switch_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus[TrackingStatus.TR_OFF.ordinal()] = 1;
                        try {
                        	switch_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus[TrackingStatus.TR_ON.ordinal()] = 2;
                            VarioModel.SWITCH_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus = switch_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus;
                        }
                        catch (NoSuchFieldError noSuchFieldError) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError2) {}
                }
                catch (NoSuchFieldError noSuchFieldError3) {
                    continue;
                }
                break;
            }
        }
        return switch_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus;
    }
    
    public VarioModel(final VarioView view) {
        super();
        this.satView = null;
        //this.mapModel = null;
        this.trackingStatus = TrackingStatus.TR_OFF;
        this.orentation = 0.0f;
        this.windDirection = Float.NaN;
        this.thermalLiftDirection = Float.NaN;
        this.thermalLiftDistance = Float.NaN;
        this.data = new ServiceDataModel();
        this.labelModels = new LabelModel[] { null, new LabelModel("Sp. m/s", 10) {
                float getValue() {
                    return VarioModel.this.data.getSpeed();
                }
            }, new LabelModel("FL (m)", 1) {
                float getValue() {
                    return VarioModel.this.data.getFl();
                }
            }, new LabelModel("A1 (m)", 1) {
                float getValue() {
                    return VarioModel.this.data.getA1();
                }
            }, new LabelModel("A2 (m)", 1) {
                float getValue() {
                    return VarioModel.this.data.getA2();
                }
            }, new LabelModel("lift dist (m)", 1) {
                float getValue() {
                    return VarioModel.this.thermalLiftDistance;
                }
            }, new LabelModel("Sp. ft/s", 10) {
                float getValue() {
                    return 3.28084f * VarioModel.this.data.getSpeed();
                }
            }, new LabelModel("FL (ft)", 1) {
                float getValue() {
                    return 3.28084f * VarioModel.this.data.getFl();
                }
            }, new LabelModel("A1 (ft)", 1) {
                float getValue() {
                    return 3.28084f * VarioModel.this.data.getA1();
                }
            }, new LabelModel("A2 (ft)", 1) {
                float getValue() {
                    return 3.28084f * VarioModel.this.data.getA2();
                }
            }, new LabelModel("lift dist (ft)", 1) {
                float getValue() {
                    return 3.28084f * VarioModel.this.thermalLiftDistance;
                }
            }, new LabelModel("QNH (hPa)", 1) {
                float getValue() {
                    return VarioModel.this.data.getQnh();
                }
            }, new LabelModel("QFE (hPa)", 1) {
                float getValue() {
                    return VarioModel.this.data.getQfe();
                }
            }, new LabelModel("Aitfeld (m)", 1) {
                float getValue() {
                    return VarioModel.this.data.getAitfeldHeight();
                }
            }, new LabelModel("Aitfeld (ft)", 1) {
                float getValue() {
                    return 3.28084f * VarioModel.this.data.getAitfeldHeight();
                }
            } };
        this.thermalLiftVerticalSpeed = 0.0f;
        this.thermalLiftBarrier = 1.0f;
        this.thermalLiftMaxDist = 500;
        this.view = view;
        this.readSharedPreferences();
    }
    
    private void resetLiftData(final Location thermalLiftLocation) {
        this.thermalLiftVerticalSpeed = this.data.getSpeed();
        this.thermalLiftLocation = thermalLiftLocation;
        this.thermalLiftVerticalSpeed = 0.0f;
        this.thermalLiftDirection = Float.NaN;
        this.thermalLiftDistance = Float.NaN;
    }
    
    private void updateView() {
        if (this.view != null) {
            this.view.fireDataChanged();
        }
    }
    
    public void cancelSettings() {
        if (this.view != null) {
            this.view.cancelSettings();
        }
    }
    
    public void destroy() {
        if (this.view != null) {
            this.view.destroy();
        }
        if (this.satView != null) {
            this.satView.destroy();
        }
    }
    
    public float getOrentation() {
        return this.orentation;
    }
    
    public float getThermalLiftDirection() {
        return this.thermalLiftDirection;
    }
    
/*    public TrackingStatus getTrackingStatus() {
        return this.trackingStatus;
    }*/
    
    public float getVerticalSpeed() {
        return this.data.getSpeed();
    }
    
    public float getWindDirection() {
        return this.windDirection;
    }
    
/*    public boolean isSettingMode() {
        return this.view.isSettingMode();
    }*/
    
    public void readSharedPreferences() {
        if (this.view != null && this.view.getContext() != null && VarioUtil.getSharedPreferences(this.view.getContext()) != null) {
            this.thermalLiftBarrier = VarioUtil.getFloat(VarioUtil.getSharedPreferences(this.view.getContext()).getString("liftBarrierEdit", "xxx"), 1.0f);
            this.thermalLiftMaxDist = VarioUtil.getInt(VarioUtil.getSharedPreferences(this.view.getContext()).getString("liftMaxDistEdit", "xxx"), 500);
        }
    }
    
    public void resetSettings() {
        if (this.view != null) {
            this.view.resetSettings();
        }
    }
    
    public void saveSettings() {
        if (this.view != null) {
            this.view.saveSettings();
        }
    }
    
    public void setData(final ServiceDataModel data) {
        this.data = data;
        if (data.getLocation() != null) {
/*            if (this.mapModel != null) {
                this.mapModel.setLocation(data.getLocation());
            }*/
            if (this.trackingStatus == TrackingStatus.TR_ON) {
                if (this.thermalLiftLocation == null || Math.max(this.thermalLiftVerticalSpeed, this.thermalLiftBarrier) <= data.getSpeed()) {
                    this.resetLiftData(data.getLocation());
                }
                else {
                    this.thermalLiftDistance = data.getLocation().distanceTo(this.thermalLiftLocation);
                    if (this.thermalLiftDistance > (float)this.thermalLiftMaxDist) {
                        this.resetLiftData(data.getLocation());
                    }
                    else if (this.thermalLiftDistance < 4.0f) {
                        this.thermalLiftDirection = Float.NaN;
                    }
                    else {
                        this.thermalLiftDirection = data.getLocation().bearingTo(this.thermalLiftLocation);
                    }
                }
            }
        }
        else {
            this.thermalLiftDirection = Float.NaN;
            this.thermalLiftDistance = Float.NaN;
        }
        this.updateView();
    }
    
   /* public void setMapModel(final MapModel mapModel) {
        this.mapModel = mapModel;
    }*/
    
    public void setOrentation(final float orentation) {
        this.orentation = orentation;
        this.updateView();
    }
    
    public void setSatView(final ImageButton satView) {
        this.satView = satView;
        if (this.satView != null) {
            this.satView.setEnabled(false);
        }
    }
    
    public void setSettingMode(final boolean settingMode) {
        this.view.setSettingMode(settingMode);
    }
    
    public void setTrackingStatus(final TrackingStatus trackingStatus) {
        this.trackingStatus = trackingStatus;
        if (this.satView != null) {
            switch (SWITCH_TABLE_de_oganisyan_paraglidervario_model_VarioModel_TrackingStatus()[trackingStatus.ordinal()]) {
                case 1: {
                    this.satView.setChecked(false);
                    break;
                }
                case 2: {
                    this.satView.setChecked(true);
                    break;
                }
                case 3: {
                    this.satView.setShutdown(true);
                    break;
                }
            }
            this.satView.invalidate();
        }
        this.updateView();
    }
    
    public abstract static class LabelModel
    {
        private int accuracy;
        private String title;
        
        public LabelModel(final String title, final int accuracy) {
            super();
            this.title = title;
            this.accuracy = accuracy;
        }
        
        public String getTitle() {
            return this.title;
        }
        
        abstract float getValue();
        
        public String getValueString() {
            if (this.accuracy <= 1) {
                this.accuracy = 1;
            }
            final float n = Math.round(this.getValue() * this.accuracy) / this.accuracy;
            String s;
            if (this.accuracy == 1) {
                s = Integer.toString((int)n);
            }
            else {
                s = Float.toString(n);
            }
            return s;
        }
    }
    
    public enum TrackingStatus
    {
        TR_DOWN("TR_DOWN"),
        TR_OFF("TR_OFF"),
        TR_ON("TR_ON");
        
        private String stringValue;
        
        private TrackingStatus(String toString) {
            stringValue = toString;
        }
        @Override
        public String toString() {
            return stringValue;
        } 
        
    }

}
