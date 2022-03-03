package com.samsung.android.game.gos.feature.touchboost;

import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.feature.RuntimeInterface;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.os.SemPerfManager;

public class TouchBoostFeature implements RuntimeInterface {
    private static final String LOG_TAG = "TouchBoostFeature";
    private TouchBoost mTouchBoost;

    public String getName() {
        return Constants.V4FeatureFlag.BOOST_TOUCH;
    }

    private TouchBoostFeature() {
        this.mTouchBoost = new TouchBoost();
    }

    public static TouchBoostFeature getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onFocusIn(PkgData pkgData, boolean z) {
        GosLog.v(LOG_TAG, "onResume. " + pkgData.getPackageName());
        String touchBoostCommand = this.mTouchBoost.getTouchBoostCommand(pkgData);
        GosLog.i(LOG_TAG, "onResume. invoke sendCommandToSsrm(COMMAND_GAME_TOUCH_BOOSTER), value: " + touchBoostCommand);
        SemPerfManager.sendCommandToSsrm("GAME_TOUCH_BOOSTER", touchBoostCommand);
    }

    public void onFocusOut(PkgData pkgData) {
        GosLog.i(LOG_TAG, "onPause. invoke sendCommandToSsrm(COMMAND_GAME_TOUCH_BOOSTER, VALUE_GAME_TOUCH_BOOSTER_OFF)");
        SemPerfManager.sendCommandToSsrm("GAME_TOUCH_BOOSTER", "off_game_touch_booster");
    }

    public void restoreDefault(PkgData pkgData) {
        GosLog.i(LOG_TAG, "restoreDefault. invoke sendCommandToSsrm(COMMAND_GAME_TOUCH_BOOSTER, VALUE_GAME_TOUCH_BOOSTER_OFF)");
        SemPerfManager.sendCommandToSsrm("GAME_TOUCH_BOOSTER", "off_game_touch_booster");
    }

    public boolean isAvailableForSystemHelper() {
        String propBoard = SeSysProp.getPropBoard();
        return propBoard.equals(Constants.ChipsetType.MSM8996) || propBoard.equals(Constants.ChipsetType.universal8890) || propBoard.equals(Constants.ChipsetType.universal7420) || propBoard.equals(Constants.ChipsetType.universal8895) || propBoard.equals(Constants.ChipsetType.universal9810);
    }

    public String getTouchBoostCommand(PkgData pkgData) {
        return this.mTouchBoost.getTouchBoostCommand(pkgData);
    }

    private static class SingletonHolder {
        public static TouchBoostFeature INSTANCE = new TouchBoostFeature();

        private SingletonHolder() {
        }
    }
}
