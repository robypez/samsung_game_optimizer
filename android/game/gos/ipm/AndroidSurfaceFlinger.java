package com.samsung.android.game.gos.ipm;

import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper;
import java.io.IOException;
import java.util.Locale;

public class AndroidSurfaceFlinger extends SurfaceFlinger {
    private static final int CONNECT_LAYER_METRICS_PIPE_CODE = 5589316;
    private static final int FRAME_STATS_CODE = 1131;
    private static final int GET_INTERPOLATION_RINGLOG_CODE = 1128;
    private static final int GET_PAGE_FLIP_COUNT_CODE = 1013;
    private static final String LOG_TAG = "AndroidSurfaceFlinger";
    private static final int SET_POWER_SAVE_LEVEL_CODE = 1122;
    private final AndroidDisplay mAndroidDisplay;
    private final AndroidPackage mAndroidPackage;
    private final IBinder mBinder;
    private final FrameInterpolator mFrameInterpolator;

    public AndroidSurfaceFlinger(IBinder iBinder, AndroidPackage androidPackage, AndroidDisplay androidDisplay, FrameInterpolator frameInterpolator) {
        this.mBinder = iBinder;
        this.mAndroidPackage = androidPackage;
        this.mAndroidDisplay = androidDisplay;
        this.mFrameInterpolator = frameInterpolator;
    }

    public boolean dumpPipe(int i, VectorString vectorString) {
        try {
            this.mBinder.dump(ParcelFileDescriptor.fromFd(i).getFileDescriptor(), WrapUtility.toArray(vectorString));
            return true;
        } catch (RemoteException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int connectPipe(int i, int i2) {
        Parcel obtainDataParcel = obtainDataParcel();
        Parcel obtain = Parcel.obtain();
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            ParcelFileDescriptor fromFd = ParcelFileDescriptor.fromFd(i);
            obtainDataParcel.writeInt(i2);
            obtainDataParcel.writeFileDescriptor(fromFd.getFileDescriptor());
            if (transact(CONNECT_LAYER_METRICS_PIPE_CODE, obtainDataParcel, obtain)) {
                int readInt = obtain.readInt();
                obtainDataParcel.recycle();
                obtain.recycle();
                if (fromFd != null) {
                    try {
                        fromFd.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return -1;
                    }
                }
                return readInt;
            }
            obtainDataParcel.recycle();
            obtain.recycle();
            if (fromFd != null) {
                try {
                    fromFd.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            return -1;
        } catch (IOException e3) {
            e3.printStackTrace();
            obtainDataParcel.recycle();
            obtain.recycle();
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            return -1;
        } catch (Throwable th) {
            obtainDataParcel.recycle();
            obtain.recycle();
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                    return -1;
                }
            }
            throw th;
        }
    }

    public int flipCount() {
        int i;
        Parcel obtainDataParcel = obtainDataParcel();
        Parcel obtain = Parcel.obtain();
        try {
            if (transact(1013, obtainDataParcel, obtain)) {
                i = obtain.readInt();
            } else {
                i = -1;
            }
            return i;
        } finally {
            obtainDataParcel.recycle();
            obtain.recycle();
        }
    }

    public boolean isUsingGfi(int i) {
        Parcel obtainDataParcel = obtainDataParcel();
        Parcel obtain = Parcel.obtain();
        try {
            obtainDataParcel.writeInt(i);
            boolean z = false;
            if (transact(GET_INTERPOLATION_RINGLOG_CODE, obtainDataParcel, obtain) && obtain.readInt() != 0) {
                z = true;
            }
            return z;
        } finally {
            obtainDataParcel.recycle();
            obtain.recycle();
        }
    }

    public boolean enableGfi(float f) {
        if (!this.mFrameInterpolator.isUsingDfs() || setDfs(f)) {
            return this.mFrameInterpolator.enable(f, this.mAndroidDisplay.getRefreshRate(), this.mAndroidPackage.getName(), this.mAndroidPackage.getApplicationUid());
        }
        return false;
    }

    public boolean registerForFrameStats(int i, int i2) {
        Parcel obtainDataParcel = obtainDataParcel();
        Parcel obtain = Parcel.obtain();
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            ParcelFileDescriptor fromFd = ParcelFileDescriptor.fromFd(i2);
            obtainDataParcel.writeInt(i);
            fromFd.writeToParcel(obtainDataParcel, 0);
            boolean transact = transact(FRAME_STATS_CODE, obtainDataParcel, obtain);
            obtainDataParcel.recycle();
            obtain.recycle();
            if (fromFd != null) {
                try {
                    fromFd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return transact;
        } catch (IOException e2) {
            e2.printStackTrace();
            obtainDataParcel.recycle();
            obtain.recycle();
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            return false;
        } catch (Throwable th) {
            obtainDataParcel.recycle();
            obtain.recycle();
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                    return false;
                }
            }
            throw th;
        }
    }

    private boolean setDfs(float f) {
        Parcel obtainDataParcel = obtainDataParcel();
        try {
            obtainDataParcel.writeInt(Math.round(f));
            return transact(SET_POWER_SAVE_LEVEL_CODE, obtainDataParcel, (Parcel) null);
        } finally {
            obtainDataParcel.recycle();
        }
    }

    private Parcel obtainDataParcel() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(GfiSurfaceFlingerHelper.SURFACEFLINGER_INTERFACE_TOKEN);
        return obtain;
    }

    private boolean transact(int i, Parcel parcel, Parcel parcel2) {
        try {
            if (this.mBinder.transact(i, parcel, parcel2, 0)) {
                return true;
            }
            Log.w(LOG_TAG, String.format(Locale.US, "Transaction code '%d' not understood.", new Object[]{Integer.valueOf(i)}));
            return false;
        } catch (SecurityException unused) {
            Log.w(LOG_TAG, String.format("Transaction code '%d' is not approved.", new Object[]{Integer.valueOf(i)}));
            return false;
        } catch (RemoteException e) {
            Log.w(LOG_TAG, String.format(Locale.US, "Transaction '%d' failed.", new Object[]{Integer.valueOf(i)}));
            e.printStackTrace();
            return false;
        }
    }
}
