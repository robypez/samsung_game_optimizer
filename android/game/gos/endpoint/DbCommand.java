package com.samsung.android.game.gos.endpoint;

import android.app.Application;
import android.util.Base64;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.database.GosDatabase;
import com.samsung.android.game.gos.data.dbhelper.ReportDbHelper;
import com.samsung.android.game.gos.data.type.ReportData;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.util.FileUtil;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class DbCommand {
    private static final String LOG_TAG = DbCommand.class.getSimpleName();
    static final int MAX_BYTE_FOR_TRANSACTION = 262144;
    private static final int MAX_SIZE = 1024;
    private static String sEncodedDatabase = BuildConfig.VERSION_NAME;
    private static String sReport = null;

    DbCommand() {
    }

    /* access modifiers changed from: package-private */
    public String getReport(String str) {
        String str2;
        String str3 = LOG_TAG;
        GosLog.i(str3, "getReport(), jsonParam: " + str);
        JSONObject jSONObject = new JSONObject();
        if (str == null) {
            try {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                jSONObject.put(GosInterface.KeyName.COMMENT, "Your jsonParam is null.");
                return jSONObject.toString();
            } catch (JSONException e) {
                String str4 = LOG_TAG;
                GosLog.w(str4, "getReport(), " + e);
            }
        } else {
            JSONObject jSONObject2 = new JSONObject(str);
            int i = jSONObject2.getInt(GosInterface.KeyName.INDEX);
            String str5 = LOG_TAG;
            GosLog.d(str5, "getReport(), index : " + i);
            if (i == 0) {
                makeReport(jSONObject2);
            }
            if (sReport != null) {
                if (!sReport.isEmpty()) {
                    int length = ((sReport.length() - 1) / 262144) + 1;
                    jSONObject.put("string_array_length", length);
                    String str6 = LOG_TAG;
                    GosLog.d(str6, "getReport(), stringArrayLength: " + length);
                    int i2 = (i + 1) * 262144;
                    if (sReport.length() > i2) {
                        str2 = sReport.substring(262144 * i, i2);
                    } else {
                        str2 = sReport.substring(262144 * i);
                        sReport = null;
                    }
                    jSONObject.put("response_index", i);
                    String str7 = LOG_TAG;
                    GosLog.d(str7, "getReport(), index: " + i);
                    jSONObject.put("contents", str2);
                    String str8 = LOG_TAG;
                    GosLog.d(str8, "getReport(), contents.length(): " + str2.length());
                    return jSONObject.toString();
                }
            }
            return jSONObject.toString();
        }
    }

    private void makeReport(JSONObject jSONObject) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        long j = -1;
        long j2 = jSONObject.has(GosInterface.KeyName.BEGIN_REPORTING_TIME) ? jSONObject.getLong(GosInterface.KeyName.BEGIN_REPORTING_TIME) : -1;
        if (jSONObject.has(GosInterface.KeyName.END_REPORTING_TIME)) {
            j = jSONObject.getLong(GosInterface.KeyName.END_REPORTING_TIME);
        }
        String str = LOG_TAG;
        GosLog.d(str, "makeReport(), beginTime : " + j2 + ", endTime : " + j);
        List<ReportData> combinationReportData = ReportDbHelper.getInstance().getCombinationReportData(jSONObject.getString(GosInterface.KeyName.REPORT_TAG));
        ArrayList arrayList = new ArrayList();
        for (ReportData msg : combinationReportData) {
            String msg2 = msg.getMsg();
            if (msg2 != null) {
                JSONObject jSONObject2 = new JSONObject(msg2);
                if (jSONObject2.has("reporting_time")) {
                    try {
                        long j3 = jSONObject2.getLong("reporting_time");
                        if ((j2 < 0 || j3 >= j2) && (j < 0 || j3 <= j)) {
                            jSONArray.put(msg2);
                        }
                    } catch (JSONException unused) {
                        arrayList.add(jSONObject2.toString());
                    }
                } else {
                    jSONArray.put(msg2);
                }
            }
        }
        if (!arrayList.isEmpty()) {
            String str2 = LOG_TAG;
            GosLog.w(str2, "These JSONs have issues with reporting_time: " + arrayList.toString());
        }
        sReport = jSONArray.toString();
        String str3 = LOG_TAG;
        GosLog.d(str3, "makeReport(), length : " + jSONArray.length() + ", sReport : " + sReport);
    }

    /* access modifiers changed from: package-private */
    public String getEncodedDatabase(String str) {
        String str2;
        JSONObject jSONObject = new JSONObject();
        try {
            int i = new JSONObject(str).getInt(GosInterface.KeyName.INDEX);
            if (i == 0) {
                makeEncodedDatabase();
            }
            if (sEncodedDatabase != null) {
                if (!sEncodedDatabase.isEmpty()) {
                    int length = ((sEncodedDatabase.length() - 1) / 262144) + 1;
                    jSONObject.put("string_array_length", length);
                    String str3 = LOG_TAG;
                    GosLog.d(str3, "getEncodedDatabase(), stringArrayLength: " + length);
                    int i2 = (i + 1) * 262144;
                    if (sEncodedDatabase.length() > i2) {
                        str2 = sEncodedDatabase.substring(262144 * i, i2);
                    } else {
                        str2 = sEncodedDatabase.substring(262144 * i);
                        sEncodedDatabase = null;
                    }
                    jSONObject.put("response_index", i);
                    String str4 = LOG_TAG;
                    GosLog.d(str4, "getEncodedDatabase(), index: " + i);
                    jSONObject.put("contents", str2);
                    String str5 = LOG_TAG;
                    GosLog.d(str5, "getEncodedDatabase(), contents.length(): " + str2.length());
                    return jSONObject.toString();
                }
            }
            return jSONObject.toString();
        } catch (IndexOutOfBoundsException | JSONException e) {
            GosLog.w(LOG_TAG, e);
        }
    }

    private void makeEncodedDatabase() {
        Application application = AppContext.get();
        String[] strArr = {application.getDatabasePath(GosDatabase.GOS_DATABASE_FILE_DB).getAbsolutePath(), application.getDatabasePath(GosDatabase.GOS_DATABASE_FILE_DB_SHM).getAbsolutePath(), application.getDatabasePath(GosDatabase.GOS_DATABASE_FILE_DB_WAL).getAbsolutePath()};
        byte[] bArr = new byte[1024];
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream((application.getDataDir().getPath() + "/databases") + "/Databases.zip"));
            for (int i = 0; i < 3; i++) {
                String str = strArr[i];
                FileInputStream fileInputStream = new FileInputStream(str);
                zipOutputStream.putNextEntry(new ZipEntry(str.substring(str.lastIndexOf(47) + 1)));
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read <= 0) {
                        break;
                    }
                    zipOutputStream.write(bArr, 0, read);
                }
                zipOutputStream.closeEntry();
                fileInputStream.close();
            }
            zipOutputStream.close();
        } catch (IOException e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
        File databasePath = application.getDatabasePath("Databases.zip");
        if (databasePath != null) {
            GosLog.d(LOG_TAG, "makeEncodedDatabase(), roomFile path: " + databasePath.getAbsolutePath());
            try {
                byte[] compressBytes = TypeConverter.compressBytes(FileUtil.getBytes(databasePath));
                if (compressBytes != null) {
                    sEncodedDatabase = Base64.encodeToString(compressBytes, 2);
                    GosLog.d(LOG_TAG, "makeEncodedDatabase(), encoded string length: " + sEncodedDatabase.length());
                } else {
                    GosLog.w(LOG_TAG, "makeEncodedDatabase(), compressed bytes are null.");
                }
            } catch (IOException e2) {
                GosLog.w(LOG_TAG, (Throwable) e2);
            } catch (Throwable th) {
                th.printStackTrace();
            }
            databasePath.delete();
        }
        if (!AppVariable.isUnitTest()) {
            IpmCore instance = IpmCore.getInstance(application);
            String encodedRinglog = instance.getEncodedRinglog();
            if (encodedRinglog != null) {
                sEncodedDatabase += "\nIPM:\n" + encodedRinglog;
            }
            String readableRinglog = instance.getReadableRinglog();
            if (readableRinglog != null) {
                sEncodedDatabase += "\n\nRingLog:\n" + readableRinglog;
            }
        }
        sEncodedDatabase += "\n\nROOM.zip";
    }

    /* access modifiers changed from: package-private */
    public void emptySReport() {
        sReport = null;
    }

    /* access modifiers changed from: package-private */
    public void emptySEncodedDatabse() {
        sEncodedDatabase = null;
    }
}
