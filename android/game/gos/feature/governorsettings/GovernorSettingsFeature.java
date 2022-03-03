package com.samsung.android.game.gos.feature.governorsettings;

import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.SemPackageConfiguration;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.feature.StaticInterface;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class GovernorSettingsFeature implements StaticInterface {
    private static final String LOG_TAG = "GovernorSettingsCore";

    public String getName() {
        return Constants.V4FeatureFlag.GOVERNOR_SETTINGS;
    }

    public boolean isAvailableForSystemHelper() {
        return true;
    }

    public static GovernorSettingsFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private GovernorSettingsFeature() {
    }

    public SemPackageConfiguration getUpdatedConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration, JSONObject jSONObject) {
        String governorSettings = pkgData.getPkg().getGovernorSettings();
        if (governorSettings == null) {
            governorSettings = DbHelper.getInstance().getGlobalDao().getGovernorSettings();
            GosLog.i(LOG_TAG, "no package governorSettings. use global one. " + governorSettings);
        }
        try {
            jSONObject.put(ManagerInterface.KeyName.GOVERNOR_SETTING, governorSettings);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return semPackageConfiguration;
    }

    public SemPackageConfiguration getDefaultConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration, JSONObject jSONObject) {
        try {
            jSONObject.put(ManagerInterface.KeyName.GOVERNOR_SETTING, (Object) null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return semPackageConfiguration;
    }

    private static class SingletonHolder {
        public static GovernorSettingsFeature INSTANCE = new GovernorSettingsFeature();

        private SingletonHolder() {
        }
    }
}
