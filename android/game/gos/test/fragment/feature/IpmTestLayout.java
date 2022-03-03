package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.test.util.GosTestLog;

public class IpmTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = "IpmTestLayout";
    private static final String TITLE = "SPA";

    /* access modifiers changed from: protected */
    public void setNewValue() {
    }

    public void setPkgData(PkgData pkgData) {
    }

    public void setPkgData(Package packageR) {
    }

    public IpmTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmTestLayout.this.lambda$new$0$IpmTestLayout(compoundButton, z);
            }
        });
        ((LinearLayout) this.mView.findViewById(R.id.linearLayout_setValue)).setVisibility(8);
    }

    public /* synthetic */ void lambda$new$0$IpmTestLayout(CompoundButton compoundButton, boolean z) {
        if (z) {
            turnOn();
        } else {
            turnOff();
        }
        refreshInfo();
    }

    public void refreshInfo() {
        setEnabledOfUI(FeatureHelper.isAvailable("ipm"));
        this.mTurnOnSwitch.setChecked(isEnabled());
    }

    private void turnOn() {
        DbHelper.getInstance().getGlobalFeatureFlagDao().setState(new GlobalFeatureFlag.NameAndState("ipm", State.FORCIBLY_ENABLED));
    }

    private void turnOff() {
        DbHelper.getInstance().getGlobalFeatureFlagDao().setState(new GlobalFeatureFlag.NameAndState("ipm", State.FORCIBLY_DISABLED));
    }

    private boolean isEnabled() {
        boolean isEnabledFlagByUser = DbHelper.getInstance().getGlobalFeatureFlagDao().isEnabledFlagByUser("ipm");
        GosTestLog.d(LOG_TAG, "IPM is enabled: " + isEnabledFlagByUser);
        return isEnabledFlagByUser;
    }
}
