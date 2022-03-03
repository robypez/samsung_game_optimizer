package com.samsung.android.game.gos.data;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.type.ReportData;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class GameInfoCollector {
    private static final long BYTE_TO_MB_DIVISION_FACTOR = 1048576;
    private static final String LOG_TAG = "GameInfoCollector";
    private ApplicationInfo mAppInfo = null;
    private Context mContext = null;
    private String mPkgName = null;
    private ArrayList<String> mSOFileList = null;
    private long mVersionCode = -1;
    private String mVersionName = null;

    public GameInfoCollector(Context context, String str) {
        this.mContext = context;
        this.mPkgName = str;
        try {
            this.mAppInfo = context.getPackageManager().getApplicationInfo(this.mPkgName, 0);
        } catch (Exception e) {
            GosLog.e(LOG_TAG, e.getMessage());
        }
        try {
            PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(this.mPkgName, 0);
            this.mVersionName = packageInfo.versionName;
            this.mVersionCode = packageInfo.getLongVersionCode();
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
        }
        GosLog.d(LOG_TAG, " Version Name : " + this.mVersionName);
        GosLog.d(LOG_TAG, " Version Code : " + this.mVersionCode);
    }

    private String getGameEngineCSV() {
        if (this.mAppInfo != null) {
            this.mSOFileList = new ArrayList<>();
            ArrayList<String> gameEngineList = getGameEngineList(this.mAppInfo.dataDir + "/lib");
            this.mSOFileList = gameEngineList;
            if (gameEngineList.isEmpty()) {
                this.mSOFileList = getGameEngineList(this.mAppInfo.nativeLibraryDir);
            }
        }
        String stringsToCsv = TypeConverter.stringsToCsv((Iterable<String>) this.mSOFileList);
        GosLog.d(LOG_TAG, " GameEngineNameCSV : " + stringsToCsv);
        return stringsToCsv;
    }

    private ArrayList<String> getGameEngineList(String str) {
        File[] listFiles;
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            File file = new File(str);
            if (!file.isDirectory() || (listFiles = file.listFiles()) == null) {
                return arrayList;
            }
            for (File file2 : listFiles) {
                String name = file2.getName();
                if (file2.isDirectory()) {
                    arrayList.addAll(getGameEngineList(name));
                } else if (file2.isFile()) {
                    arrayList.add(name);
                }
            }
            return arrayList;
        } catch (Exception e) {
            GosLog.e(LOG_TAG, "file exception", e);
            return arrayList;
        }
    }

    public String getVersionName() {
        return this.mVersionName;
    }

    public long getVersionCode() {
        return this.mVersionCode;
    }

    private long getAPKSize() {
        long length = this.mAppInfo != null ? new File(this.mAppInfo.sourceDir).length() / BYTE_TO_MB_DIVISION_FACTOR : -1;
        GosLog.d(LOG_TAG, "File size " + length);
        return length;
    }

    public String getGameInfoJsonMsg(int i) {
        String str = this.mPkgName;
        if (str == null || str.length() <= 0) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        String gameEngineCSV = getGameEngineCSV();
        String versionName = getVersionName();
        long versionCode = getVersionCode();
        long aPKSize = getAPKSize();
        long currentTimeMillis = System.currentTimeMillis();
        try {
            jSONObject.put("package_name", this.mPkgName);
            if (i >= 0 && i <= 4) {
                jSONObject.put(ReportData.GameInfoKey.UPDATE_TYPE, i);
            }
            if (gameEngineCSV != null) {
                jSONObject.put(ReportData.GameInfoKey.GAME_ENGINE_CSV, gameEngineCSV);
            }
            if (versionName != null) {
                jSONObject.put("version_name", versionName);
            }
            if (versionCode != 0) {
                jSONObject.put("version_code", versionCode);
            }
            if (aPKSize != 0) {
                jSONObject.put(ReportData.GameInfoKey.APK_SIZE, aPKSize);
            }
            if (currentTimeMillis >= 0) {
                jSONObject.put("reporting_time", currentTimeMillis);
            }
            jSONObject.put(ReportData.GameInfoKey.INSTALLER_PACKAGE_NAME, getInstallerPackageName(this.mPkgName));
        } catch (JSONException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        return jSONObject.toString();
    }

    private String getInstallerPackageName(String str) {
        try {
            PackageManager packageManager = AppContext.get().getPackageManager();
            if (packageManager != null) {
                return packageManager.getInstallerPackageName(str);
            }
            return null;
        } catch (Exception e) {
            GosLog.w(LOG_TAG, "getInstallerPackageName(), Getting installerPackageName failed.", e);
            return null;
        }
    }
}
