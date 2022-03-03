package com.samsung.android.game.gos.feature.gfi.value;

import android.os.IBinder;
import android.os.Parcel;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.selibrary.SeServiceManager;
import com.samsung.android.game.gos.util.GosLog;

public class GfiSurfaceFlingerHelper {
    private static final String LOG_TAG = "GfiSurfaceFlingerHelper";
    public static final String SURFACEFLINGER_INTERFACE_TOKEN = "android.ui.ISurfaceComposer";
    public static final int TRANSACTION_INTERROGATE_SURFACEFLINGER = 1010;
    public static final int TRANSACTION_SUBMIT_SETTINGS = 1127;
    private static IBinder mSurfaceFlingerBinder;

    public GfiSurfaceFlingerHelper(IBinder iBinder) {
        mSurfaceFlingerBinder = iBinder;
        if (iBinder == null) {
            mSurfaceFlingerBinder = SeServiceManager.getInstance().getService("SurfaceFlinger");
        }
    }

    public static class SurfaceFlingerConfig {
        public static final int INTS_IN_REPLY_PARCEL = 5;
        private static final String LOG_TAG = "GfiSurfaceFlingerHelper.SurfaceFlingerConfig";
        public final boolean hwOverlaysDisabled;
        public final boolean showSurfaceUpdates;

        public SurfaceFlingerConfig(Parcel parcel) {
            boolean z = true;
            if (parcel == null || parcel.dataSize() < 20) {
                GosLog.d(LOG_TAG, "invalid reply parcel");
                this.showSurfaceUpdates = true;
                this.hwOverlaysDisabled = true;
            } else {
                parcel.readInt();
                parcel.readInt();
                this.showSurfaceUpdates = parcel.readInt() != 0;
                parcel.readInt();
                this.hwOverlaysDisabled = parcel.readInt() == 0 ? false : z;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("overlays are ");
            sb.append(this.hwOverlaysDisabled ? State.DISABLED : "enabled");
            GosLog.d(LOG_TAG, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("show surface updates is ");
            sb2.append(this.showSurfaceUpdates ? "on" : "off");
            GosLog.d(LOG_TAG, sb2.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x004d  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x005e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper.SurfaceFlingerConfig interrogateSF() {
        /*
            r7 = this;
            r0 = 0
            android.os.Parcel r1 = android.os.Parcel.obtain()     // Catch:{ RemoteException | SecurityException -> 0x003d, all -> 0x0038 }
            android.os.Parcel r2 = android.os.Parcel.obtain()     // Catch:{ RemoteException | SecurityException -> 0x0036, all -> 0x0031 }
            java.lang.String r3 = "android.ui.ISurfaceComposer"
            r1.writeInterfaceToken(r3)     // Catch:{ RemoteException | SecurityException -> 0x003f }
            android.os.IBinder r3 = mSurfaceFlingerBinder     // Catch:{ RemoteException | SecurityException -> 0x003f }
            r4 = 1010(0x3f2, float:1.415E-42)
            r5 = 0
            boolean r3 = r3.transact(r4, r1, r2, r5)     // Catch:{ RemoteException | SecurityException -> 0x003f }
            if (r3 == 0) goto L_0x0029
            com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper$SurfaceFlingerConfig r3 = new com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper$SurfaceFlingerConfig     // Catch:{ RemoteException | SecurityException -> 0x003f }
            r3.<init>(r2)     // Catch:{ RemoteException | SecurityException -> 0x003f }
            if (r1 == 0) goto L_0x0023
            r1.recycle()
        L_0x0023:
            if (r2 == 0) goto L_0x0028
            r2.recycle()
        L_0x0028:
            return r3
        L_0x0029:
            android.os.RemoteException r3 = new android.os.RemoteException     // Catch:{ RemoteException | SecurityException -> 0x003f }
            java.lang.String r4 = "SurfaceFlinger transaction unsuccessful"
            r3.<init>(r4)     // Catch:{ RemoteException | SecurityException -> 0x003f }
            throw r3     // Catch:{ RemoteException | SecurityException -> 0x003f }
        L_0x0031:
            r2 = move-exception
            r6 = r2
            r2 = r0
            r0 = r6
            goto L_0x0057
        L_0x0036:
            r2 = r0
            goto L_0x003f
        L_0x0038:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
            goto L_0x0057
        L_0x003d:
            r1 = r0
            r2 = r1
        L_0x003f:
            java.lang.String r3 = "GfiSurfaceFlingerHelper"
            java.lang.String r4 = "interrogateSF: failed to interrogate SurfaceFlinger"
            com.samsung.android.game.gos.util.GosLog.d(r3, r4)     // Catch:{ all -> 0x0056 }
            if (r1 == 0) goto L_0x004b
            r1.recycle()
        L_0x004b:
            if (r2 == 0) goto L_0x0050
            r2.recycle()
        L_0x0050:
            com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper$SurfaceFlingerConfig r1 = new com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper$SurfaceFlingerConfig
            r1.<init>(r0)
            return r1
        L_0x0056:
            r0 = move-exception
        L_0x0057:
            if (r1 == 0) goto L_0x005c
            r1.recycle()
        L_0x005c:
            if (r2 == 0) goto L_0x0061
            r2.recycle()
        L_0x0061:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper.interrogateSF():com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper$SurfaceFlingerConfig");
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0060  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setKeepTwoHwcLayers(boolean r7) {
        /*
            r6 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "setKeepTwoHwcLayers "
            r0.append(r1)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "GfiSurfaceFlingerHelper"
            com.samsung.android.game.gos.util.GosLog.d(r1, r0)
            r0 = 0
            r2 = 0
            android.os.Parcel r3 = android.os.Parcel.obtain()     // Catch:{ RemoteException | SecurityException -> 0x0053 }
            java.lang.String r4 = "android.ui.ISurfaceComposer"
            r3.writeInterfaceToken(r4)     // Catch:{ RemoteException | SecurityException -> 0x004f, all -> 0x004c }
            r3.writeInt(r2)     // Catch:{ RemoteException | SecurityException -> 0x004f, all -> 0x004c }
            r4 = 1
            r3.writeInt(r4)     // Catch:{ RemoteException | SecurityException -> 0x004f, all -> 0x004c }
            r5 = 115(0x73, float:1.61E-43)
            r3.writeInt(r5)     // Catch:{ RemoteException | SecurityException -> 0x004f, all -> 0x004c }
            if (r7 == 0) goto L_0x0030
            goto L_0x0031
        L_0x0030:
            r4 = r2
        L_0x0031:
            r3.writeInt(r4)     // Catch:{ RemoteException | SecurityException -> 0x004f, all -> 0x004c }
            android.os.IBinder r7 = mSurfaceFlingerBinder     // Catch:{ RemoteException | SecurityException -> 0x004f, all -> 0x004c }
            r4 = 1127(0x467, float:1.579E-42)
            boolean r2 = r7.transact(r4, r3, r0, r2)     // Catch:{ RemoteException | SecurityException -> 0x004f, all -> 0x004c }
            if (r2 == 0) goto L_0x0044
            if (r3 == 0) goto L_0x005d
            r3.recycle()
            goto L_0x005d
        L_0x0044:
            android.os.RemoteException r7 = new android.os.RemoteException     // Catch:{ RemoteException | SecurityException -> 0x004f, all -> 0x004c }
            java.lang.String r0 = "SurfaceFlinger transaction unsuccessful"
            r7.<init>(r0)     // Catch:{ RemoteException | SecurityException -> 0x004f, all -> 0x004c }
            throw r7     // Catch:{ RemoteException | SecurityException -> 0x004f, all -> 0x004c }
        L_0x004c:
            r7 = move-exception
            r0 = r3
            goto L_0x005e
        L_0x004f:
            r0 = r3
            goto L_0x0053
        L_0x0051:
            r7 = move-exception
            goto L_0x005e
        L_0x0053:
            java.lang.String r7 = "setKeepTwoHwcLayers: failed"
            com.samsung.android.game.gos.util.GosLog.d(r1, r7)     // Catch:{ all -> 0x0051 }
            if (r0 == 0) goto L_0x005d
            r0.recycle()
        L_0x005d:
            return r2
        L_0x005e:
            if (r0 == 0) goto L_0x0063
            r0.recycle()
        L_0x0063:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.gfi.value.GfiSurfaceFlingerHelper.setKeepTwoHwcLayers(boolean):boolean");
    }
}
