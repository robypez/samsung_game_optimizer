package com.samsung.android.game.gos.test.fragment.feature;

import android.content.Context;
import android.view.View;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.feature.dfs.DfsFeature;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.value.Constants;

public class DfsTestLayout extends FeatureTestLayout {
    private static final String LOG_TAG = "DfsTestLayout";
    private static final String TITLE = "DFS (1.0~120.0)";
    private Package mPkg;

    public DfsTestLayout(Context context) {
        super(context);
        this.mTitleTextView.setText(TITLE);
        this.mTurnOnSwitch.setVisibility(8);
        this.mSetButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                DfsTestLayout.this.lambda$new$0$DfsTestLayout(view);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$DfsTestLayout(View view) {
        setNewValue();
    }

    public void setPkgData(Package packageR) {
        this.mPkg = packageR;
    }

    public void refreshInfo() {
        if (!(!FeatureHelper.isAvailable(Constants.V4FeatureFlag.DFS) || this.mPkg == null || this.mCurrentValueTextView == null)) {
            float defaultDfs = DfsFeature.getDefaultDfs(this.mPkg);
            GosTestLog.d(LOG_TAG, "refreshInfo(), defaultDfs: " + defaultDfs);
            this.mCurrentValueTextView.setText(String.valueOf(defaultDfs));
        }
        setEnabledOfUI(FeatureHelper.isAvailable(Constants.V4FeatureFlag.DFS));
    }

    /* access modifiers changed from: protected */
    public void setNewValue() {
        if (!(this.mNewValueEditText == null || this.mPkg == null)) {
            GosTestLog.d(LOG_TAG, "setNewValue(), pkg name : " + this.mPkg.pkgName);
            try {
                float parseFloat = Float.parseFloat(this.mNewValueEditText.getText().toString());
                float[] eachModeDfsArray = this.mPkg.getEachModeDfsArray();
                if (eachModeDfsArray == null) {
                    eachModeDfsArray = new float[]{-1.0f, -1.0f, -1.0f, -1.0f};
                }
                eachModeDfsArray[1] = parseFloat;
                this.mPkg.setEachModeDfs(eachModeDfsArray);
                DbHelper.getInstance().getPackageDao().insertOrUpdate(this.mPkg);
            } catch (Exception e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        refreshInfo();
    }
}
