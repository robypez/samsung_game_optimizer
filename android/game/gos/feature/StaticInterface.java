package com.samsung.android.game.gos.feature;

import com.samsung.android.game.SemPackageConfiguration;
import com.samsung.android.game.gos.data.PkgData;
import org.json.JSONObject;

public interface StaticInterface extends CommonInterface {
    SemPackageConfiguration getDefaultConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration, JSONObject jSONObject);

    SemPackageConfiguration getUpdatedConfig(PkgData pkgData, SemPackageConfiguration semPackageConfiguration, JSONObject jSONObject);
}
