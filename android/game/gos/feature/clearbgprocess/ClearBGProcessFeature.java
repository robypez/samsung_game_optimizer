package com.samsung.android.game.gos.feature.clearbgprocess;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SemSystemProperties;
import com.samsung.android.app.SemMultiWindowManager;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PackageUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClearBGProcessFeature implements RuntimeInterface {
    private static final String LOG_TAG = "ClearBGProcessFeature";
    static final int MARS_CLEAR_BG_MAX_PROTECT_NUM = 6;
    private static final int MSG_CLEARBG_END = 20005;
    private static final int MSG_CLEARBG_START = 20004;
    private static final String[] SURVIVE_APP = {"com.samsung.android.calendar", "com.samsung.android.app.reminder", "com.samsung.android.spay", "com.samsung.android.spaymini", "com.samsung.android.rajaampat", "com.samsung.android.brdigitalaccount", "com.samsung.android.brdigitalaccount.swipe", "com.samsung.android.brdigitalaccount.swipelite", "com.samsung.android.voc", "com.android.vending", "com.sec.android.app.clockpackage"};
    private String mCurrentPkg;
    private InnerClassHandler mHandler;
    private int mLruNum;
    private SemMultiWindowManager mMultiWindowManager;
    private ArrayList<String> mSurviveAppList;
    private Map<String, Integer> mSurviveAppMap;

    public String getName() {
        return Constants.V4FeatureFlag.CLEAR_BG_PROCESS;
    }

    public void setLRU_num(int i) {
        if (i < 1) {
            i = 1;
        } else if (i > 6) {
            i = 6;
        }
        this.mLruNum = i;
        GosLog.i(LOG_TAG, "setLRU_num mLruNum=" + this.mLruNum);
        DbHelper.getInstance().getGlobalDao().setClearBGLruNum(new Global.IdAndClearBGLruNum(this.mLruNum));
    }

    public int getLRU_Num() {
        return this.mLruNum;
    }

    public void setSurviveList(ArrayList<String> arrayList, String str) {
        DbHelper.getInstance().getClearBGSurviveAppsDao().setSurviveList(str, arrayList);
        reloadSurviveAppList();
    }

    /* access modifiers changed from: package-private */
    public Map<String, Integer> loadSurviveMap() {
        HashMap hashMap = new HashMap();
        String clearBGSurviveAppFromServer = DbHelper.getInstance().getGlobalDao().getClearBGSurviveAppFromServer();
        GosLog.i(LOG_TAG, "loadSurviveMap apps=" + clearBGSurviveAppFromServer);
        List<String> csvToStringList = TypeConverter.csvToStringList(clearBGSurviveAppFromServer);
        if (csvToStringList != null) {
            for (String put : csvToStringList) {
                hashMap.put(put, -1);
            }
        }
        for (String put2 : DbHelper.getInstance().getClearBGSurviveAppsDao().getSurviveAppList()) {
            hashMap.put(put2, -1);
        }
        for (String put3 : SURVIVE_APP) {
            hashMap.put(put3, -1);
        }
        return hashMap;
    }

    private void updateAppList() {
        this.mSurviveAppList = new ArrayList<>();
        for (Map.Entry next : this.mSurviveAppMap.entrySet()) {
            String str = (String) next.getKey();
            Integer num = (Integer) next.getValue();
            if (!(num == null || num.intValue() == -1)) {
                ArrayList<String> arrayList = this.mSurviveAppList;
                arrayList.add(str + ", " + num);
            }
        }
        GosLog.i(LOG_TAG, "updateSurviveList mSurviveAppList=" + this.mSurviveAppList.toString());
    }

    private boolean addToAppMap(String str) {
        if (this.mSurviveAppMap.containsKey(str)) {
            return false;
        }
        this.mSurviveAppMap.put(str, Integer.valueOf(PackageUtil.getPkgUid(str)));
        updateAppList();
        return true;
    }

    private void rmFromAppMap(String str) {
        if (this.mSurviveAppMap.containsKey(str)) {
            this.mSurviveAppMap.remove(str);
            updateAppList();
        }
    }

    public void deleteFromAppMap(String str) {
        if (this.mSurviveAppMap.containsKey(str)) {
            this.mSurviveAppMap.put(str, -1);
            updateAppList();
        }
    }

    public void resetAppMap(String str) {
        if (this.mSurviveAppMap.containsKey(str)) {
            this.mSurviveAppMap.put(str, Integer.valueOf(PackageUtil.getPkgUid(str)));
            updateAppList();
        }
    }

    private void loadSurviveAppList() {
        Map<String, Integer> loadSurviveMap = loadSurviveMap();
        this.mSurviveAppMap = loadSurviveMap;
        PackageUtil.setUidForPkgMap(loadSurviveMap);
        updateAppList();
    }

    public void reloadSurviveAppList() {
        Map<String, Integer> map = this.mSurviveAppMap;
        this.mSurviveAppMap = loadSurviveMap();
        HashMap hashMap = new HashMap();
        for (String next : this.mSurviveAppMap.keySet()) {
            if (map == null || !map.containsKey(next)) {
                hashMap.put(next, -1);
            } else {
                Integer num = map.get(next);
                if (!(num == null || num.intValue() == -1)) {
                    this.mSurviveAppMap.put(next, map.get(next));
                }
            }
        }
        if (hashMap.size() > 0) {
            PackageUtil.setUidForPkgMap(hashMap);
            for (Map.Entry entry : hashMap.entrySet()) {
                this.mSurviveAppMap.put(entry.getKey(), entry.getValue());
            }
        }
        updateAppList();
    }

    private ClearBGProcessFeature() {
        this.mSurviveAppList = null;
        this.mSurviveAppMap = null;
        this.mCurrentPkg = null;
        this.mLruNum = 1;
        this.mHandler = new InnerClassHandler(this);
        this.mMultiWindowManager = new SemMultiWindowManager();
        this.mLruNum = DbHelper.getInstance().getGlobalDao().getClearBGLruNum();
        GosLog.i(LOG_TAG, "mLruNum=" + this.mLruNum);
        loadSurviveAppList();
    }

    public static ClearBGProcessFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean isAvailableForSystemHelper() {
        String prop = SeSysProp.getProp("sys.config.mars.game_policy");
        if (Build.VERSION.SDK_INT > 29) {
            if (!"false".equals(prop)) {
                return true;
            }
        } else if ("CHINA".equalsIgnoreCase(SemSystemProperties.getCountryCode()) || "true".equals(prop)) {
            return true;
        }
        return false;
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        GosLog.i(LOG_TAG, "onResume");
        if (this.mHandler != null) {
            GosLog.i(LOG_TAG, "prepare send the clearbg msg ");
            if (!PackageUtil.isTopPackage(AppContext.get(), pkgData.getPackageName())) {
                GosLog.i(LOG_TAG, "return, wrong top package, not send MSG_CLEARBG_START");
                return;
            }
            int mode = this.mMultiWindowManager.getMode();
            if (mode != 0) {
                GosLog.i(LOG_TAG, "return, not send MSG_CLEARBG_START, getMode = " + mode);
                return;
            }
            this.mCurrentPkg = pkgData.getPackageName();
            this.mHandler.sendEmptyMessage(MSG_CLEARBG_START);
        }
    }

    public void onFocusOut(PkgData pkgData) {
        GosLog.i(LOG_TAG, "onPause ");
        endClearBG();
    }

    public void restoreDefault(PkgData pkgData) {
        GosLog.i(LOG_TAG, "restoreDefault ");
        endClearBG();
    }

    private void endClearBG() {
        InnerClassHandler innerClassHandler = this.mHandler;
        if (innerClassHandler != null) {
            innerClassHandler.removeMessages(MSG_CLEARBG_START);
            this.mHandler.sendEmptyMessageDelayed(MSG_CLEARBG_END, 0);
        }
    }

    private static class SingletonHolder {
        public static ClearBGProcessFeature INSTANCE = new ClearBGProcessFeature();

        private SingletonHolder() {
        }
    }

    private void setClearBGProcess() {
        int mode = this.mMultiWindowManager.getMode();
        if (mode != 0) {
            GosLog.i(LOG_TAG, "return, cancel setClearBGProcess, getMode = " + mode);
            return;
        }
        boolean addToAppMap = addToAppMap(this.mCurrentPkg);
        new PreferenceHelper().put(PreferenceHelper.PREF_CLEAR_BG_PROCESS_DONE, true);
        Intent intent = new Intent();
        intent.setAction("com.android.server.am.MARS_TRIGGER_GAME_MODE_POLICY");
        intent.putExtra("package", "com.samsung.android.game.gos");
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("survive_app", this.mSurviveAppList);
        bundle.putInt(GosInterface.KeyName.LRU_NUM, this.mLruNum);
        intent.putExtras(bundle);
        intent.setPackage("android");
        AppContext.get().sendBroadcast(intent);
        if (addToAppMap) {
            rmFromAppMap(this.mCurrentPkg);
        }
    }

    private void cancelClearBGProcess() {
        PreferenceHelper preferenceHelper = new PreferenceHelper();
        if (preferenceHelper.getValue(PreferenceHelper.PREF_CLEAR_BG_PROCESS_DONE, false)) {
            preferenceHelper.put(PreferenceHelper.PREF_CLEAR_BG_PROCESS_DONE, false);
            Intent intent = new Intent();
            intent.setAction("com.android.server.am.MARS_CANCEL_GAME_MODE_POLICY");
            intent.putExtra("package", "com.samsung.android.game.gos");
            intent.setPackage("android");
            AppContext.get().sendBroadcast(intent);
        }
    }

    /* access modifiers changed from: private */
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == MSG_CLEARBG_START) {
            setClearBGProcess();
        } else if (i == MSG_CLEARBG_END) {
            cancelClearBGProcess();
        }
    }

    private static class InnerClassHandler extends Handler {
        private final WeakReference<ClearBGProcessFeature> mClearBGFeature;

        InnerClassHandler(ClearBGProcessFeature clearBGProcessFeature) {
            super(Looper.getMainLooper());
            this.mClearBGFeature = new WeakReference<>(clearBGProcessFeature);
        }

        public void handleMessage(Message message) {
            ClearBGProcessFeature clearBGProcessFeature = (ClearBGProcessFeature) this.mClearBGFeature.get();
            if (clearBGProcessFeature != null) {
                clearBGProcessFeature.handleMessage(message);
            }
        }
    }
}
