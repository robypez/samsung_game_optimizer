package com.samsung.android.game.gos.feature.gfi;

import android.os.Parcel;
import com.samsung.android.game.gos.util.GosLog;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class GfiRinglog {
    private static final int EVENT_BYTE_SIZE = 16;
    protected static final int HEADER_SIZE_INDEX = 104;
    protected static final int INDEX_INDEX = 12;
    private static final int INT_BYTE_SIZE = 4;
    private static final String LOG_TAG = "GfiRinglog";
    protected static final int MAXIMUM_INTERPRETABLE_VERSION = 6;
    protected static final int MINIMUM_INTERPRETABLE_VERSION = 3;
    protected static final int MINIMUM_REPLY_PARCEL_SIZE = 128;
    protected static final int[] RINGBUFFER_START_BYTE_INDICES = {84, 96, 104};
    protected static final int SIZE_INDEX = 0;
    private static final int TRANSACTION_GET_RINGLOG = 1128;
    protected static final int VERSION_INDEX = 8;
    public byte[] mHeader;
    public final int mIndex;
    public byte[] mRingBuffer;
    public final int mSize;
    public final int mVersion;

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0067, code lost:
        if (r7 != null) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0069, code lost:
        r7.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x009e, code lost:
        if (r7 != null) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00a1, code lost:
        if (r0 != null) goto L_0x00a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00a3, code lost:
        com.samsung.android.game.gos.util.GosLog.e(LOG_TAG, "requestRinglog: failed to obtain a valid ringlog.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a8, code lost:
        return r0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00ac  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00b1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.samsung.android.game.gos.feature.gfi.GfiRinglog requestRinglog(int r7) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "requestRinglog "
            r0.append(r1)
            r0.append(r7)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "GfiRinglog"
            com.samsung.android.game.gos.util.GosLog.d(r1, r0)
            r0 = 0
            android.os.Parcel r2 = android.os.Parcel.obtain()     // Catch:{ RemoteException -> 0x0082, SecurityException -> 0x0080, all -> 0x007b }
            java.lang.String r3 = "android.ui.ISurfaceComposer"
            r2.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0078, SecurityException -> 0x0076, all -> 0x0071 }
            r2.writeInt(r7)     // Catch:{ RemoteException -> 0x0078, SecurityException -> 0x0076, all -> 0x0071 }
            android.os.Parcel r7 = android.os.Parcel.obtain()     // Catch:{ RemoteException -> 0x0078, SecurityException -> 0x0076, all -> 0x0071 }
            android.os.IBinder r3 = com.samsung.android.game.gos.feature.gfi.GfiFeature.getSurfaceFlinger()     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            r4 = 1128(0x468, float:1.58E-42)
            r5 = 0
            r3.transact(r4, r2, r7, r5)     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            r3.<init>()     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            java.lang.String r4 = "requestRinglog: received a parcel with "
            r3.append(r4)     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            int r4 = r7.dataAvail()     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            r3.append(r4)     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            java.lang.String r4 = " data left"
            r3.append(r4)     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            java.lang.String r3 = r3.toString()     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            com.samsung.android.game.gos.util.GosLog.d(r1, r3)     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            int r3 = r7.dataAvail()     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            r4 = 128(0x80, float:1.794E-43)
            if (r3 <= r4) goto L_0x005d
            com.samsung.android.game.gos.feature.gfi.GfiRinglog r3 = new com.samsung.android.game.gos.feature.gfi.GfiRinglog     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            r3.<init>(r7)     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
            r0 = r3
            goto L_0x0062
        L_0x005d:
            java.lang.String r3 = "requestRinglog reply parcel is too small to contain a ringlog"
            com.samsung.android.game.gos.util.GosLog.e(r1, r3)     // Catch:{ RemoteException -> 0x006f, SecurityException -> 0x006d }
        L_0x0062:
            if (r2 == 0) goto L_0x0067
            r2.recycle()
        L_0x0067:
            if (r7 == 0) goto L_0x00a1
        L_0x0069:
            r7.recycle()
            goto L_0x00a1
        L_0x006d:
            r3 = move-exception
            goto L_0x0085
        L_0x006f:
            r3 = move-exception
            goto L_0x0085
        L_0x0071:
            r7 = move-exception
            r6 = r0
            r0 = r7
            r7 = r6
            goto L_0x00aa
        L_0x0076:
            r3 = move-exception
            goto L_0x0079
        L_0x0078:
            r3 = move-exception
        L_0x0079:
            r7 = r0
            goto L_0x0085
        L_0x007b:
            r7 = move-exception
            r2 = r0
            r0 = r7
            r7 = r2
            goto L_0x00aa
        L_0x0080:
            r3 = move-exception
            goto L_0x0083
        L_0x0082:
            r3 = move-exception
        L_0x0083:
            r7 = r0
            r2 = r7
        L_0x0085:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a9 }
            r4.<init>()     // Catch:{ all -> 0x00a9 }
            java.lang.String r5 = "requestRinglog: failed with exception "
            r4.append(r5)     // Catch:{ all -> 0x00a9 }
            r4.append(r3)     // Catch:{ all -> 0x00a9 }
            java.lang.String r3 = r4.toString()     // Catch:{ all -> 0x00a9 }
            com.samsung.android.game.gos.util.GosLog.e(r1, r3)     // Catch:{ all -> 0x00a9 }
            if (r2 == 0) goto L_0x009e
            r2.recycle()
        L_0x009e:
            if (r7 == 0) goto L_0x00a1
            goto L_0x0069
        L_0x00a1:
            if (r0 != 0) goto L_0x00a8
            java.lang.String r7 = "requestRinglog: failed to obtain a valid ringlog."
            com.samsung.android.game.gos.util.GosLog.e(r1, r7)
        L_0x00a8:
            return r0
        L_0x00a9:
            r0 = move-exception
        L_0x00aa:
            if (r2 == 0) goto L_0x00af
            r2.recycle()
        L_0x00af:
            if (r7 == 0) goto L_0x00b4
            r7.recycle()
        L_0x00b4:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.gfi.GfiRinglog.requestRinglog(int):com.samsung.android.game.gos.feature.gfi.GfiRinglog");
    }

    public GfiRinglog(Parcel parcel) {
        byte[] createByteArray = parcel.createByteArray();
        ByteBuffer wrap = ByteBuffer.wrap(createByteArray);
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        int i = wrap.getInt(8);
        this.mVersion = i;
        if (i >= 3 && i < 6) {
            this.mSize = (int) wrap.getLong(0);
            this.mIndex = wrap.getInt(12);
            this.mHeader = Arrays.copyOfRange(createByteArray, 0, RINGBUFFER_START_BYTE_INDICES[this.mVersion - 3]);
            this.mRingBuffer = Arrays.copyOfRange(createByteArray, RINGBUFFER_START_BYTE_INDICES[this.mVersion - 3], createByteArray.length);
        } else if (this.mVersion == 6) {
            wrap.getLong(104);
            int readInt = parcel.readInt();
            this.mSize = parcel.readInt();
            this.mIndex = parcel.readInt();
            ByteBuffer allocate = ByteBuffer.allocate(createByteArray.length + 16);
            allocate.order(ByteOrder.LITTLE_ENDIAN);
            allocate.put(createByteArray);
            allocate.putInt(readInt);
            allocate.putInt(this.mSize);
            allocate.putInt(this.mIndex);
            allocate.putInt(0);
            this.mHeader = allocate.array();
            this.mRingBuffer = parcel.createByteArray();
        } else {
            GosLog.e(LOG_TAG, "Ringlog of version " + this.mVersion + " is invalid");
            this.mSize = 0;
            this.mIndex = 0;
        }
        GosLog.d(LOG_TAG, "created Ringlog of version " + this.mVersion);
    }

    protected GfiRinglog() {
        this.mVersion = 0;
        this.mSize = 0;
        this.mIndex = 0;
        this.mHeader = null;
        this.mRingBuffer = null;
    }

    public boolean isValid() {
        int i = this.mVersion;
        return i >= 3 && i <= 6;
    }
}
