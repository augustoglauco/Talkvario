package de.oganisyan.paraglidervario;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import de.oganisyan.paraglidervario.model.ServiceDataModel;

public interface IServiceDataListener extends IInterface
{
    void onDataChanged(ServiceDataModel p0) throws RemoteException;
    
    abstract class Stub extends Binder implements IServiceDataListener
    {
        private static final String DESCRIPTOR = "de.oganisyan.paraglidervario.IServiceDataListener";
        static final int TRANSACTION_onDataChanged = 1;
        
        public Stub() {
            super();
            this.attachInterface(this, DESCRIPTOR);
        }
        
        public static IServiceDataListener asInterface(IBinder binder) {
            IServiceDataListener serviceDataListener;
            IInterface queryLocalInterface;
            if (binder == null) {
                serviceDataListener = null;
            }
            else {
                queryLocalInterface = binder.queryLocalInterface(DESCRIPTOR);
                if (queryLocalInterface != null && queryLocalInterface instanceof IServiceDataListener) {
                    serviceDataListener = (IServiceDataListener)queryLocalInterface;
                }
                else {
                    serviceDataListener = new Proxy(binder);
                }
            }
            return serviceDataListener;
        }
        
        public IBinder asBinder() {
            return this;
        }
        
        @Override
        public boolean onTransact(int n, Parcel parcel, Parcel parcel2, int n2) throws RemoteException {
            boolean onTransact;
            ServiceDataModel serviceDataModel;
            onTransact = true;
            switch (n) {
                default: {
                    onTransact = super.onTransact(n, parcel, parcel2, n2);
                    break;
                }
                case 1598968902: {
                    parcel2.writeString(DESCRIPTOR);
                    break;
                }
                case 1: {
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        serviceDataModel = ServiceDataModel.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        serviceDataModel = null;
                    }
                    this.onDataChanged(serviceDataModel);
                    parcel2.writeNoException();
                    if (serviceDataModel != null) {
                        parcel2.writeInt(onTransact ? 1 : 0);
                        serviceDataModel.writeToParcel(parcel2, 1);
                        break;
                    }
                    parcel2.writeInt(0);
                    break;
                }
            }
            return onTransact;
        }
        
        private static class Proxy implements IServiceDataListener
        {
            private IBinder mRemote;
            
            Proxy(IBinder mRemote) {
                super();
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            //TODO para que isto?
           // public String getInterfaceDescriptor() {
           //     return "de.oganisyan.paraglidervario.IServiceDataListener";
           // }
            
            public void onDataChanged(ServiceDataModel serviceDataModel) throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    if (serviceDataModel != null) {
                        obtain.writeInt(1);
                        serviceDataModel.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(TRANSACTION_onDataChanged, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        if (serviceDataModel != null) serviceDataModel.readFromParcel(obtain2);
                    }
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
