package de.oganisyan.tracking;

import android.os.*;

public interface IStatusListener extends IInterface
{
    void onStatusChanged(boolean p0) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IStatusListener
    {
        private static final String DESCRIPTOR = "de.oganisyan.tracking.IStatusListener";
        static final int TRANSACTION_onStatusChanged = 1;
        
        public Stub() {
            super();
            this.attachInterface(this, DESCRIPTOR);
        }
        
        public static IStatusListener asInterface(IBinder binder) {
            IStatusListener statusListener;
            IInterface queryLocalInterface;
            if (binder == null) {
                statusListener = null;
            }
            else {
                queryLocalInterface = binder.queryLocalInterface(DESCRIPTOR);
                if (queryLocalInterface != null && queryLocalInterface instanceof IStatusListener) {
                    statusListener = (IStatusListener)queryLocalInterface;
                }
                else {
                    statusListener = new Proxy(binder);
                }
            }
            return statusListener;
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
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
                    this.onStatusChanged(parcel.readInt() != 0 && onTransact);
                    parcel2.writeNoException();
                    break;
                }
            }
            return onTransact;
        }
        
        private static class Proxy implements IStatusListener
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
            //public String getInterfaceDescriptor() {
            //    return DESCRIPTOR;
           // }
            
            public void onStatusChanged(boolean b) throws RemoteException {
                int n;
                Parcel obtain;
                Parcel obtain2;
                n = 1;
                obtain = Parcel.obtain();
                obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(DESCRIPTOR);
                    if (!b) {
                        n = 0;
                    }
                    obtain.writeInt(n);
                    this.mRemote.transact(1, obtain, obtain2, 0);
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
