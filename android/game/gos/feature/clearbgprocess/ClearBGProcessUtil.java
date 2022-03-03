package com.samsung.android.game.gos.feature.clearbgprocess;

import com.samsung.android.game.gos.data.dao.ClearBGSurviveAppsDao;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClearBGProcessUtil {
    private static final String LOG_TAG = ClearBGProcessUtil.class.getSimpleName();

    private ClearBGProcessUtil() {
    }

    public static String setLRU_num(String str) {
        JSONObject jSONObject = new JSONObject();
        if (str == null) {
            try {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                jSONObject.put(GosInterface.KeyName.COMMENT, "Invalid parameters.");
                GosLog.e(LOG_TAG, "setLRU_num invalid request");
                return jSONObject.toString();
            } catch (JSONException e) {
                String str2 = LOG_TAG;
                GosLog.e(str2, "Exception during setLRU_num " + e.getMessage());
            }
        } else {
            int optInt = new JSONObject(str).optInt(GosInterface.KeyName.LRU_NUM, 0);
            ClearBGProcessFeature.getInstance().setLRU_num(optInt);
            String str3 = LOG_TAG;
            GosLog.i(str3, "setLRU_num, lru_num=" + optInt);
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, true);
            return jSONObject.toString();
        }
    }

    public static String setSurviveList(String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        if (str == null) {
            try {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                jSONObject.put(GosInterface.KeyName.COMMENT, "Invalid parameters.");
                GosLog.e(LOG_TAG, "setSurviveList invalid request");
                return jSONObject.toString();
            } catch (Exception e) {
                String str3 = LOG_TAG;
                GosLog.e(str3, "Exception during setSurviveList " + e.getMessage());
            }
        } else {
            JSONArray jSONArray = new JSONObject(str).getJSONArray(GosInterface.KeyName.SURVIVE_LIST);
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jSONArray.getString(i));
            }
            ClearBGProcessFeature.getInstance().setSurviveList(arrayList, str2);
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, true);
            return jSONObject.toString();
        }
    }

    public static void observerRemovePkg(String str) {
        ClearBGProcessFeature.getInstance().deleteFromAppMap(str);
        ClearBGSurviveAppsDao clearBGSurviveAppsDao = DbHelper.getInstance().getClearBGSurviveAppsDao();
        Map<String, Integer> loadSurviveMap = ClearBGProcessFeature.getInstance().loadSurviveMap();
        clearBGSurviveAppsDao.deleteWithCallerPkgName(str);
        Map<String, Integer> loadSurviveMap2 = ClearBGProcessFeature.getInstance().loadSurviveMap();
        if (loadSurviveMap2 != null && loadSurviveMap != null) {
            for (String next : loadSurviveMap.keySet()) {
                if (!loadSurviveMap2.containsKey(next)) {
                    ClearBGProcessFeature.getInstance().deleteFromAppMap(next);
                }
            }
        }
    }

    public static void observerInstallPkg(String str) {
        ClearBGProcessFeature.getInstance().resetAppMap(str);
    }

    public static String getJsonForTest() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(GosInterface.KeyName.LRU_NUM, 5);
            return jSONObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getJsonForSetSurviveAppTest() {
        try {
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            jSONArray.put("com.netease.newsreader.activity");
            jSONArray.put("com.happyelements.AndroidAnimal.qq");
            jSONObject.put(GosInterface.KeyName.SURVIVE_LIST, jSONArray);
            return jSONObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
