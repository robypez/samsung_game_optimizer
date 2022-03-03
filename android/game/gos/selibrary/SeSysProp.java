package com.samsung.android.game.gos.selibrary;

import android.os.SemSystemProperties;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.Constants;

public class SeSysProp {
    private static CpuGpuLevel mCpuGpuLevel;

    public static String getProp(String str) {
        return SemSystemProperties.get(str);
    }

    public static String getPropBigturboEnable() {
        if (AppVariable.isUnitTest()) {
            return "true";
        }
        return getProp("dev.ssrm.turbo");
    }

    static String getSsrmGameLevel() {
        if (AppVariable.isUnitTest()) {
            return "-4,5,-1,2";
        }
        return getProp("dev.ssrm.gamelevel");
    }

    public static String getPropBoard() {
        if (AppVariable.isUnitTest()) {
            return Constants.ChipsetType.universal9810;
        }
        return getProp("ro.product.board");
    }

    public static CpuGpuLevel getCpuGpuLevelInstance() {
        if (mCpuGpuLevel == null) {
            mCpuGpuLevel = new CpuGpuLevel();
        }
        return mCpuGpuLevel;
    }

    private static class CpuGpuLevelHolder {
        private String ssrmGameLevel;

        private CpuGpuLevelHolder() {
            this.ssrmGameLevel = SeSysProp.getSsrmGameLevel();
        }

        /* access modifiers changed from: package-private */
        public String getSsrmGameLevel() {
            return this.ssrmGameLevel;
        }

        /* access modifiers changed from: private */
        public static CpuGpuLevelHolder getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private static class SingletonHolder {
            static CpuGpuLevelHolder INSTANCE = new CpuGpuLevelHolder();

            private SingletonHolder() {
            }
        }
    }

    public static class CpuGpuLevel {
        private static final String LOG_TAG = "CpuGpuLevel";
        private String mCpuLevelsCsv;
        private String mGpuLevelsCsv;
        private int mMaxCpuLevel;
        private int mMaxGpuLevel;
        private int mMinCpuLevel;
        private int mMinGpuLevel;

        private CpuGpuLevel() {
            int[] csvToInts;
            Class<CpuGpuLevel> cls = CpuGpuLevel.class;
            this.mMinCpuLevel = Integer.MIN_VALUE;
            this.mMaxCpuLevel = Integer.MAX_VALUE;
            this.mMinGpuLevel = Integer.MIN_VALUE;
            this.mMaxGpuLevel = Integer.MAX_VALUE;
            this.mCpuLevelsCsv = null;
            this.mGpuLevelsCsv = null;
            String ssrmGameLevel = CpuGpuLevelHolder.getInstance().getSsrmGameLevel();
            GosLog.i(LOG_TAG, cls.getSimpleName() + ", gameLevelFromSSRM: " + ssrmGameLevel);
            if (ssrmGameLevel != null && ssrmGameLevel.length() > 0 && (csvToInts = TypeConverter.csvToInts(ssrmGameLevel)) != null && csvToInts.length >= 4) {
                int i = csvToInts[0];
                this.mMinCpuLevel = i;
                int i2 = csvToInts[1];
                this.mMaxCpuLevel = i2;
                this.mMinGpuLevel = csvToInts[2];
                this.mMaxGpuLevel = csvToInts[3];
                if (i <= i2) {
                    int i3 = (i2 - i) + 1;
                    int[] iArr = new int[i3];
                    for (int i4 = 0; i4 < i3; i4++) {
                        iArr[i4] = this.mMinCpuLevel + i4;
                    }
                    this.mCpuLevelsCsv = TypeConverter.intsToCsv(iArr);
                }
                int i5 = this.mMinGpuLevel;
                int i6 = this.mMaxGpuLevel;
                if (i5 <= i6) {
                    int i7 = (i6 - i5) + 1;
                    int[] iArr2 = new int[i7];
                    for (int i8 = 0; i8 < i7; i8++) {
                        iArr2[i8] = this.mMinGpuLevel + i8;
                    }
                    this.mGpuLevelsCsv = TypeConverter.intsToCsv(iArr2);
                }
            }
            String originalDeviceName = AppVariable.getOriginalDeviceName();
            if (originalDeviceName != null && (originalDeviceName.startsWith("grace") || originalDeviceName.startsWith("SC-01J") || originalDeviceName.startsWith("SCV34") || originalDeviceName.startsWith("hero") || originalDeviceName.startsWith("poseidon") || originalDeviceName.startsWith("SC-02H") || originalDeviceName.startsWith("SCV33"))) {
                GosLog.i(LOG_TAG, cls.getSimpleName() + ", uses hard-coded values");
                this.mMinCpuLevel = -4;
                this.mMaxCpuLevel = 5;
                this.mMinGpuLevel = -1;
                this.mMaxGpuLevel = 2;
                this.mCpuLevelsCsv = "-4,-3,-2,-1,0,1,2,3,4,5";
                this.mGpuLevelsCsv = "-1,0,1,2";
            }
            GosLog.i(LOG_TAG, cls.getSimpleName() + ", mMinCpuLevel: " + this.mMinCpuLevel + ", mMaxCpuLevel: " + this.mMaxCpuLevel + ", mMinGpuLevel: " + this.mMinGpuLevel + ", mMaxGpuLevel: " + this.mMaxGpuLevel + ", mCpuLevelsCsv: " + this.mCpuLevelsCsv + ", mGpuLevelsCsv: " + this.mGpuLevelsCsv);
        }

        public int getMinCpuLevel() {
            return this.mMinCpuLevel;
        }

        public int getMaxCpuLevel() {
            return this.mMaxCpuLevel;
        }

        public int getMinGpuLevel() {
            return this.mMinGpuLevel;
        }

        public int getMaxGpuLevel() {
            return this.mMaxGpuLevel;
        }

        public String getCpuLevelsCsv() {
            return this.mCpuLevelsCsv;
        }

        public String getGpuLevelsCsv() {
            return this.mGpuLevelsCsv;
        }

        public int convertToValidCpuLevel(int i) {
            int minCpuLevel = getMinCpuLevel();
            int maxCpuLevel = getMaxCpuLevel();
            if (i < minCpuLevel) {
                i = minCpuLevel;
            }
            return i > maxCpuLevel ? maxCpuLevel : i;
        }

        public int convertToValidGpuLevel(int i) {
            int minGpuLevel = getMinGpuLevel();
            int maxGpuLevel = getMaxGpuLevel();
            if (i < minGpuLevel) {
                i = minGpuLevel;
            }
            return i > maxGpuLevel ? maxGpuLevel : i;
        }
    }
}
