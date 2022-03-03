package com.samsung.android.game.tencentsdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IToTGPACallback extends IInterface {

    public static class Default implements IToTGPACallback {
        public IBinder asBinder() {
            return null;
        }

        public void totgpa(String str) throws RemoteException {
        }
    }

    void totgpa(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements IToTGPACallback {
        private static final String DESCRIPTOR = "com.samsung.android.game.tencentsdk.IToTGPACallback";
        static final int TRANSACTION_totgpa = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IToTGPACallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IToTGPACallback)) {
                return new Proxy(iBinder);
            }
            return (IToTGPACallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                totgpa(parcel.readString());
                parcel2.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IToTGPACallback {
            public static IToTGPACallback sDefaultImpl;
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

            public void totgpa(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(1, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().totgpa(str);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IToTGPACallback iToTGPACallback) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iToTGPACallback == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iToTGPACallback;
                return true;
            }
        }

        public static IToTGPACallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
