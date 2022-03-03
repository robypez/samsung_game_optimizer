package com.samsung.android.game.gos.feature.ipm;

import com.samsung.android.game.gos.feature.gfi.GfiFeature;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicy;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicyException;
import com.samsung.android.game.gos.feature.gfi.value.GfiVersion;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.ipm.FrameInterpolator;
import com.samsung.android.game.gos.util.GosLog;
import org.json.JSONException;
import org.json.JSONObject;

public class GosFrameInterpolator implements FrameInterpolator {
    private static final GfiVersion FIRST_GFI_VERSION_NOT_USING_DFS = new GfiVersion(1, 4, 0, BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME);
    private static final String LOG_TAG = "FrameInterpolator";
    private final GfiFeature mGfiFeature;
    private final JSONObject mGfiPolicy = new JSONObject();

    public GosFrameInterpolator(GfiFeature gfiFeature) {
        this.mGfiFeature = gfiFeature;
    }

    public boolean enable(float f, float f2, String str, int i) {
        try {
            this.mGfiPolicy.put("enabled", true);
            this.mGfiPolicy.put(GfiPolicy.KEY_TARGET_DFS, (double) f);
            this.mGfiPolicy.put(GfiPolicy.KEY_INTERPOLATION_FPS, (double) f2);
            return this.mGfiFeature.setInterpolation(str, i, this.mGfiPolicy.toString());
        } catch (GfiPolicyException | JSONException e) {
            GosLog.e(LOG_TAG, "Failed to enable GFI: " + e);
            return false;
        }
    }

    public boolean isUsingDfs() {
        return this.mGfiFeature.getVersion().compareTo(FIRST_GFI_VERSION_NOT_USING_DFS) < 0;
    }
}
