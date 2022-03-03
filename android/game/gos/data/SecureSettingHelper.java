package com.samsung.android.game.gos.data;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.selibrary.SeSecureSetting;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.SecureSettingConstants;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SecureSettingHelper {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = SecureSettingHelper.class.getSimpleName();
    /* access modifiers changed from: private */
    public static final Map<String, Object> mKeyAndDefMap;
    /* access modifiers changed from: private */
    public final Map<String, Set<ISecureSettingChangeListener>> mSecureSettingChangeListenerMap;
    private final ContentObserver mSecureSettingObserver;

    public static SecureSettingHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void registerListener(String str, ISecureSettingChangeListener iSecureSettingChangeListener) {
        Set set = this.mSecureSettingChangeListenerMap.get(str);
        if (set == null) {
            set = new HashSet();
        }
        set.add(iSecureSettingChangeListener);
        this.mSecureSettingChangeListenerMap.put(str, set);
    }

    public void unregisterListener(String str, ISecureSettingChangeListener iSecureSettingChangeListener) {
        Set set = this.mSecureSettingChangeListenerMap.get(str);
        if (set != null) {
            set.remove(iSecureSettingChangeListener);
            if (set.isEmpty()) {
                this.mSecureSettingChangeListenerMap.remove(str);
            } else {
                this.mSecureSettingChangeListenerMap.put(str, set);
            }
        }
    }

    static {
        HashMap hashMap = new HashMap();
        mKeyAndDefMap = hashMap;
        hashMap.put(SecureSettingConstants.KEY_GAME_BOOSTER_PRIORITY_MODE, 0);
        mKeyAndDefMap.put(SecureSettingConstants.KEY_AUTO_CONTROL, 0);
        mKeyAndDefMap.put(SecureSettingConstants.KEY_ALLOW_MORE_HEAT, Float.valueOf(0.0f));
        mKeyAndDefMap.put(SecureSettingConstants.KEY_REFRESH_RATE_MODE, -1);
        mKeyAndDefMap.put(SecureSettingConstants.KEY_VRR_48HZ_ALLOWED, 0);
        mKeyAndDefMap.put(SecureSettingConstants.KEY_VRR_MAX_HZ, 0);
    }

    public void updateValue(String str) {
        if (mKeyAndDefMap.containsKey(str)) {
            Object doByDefType = doByDefType(str, mKeyAndDefMap.get(str));
            String str2 = LOG_TAG;
            GosLog.d(str2, "updateValue(), " + str + "=" + doByDefType);
        }
    }

    private void updateValues() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : mKeyAndDefMap.entrySet()) {
            String str = (String) next.getKey();
            Object doByDefType = doByDefType(str, next.getValue());
            sb.append(", ");
            sb.append(str);
            sb.append("=");
            sb.append(doByDefType);
        }
        String str2 = LOG_TAG;
        GosLog.d(str2, "updateValues()" + sb);
    }

    private void registerOwnContentObserver() {
        GosLog.d(LOG_TAG, "registerListener()");
        SeSecureSetting seSecureSetting = new SeSecureSetting();
        ContentResolver contentResolver = AppContext.get().getContentResolver();
        for (String uriFor : mKeyAndDefMap.keySet()) {
            contentResolver.registerContentObserver(seSecureSetting.getUriFor(uriFor), false, this.mSecureSettingObserver);
        }
    }

    /* access modifiers changed from: private */
    public Object doByDefType(String str, Object obj) {
        SeSecureSetting seSecureSetting = new SeSecureSetting();
        ContentResolver contentResolver = AppContext.get().getContentResolver();
        PreferenceHelper preferenceHelper = new PreferenceHelper();
        if (obj instanceof Integer) {
            Integer valueOf = Integer.valueOf(seSecureSetting.getInt(contentResolver, str, ((Integer) obj).intValue()));
            preferenceHelper.put(str, valueOf.intValue());
            return valueOf;
        } else if (!(obj instanceof Float)) {
            return null;
        } else {
            Float valueOf2 = Float.valueOf(seSecureSetting.getFloat(contentResolver, str, ((Float) obj).floatValue()));
            preferenceHelper.put(str, valueOf2.floatValue());
            return valueOf2;
        }
    }

    private SecureSettingHelper() {
        this.mSecureSettingChangeListenerMap = new HashMap();
        this.mSecureSettingObserver = new ContentObserver((Handler) null) {
            public boolean deliverSelfNotifications() {
                return true;
            }

            public void onChange(boolean z, Uri uri) {
                String str;
                GosLog.d(SecureSettingHelper.LOG_TAG, "mSecureSettingObserver.onChange(), uri: " + uri);
                if (uri == null || uri.getAuthority() == null || uri.getPath() == null || uri.getLastPathSegment() == null || Settings.Secure.CONTENT_URI == null || Settings.Secure.CONTENT_URI.getAuthority() == null || Settings.Secure.CONTENT_URI.getPath() == null) {
                    GosLog.w(SecureSettingHelper.LOG_TAG, "mSecureSettingObserver.onChange(), uri is null");
                } else if (!uri.getAuthority().equals(Settings.Secure.CONTENT_URI.getAuthority()) || !uri.getPath().startsWith(Settings.Secure.CONTENT_URI.getPath())) {
                    GosLog.w(SecureSettingHelper.LOG_TAG, "mSecureSettingObserver.onChange(), uri is different");
                } else {
                    String lastPathSegment = uri.getLastPathSegment();
                    Object obj = null;
                    if (SecureSettingHelper.mKeyAndDefMap.containsKey(lastPathSegment)) {
                        obj = SecureSettingHelper.this.doByDefType(lastPathSegment, SecureSettingHelper.mKeyAndDefMap.get(lastPathSegment));
                    }
                    Set<ISecureSettingChangeListener> set = (Set) SecureSettingHelper.this.mSecureSettingChangeListenerMap.get(lastPathSegment);
                    if (set != null) {
                        for (ISecureSettingChangeListener onSecureSettingChanged : set) {
                            onSecureSettingChanged.onSecureSettingChanged(lastPathSegment, obj);
                        }
                    }
                    String access$100 = SecureSettingHelper.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("mSecureSettingObserver.onChange(), key=");
                    sb.append(lastPathSegment);
                    sb.append(", value=");
                    sb.append(obj);
                    if (set != null) {
                        str = " was sent to " + set;
                    } else {
                        str = BuildConfig.VERSION_NAME;
                    }
                    sb.append(str);
                    GosLog.d(access$100, sb.toString());
                }
            }
        };
        registerOwnContentObserver();
        updateValues();
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        unRegisterListener();
        super.finalize();
    }

    private void unRegisterListener() {
        AppContext.get().getContentResolver().unregisterContentObserver(this.mSecureSettingObserver);
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final SecureSettingHelper INSTANCE = new SecureSettingHelper();

        private SingletonHolder() {
        }
    }
}
