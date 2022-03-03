package com.samsung.android.game.gos;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGosSystemService extends IInterface {

    public static class Default implements IGosSystemService {
        public IBinder asBinder() {
            return null;
        }

        public String requestOnlyForDumpOrTest(String str, String str2) throws RemoteException {
            return null;
        }

        public void requestWithJsonNoReturn(String str, String str2) throws RemoteException {
        }
    }

    String requestOnlyForDumpOrTest(String str, String str2) throws RemoteException;

    void requestWithJsonNoReturn(String str, String str2) throws RemoteException;

    public static abstract class Stub extends Binder implements IGosSystemService {
        private static final String DESCRIPTOR = "com.samsung.android.game.gos.IGosSystemService";
        static final int TRANSACTION_requestOnlyForDumpOrTest = 1;
        static final int TRANSACTION_requestWithJsonNoReturn = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGosSystemService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IGosSystemService)) {
                return new Proxy(iBinder);
            }
            return (IGosSystemService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                String requestOnlyForDumpOrTest = requestOnlyForDumpOrTest(parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                parcel2.writeString(requestOnlyForDumpOrTest);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                requestWithJsonNoReturn(parcel.readString(), parcel.readString());
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IGosSystemService {
            public static IGosSystemService sDefaultImpl;
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

            public String requestOnlyForDumpOrTest(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestOnlyForDumpOrTest(str, str2);
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

            public void requestWithJsonNoReturn(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().requestWithJsonNoReturn(str, str2);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IGosSystemService iGosSystemService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iGosSystemService == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iGosSystemService;
                return true;
            }
        }

        public static IGosSystemService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
