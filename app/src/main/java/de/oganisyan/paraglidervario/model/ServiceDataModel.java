package de.oganisyan.paraglidervario.model;

import android.location.*;
import android.os.*;

public class ServiceDataModel extends PressureDataModel implements Parcelable
{
    public static final Parcelable.Creator<ServiceDataModel> CREATOR;
    Location location;
    
    static {
        CREATOR = new Parcelable.Creator<ServiceDataModel>() {
            public ServiceDataModel createFromParcel(final Parcel parcel) {
                final ServiceDataModel serviceDataModel = new ServiceDataModel();
                serviceDataModel.readFromParcel(parcel);
                return serviceDataModel;
            }
            
            public ServiceDataModel[] newArray(final int n) {
                return new ServiceDataModel[n];
            }
        };
    }
    
    public ServiceDataModel() {
        super();
        this.location = null;
    }
    
    public ServiceDataModel(Location location, final PressureDataModel pressureDataModel) {
        super(pressureDataModel.verticalSpeed, pressureDataModel.fl, pressureDataModel.a1, pressureDataModel.a2, pressureDataModel.qnh, pressureDataModel.qfe, pressureDataModel.airfieldHeight);
        this.location = null;
        if (location == null) {
            location = new Location("gps");
        }
        this.location = location;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.verticalSpeed = parcel.readFloat();
        this.fl = parcel.readFloat();
        this.a1 = parcel.readFloat();
        this.a2 = parcel.readFloat();
        this.qnh = parcel.readFloat();
        this.qfe = parcel.readFloat();
        this.airfieldHeight = parcel.readFloat();
        this.location = Location.CREATOR.createFromParcel(parcel);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeFloat(this.verticalSpeed);
        parcel.writeFloat(this.fl);
        parcel.writeFloat(this.a1);
        parcel.writeFloat(this.a2);
        parcel.writeFloat(this.qnh);
        parcel.writeFloat(this.qfe);
        parcel.writeFloat(this.airfieldHeight);
        this.location.writeToParcel(parcel, n);
    }
}
