package com.samsung.android.game.tencentsdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.samsung.android.game.compatibility.SharedMemory;
import com.samsung.android.game.tencentsdk.ISceneSdkListener;
import com.samsung.android.game.tencentsdk.IToTGPACallback;

public interface ISceneSdkService extends IInterface {

    public static class Default implements ISceneSdkService {
        public int applyHardwareResource(String str) throws RemoteException {
            return 0;
        }

        public int applyThreadGuarantee(String str) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }

        public String getVendorSupportStrategy(String str) throws RemoteException {
            return null;
        }

        public float getVersion() throws RemoteException {
            return 0.0f;
        }

        public int initLowLatencyIPC(String str, SharedMemory sharedMemory) throws RemoteException {
            return 0;
        }

        public boolean initSceneSdk() throws RemoteException {
            return false;
        }

        public int registerToTGPACallback(IToTGPACallback iToTGPACallback, float f) throws RemoteException {
            return 0;
        }

        public boolean setSceneSdkListener(ISceneSdkListener iSceneSdkListener) throws RemoteException {
            return false;
        }

        public String totgpa() throws RemoteException {
            return null;
        }

        public int updateGameInfo(String str) throws RemoteException {
            return 0;
        }
    }

    int applyHardwareResource(String str) throws RemoteException;

    int applyThreadGuarantee(String str) throws RemoteException;

    String getVendorSupportStrategy(String str) throws RemoteException;

    float getVersion() throws RemoteException;

    int initLowLatencyIPC(String str, SharedMemory sharedMemory) throws RemoteException;

    boolean initSceneSdk() throws RemoteException;

    int registerToTGPACallback(IToTGPACallback iToTGPACallback, float f) throws RemoteException;

    boolean setSceneSdkListener(ISceneSdkListener iSceneSdkListener) throws RemoteException;

    String totgpa() throws RemoteException;

    int updateGameInfo(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements ISceneSdkService {
        private static final String DESCRIPTOR = "com.samsung.android.game.tencentsdk.ISceneSdkService";
        static final int TRANSACTION_applyHardwareResource = 4;
        static final int TRANSACTION_applyThreadGuarantee = 5;
        static final int TRANSACTION_getVendorSupportStrategy = 10;
        static final int TRANSACTION_getVersion = 6;
        static final int TRANSACTION_initLowLatencyIPC = 7;
        static final int TRANSACTION_initSceneSdk = 1;
        static final int TRANSACTION_registerToTGPACallback = 9;
        static final int TRANSACTION_setSceneSdkListener = 2;
        static final int TRANSACTION_totgpa = 8;
        static final int TRANSACTION_updateGameInfo = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISceneSdkService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISceneSdkService)) {
                return new Proxy(iBinder);
            }
            return (ISceneSdkService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(DESCRIPTOR);
                        boolean initSceneSdk = initSceneSdk();
                        parcel2.writeNoException();
                        parcel2.writeInt(initSceneSdk ? 1 : 0);
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        boolean sceneSdkListener = setSceneSdkListener(ISceneSdkListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel2.writeInt(sceneSdkListener ? 1 : 0);
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        int updateGameInfo = updateGameInfo(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(updateGameInfo);
                        return true;
                    case 4:
                        parcel.enforceInterface(DESCRIPTOR);
                        int applyHardwareResource = applyHardwareResource(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(applyHardwareResource);
                        return true;
                    case 5:
                        parcel.enforceInterface(DESCRIPTOR);
                        int applyThreadGuarantee = applyThreadGuarantee(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(applyThreadGuarantee);
                        return true;
                    case 6:
                        parcel.enforceInterface(DESCRIPTOR);
                        float version = getVersion();
                        parcel2.writeNoException();
                        parcel2.writeFloat(version);
                        return true;
                    case 7:
                        parcel.enforceInterface(DESCRIPTOR);
                        int initLowLatencyIPC = initLowLatencyIPC(parcel.readString(), parcel.readInt() != 0 ? SharedMemory.CREATOR.createFromParcel(parcel) : null);
                        parcel2.writeNoException();
                        parcel2.writeInt(initLowLatencyIPC);
                        return true;
                    case 8:
                        parcel.enforceInterface(DESCRIPTOR);
                        String str = totgpa();
                        parcel2.writeNoException();
                        parcel2.writeString(str);
                        return true;
                    case 9:
                        parcel.enforceInterface(DESCRIPTOR);
                        int registerToTGPACallback = registerToTGPACallback(IToTGPACallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readFloat());
                        parcel2.writeNoException();
                        parcel2.writeInt(registerToTGPACallback);
                        return true;
                    case 10:
                        parcel.enforceInterface(DESCRIPTOR);
                        String vendorSupportStrategy = getVendorSupportStrategy(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeString(vendorSupportStrategy);
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements ISceneSdkService {
            public static ISceneSdkService sDefaultImpl;
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

            public boolean initSceneSdk() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().initSceneSdk();
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

            public boolean setSceneSdkListener(ISceneSdkListener iSceneSdkListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iSceneSdkListener != null ? iSceneSdkListener.asBinder() : null);
                    boolean z = false;
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setSceneSdkListener(iSceneSdkListener);
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

            public int updateGameInfo(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateGameInfo(str);
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

            public int applyHardwareResource(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().applyHardwareResource(str);
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

            public int applyThreadGuarantee(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().applyThreadGuarantee(str);
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
                    if (!this.mRemote.transact(6, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
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

            public int initLowLatencyIPC(String str, SharedMemory sharedMemory) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (sharedMemory != null) {
                        obtain.writeInt(1);
                        sharedMemory.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(7, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().initLowLatencyIPC(str, sharedMemory);
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

            public String totgpa() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().totgpa();
                    }
                    obtain2.readException();
                    String readString = obtain2.readString();
                    obtain2.recycle();
                    obtain.recycle();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int registerToTGPACallback(IToTGPACallback iToTGPACallback, float f) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iToTGPACallback != null ? iToTGPACallback.asBinder() : null);
                    obtain.writeFloat(f);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerToTGPACallback(iToTGPACallback, f);
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

            public String getVendorSupportStrategy(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(10, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVendorSupportStrategy(str);
                    }
                    obtain2.readException();
                    String readString = obtain2.readString();
                    obtain2.recycle();
                    obtain.recycle();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISceneSdkService iSceneSdkService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iSceneSdkService == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iSceneSdkService;
                return true;
            }
        }

        public static ISceneSdkService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
