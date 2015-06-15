package de.oganisyan.paraglidervario;

import android.os.*;

public interface IServiceController extends IInterface
{
    void addListener(IServiceDataListener p0) throws RemoteException;
    
    void disableBeepEmulation() throws RemoteException;
    
    void enableBeepEmulation(float p0, float p1, float p2, float p3, float p4, float p5) throws RemoteException;
    
    void getSettingsFromPreferences() throws RemoteException;
    
    void initBeepDevice() throws RemoteException;
    
    void removeListener(IServiceDataListener p0) throws RemoteException;
    
    void setBeepEmulatioValue(float p0) throws RemoteException;
    
    void setEnebleBeep(boolean p0) throws RemoteException;
    
    void fala(int n) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IServiceController
    {
        private static final String DESCRIPTOR = "de.oganisyan.paraglidervario.IServiceController";
        static final int TRANSACTION_addListener = 1;
        static final int TRANSACTION_disableBeepEmulation = 7;
        static final int TRANSACTION_enableBeepEmulation = 6;
        static final int TRANSACTION_getSettingsFromPreferences = 3;
        static final int TRANSACTION_initBeepDevice = 5;
        static final int TRANSACTION_removeListener = 2;
        static final int TRANSACTION_setBeepEmulatioValue = 8;
        static final int TRANSACTION_setEnebleBeep = 4;
        static final int TRANSACTION_fala = 9;
        
        public Stub() {
            super();
            this.attachInterface(this, DESCRIPTOR);
        }
        
        public static IServiceController asInterface(IBinder binder) {
            IServiceController serviceController;
            IInterface queryLocalInterface;
            if (binder == null) {
                serviceController = null;
            }
            else {
                queryLocalInterface = binder.queryLocalInterface(DESCRIPTOR);
                if (queryLocalInterface != null && queryLocalInterface instanceof IServiceController) {
                    serviceController = (IServiceController)queryLocalInterface;
                }
                else {
                    serviceController = new Proxy(binder);
                }
            }
            return serviceController;
        }
        
        public IBinder asBinder() {
            return this;
        }
        
        public boolean onTransact(int n, Parcel parcel, Parcel parcel2, int n2) throws RemoteException {
            boolean onTransact;
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
                    this.addListener(IServiceDataListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    break;
                }
                case 2: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.removeListener(IServiceDataListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    break;
                }
                case 3: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.getSettingsFromPreferences();
                    parcel2.writeNoException();
                    break;
                }
                case 4: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.setEnebleBeep(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    break;
                }
                case 5: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.initBeepDevice();
                    parcel2.writeNoException();
                    break;
                }
                case 6: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.enableBeepEmulation(parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat());
                    parcel2.writeNoException();
                    break;
                }
                case 7: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.disableBeepEmulation();
                    parcel2.writeNoException();
                    break;
                }
                case 8: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.setBeepEmulatioValue(parcel.readFloat());
                    parcel2.writeNoException();
                    break;
                }
                case 9: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.fala(parcel.readInt());
                    parcel2.writeNoException();
                    break;
                }
            }
            return onTransact;
        }
        
        private static class Proxy implements IServiceController
        {
            private IBinder mRemote;
            
            Proxy(IBinder mRemote) {
                super();
                this.mRemote = mRemote;
            }
            
            public void addListener(IServiceDataListener serviceDataListener) throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                IBinder binder;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    if (serviceDataListener != null) {
                        binder = serviceDataListener.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.mRemote.transact(TRANSACTION_addListener, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public void disableBeepEmulation() throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    this.mRemote.transact(TRANSACTION_disableBeepEmulation, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public void enableBeepEmulation(float n, float n2, float n3, float n4, float n5, float n6) throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeFloat(n);
                    obtain.writeFloat(n2);
                    obtain.writeFloat(n3);
                    obtain.writeFloat(n4);
                    obtain.writeFloat(n5);
                    obtain.writeFloat(n6);
                    this.mRemote.transact(TRANSACTION_enableBeepEmulation, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            //TODO para que isto?
            //public String getInterfaceDescriptor() {
            //    return "de.oganisyan.paraglidervario.IServiceController";
           // }
            
            public void getSettingsFromPreferences() throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    this.mRemote.transact(TRANSACTION_getSettingsFromPreferences, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public void initBeepDevice() throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    this.mRemote.transact(TRANSACTION_initBeepDevice, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public void removeListener(IServiceDataListener serviceDataListener) throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                IBinder binder;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    if (serviceDataListener != null) {
                        binder = serviceDataListener.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.mRemote.transact(TRANSACTION_removeListener, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public void setBeepEmulatioValue(float n) throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeFloat(n);
                    this.mRemote.transact(TRANSACTION_setBeepEmulatioValue, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public void fala(int n) throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    obtain.writeInt(n);
                    this.mRemote.transact(TRANSACTION_fala, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
                                 
            public void setEnebleBeep(boolean b) throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                int n;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    n = 0;
                    if (b) {
                        n = 1;
                    }
                    obtain.writeInt(n);
                    this.mRemote.transact(TRANSACTION_setEnebleBeep, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
