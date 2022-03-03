package com.samsung.android.game.tencentsdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISceneSdkListener extends IInterface {

    public static class Default implements ISceneSdkListener {
        public IBinder asBinder() {
            return null;
        }

        public void resultCallBack(int i, int i2) throws RemoteException {
        }

        public void systemCallBack(int i) throws RemoteException {
        }
    }

    void resultCallBack(int i, int i2) throws RemoteException;

    void systemCallBack(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ISceneSdkListener {
        private static final String DESCRIPTOR = "com.samsung.android.game.tencentsdk.ISceneSdkListener";
        static final int TRANSACTION_resultCallBack = 2;
        static final int TRANSACTION_systemCallBack = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISceneSdkListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISceneSdkListener)) {
                return new Proxy(iBinder);
            }
            return (ISceneSdkListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                systemCallBack(parcel.readInt());
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                resultCallBack(parcel.readInt(), parcel.readInt());
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements ISceneSdkListener {
            public static ISceneSdkListener sDefaultImpl;
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void systemCallBack(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().systemCallBack(i);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void resultCallBack(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().resultCallBack(i, i2);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISceneSdkListener iSceneSdkListener) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iSceneSdkListener == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iSceneSdkListener;
                return true;
            }
        }

        public static ISceneSdkListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
