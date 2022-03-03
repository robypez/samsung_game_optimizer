package com.samsung.android.game.gos.feature.gfi.value;

import android.os.Parcel;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.util.GosLog;

public class GfiVersion implements Comparable<GfiVersion> {
    private static final String LOG_TAG = "GfiVersion";
    private static final int TRANSACTION_GET_GFI_VERSION = 1126;
    protected static GfiVersion mDefaultVersion = new GfiVersion(1, 1, 0, "release", "null");
    public final String mBranch;
    public final String mChecksum;
    public final int mMajorVersion;
    public final int mMinorVersion;
    public final int mRevision;

    public GfiVersion(int i, int i2, int i3, String str, String str2) {
        this.mMajorVersion = i;
        this.mMinorVersion = i2;
        this.mRevision = i3;
        this.mBranch = str;
        this.mChecksum = str2;
    }

    private static GfiVersion getDefaultVersion() {
        GosLog.d(LOG_TAG, "getDefaultVersion");
        return mDefaultVersion;
    }

    public static GfiVersion versionFromParcel(Parcel parcel) {
        GosLog.d(LOG_TAG, "versionFromParcel");
        if (parcel.dataSize() < 12) {
            GosLog.e(LOG_TAG, "versionFromParcel parcel too small");
            return getDefaultVersion();
        }
        int readInt = parcel.readInt();
        if (readInt != 0) {
            return new GfiVersion(readInt, parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.readString());
        }
        GosLog.e(LOG_TAG, "versionFromParcel major version 0");
        return getDefaultVersion();
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0082  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.samsung.android.game.gos.feature.gfi.value.GfiVersion getVersion() {
        /*
            java.lang.String r0 = "GfiVersion"
            java.lang.String r1 = "getVersion"
            com.samsung.android.game.gos.util.GosLog.d(r0, r1)
            r1 = 0
            com.samsung.android.game.gos.selibrary.SeServiceManager r2 = com.samsung.android.game.gos.selibrary.SeServiceManager.getInstance()     // Catch:{ RemoteException -> 0x0054, NullPointerException -> 0x0052, SecurityException -> 0x0050, all -> 0x004d }
            java.lang.String r3 = "SurfaceFlinger"
            android.os.IBinder r2 = r2.getService(r3)     // Catch:{ RemoteException -> 0x0054, NullPointerException -> 0x0052, SecurityException -> 0x0050, all -> 0x004d }
            android.os.Parcel r3 = android.os.Parcel.obtain()     // Catch:{ RemoteException -> 0x0054, NullPointerException -> 0x0052, SecurityException -> 0x0050, all -> 0x004d }
            android.os.Parcel r1 = android.os.Parcel.obtain()     // Catch:{ RemoteException -> 0x0047, NullPointerException -> 0x0045, SecurityException -> 0x0043, all -> 0x003f }
            java.lang.String r4 = "android.ui.ISurfaceComposer"
            r3.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0047, NullPointerException -> 0x0045, SecurityException -> 0x0043, all -> 0x003f }
            r4 = 1126(0x466, float:1.578E-42)
            r5 = 0
            boolean r2 = r2.transact(r4, r3, r1, r5)     // Catch:{ RemoteException -> 0x0047, NullPointerException -> 0x0045, SecurityException -> 0x0043, all -> 0x003f }
            if (r2 == 0) goto L_0x0037
            com.samsung.android.game.gos.feature.gfi.value.GfiVersion r0 = versionFromParcel(r1)     // Catch:{ RemoteException -> 0x0047, NullPointerException -> 0x0045, SecurityException -> 0x0043, all -> 0x003f }
            if (r3 == 0) goto L_0x0031
            r3.recycle()
        L_0x0031:
            if (r1 == 0) goto L_0x0036
            r1.recycle()
        L_0x0036:
            return r0
        L_0x0037:
            android.os.RemoteException r2 = new android.os.RemoteException     // Catch:{ RemoteException -> 0x0047, NullPointerException -> 0x0045, SecurityException -> 0x0043, all -> 0x003f }
            java.lang.String r4 = "Could not get GFI Version"
            r2.<init>(r4)     // Catch:{ RemoteException -> 0x0047, NullPointerException -> 0x0045, SecurityException -> 0x0043, all -> 0x003f }
            throw r2     // Catch:{ RemoteException -> 0x0047, NullPointerException -> 0x0045, SecurityException -> 0x0043, all -> 0x003f }
        L_0x003f:
            r0 = move-exception
            r2 = r1
            r1 = r3
            goto L_0x007b
        L_0x0043:
            r2 = move-exception
            goto L_0x0048
        L_0x0045:
            r2 = move-exception
            goto L_0x0048
        L_0x0047:
            r2 = move-exception
        L_0x0048:
            r6 = r2
            r2 = r1
            r1 = r3
            r3 = r6
            goto L_0x0057
        L_0x004d:
            r0 = move-exception
            r2 = r1
            goto L_0x007b
        L_0x0050:
            r2 = move-exception
            goto L_0x0055
        L_0x0052:
            r2 = move-exception
            goto L_0x0055
        L_0x0054:
            r2 = move-exception
        L_0x0055:
            r3 = r2
            r2 = r1
        L_0x0057:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x007a }
            r4.<init>()     // Catch:{ all -> 0x007a }
            java.lang.String r5 = "Error when querying GFI version, using default. "
            r4.append(r5)     // Catch:{ all -> 0x007a }
            r4.append(r3)     // Catch:{ all -> 0x007a }
            java.lang.String r3 = r4.toString()     // Catch:{ all -> 0x007a }
            com.samsung.android.game.gos.util.GosLog.d(r0, r3)     // Catch:{ all -> 0x007a }
            com.samsung.android.game.gos.feature.gfi.value.GfiVersion r0 = getDefaultVersion()     // Catch:{ all -> 0x007a }
            if (r1 == 0) goto L_0x0074
            r1.recycle()
        L_0x0074:
            if (r2 == 0) goto L_0x0079
            r2.recycle()
        L_0x0079:
            return r0
        L_0x007a:
            r0 = move-exception
        L_0x007b:
            if (r1 == 0) goto L_0x0080
            r1.recycle()
        L_0x0080:
            if (r2 == 0) goto L_0x0085
            r2.recycle()
        L_0x0085:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.gfi.value.GfiVersion.getVersion():com.samsung.android.game.gos.feature.gfi.value.GfiVersion");
    }

    public String getVersionString() {
        return BuildConfig.VERSION_NAME + this.mMajorVersion + "." + this.mMinorVersion + "." + this.mRevision + " - " + this.mBranch + "\n" + this.mChecksum;
    }

    public String toString() {
        return BuildConfig.VERSION_NAME + this.mMajorVersion + "." + this.mMinorVersion + "." + this.mRevision;
    }

    private class VersionContainer {
        public int major;
        public int minor;
        public int revision;

        private VersionContainer() {
            this.revision = 0;
            this.minor = 1;
            this.major = 1;
        }
    }

    public boolean higherOrEqualThan(String str) {
        GosLog.d(LOG_TAG, "higherOrEqualThan " + str);
        VersionContainer versionContainer = new VersionContainer();
        try {
            parseVersion(str, versionContainer);
            if (this.mMajorVersion < versionContainer.major || this.mMinorVersion < versionContainer.minor || this.mRevision < versionContainer.revision) {
                return false;
            }
            return true;
        } catch (NumberFormatException unused) {
            GosLog.w(LOG_TAG, "higherOrEqualThan passed malformed version string " + str);
            return false;
        }
    }

    public boolean lowerOrEqualThan(String str) {
        GosLog.d(LOG_TAG, "lowerOrEqualThan " + str);
        VersionContainer versionContainer = new VersionContainer();
        try {
            parseVersion(str, versionContainer);
            if (this.mMajorVersion > versionContainer.major || this.mMinorVersion > versionContainer.minor || this.mRevision > versionContainer.revision) {
                return false;
            }
            return true;
        } catch (NumberFormatException unused) {
            GosLog.w(LOG_TAG, "lowerOrEqualThan passed malformed version string " + str);
            return false;
        }
    }

    public int compareTo(GfiVersion gfiVersion) {
        int compare = Integer.compare(this.mMajorVersion, gfiVersion.mMajorVersion);
        if (compare != 0) {
            return compare;
        }
        int compare2 = Integer.compare(this.mMinorVersion, gfiVersion.mMinorVersion);
        if (compare2 != 0) {
            return compare2;
        }
        return Integer.compare(this.mRevision, gfiVersion.mRevision);
    }

    private void parseVersion(String str, VersionContainer versionContainer) throws NumberFormatException {
        String[] split = str.split("\\.");
        versionContainer.major = 1;
        versionContainer.minor = 1;
        versionContainer.revision = 0;
        versionContainer.revision = Integer.parseInt(split[split.length - 1]);
        if (split.length > 1) {
            versionContainer.minor = Integer.parseInt(split[split.length - 2]);
        }
        if (split.length > 2) {
            versionContainer.major = Integer.parseInt(split[split.length - 3]);
        }
        if (split.length > 3) {
            throw new NumberFormatException("Too many numbers in version string");
        }
    }
}
