package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import androidx.core.view.InputDeviceCompat;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.test.util.GosTestLog;

public class GpuTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = "GpuTestLayout";
    private static final String TITLE = ("GPU level (" + SeSysProp.getCpuGpuLevelInstance().getGpuLevelsCsv() + ")");
    private String mPkgName = null;

    public GpuTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setVisibility(8);
        this.mNewValueEditText.setInputType(InputDeviceCompat.SOURCE_TOUCHSCREEN);
        this.mSetButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                GpuTestLayout.this.lambda$new$0$GpuTestLayout(view);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$GpuTestLayout(View view) {
        showSetValueAndForceToStopDialog(this.mPkgName);
    }

    public void setPkgData(Package packageR) {
        if (packageR != null) {
            this.mPkgName = packageR.getPkgName();
        } else {
            Toast.makeText(this.mContext, "pkg is null", 0).show();
        }
    }

    public void refreshInfo() {
        Package packageR;
        if (!(this.mCurrentValueTextView == null || (packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mPkgName)) == null)) {
            int defaultGpuLevel = packageR.getDefaultGpuLevel();
            GosTestLog.d(LOG_TAG, "refreshInfo(), defaultGpuLevel: " + defaultGpuLevel);
            this.mCurrentValueTextView.setText(String.valueOf(defaultGpuLevel));
        }
        setEnabledOfUI(true);
    }

    /* access modifiers changed from: protected */
    public void setNewValue() {
        if (this.mNewValueEditText != null) {
            GosTestLog.d(LOG_TAG, "setNewValue(), pkg name : " + this.mPkgName);
            try {
                DbHelper.getInstance().getPackageDao().setDefaultGpuLevel(new Package.PkgNameAndDefaultGpuLevel(this.mPkgName, Integer.parseInt(this.mNewValueEditText.getText().toString())));
                GmsGlobalPackageDataSetter.getInstance().applySingleGame(this.mPkgName);
            } catch (Exception e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        refreshInfo();
    }
}
