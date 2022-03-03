package com.samsung.android.game.gos.test.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.DataManager;
import com.samsung.android.game.gos.data.TestDataManager;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.endpoint.MethodsForJsonCommand;
import com.samsung.android.game.gos.feature.dss.TssCore;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.test.fragment.feature.CpuTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.DfsTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.DssTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.FeatureFragmentUtil;
import com.samsung.android.game.gos.test.fragment.feature.FeatureInfo;
import com.samsung.android.game.gos.test.fragment.feature.FeatureTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.GpuTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.IpmTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.PackageFeatureTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.ShiftTempTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.SiopModePolicyJsonTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.SiopModeTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.TspPolicyJsonTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.TssTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.VrrMaxTestLayout;
import com.samsung.android.game.gos.test.fragment.feature.VrrMinTestLayout;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.test.util.TestDataSetter;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;

public class FeatureTestFragment extends BaseFragment implements TestDataSetter.DataSettingFeedBack {
    private static final String LOG_TAG = "FeatureTestFragment";
    private Button btn_getAllPkgData;
    private LinearLayout linearLayout_getAllPkgData;
    private LinearLayout linearLayout_getPkgData;
    private LinearLayout mFeatureFlagsTable;
    private List<FeatureTestLayout> mGlobalFeatureTestLayoutList;
    private List<String> mInstalledPackageNameArray;
    /* access modifiers changed from: private */
    public ArrayAdapter<String> mInstalledPackageNameArrayAdapter;
    private LinearLayout mLinearLayout_editAndRestorePkgValues;
    private List<FeatureTestLayout> mPkgFeatureTestLayoutList;
    private TestDataSetter mTestDataSetter;
    private TextView mTextView_changeModeGuide;
    private Spinner spinner_targetPkgData;
    private Switch switch_automaticUpdate;
    private TextView txt_custom_dfs;
    private TextView txt_custom_dfs_mode;
    private TextView txt_custom_dss;
    private TextView txt_custom_resolution_mode;

    public int getNavItemId() {
        return R.id.nav_feature_test;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        GosTestLog.d(LOG_TAG, "onCreateView()");
        this.mTestDataSetter = new TestDataSetter(getActivity(), this);
        View inflate = layoutInflater.inflate(R.layout.fragment_feature_test, viewGroup, false);
        Switch switchR = (Switch) inflate.findViewById(R.id.switch_automaticUpdate);
        this.switch_automaticUpdate = switchR;
        switchR.setOnCheckedChangeListener($$Lambda$FeatureTestFragment$AcOrIYce4kkJBxrdxuxHoVokM_A.INSTANCE);
        initGlobal(inflate);
        initPkg(inflate);
        return inflate;
    }

    private void initGlobal(View view) {
        GosTestLog.d(LOG_TAG, "initGlobal()");
        ArrayList arrayList = new ArrayList();
        this.mGlobalFeatureTestLayoutList = arrayList;
        arrayList.add(new IpmTestLayout(getActivity()));
        this.mGlobalFeatureTestLayoutList.add(new SiopModeTestLayout(getActivity()));
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_globalValues);
        Button button = (Button) view.findViewById(R.id.btn_restoreGlobal);
        if (this.mGlobalFeatureTestLayoutList.size() > 0) {
            for (FeatureTestLayout view2 : this.mGlobalFeatureTestLayoutList) {
                linearLayout.addView(view2.getView());
            }
            button.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    FeatureTestFragment.this.lambda$initGlobal$1$FeatureTestFragment(view);
                }
            });
            return;
        }
        linearLayout.setVisibility(8);
        button.setVisibility(8);
    }

    public /* synthetic */ void lambda$initGlobal$1$FeatureTestFragment(View view) {
        this.mTestDataSetter.showDialogToRestore(TestDataSetter.DataRangeToBeSet.GLOBAL, (String) null);
    }

    private void initPkg(View view) {
        GosTestLog.d(LOG_TAG, "initPkg()");
        this.mInstalledPackageNameArray = new ArrayList();
        this.mInstalledPackageNameArrayAdapter = new PkgIconAndTextArrayAdapter(getActivity(), R.layout.row_icon_text, new ArrayList());
        Button button = (Button) view.findViewById(R.id.btn_getAllPkgData);
        this.btn_getAllPkgData = button;
        button.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FeatureTestFragment.this.lambda$initPkg$2$FeatureTestFragment(view);
            }
        });
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_targetPkgData);
        this.spinner_targetPkgData = spinner;
        spinner.setAdapter(this.mInstalledPackageNameArrayAdapter);
        this.spinner_targetPkgData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                String str = (String) FeatureTestFragment.this.mInstalledPackageNameArrayAdapter.getItem(i);
                GosTestLog.d(FeatureTestFragment.LOG_TAG, "onItemSelected(), position: " + i + ", pkgName: " + str);
                FeatureTestFragment.this.refreshCurrentValues(str);
            }
        });
        this.linearLayout_getAllPkgData = (LinearLayout) view.findViewById(R.id.linearLayout_getAllPkgData);
        this.linearLayout_getPkgData = (LinearLayout) view.findViewById(R.id.linearLayout_getPkgData);
        ((Button) view.findViewById(R.id.btn_getPkgData)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FeatureTestFragment.this.lambda$initPkg$3$FeatureTestFragment(view);
            }
        });
        ((Button) view.findViewById(R.id.btn_restorePackage)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FeatureTestFragment.this.lambda$initPkg$4$FeatureTestFragment(view);
            }
        });
        this.mTextView_changeModeGuide = (TextView) view.findViewById(R.id.textView_changeModeGuide);
        this.mLinearLayout_editAndRestorePkgValues = (LinearLayout) view.findViewById(R.id.linearLayout_editAndRestorePkgValues);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_editPkgValues);
        this.mPkgFeatureTestLayoutList = new ArrayList();
        if (TssCore.isAvailable()) {
            this.mPkgFeatureTestLayoutList.add(new TssTestLayout(getActivity()));
        } else {
            DssTestLayout dssTestLayout = new DssTestLayout(getActivity());
            dssTestLayout.switchTitle();
            this.mPkgFeatureTestLayoutList.add(dssTestLayout);
        }
        this.mPkgFeatureTestLayoutList.add(new DfsTestLayout(getActivity()));
        if (SeSysProp.getCpuGpuLevelInstance().getCpuLevelsCsv() != null) {
            this.mPkgFeatureTestLayoutList.add(new CpuTestLayout(getActivity()));
            this.mPkgFeatureTestLayoutList.add(new ShiftTempTestLayout(getActivity()));
        }
        if (SeSysProp.getCpuGpuLevelInstance().getGpuLevelsCsv() != null) {
            this.mPkgFeatureTestLayoutList.add(new GpuTestLayout(getActivity()));
        }
        this.mPkgFeatureTestLayoutList.add(new SiopModePolicyJsonTestLayout(getActivity()));
        this.mPkgFeatureTestLayoutList.add(new VrrMaxTestLayout(getActivity()));
        this.mPkgFeatureTestLayoutList.add(new VrrMinTestLayout(getActivity()));
        this.mPkgFeatureTestLayoutList.add(new TspPolicyJsonTestLayout(getActivity()));
        for (FeatureTestLayout view2 : this.mPkgFeatureTestLayoutList) {
            linearLayout.addView(view2.getView());
        }
        ((Button) view.findViewById(R.id.btn_launchApp)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FeatureTestFragment.this.lambda$initPkg$5$FeatureTestFragment(view);
            }
        });
        this.mFeatureFlagsTable = (LinearLayout) view.findViewById(R.id.linearLayout_featureFlagsTable);
        this.txt_custom_resolution_mode = (TextView) view.findViewById(R.id.txt_custom_resolution_mode);
        this.txt_custom_dss = (TextView) view.findViewById(R.id.txt_custom_dss);
        this.txt_custom_dfs_mode = (TextView) view.findViewById(R.id.txt_custom_dfs_mode);
        this.txt_custom_dfs = (TextView) view.findViewById(R.id.txt_custom_dfs);
        this.mFeatureFlagsTable.addView((LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.row_package_feature_flag_title, (ViewGroup) null));
        Iterator<FeatureInfo> it = FeatureFragmentUtil.getFeatureInfoArrayList().iterator();
        while (it.hasNext()) {
            FeatureInfo next = it.next();
            this.mFeatureFlagsTable.addView(new PackageFeatureTestLayout(next.getKey(), next.getValue(), getTargetPkgName()).getLayout());
        }
    }

    public /* synthetic */ void lambda$initPkg$2$FeatureTestFragment(View view) {
        getAllPkgData();
    }

    public /* synthetic */ void lambda$initPkg$3$FeatureTestFragment(View view) {
        getPkgData();
    }

    public /* synthetic */ void lambda$initPkg$4$FeatureTestFragment(View view) {
        this.mTestDataSetter.showDialogToRestore(TestDataSetter.DataRangeToBeSet.SINGLE_PKG, getTargetPkgName());
    }

    public /* synthetic */ void lambda$initPkg$5$FeatureTestFragment(View view) {
        Intent launchIntentForPackage;
        String targetPkgName = getTargetPkgName();
        if (targetPkgName != null && (launchIntentForPackage = getActivity().getPackageManager().getLaunchIntentForPackage(targetPkgName)) != null) {
            launchIntentForPackage.addFlags(268435456);
            startActivity(launchIntentForPackage);
        }
    }

    public void onResume() {
        GosTestLog.d(LOG_TAG, "onResume(). " + "begin");
        super.onResume();
        Switch switchR = this.switch_automaticUpdate;
        if (switchR != null) {
            switchR.setChecked(DbHelper.getInstance().getGlobalDao().isAutomaticUpdate());
        }
        this.mInstalledPackageNameArray.clear();
        this.mInstalledPackageNameArray.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME));
        GosTestLog.d(LOG_TAG, "onResume(). " + "mInstalledPackageNameArray.size(): " + this.mInstalledPackageNameArray.size());
        this.mInstalledPackageNameArrayAdapter.clear();
        this.mInstalledPackageNameArrayAdapter.addAll(this.mInstalledPackageNameArray);
        GosTestLog.d(LOG_TAG, "onResume(). " + "mInstalledPackageNameArrayAdapter.getCount(): " + this.mInstalledPackageNameArrayAdapter.getCount());
        this.mInstalledPackageNameArrayAdapter.notifyDataSetChanged();
        boolean z = true;
        int i = 0;
        if (TestDataManager.isDevMode()) {
            this.linearLayout_getAllPkgData.setVisibility(0);
            this.linearLayout_getPkgData.setVisibility(0);
            this.btn_getAllPkgData.setText(String.format(Locale.US, "%s (%d)", new Object[]{getResources().getText(R.string.show_all_package_data), Integer.valueOf(this.mInstalledPackageNameArrayAdapter.getCount())}));
        }
        if (DataManager.getInstance().getResolutionMode() != 1) {
            z = false;
        }
        TextView textView = this.mTextView_changeModeGuide;
        if (textView != null) {
            textView.setVisibility(z ? 8 : 0);
        }
        LinearLayout linearLayout = this.mLinearLayout_editAndRestorePkgValues;
        if (linearLayout != null) {
            if (!z) {
                i = 8;
            }
            linearLayout.setVisibility(i);
        }
        refreshCurrentValues(getTargetPkgName());
    }

    private void getAllPkgData() {
        int i = 0;
        String[] strArr = (String[]) this.mInstalledPackageNameArray.toArray(new String[0]);
        int length = strArr.length;
        while (i < length) {
            String str = strArr[i];
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("package_name", str);
                String respondWithJson = MethodsForJsonCommand.respondWithJson(GosInterface.Command.GET_PACKAGE_DATA, jSONObject.toString(), (String) null);
                if (respondWithJson != null) {
                    showResult(LOG_TAG, getResultStringFromPkgDataJsonResponse("getAllPkgData(), ", str, new JSONObject(respondWithJson)));
                    i++;
                } else {
                    throw new NullPointerException("getAllPkgData(), " + " response is null for " + str);
                }
            } catch (Exception e) {
                showResult(LOG_TAG, "getAllPkgData(), " + " error: " + str + " " + e.getLocalizedMessage());
                StringBuilder sb = new StringBuilder();
                sb.append("getAllPkgData(), ");
                sb.append(" error: ");
                sb.append(str);
                sb.append(" ");
                GosTestLog.w(LOG_TAG, sb.toString(), e);
            }
        }
    }

    private void getPkgData() {
        try {
            JSONObject jSONObject = new JSONObject();
            String targetPkgName = getTargetPkgName();
            jSONObject.put("package_name", targetPkgName);
            String respondWithJson = MethodsForJsonCommand.respondWithJson(GosInterface.Command.GET_PACKAGE_DATA, jSONObject.toString(), (String) null);
            if (respondWithJson != null) {
                showResult(LOG_TAG, getResultStringFromPkgDataJsonResponse("getPkgData(), ", targetPkgName, new JSONObject(respondWithJson)));
                return;
            }
            throw new NullPointerException("getPkgData(), " + " response is null for " + targetPkgName);
        } catch (Exception e) {
            showResult(LOG_TAG, "getPkgData(), " + "error: " + e.getLocalizedMessage());
            StringBuilder sb = new StringBuilder();
            sb.append("getPkgData(), ");
            sb.append(" error: ");
            GosTestLog.w(LOG_TAG, sb.toString(), e);
        }
    }

    private String getResultStringFromPkgDataJsonResponse(String str, String str2, JSONObject jSONObject) {
        String str3 = str + "pkgName: " + str2;
        Iterator<String> keys = jSONObject.keys();
        StringBuilder sb = new StringBuilder();
        while (keys.hasNext()) {
            String next = keys.next();
            try {
                sb.append(str3);
                sb.append(", ");
                sb.append(next);
                sb.append(": ");
                sb.append(jSONObject.get(next).toString());
                sb.append("\n");
            } catch (Exception e) {
                GosTestLog.w(LOG_TAG, (Throwable) e);
            }
        }
        return sb.toString();
    }

    private String getTargetPkgName() {
        String str;
        int selectedItemPosition = this.spinner_targetPkgData.getSelectedItemPosition();
        if (selectedItemPosition < 0) {
            selectedItemPosition = this.spinner_targetPkgData.getFirstVisiblePosition();
        }
        try {
            str = this.mInstalledPackageNameArrayAdapter.getItem(selectedItemPosition);
        } catch (IndexOutOfBoundsException e) {
            GosTestLog.w(LOG_TAG, (Throwable) e);
            str = null;
        }
        GosTestLog.d(LOG_TAG, "getTargetPkgName(), targetPkgItemPosition: " + selectedItemPosition + ", pkgName: " + str);
        return str;
    }

    /* access modifiers changed from: private */
    public void refreshCurrentValues(String str) {
        GosTestLog.d(LOG_TAG, "refreshCurrentValues(), begin.");
        for (FeatureTestLayout refreshInfo : this.mGlobalFeatureTestLayoutList) {
            refreshInfo.refreshInfo();
        }
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(str);
        for (FeatureTestLayout next : this.mPkgFeatureTestLayoutList) {
            next.setPkgData(packageR);
            next.refreshInfo();
        }
        if (packageR != null) {
            this.txt_custom_dfs.setText(String.valueOf(packageR.getCustomDfs()));
            this.txt_custom_dfs_mode.setText(String.valueOf(packageR.getCustomDfsMode()));
            this.txt_custom_dss.setText(String.valueOf(packageR.getCustomDss()));
            this.txt_custom_resolution_mode.setText(String.valueOf(packageR.getCustomResolutionMode()));
        }
    }

    public void onRestore(TestDataSetter.DataRangeToBeSet dataRangeToBeSet, String str) {
        if (dataRangeToBeSet == TestDataSetter.DataRangeToBeSet.GLOBAL) {
            for (FeatureTestLayout refreshInfo : this.mGlobalFeatureTestLayoutList) {
                refreshInfo.refreshInfo();
            }
        } else if (dataRangeToBeSet == TestDataSetter.DataRangeToBeSet.SINGLE_PKG) {
            refreshCurrentValues(str);
        }
    }
}
