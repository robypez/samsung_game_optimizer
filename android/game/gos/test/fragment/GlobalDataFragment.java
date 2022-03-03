package com.samsung.android.game.gos.test.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.test.fragment.feature.FeatureFragmentUtil;
import com.samsung.android.game.gos.test.fragment.feature.FeatureInfo;
import com.samsung.android.game.gos.test.fragment.feature.GlobalFeatureTestLayout;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.test.util.TestDataSetter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GlobalDataFragment extends BaseFragment implements TestDataSetter.DataSettingFeedBack {
    private static final String LOG_TAG = "GlobalDataFragment";
    private List<GlobalFeatureTestLayout> mGlobalGlobalFeatureTestLayoutList;
    private TestDataSetter mTestDataSetter;
    private Switch switch_automaticUpdate;

    public int getNavItemId() {
        return R.id.nav_globalData;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        GosTestLog.d(LOG_TAG, "onCreateView()");
        View inflate = layoutInflater.inflate(R.layout.fragment_global_data, viewGroup, false);
        init(inflate);
        return inflate;
    }

    private void init(View view) {
        GosTestLog.d(LOG_TAG, "init()");
        Switch switchR = (Switch) view.findViewById(R.id.switch_automaticUpdate);
        this.switch_automaticUpdate = switchR;
        switchR.setOnCheckedChangeListener($$Lambda$GlobalDataFragment$QMKwMthy4qj1e8FY8NPzWEPwf1A.INSTANCE);
        this.mTestDataSetter = new TestDataSetter(getActivity(), this);
        this.mGlobalGlobalFeatureTestLayoutList = new ArrayList();
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_featureFlagsTable);
        LinearLayout linearLayout2 = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.row_feature_flag, (ViewGroup) null);
        ((Switch) linearLayout2.findViewById(R.id.switch_enabled)).setVisibility(8);
        ((TextView) linearLayout2.findViewById(R.id.txt_enabled)).setVisibility(0);
        ((Switch) linearLayout2.findViewById(R.id.switch_using_user)).setVisibility(8);
        ((Switch) linearLayout2.findViewById(R.id.switch_using_pkg)).setVisibility(8);
        ((TextView) linearLayout2.findViewById(R.id.txt_using_user)).setVisibility(0);
        ((TextView) linearLayout2.findViewById(R.id.txt_using_pkg)).setVisibility(0);
        ((Spinner) linearLayout2.findViewById(R.id.spinner_state)).setVisibility(8);
        ((TextView) linearLayout2.findViewById(R.id.txt_state)).setVisibility(0);
        linearLayout.addView(linearLayout2);
        Iterator<FeatureInfo> it = FeatureFragmentUtil.getFeatureInfoArrayList().iterator();
        while (it.hasNext()) {
            FeatureInfo next = it.next();
            this.mGlobalGlobalFeatureTestLayoutList.add(new GlobalFeatureTestLayout(next.getKey(), next.getValue()));
        }
        for (GlobalFeatureTestLayout layout : this.mGlobalGlobalFeatureTestLayoutList) {
            linearLayout.addView(layout.getLayout());
        }
        ((Button) view.findViewById(R.id.btn_restore)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                GlobalDataFragment.this.lambda$init$1$GlobalDataFragment(view);
            }
        });
    }

    public /* synthetic */ void lambda$init$1$GlobalDataFragment(View view) {
        this.mTestDataSetter.showDialogToRestore(TestDataSetter.DataRangeToBeSet.GLOBAL, (String) null);
    }

    public void onResume() {
        GosTestLog.d(LOG_TAG, "onResume()");
        super.onResume();
        Switch switchR = this.switch_automaticUpdate;
        if (switchR != null) {
            switchR.setChecked(DbHelper.getInstance().getGlobalDao().isAutomaticUpdate());
        }
    }

    public void onRestore(TestDataSetter.DataRangeToBeSet dataRangeToBeSet, String str) {
        GosTestLog.d(LOG_TAG, "onRestore()");
        for (GlobalFeatureTestLayout loadData : this.mGlobalGlobalFeatureTestLayoutList) {
            loadData.loadData();
        }
    }
}
