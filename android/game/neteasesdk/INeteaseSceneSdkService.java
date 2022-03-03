package com.samsung.android.game.neteasesdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.samsung.android.game.neteasesdk.INeteaseSceneSdkListener;

public interface INeteaseSceneSdkService extends IInterface {

    public static class Default implements INeteaseSceneSdkService {
        public IBinder asBinder() {
            return null;
        }

        public float getVersion() throws RemoteException {
            return 0.0f;
        }

        public boolean initNeteaseSceneSdk() throws RemoteException {
            return false;
        }

        public boolean setNeteaseSceneSdkListener(INeteaseSceneSdkListener iNeteaseSceneSdkListener) throws RemoteException {
            return false;
        }

        public int transferGameInfo(String str) throws RemoteException {
            return 0;
        }

        public int transferThreadId(String str) throws RemoteException {
            return 0;
        }
    }

    float getVersion() throws RemoteException;

    boolean initNeteaseSceneSdk() throws RemoteException;

    boolean setNeteaseSceneSdkListener(INeteaseSceneSdkListener iNeteaseSceneSdkListener) throws RemoteException;

    int transferGameInfo(String str) throws RemoteException;

    int transferThreadId(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements INeteaseSceneSdkService {
        private static final String DESCRIPTOR = "com.samsung.android.game.neteasesdk.INeteaseSceneSdkService";
        static final int TRANSACTION_getVersion = 5;
        static final int TRANSACTION_initNeteaseSceneSdk = 1;
        static final int TRANSACTION_setNeteaseSceneSdkListener = 2;
        static final int TRANSACTION_transferGameInfo = 3;
        static final int TRANSACTION_transferThreadId = 4;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INeteaseSceneSdkService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INeteaseSceneSdkService)) {
                return new Proxy(iBinder);
            }
            return (INeteaseSceneSdkService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                boolean initNeteaseSceneSdk = initNeteaseSceneSdk();
                parcel2.writeNoException();
                parcel2.writeInt(initNeteaseSceneSdk ? 1 : 0);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                boolean neteaseSceneSdkListener = setNeteaseSceneSdkListener(INeteaseSceneSdkListener.Stub.asInterface(parcel.readStrongBinder()));
                parcel2.writeNoException();
                parcel2.writeInt(neteaseSceneSdkListener ? 1 : 0);
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                int transferGameInfo = transferGameInfo(parcel.readString());
                parcel2.writeNoException();
                parcel2.writeInt(transferGameInfo);
                return true;
            } else if (i == 4) {
                parcel.enforceInterface(DESCRIPTOR);
                int transferThreadId = transferThreadId(parcel.readString());
                parcel2.writeNoException();
                parcel2.writeInt(transferThreadId);
                return true;
            } else if (i == 5) {
                parcel.enforceInterface(DESCRIPTOR);
                float version = getVersion();
                parcel2.writeNoException();
                parcel2.writeFloat(version);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements INeteaseSceneSdkService {
            public static INeteaseSceneSdkService sDefaultImpl;
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

            public boolean initNeteaseSceneSdk() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().initNeteaseSceneSdk();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setNeteaseSceneSdkListener(INeteaseSceneSdkListener iNeteaseSceneSdkListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iNeteaseSceneSdkListener != null ? iNeteaseSceneSdkListener.asBinder() : null);
                    boolean z = false;
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setNeteaseSceneSdkListener(iNeteaseSceneSdkListener);
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int transferGameInfo(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().transferGameInfo(str);
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.recycle();
                    obtain.recycle();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int transferThreadId(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().transferThreadId(str);
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.recycle();
                    obtain.recycle();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public float getVersion() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVersion();
                    }
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    obtain2.recycle();
                    obtain.recycle();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(INeteaseSceneSdkService iNeteaseSceneSdkService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iNeteaseSceneSdkService == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iNeteaseSceneSdkService;
                return true;
            }
        }

        public static INeteaseSceneSdkService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
