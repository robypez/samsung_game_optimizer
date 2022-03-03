package com.samsung.android.game.neteasesdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INeteaseSceneSdkListener extends IInterface {

    public static class Default implements INeteaseSceneSdkListener {
        public IBinder asBinder() {
            return null;
        }

        public void systemCallBack(int i) throws RemoteException {
        }
    }

    void systemCallBack(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements INeteaseSceneSdkListener {
        private static final String DESCRIPTOR = "com.samsung.android.game.neteasesdk.INeteaseSceneSdkListener";
        static final int TRANSACTION_systemCallBack = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INeteaseSceneSdkListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INeteaseSceneSdkListener)) {
                return new Proxy(iBinder);
            }
            return (INeteaseSceneSdkListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                systemCallBack(parcel.readInt());
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements INeteaseSceneSdkListener {
            public static INeteaseSceneSdkListener sDefaultImpl;
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
        }

        public static boolean setDefaultImpl(INeteaseSceneSdkListener iNeteaseSceneSdkListener) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iNeteaseSceneSdkListener == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iNeteaseSceneSdkListener;
                return true;
            }
        }

        public static INeteaseSceneSdkListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
