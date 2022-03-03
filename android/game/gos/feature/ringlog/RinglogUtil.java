package com.samsung.android.game.gos.feature.ringlog;

import com.google.gson.Gson;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.SystemDataHelper;
import com.samsung.android.game.gos.feature.ringlog.TaskSaveSessionData;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.util.ExecutorProvider;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.RinglogConstants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class RinglogUtil {
    private static final String TAG = "RinglogUtil";

    static boolean validateAggr(int i) {
        return i >= 0 && i <= 5;
    }

    static boolean validateRate(long j) {
        return j > 0;
    }

    static String paramListToCsv(List<RinglogConstants.PerfParams> list) {
        if (list == null) {
            return BuildConfig.VERSION_NAME;
        }
        ArrayList arrayList = new ArrayList();
        for (RinglogConstants.PerfParams name : list) {
            arrayList.add(name.getName());
        }
        return String.join(",", arrayList);
    }

    public static RinglogConstants.SessionWrapper getLatestSessionInfo(String str) throws JSONException {
        return getLatestSessionInfo(str, (StringBuilder) null);
    }

    private static RinglogConstants.SessionWrapper getLatestSessionInfo(String str, StringBuilder sb) throws JSONException {
        RinglogConstants.SessionWrapper sessionWrapper = new RinglogConstants.SessionWrapper();
        JSONObject jSONObject = new JSONObject(RinglogInterface.getInstance().getAvailableSessionsJSON(str, "{\"session\":[-1]}"));
        sessionWrapper.id = jSONObject.getInt(GosInterface.KeyName.LATEST_SESSION);
        String num = Integer.toString(sessionWrapper.id);
        if (jSONObject.has(num)) {
            String string = jSONObject.getString(num);
            if (sb != null) {
                sb.append(string);
            }
            sessionWrapper.info = (RinglogConstants.SessionInfo) new Gson().fromJson(string, RinglogConstants.SessionInfo.class);
        }
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getLatestSessionInfo " + sessionWrapper);
        }
        return sessionWrapper;
    }

    static RinglogConstants.SessionWrapper getSessionInfo(String str, int i, StringBuilder sb) throws JSONException {
        RinglogConstants.SessionWrapper sessionWrapper = new RinglogConstants.SessionWrapper();
        String num = Integer.toString(i);
        RinglogInterface instance = RinglogInterface.getInstance();
        JSONObject jSONObject = new JSONObject(instance.getAvailableSessionsJSON(str, "{\"session\":[" + num + "]}"));
        sessionWrapper.id = i;
        if (jSONObject.has(num)) {
            String string = jSONObject.getString(num);
            if (sb != null) {
                sb.append(string);
            }
            sessionWrapper.info = (RinglogConstants.SessionInfo) new Gson().fromJson(string, RinglogConstants.SessionInfo.class);
        }
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getLatestSessionInfo " + sessionWrapper);
        }
        return sessionWrapper;
    }

    static boolean isOngoingSession(RinglogConstants.SessionWrapper sessionWrapper) {
        if (sessionWrapper == null || sessionWrapper.info == null || System.currentTimeMillis() - sessionWrapper.info.data_end_ms >= RinglogConstants.ONGOING_SESSION_TIME_GAP_MS) {
            return false;
        }
        return true;
    }

    static int LCM(int i, int i2) {
        return (i * i2) / GCF(i, i2);
    }

    private static int GCF(int i, int i2) {
        return i2 == 0 ? i : GCF(i2, i % i2);
    }

    public static boolean saveRinglogSessionToDb() {
        GosLog.d(TAG, "saveRinglogSessionToDb()");
        if (!SystemDataHelper.isCollectingAgreedByUser(AppContext.get())) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        try {
            RinglogConstants.SessionWrapper latestSessionInfo = getLatestSessionInfo("com.samsung.android.game.gos", sb);
            if (latestSessionInfo.info == null || latestSessionInfo.info.data_end_ms - latestSessionInfo.info.data_start_ms < 5000) {
                GosLog.w(TAG, "session duration is short or error in session, return, " + latestSessionInfo);
                return false;
            }
            long value = new PreferenceHelper().getValue(PreferenceHelper.PREF_LAST_PAUSED_GAME_TIME_STAMP, -1);
            if (value == -1 || Math.abs(latestSessionInfo.info.data_end_ms - value) >= RinglogConstants.ONGOING_SESSION_TIME_GAP_MS) {
                return false;
            }
            ExecutorProvider.getInstance().getDbExecutor().submit(new TaskSaveSessionData.Builder(latestSessionInfo).setSessionInfoStr(sb.toString()).setLastGamePauseTime(value).build());
            return true;
        } catch (NullPointerException | JSONException e) {
            GosLog.e(TAG, "saveRinglogSessionToDb " + e);
            return false;
        }
    }

    private RinglogUtil() {
    }
}
