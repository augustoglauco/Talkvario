package de.oganisyan.tracking;

import android.os.*;

public interface IController extends IInterface
{
    void addListener(IStatusListener p0) throws RemoteException;
    
    boolean getStatus() throws RemoteException;
    
    void removeListener(IStatusListener p0) throws RemoteException;
    
    void startLogger() throws RemoteException;
    
    void stopLogger() throws RemoteException;
    
    public abstract static class Stub extends Binder implements IController
    {
        private static final String DESCRIPTOR = "de.oganisyan.tracking.IController";
        static final int TRANSACTION_addListener = 4;
        static final int TRANSACTION_getStatus = 3;
        static final int TRANSACTION_removeListener = 5;
        static final int TRANSACTION_startLogger = 1;
        static final int TRANSACTION_stopLogger = 2;
        
        public Stub() {
            super();
            this.attachInterface(this, DESCRIPTOR);
        }
        
        public static IController asInterface(IBinder binder) {
            IController controller;
            IInterface queryLocalInterface;
            if (binder == null) {
                controller = null;
            }
            else {
                queryLocalInterface = binder.queryLocalInterface(DESCRIPTOR);
                if (queryLocalInterface != null && queryLocalInterface instanceof IController) {
                    controller = (IController)queryLocalInterface;
                }
                else {
                    controller = new Proxy(binder);
                }
            }
            return controller;
        }
        
        public IBinder asBinder() {
            return this;
        }
        
        public boolean onTransact(int n, Parcel parcel, Parcel parcel2, int n2) throws RemoteException {
            boolean onTransact;
            boolean status;
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
                    this.startLogger();
                    parcel2.writeNoException();
                    break;
                }
                case 2: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.stopLogger();
                    parcel2.writeNoException();
                    break;
                }
                case 3: {
                    parcel.enforceInterface(DESCRIPTOR);
                    status = this.getStatus();
                    parcel2.writeNoException();
                    parcel2.writeInt(status && onTransact ? 1 : 0);
                    break;
                }
                case 4: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.addListener(IStatusListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    break;
                }
                case 5: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.removeListener(IStatusListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    break;
                }
            }
            return onTransact;
        }
        
        private static class Proxy implements IController
        {
            private IBinder mRemote;
            
            Proxy(IBinder mRemote) {
                super();
                this.mRemote = mRemote;
            }
            
            public void addListener(IStatusListener statusListener) throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                IBinder binder;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    if (statusListener != null) {
                        binder = statusListener.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.mRemote.transact(4, obtain, obtain2, 0);
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
            
           //TODO para que isto?
           // public String getInterfaceDescriptor() {
           //     return DESCRIPTOR;
           // }
            
            public boolean getStatus() throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                int int1;
                boolean b;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    int1 = obtain2.readInt();
                    b = false;
                    if (int1 != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public void removeListener(IStatusListener statusListener) throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                IBinder binder;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    if (statusListener != null) {
                        binder = statusListener.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public void startLogger() throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public void stopLogger() throws RemoteException {
                Parcel obtain;
                Parcel obtain2;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
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
