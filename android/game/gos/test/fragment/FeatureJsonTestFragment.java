package com.samsung.android.game.gos.test.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.test.fragment.feature.GovernorSettingsOptionsLayout;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.test.value.GovernorFragmentConst;
import com.samsung.android.game.gos.value.Constants;
import java.util.ArrayList;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

public class FeatureJsonTestFragment extends BaseFragment {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = FeatureJsonTestFragment.class.getSimpleName();
    private Button button_ClearGovernorSettings;
    private Button button_ClearSiopPolicy;
    private EditText editText_CpuDefaultLevel;
    private EditText editText_CpuMinus;
    private EditText editText_CpuPlus;
    private EditText editText_CpuZero;
    private EditText editText_GpuDefaultLevel;
    /* access modifiers changed from: private */
    public boolean isSetGlobal = false;
    private LinearLayout linearLayout_GovernorSettingsOptions;
    /* access modifiers changed from: private */
    public String mCurrentPkgName = null;
    private ArrayList<String> mInstalledPackageNameArray;
    /* access modifiers changed from: private */
    public PkgIconAndTextArrayAdapter mInstalledPackageNameArrayAdapter;
    private Spinner spinner_GovernorSettingsOptions;
    private Spinner spinner_TargetPkgData;
    private Switch switch_SetGlobal;
    private TextView textView_SiopPolicy;

    public int getNavItemId() {
        return R.id.nav_json_feature_test;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        GosTestLog.d(LOG_TAG, "onCreateView");
        View inflate = layoutInflater.inflate(R.layout.fragment_json_feature_test, viewGroup, false);
        init(inflate);
        return inflate;
    }

    private void init(View view) {
        GosTestLog.d(LOG_TAG, "init");
        if (view == null) {
            GosTestLog.d(LOG_TAG, "rootView is null");
            return;
        }
        TextView textView = (TextView) view.findViewById(R.id.textView_siop_policy);
        this.textView_SiopPolicy = textView;
        textView.setText(BuildConfig.VERSION_NAME);
        Switch switchR = (Switch) view.findViewById(R.id.switch_set_global_json_policy);
        this.switch_SetGlobal = switchR;
        switchR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                FeatureJsonTestFragment.this.lambda$init$0$FeatureJsonTestFragment(compoundButton, z);
            }
        });
        EditText editText = (EditText) view.findViewById(R.id.edittext_siop_cpu_default_level);
        this.editText_CpuDefaultLevel = editText;
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    if (FeatureJsonTestFragment.this.isSetGlobal) {
                        Global global = DbHelper.getInstance().getGlobalDao().get();
                        if (global != null) {
                            global.defaultCpuLevel = Integer.parseInt(charSequence.toString());
                            DbHelper.getInstance().getGlobalDao().insertOrUpdate(global);
                            return;
                        }
                        return;
                    }
                    Package packageR = DbHelper.getInstance().getPackageDao().getPackage(FeatureJsonTestFragment.this.mCurrentPkgName);
                    if (packageR != null) {
                        packageR.setDefaultCpuLevel(Integer.parseInt(charSequence.toString()));
                        DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
                    }
                } catch (NumberFormatException unused) {
                    GosTestLog.d(FeatureJsonTestFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        EditText editText2 = (EditText) view.findViewById(R.id.edittext_siop_gpu_default_level);
        this.editText_GpuDefaultLevel = editText2;
        editText2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    if (FeatureJsonTestFragment.this.isSetGlobal) {
                        Global global = DbHelper.getInstance().getGlobalDao().get();
                        if (global != null) {
                            global.defaultGpuLevel = Integer.parseInt(charSequence.toString());
                            DbHelper.getInstance().getGlobalDao().insertOrUpdate(global);
                            return;
                        }
                        return;
                    }
                    Package packageR = DbHelper.getInstance().getPackageDao().getPackage(FeatureJsonTestFragment.this.mCurrentPkgName);
                    if (packageR != null) {
                        packageR.setDefaultGpuLevel(Integer.parseInt(charSequence.toString()));
                        DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
                    }
                } catch (NumberFormatException unused) {
                    GosTestLog.d(FeatureJsonTestFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        EditText editText3 = (EditText) view.findViewById(R.id.edittext_siop_cpu_zero);
        this.editText_CpuZero = editText3;
        editText3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    FeatureJsonTestFragment.this.updateSiopPolicy("cpu_level_for_mode_0", Integer.valueOf(Integer.parseInt(charSequence.toString())));
                } catch (NumberFormatException unused) {
                    GosTestLog.d(FeatureJsonTestFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        EditText editText4 = (EditText) view.findViewById(R.id.edittext_siop_cpu_plus);
        this.editText_CpuPlus = editText4;
        editText4.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    FeatureJsonTestFragment.this.updateSiopPolicy("cpu_level_for_mode_1", Integer.valueOf(Integer.parseInt(charSequence.toString())));
                } catch (NumberFormatException unused) {
                    GosTestLog.d(FeatureJsonTestFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        EditText editText5 = (EditText) view.findViewById(R.id.edittext_siop_cpu_minus);
        this.editText_CpuMinus = editText5;
        editText5.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    FeatureJsonTestFragment.this.updateSiopPolicy("cpu_level_for_mode_-1", Integer.valueOf(Integer.parseInt(charSequence.toString())));
                } catch (NumberFormatException unused) {
                    GosTestLog.d(FeatureJsonTestFragment.LOG_TAG, "onTextChanged(): text not integer");
                }
            }
        });
        this.mInstalledPackageNameArray = new ArrayList<>();
        this.mInstalledPackageNameArrayAdapter = new PkgIconAndTextArrayAdapter((Activity) Objects.requireNonNull(getActivity()), R.layout.row_icon_text, new ArrayList());
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_selectPkg);
        this.spinner_TargetPkgData = spinner;
        spinner.setAdapter(this.mInstalledPackageNameArrayAdapter);
        this.spinner_TargetPkgData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                String str = (String) FeatureJsonTestFragment.this.mInstalledPackageNameArrayAdapter.getItem(i);
                String access$200 = FeatureJsonTestFragment.LOG_TAG;
                GosTestLog.d(access$200, "onItemSelected(), position: " + i + ", pkgName: " + str);
                if (!FeatureJsonTestFragment.this.isSetGlobal) {
                    FeatureJsonTestFragment.this.refreshSiopLayout(str);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                GosTestLog.d(FeatureJsonTestFragment.LOG_TAG, "onNothingSelected()");
            }
        });
        Button button = (Button) view.findViewById(R.id.btn_clear_siop_policy);
        this.button_ClearSiopPolicy = button;
        button.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FeatureJsonTestFragment.this.lambda$init$1$FeatureJsonTestFragment(view);
            }
        });
        this.spinner_GovernorSettingsOptions = (Spinner) view.findViewById(R.id.spinner_governor_settings_options);
        this.spinner_GovernorSettingsOptions.setAdapter(new ArrayAdapter(AppContext.get(), 17367043, GovernorFragmentConst.GOVERNOR_OPTIONS));
        this.spinner_GovernorSettingsOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                FeatureJsonTestFragment.this.refreshGovernorLayout(i);
            }
        });
        Button button2 = (Button) view.findViewById(R.id.btn_clear_governor_settings);
        this.button_ClearGovernorSettings = button2;
        button2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FeatureJsonTestFragment.this.lambda$init$2$FeatureJsonTestFragment(view);
            }
        });
        this.linearLayout_GovernorSettingsOptions = (LinearLayout) view.findViewById(R.id.linearLayout_governor_settings_options);
        refreshLayout();
        if (this.isSetGlobal) {
            refreshSiopLayout((String) null);
        }
    }

    public /* synthetic */ void lambda$init$0$FeatureJsonTestFragment(CompoundButton compoundButton, boolean z) {
        this.isSetGlobal = z;
        refreshLayout();
        refreshSiopLayout(this.mCurrentPkgName);
        this.linearLayout_GovernorSettingsOptions.removeAllViews();
    }

    public /* synthetic */ void lambda$init$1$FeatureJsonTestFragment(View view) {
        clearSiopPolicy();
    }

    public /* synthetic */ void lambda$init$2$FeatureJsonTestFragment(View view) {
        clearGovernorSettings();
    }

    /* access modifiers changed from: private */
    public <T> void updateSiopPolicy(String str, T t) {
        if (str.equals("cpu_level_for_mode_0") || str.equals("cpu_level_for_mode_1") || str.equals("cpu_level_for_mode_-1")) {
            try {
                if (this.isSetGlobal) {
                    Global global = DbHelper.getInstance().getGlobalDao().get();
                    if (global != null) {
                        String siopModePolicy = DbHelper.getInstance().getGlobalDao().getSiopModePolicy();
                        JSONObject jSONObject = siopModePolicy != null ? new JSONObject(siopModePolicy) : new JSONObject();
                        jSONObject.put(str, t);
                        global.siopModePolicy = jSONObject.toString();
                        DbHelper.getInstance().getGlobalDao().insertOrUpdate(global);
                        this.textView_SiopPolicy.setText(global.siopModePolicy);
                        return;
                    }
                    GosTestLog.d(LOG_TAG, "updatePolicy(), global is null!");
                    return;
                }
                Package packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mCurrentPkgName);
                if (packageR != null) {
                    JSONObject jSONObject2 = packageR.getSiopModePolicy() != null ? new JSONObject(packageR.getSiopModePolicy()) : new JSONObject();
                    jSONObject2.put(str, t);
                    packageR.setSiopModePolicy(jSONObject2.toString());
                    DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
                    this.textView_SiopPolicy.setText(packageR.getSiopModePolicy());
                    return;
                }
                GosTestLog.d(LOG_TAG, "updatePolicy(), pkg is null!");
            } catch (JSONException e) {
                String str2 = LOG_TAG;
                GosTestLog.e(str2, "updatePolicy() JSON manipulation error: " + e);
            } catch (NullPointerException e2) {
                String str3 = LOG_TAG;
                GosTestLog.e(str3, "updatePolicy() null pointer error: " + e2);
            }
        } else {
            GosTestLog.d(LOG_TAG, "updatePolicy(), Invalid key!");
        }
    }

    private String getSiopPolicyString(Package packageR) {
        String str = "{}";
        if (this.isSetGlobal) {
            String siopModePolicy = DbHelper.getInstance().getGlobalDao().getSiopModePolicy();
            if (siopModePolicy != null) {
                str = siopModePolicy;
            }
            Global global = DbHelper.getInstance().getGlobalDao().get();
            if (global != null) {
                global.governorSettings = str;
                DbHelper.getInstance().getGlobalDao().insertOrUpdate(global);
            }
        } else if (packageR != null) {
            String siopModePolicy2 = packageR.getSiopModePolicy();
            if (siopModePolicy2 == null) {
                packageR.setSiopModePolicy(str);
            } else {
                str = siopModePolicy2;
            }
            DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
        } else {
            GosTestLog.w(LOG_TAG, "getSiopPolicyString(): pkg is null");
        }
        return str;
    }

    public void onResume() {
        super.onResume();
        this.mInstalledPackageNameArray.clear();
        this.mInstalledPackageNameArray.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME));
        String str = LOG_TAG;
        GosTestLog.d(str, "mInstalledPackageNameArray.size(): " + this.mInstalledPackageNameArray.size());
        this.mInstalledPackageNameArrayAdapter.clear();
        this.mInstalledPackageNameArrayAdapter.addAll(this.mInstalledPackageNameArray);
        String str2 = LOG_TAG;
        GosTestLog.d(str2, "mInstalledPackageNameArrayAdapter.getCount(): " + this.mInstalledPackageNameArrayAdapter.getCount());
    }

    private void refreshLayout() {
        if (this.isSetGlobal) {
            this.spinner_TargetPkgData.setEnabled(false);
        } else {
            this.spinner_TargetPkgData.setEnabled(true);
        }
    }

    /* access modifiers changed from: private */
    public void refreshSiopLayout(String str) {
        String str2 = LOG_TAG;
        GosTestLog.d(str2, "onPackageSelected(): " + str);
        Package packageR = null;
        this.mCurrentPkgName = null;
        if (!this.isSetGlobal) {
            packageR = DbHelper.getInstance().getPackageDao().getPackage(str);
        }
        String siopPolicyString = getSiopPolicyString(packageR);
        try {
            this.textView_SiopPolicy.setText(siopPolicyString);
            JSONObject jSONObject = new JSONObject(siopPolicyString);
            GosTestLog.d(LOG_TAG, jSONObject.toString());
            String str3 = LOG_TAG;
            GosTestLog.d(str3, BuildConfig.VERSION_NAME + jSONObject.optInt("cpu_level_for_mode_0"));
            this.editText_CpuZero.setText(String.valueOf(jSONObject.optInt("cpu_level_for_mode_0")));
            String str4 = LOG_TAG;
            GosTestLog.d(str4, BuildConfig.VERSION_NAME + jSONObject.optInt("cpu_level_for_mode_1"));
            this.editText_CpuPlus.setText(String.valueOf(jSONObject.optInt("cpu_level_for_mode_1")));
            String str5 = LOG_TAG;
            GosTestLog.d(str5, BuildConfig.VERSION_NAME + jSONObject.optInt("cpu_level_for_mode_-1"));
            this.editText_CpuMinus.setText(String.valueOf(jSONObject.optInt("cpu_level_for_mode_-1")));
        } catch (JSONException unused) {
            GosTestLog.d(LOG_TAG, "onPackageSelected(): error reading JSON");
        }
        if (!this.isSetGlobal && packageR != null) {
            this.editText_CpuDefaultLevel.setText(String.valueOf(packageR.getDefaultCpuLevel()));
            this.editText_GpuDefaultLevel.setText(String.valueOf(packageR.getDefaultGpuLevel()));
        } else if (this.isSetGlobal) {
            this.editText_CpuDefaultLevel.setText(String.valueOf(DbHelper.getInstance().getGlobalDao().getDefaultCpuLevel()));
            this.editText_GpuDefaultLevel.setText(String.valueOf(DbHelper.getInstance().getGlobalDao().getDefaultGpuLevel()));
        }
        this.mCurrentPkgName = str;
    }

    /* access modifiers changed from: private */
    public void refreshGovernorLayout(int i) {
        String str = GovernorFragmentConst.GOVERNOR_OPTIONS[i];
        GovernorSettingsOptionsLayout governorSettingsOptionsLayout = null;
        if (!this.isSetGlobal) {
            Package packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mCurrentPkgName);
            if (packageR != null) {
                governorSettingsOptionsLayout = new GovernorSettingsOptionsLayout(AppContext.get(), str, packageR);
            }
        } else if (DbHelper.getInstance().getGlobalDao().get() != null) {
            governorSettingsOptionsLayout = new GovernorSettingsOptionsLayout(AppContext.get(), str, (Package) null);
        }
        this.linearLayout_GovernorSettingsOptions.removeAllViews();
        if (governorSettingsOptionsLayout != null) {
            this.linearLayout_GovernorSettingsOptions.addView(governorSettingsOptionsLayout.getView());
        }
    }

    private void clearSiopPolicy() {
        GosTestLog.d(LOG_TAG, "clearSiopPolicy() ");
        if (this.isSetGlobal) {
            Global global = DbHelper.getInstance().getGlobalDao().get();
            if (global != null) {
                global.siopModePolicy = new JSONObject().toString();
                String str = LOG_TAG;
                GosTestLog.d(str, "clearSiopPolicy(), type: Global, " + global.siopModePolicy);
                global.defaultCpuLevel = Global.getDefaultGlobal().defaultCpuLevel;
                global.defaultGpuLevel = Global.getDefaultGlobal().defaultGpuLevel;
                DbHelper.getInstance().getGlobalDao().insertOrUpdate(global);
                this.textView_SiopPolicy.setText(global.siopModePolicy);
                return;
            }
            GosTestLog.w(LOG_TAG, "clearSiopPolicy(), global is null!");
            return;
        }
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mCurrentPkgName);
        if (packageR != null) {
            packageR.setSiopModePolicy(new JSONObject().toString());
            String str2 = LOG_TAG;
            GosTestLog.d(str2, "clearSiopPolicy(), type: Package, " + this.mCurrentPkgName + ", " + packageR.getSiopModePolicy());
            packageR.setDefaultCpuLevel(DbHelper.getInstance().getGlobalDao().getDefaultCpuLevel());
            packageR.setDefaultGpuLevel(DbHelper.getInstance().getGlobalDao().getDefaultGpuLevel());
            DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
            this.textView_SiopPolicy.setText(packageR.getSiopModePolicy());
            return;
        }
        GosTestLog.w(LOG_TAG, "clearSiopPolicy(), pkg is null!");
    }

    private void clearGovernorSettings() {
        GosTestLog.d(LOG_TAG, "clearGovernorSettings() ");
        if (this.isSetGlobal) {
            Global global = DbHelper.getInstance().getGlobalDao().get();
            if (global != null) {
                global.governorSettings = new JSONObject().toString();
                String str = LOG_TAG;
                GosTestLog.d(str, "clearGovernorSettings(), type: Global, " + global.governorSettings);
                DbHelper.getInstance().getGlobalDao().insertOrUpdate(global);
                this.linearLayout_GovernorSettingsOptions.removeAllViews();
                return;
            }
            GosTestLog.w(LOG_TAG, "clearGovernorSettings(), global is null!");
            return;
        }
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(this.mCurrentPkgName);
        if (packageR != null) {
            packageR.setGovernorSettings(new JSONObject().toString());
            String str2 = LOG_TAG;
            GosTestLog.d(str2, "clearGovernorSettings(), type: Package, " + this.mCurrentPkgName + ", " + packageR.getSiopModePolicy());
            DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
            this.linearLayout_GovernorSettingsOptions.removeAllViews();
            return;
        }
        GosTestLog.w(LOG_TAG, "clearGovernorSettings(), pkg is null!");
    }
}
