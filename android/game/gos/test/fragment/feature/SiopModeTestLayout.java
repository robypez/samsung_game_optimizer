package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.test.util.GosTestLog;

public class SiopModeTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = "SiopModeTestLayout";
    private static final String TITLE = "SiopMode";

    /* access modifiers changed from: protected */
    public void setNewValue() {
    }

    public void setPkgData(Package packageR) {
    }

    public SiopModeTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SiopModeTestLayout.this.lambda$new$0$SiopModeTestLayout(compoundButton, z);
            }
        });
        ((LinearLayout) this.mView.findViewById(R.id.linearLayout_setValue)).setVisibility(8);
    }

    public /* synthetic */ void lambda$new$0$SiopModeTestLayout(CompoundButton compoundButton, boolean z) {
        if (z) {
            turnOn();
        } else {
            turnOff();
        }
        refreshInfo();
    }

    public void refreshInfo() {
        setEnabledOfUI(FeatureHelper.isAvailable("siop_mode"));
        this.mTurnOnSwitch.setChecked(isEnabled());
    }

    private void turnOn() {
        GlobalDbHelper.getInstance().setFeatureFlagState("siop_mode", State.FORCIBLY_ENABLED);
    }

    private void turnOff() {
        GlobalDbHelper.getInstance().setFeatureFlagState("siop_mode", State.FORCIBLY_DISABLED);
    }

    private boolean isEnabled() {
        boolean isEnabledFlagByUser = FeatureHelper.isEnabledFlagByUser("siop_mode");
        GosTestLog.d(LOG_TAG, "SiopMode is enabled: " + isEnabledFlagByUser);
        return isEnabledFlagByUser;
    }
}
