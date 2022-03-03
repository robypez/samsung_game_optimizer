package com.samsung.android.game.gos.test.fragment;

import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.feature.ipm.IpmFeature;
import com.samsung.android.game.gos.ipm.IntelMode;
import com.samsung.android.game.gos.ipm.Profile;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.util.FileUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class IpmFragment extends BaseFragment {
    private static final String LOG_TAG = "IpmFragment";
    private static final String[] TRAINING_FILE_NAME = {"q.tablex", "backup_q.tablex", "backup_q.tablex_version", "q.tablek"};
    Button btn_applyAllGameJSON;
    Button btn_debug_mode;
    Button btn_deleteTrainingData;
    Button btn_gpu_min_boost;
    Button btn_lrpstset;
    Button btn_pstreset;
    Button btn_pstset;
    Button btn_pullTrainingData;
    Button btn_pushTrainingData;
    Button btn_tcreset;
    Button btn_tcset;
    Spinner spinner_intel;
    Spinner spinner_mode;
    Switch swt_allow_mloff;
    Switch swt_cpu_minlock;
    Switch swt_custom_tfps;
    Switch swt_enable_bus_freq;
    Switch swt_enable_bus_min_freq_control;
    Switch swt_enable_frame_interpolation;
    Switch swt_enable_lrpst;
    Switch swt_gpu_minlock;
    Switch swt_json_pkg;
    Switch swt_local;
    Switch swt_loglevel;
    Switch swt_onlyCapture;
    Switch swt_run_any_mode;
    Switch swt_server;
    Switch swt_stability_mode;
    Switch swt_supertrain;
    EditText text_frame_interpolation_decay_half_life;
    EditText text_frame_interpolation_frame_rate_offset;
    EditText text_frame_interpolation_temperature_offset;
    EditText text_gpu_min_boost;
    EditText text_lrpst;
    EditText text_pst;
    TextView text_stats;
    EditText text_tc;

    public int getNavItemId() {
        return R.id.nav_spa;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        GosTestLog.d(LOG_TAG, "onCreateView()");
        View inflate = layoutInflater.inflate(R.layout.fragment_ipm, viewGroup, false);
        init(inflate);
        return inflate;
    }

    private void init(View view) {
        GosTestLog.d(LOG_TAG, "init()");
        final GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
        GlobalFeatureFlagDao globalFeatureFlagDao = DbHelper.getInstance().getGlobalFeatureFlagDao();
        ((TextView) view.findViewById(R.id.textView_trainingDataFileNames)).setText("Target training data file names : " + Arrays.toString(TRAINING_FILE_NAME));
        Button button = (Button) view.findViewById(R.id.btn_deleteTrainingData);
        this.btn_deleteTrainingData = button;
        button.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                IpmFragment.this.lambda$init$0$IpmFragment(view);
            }
        });
        ((TextView) view.findViewById(R.id.textView_applyJSON)).setText("Apply the JSON file in \"/IPM/all_games.json\" on the external storage to all games JSON.");
        Button button2 = (Button) view.findViewById(R.id.btn_applyJSON);
        this.btn_applyAllGameJSON = button2;
        button2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                IpmFragment.this.lambda$init$1$IpmFragment(view);
            }
        });
        this.swt_server = (Switch) view.findViewById(R.id.switch_server);
        Switch switchR = (Switch) view.findViewById(R.id.switch_local);
        this.swt_local = switchR;
        switchR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(globalFeatureFlagDao) {
            public final /* synthetic */ GlobalFeatureFlagDao f$1;

            {
                this.f$1 = r2;
            }

            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$2$IpmFragment(this.f$1, compoundButton, z);
            }
        });
        Switch switchR2 = (Switch) view.findViewById(R.id.switch_json_pkg);
        this.swt_json_pkg = switchR2;
        switchR2.setOnCheckedChangeListener($$Lambda$IpmFragment$kiaDR9jWEgv5hhIoIipPbndcmaI.INSTANCE);
        this.spinner_mode = (Spinner) view.findViewById(R.id.modeSpinner);
        this.spinner_mode.setAdapter(new ArrayAdapter(getContext(), 17367049, new ArrayList(Arrays.asList(new String[]{"Low", "High", "Ultra", "Custom", "Critical"}))));
        this.spinner_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                globalDao.setIpmMode(new Global.IdAndIpmMode(i));
                IpmCore.getInstance(IpmFragment.this.getContext()).setProfile(Profile.fromInt(i));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                globalDao.setIpmMode(new Global.IdAndIpmMode(Profile.HIGH.toInt()));
                IpmCore.getInstance(IpmFragment.this.getContext()).setProfile(Profile.HIGH);
            }
        });
        Switch switchR3 = (Switch) view.findViewById(R.id.switch_supertrain);
        this.swt_supertrain = switchR3;
        switchR3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(globalDao) {
            public final /* synthetic */ GlobalDao f$1;

            {
                this.f$1 = r2;
            }

            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$4$IpmFragment(this.f$1, compoundButton, z);
            }
        });
        Switch switchR4 = (Switch) view.findViewById(R.id.switch_loglevel);
        this.swt_loglevel = switchR4;
        switchR4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(globalDao) {
            public final /* synthetic */ GlobalDao f$1;

            {
                this.f$1 = r2;
            }

            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$5$IpmFragment(this.f$1, compoundButton, z);
            }
        });
        Switch switchR5 = (Switch) view.findViewById(R.id.switch_onlyCapture);
        this.swt_onlyCapture = switchR5;
        switchR5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(globalDao) {
            public final /* synthetic */ GlobalDao f$1;

            {
                this.f$1 = r2;
            }

            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$6$IpmFragment(this.f$1, compoundButton, z);
            }
        });
        Switch switchR6 = (Switch) view.findViewById(R.id.switch_cpu_minlock);
        this.swt_cpu_minlock = switchR6;
        switchR6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$7$IpmFragment(compoundButton, z);
            }
        });
        Switch switchR7 = (Switch) view.findViewById(R.id.switch_gpu_minlock);
        this.swt_gpu_minlock = switchR7;
        switchR7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$8$IpmFragment(compoundButton, z);
            }
        });
        Button button3 = (Button) view.findViewById(R.id.btn_setGpuMinBoost);
        this.btn_gpu_min_boost = button3;
        button3.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                IpmFragment.this.lambda$init$9$IpmFragment(view);
            }
        });
        this.text_gpu_min_boost = (EditText) view.findViewById(R.id.edit_gpu_min_boost);
        Switch switchR8 = (Switch) view.findViewById(R.id.switch_enable_lrpst);
        this.swt_enable_lrpst = switchR8;
        switchR8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$10$IpmFragment(compoundButton, z);
            }
        });
        Switch switchR9 = (Switch) view.findViewById(R.id.switch_allow_mloff);
        this.swt_allow_mloff = switchR9;
        switchR9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$11$IpmFragment(compoundButton, z);
            }
        });
        Switch switchR10 = (Switch) view.findViewById(R.id.switch_enable_bus_freq);
        this.swt_enable_bus_freq = switchR10;
        switchR10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$12$IpmFragment(compoundButton, z);
            }
        });
        Switch switchR11 = (Switch) view.findViewById(R.id.switch_run_any_mode);
        this.swt_run_any_mode = switchR11;
        switchR11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$13$IpmFragment(compoundButton, z);
            }
        });
        this.spinner_intel = (Spinner) view.findViewById(R.id.intelSpinner);
        ArrayList arrayList = new ArrayList();
        for (IntelMode intelMode : IntelMode.values()) {
            arrayList.add(intelMode.toString());
        }
        this.spinner_intel.setAdapter(new ArrayAdapter(getContext(), 17367049, arrayList));
        this.spinner_intel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, android.widget.AdapterView] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemSelected(android.widget.AdapterView<?> r1, android.view.View r2, int r3, long r4) {
                /*
                    r0 = this;
                    android.widget.Adapter r1 = r1.getAdapter()
                    java.lang.Object r1 = r1.getItem(r3)
                    boolean r2 = r1 instanceof java.lang.String
                    if (r2 == 0) goto L_0x0035
                    java.lang.String r1 = (java.lang.String) r1
                    com.samsung.android.game.gos.ipm.IntelMode r1 = com.samsung.android.game.gos.ipm.IntelMode.valueOf(r1)
                    java.lang.StringBuilder r2 = new java.lang.StringBuilder
                    r2.<init>()
                    java.lang.String r3 = "Setting Intel mode to "
                    r2.append(r3)
                    r2.append(r1)
                    java.lang.String r2 = r2.toString()
                    java.lang.String r3 = "IpmFragment"
                    com.samsung.android.game.gos.test.util.GosTestLog.d(r3, r2)
                    com.samsung.android.game.gos.test.fragment.IpmFragment r2 = com.samsung.android.game.gos.test.fragment.IpmFragment.this
                    android.content.Context r2 = r2.getContext()
                    com.samsung.android.game.gos.feature.ipm.IpmCore r2 = com.samsung.android.game.gos.feature.ipm.IpmCore.getInstance(r2)
                    r2.setIntelMode(r1)
                L_0x0035:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.fragment.IpmFragment.AnonymousClass2.onItemSelected(android.widget.AdapterView, android.view.View, int, long):void");
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                globalDao.setIpmMode(new Global.IdAndIpmMode(0));
            }
        });
        Switch switchR12 = (Switch) view.findViewById(R.id.switch_stabiity_mode);
        this.swt_stability_mode = switchR12;
        switchR12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$14$IpmFragment(compoundButton, z);
            }
        });
        Switch switchR13 = (Switch) view.findViewById(R.id.switch_enable_custom_tfps);
        this.swt_custom_tfps = switchR13;
        switchR13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$15$IpmFragment(compoundButton, z);
            }
        });
        Switch switchR14 = (Switch) view.findViewById(R.id.switch_enable_frame_interpolation);
        this.swt_enable_frame_interpolation = switchR14;
        switchR14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$16$IpmFragment(compoundButton, z);
            }
        });
        EditText editText = (EditText) view.findViewById(R.id.edittext_frame_interpolation_temperature_offset);
        this.text_frame_interpolation_temperature_offset = editText;
        editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                try {
                    IpmCore.getInstance(IpmFragment.this.getContext()).setFrameInterpolationTemperatureOffset(Float.parseFloat(IpmFragment.this.text_frame_interpolation_temperature_offset.getText().toString()));
                } catch (NumberFormatException unused) {
                }
            }
        });
        EditText editText2 = (EditText) view.findViewById(R.id.edittext_frame_interpolation_frame_rate_offset);
        this.text_frame_interpolation_frame_rate_offset = editText2;
        editText2.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                try {
                    IpmCore.getInstance(IpmFragment.this.getContext()).setFrameInterpolationFrameRateOffset(Float.parseFloat(IpmFragment.this.text_frame_interpolation_frame_rate_offset.getText().toString()));
                } catch (NumberFormatException unused) {
                }
            }
        });
        EditText editText3 = (EditText) view.findViewById(R.id.edittext_frame_interpolation_decay_half_life);
        this.text_frame_interpolation_decay_half_life = editText3;
        editText3.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                try {
                    IpmCore.getInstance(IpmFragment.this.getContext()).setFrameInterpolationDecayHalfLife(Float.parseFloat(IpmFragment.this.text_frame_interpolation_decay_half_life.getText().toString()));
                } catch (NumberFormatException unused) {
                }
            }
        });
        Switch switchR15 = (Switch) view.findViewById(R.id.switch_enable_bus_min_freq_control);
        this.swt_enable_bus_min_freq_control = switchR15;
        switchR15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                IpmFragment.this.lambda$init$17$IpmFragment(compoundButton, z);
            }
        });
        this.text_stats = (TextView) view.findViewById(R.id.text_statsResults);
        this.text_tc = (EditText) view.findViewById(R.id.editText_tc);
        Button button4 = (Button) view.findViewById(R.id.btn_tcreset);
        this.btn_tcreset = button4;
        button4.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                IpmFragment.this.lambda$init$18$IpmFragment(view);
            }
        });
        Button button5 = (Button) view.findViewById(R.id.btn_tcset);
        this.btn_tcset = button5;
        button5.setOnClickListener(new View.OnClickListener(globalDao) {
            public final /* synthetic */ GlobalDao f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                IpmFragment.this.lambda$init$19$IpmFragment(this.f$1, view);
            }
        });
        this.text_tc.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                IpmFragment.this.btn_tcset.callOnClick();
            }
        });
        Button button6 = (Button) view.findViewById(R.id.btn_debug_mode);
        this.btn_debug_mode = button6;
        button6.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                IpmFragment.this.lambda$init$20$IpmFragment(view);
            }
        });
        this.text_pst = (EditText) view.findViewById(R.id.editText_pst);
        Button button7 = (Button) view.findViewById(R.id.btn_pstreset);
        this.btn_pstreset = button7;
        button7.setEnabled(!this.swt_enable_lrpst.isChecked());
        this.btn_pstreset.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                IpmFragment.this.lambda$init$21$IpmFragment(view);
            }
        });
        Button button8 = (Button) view.findViewById(R.id.btn_pstset);
        this.btn_pstset = button8;
        button8.setEnabled(!this.swt_enable_lrpst.isChecked());
        this.btn_pstset.setOnClickListener(new View.OnClickListener(globalDao) {
            public final /* synthetic */ GlobalDao f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                IpmFragment.this.lambda$init$22$IpmFragment(this.f$1, view);
            }
        });
        this.text_pst.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                IpmFragment.this.btn_pstset.callOnClick();
            }
        });
        this.text_lrpst = (EditText) view.findViewById(R.id.editText_lrpst);
        Button button9 = (Button) view.findViewById(R.id.btn_lrpstset);
        this.btn_lrpstset = button9;
        button9.setEnabled(this.swt_enable_lrpst.isChecked());
        this.btn_lrpstset.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                IpmFragment.this.lambda$init$23$IpmFragment(view);
            }
        });
        this.text_lrpst.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                IpmFragment.this.btn_lrpstset.callOnClick();
            }
        });
    }

    public /* synthetic */ void lambda$init$0$IpmFragment(View view) {
        Toast.makeText(getActivity(), deleteTrainingData(), 1).show();
    }

    public /* synthetic */ void lambda$init$1$IpmFragment(View view) {
        Toast.makeText(getActivity(), applyAllGamesJSON(), 1).show();
    }

    public /* synthetic */ void lambda$init$2$IpmFragment(GlobalFeatureFlagDao globalFeatureFlagDao, CompoundButton compoundButton, boolean z) {
        if (z) {
            globalFeatureFlagDao.setEnabledFlagByUser(new GlobalFeatureFlag.NameAndEnabledFlagByUser("ipm", true));
        } else {
            globalFeatureFlagDao.setEnabledFlagByUser(new GlobalFeatureFlag.NameAndEnabledFlagByUser("ipm", false));
        }
        if (z != globalFeatureFlagDao.isEnabledFlagByUser("ipm")) {
            this.swt_local.toggle();
        } else if (this.swt_server.isChecked() != z) {
            this.swt_server.toggle();
        }
    }

    public /* synthetic */ void lambda$init$4$IpmFragment(GlobalDao globalDao, CompoundButton compoundButton, boolean z) {
        boolean[] csvToBooleans = TypeConverter.csvToBooleans(globalDao.getIpmFlag());
        if (csvToBooleans != null) {
            csvToBooleans[0] = z;
            globalDao.setIpmFlag(new Global.IdAndIpmFlag(csvToBooleans));
            IpmCore.getInstance(getContext()).setSupertrain(z);
        }
    }

    public /* synthetic */ void lambda$init$5$IpmFragment(GlobalDao globalDao, CompoundButton compoundButton, boolean z) {
        boolean[] csvToBooleans = TypeConverter.csvToBooleans(globalDao.getIpmFlag());
        if (csvToBooleans != null) {
            csvToBooleans[1] = z;
            globalDao.setIpmFlag(new Global.IdAndIpmFlag(csvToBooleans));
            IpmCore.getInstance(getContext()).setLogLevel(IpmCore.getInstance(getContext()).getDefaultLogLevel());
        }
    }

    public /* synthetic */ void lambda$init$6$IpmFragment(GlobalDao globalDao, CompoundButton compoundButton, boolean z) {
        boolean[] csvToBooleans = TypeConverter.csvToBooleans(globalDao.getIpmFlag());
        if (csvToBooleans != null) {
            csvToBooleans[4] = z;
            globalDao.setIpmFlag(new Global.IdAndIpmFlag(csvToBooleans));
            IpmCore.getInstance(getContext()).setOnlyCapture(z);
        }
    }

    public /* synthetic */ void lambda$init$7$IpmFragment(CompoundButton compoundButton, boolean z) {
        IpmCore.getInstance(getContext()).setEnableCpuMinFreqControl(z);
        IpmCore.getInstance(getContext()).setCpuGap(z ? -2 : -1);
        GosTestLog.d(LOG_TAG, "Setting CPU MinFreq Control to " + z);
    }

    public /* synthetic */ void lambda$init$8$IpmFragment(CompoundButton compoundButton, boolean z) {
        IpmCore.getInstance(getContext()).setEnableGpuMinFreqControl(z);
        IpmCore.getInstance(getContext()).setGpuGap(z ? -2 : -1);
        GosTestLog.d(LOG_TAG, "Setting GPU MinFreq Control to " + z);
    }

    public /* synthetic */ void lambda$init$9$IpmFragment(View view) {
        try {
            int parseInt = Integer.parseInt(this.text_gpu_min_boost.getText().toString());
            GosTestLog.d(LOG_TAG, "Setting Boost GPU to " + parseInt);
            IpmCore.getInstance(getContext()).setGpuMinBoost(parseInt);
        } catch (Exception unused) {
        }
    }

    public /* synthetic */ void lambda$init$10$IpmFragment(CompoundButton compoundButton, boolean z) {
        if (z) {
            this.btn_pstreset.setEnabled(false);
            this.btn_pstset.setEnabled(false);
            this.btn_lrpstset.setEnabled(true);
        } else {
            this.btn_pstreset.setEnabled(true);
            this.btn_pstset.setEnabled(true);
            this.btn_lrpstset.setEnabled(false);
        }
        IpmCore.getInstance(getContext()).setEnableLRPST(z);
        GosTestLog.d(LOG_TAG, "Setting Enable LRPST to " + z);
    }

    public /* synthetic */ void lambda$init$11$IpmFragment(CompoundButton compoundButton, boolean z) {
        IpmCore.getInstance(getContext()).setEnableAllowMlOff(z);
        IpmCore.getInstance(getContext()).setAllowMlOff(z);
        GosTestLog.d(LOG_TAG, "Setting AllowMLOff to " + z);
    }

    public /* synthetic */ void lambda$init$12$IpmFragment(CompoundButton compoundButton, boolean z) {
        IpmCore.getInstance(getContext()).setEnableBusFreq(z);
        GosTestLog.d(LOG_TAG, "Setting EnableBusFreq to " + z);
    }

    public /* synthetic */ void lambda$init$13$IpmFragment(CompoundButton compoundButton, boolean z) {
        IpmCore.getInstance(getContext()).setEnableAnyMode(z);
    }

    public /* synthetic */ void lambda$init$14$IpmFragment(CompoundButton compoundButton, boolean z) {
        IpmCore.getInstance(getContext()).setDynamicDecisions(z);
    }

    public /* synthetic */ void lambda$init$15$IpmFragment(CompoundButton compoundButton, boolean z) {
        int customTfpsFlags = IpmCore.getInstance(getContext()).getCustomTfpsFlags();
        if (customTfpsFlags == 0) {
            customTfpsFlags = 2300;
        }
        IpmCore instance = IpmCore.getInstance(getContext());
        if (!z) {
            customTfpsFlags = 0;
        }
        instance.setCustomTfpsFlags(customTfpsFlags);
    }

    public /* synthetic */ void lambda$init$16$IpmFragment(CompoundButton compoundButton, boolean z) {
        IpmCore.getInstance(getContext()).setFrameInterpolationEnabled(z);
    }

    public /* synthetic */ void lambda$init$17$IpmFragment(CompoundButton compoundButton, boolean z) {
        IpmCore.getInstance(getContext()).setEnableBusMinFreqControl(z);
        GosTestLog.d(LOG_TAG, "Setting Enable bus min freq control to " + z);
    }

    public /* synthetic */ void lambda$init$18$IpmFragment(View view) {
        this.text_tc.setText("-1");
        this.btn_tcset.callOnClick();
    }

    public /* synthetic */ void lambda$init$19$IpmFragment(GlobalDao globalDao, View view) {
        int ipmTargetPower = globalDao.getIpmTargetPower();
        try {
            int parseInt = Integer.parseInt(this.text_tc.getText().toString());
            if (parseInt != ipmTargetPower) {
                globalDao.setIpmTargetPower(new Global.IdAndIpmTargetPower(parseInt));
                this.text_tc.setText(String.format(Locale.US, "%d", new Object[]{Integer.valueOf(globalDao.getIpmTargetPower())}));
                this.text_tc.setSelection(this.text_tc.getText().length());
            }
        } catch (Exception unused) {
        }
    }

    public /* synthetic */ void lambda$init$20$IpmFragment(View view) {
        this.swt_run_any_mode.setChecked(true);
        this.swt_loglevel.setChecked(false);
    }

    public /* synthetic */ void lambda$init$21$IpmFragment(View view) {
        this.text_pst.setText(String.valueOf(GlobalDbHelper.getInstance().getIpmDefaultTemperature()));
        this.btn_pstset.callOnClick();
    }

    public /* synthetic */ void lambda$init$22$IpmFragment(GlobalDao globalDao, View view) {
        int ipmTargetTemperature = globalDao.getIpmTargetTemperature();
        try {
            int parseInt = Integer.parseInt(this.text_pst.getText().toString());
            if (parseInt != ipmTargetTemperature) {
                globalDao.setIpmTargetTemperature(new Global.IdAndIpmTargetTemperature(parseInt));
                this.text_pst.setText(String.format(Locale.US, "%d", new Object[]{Integer.valueOf(globalDao.getIpmTargetTemperature())}));
                this.text_pst.setSelection(this.text_pst.getText().length());
            }
            IpmCore.getInstance(getContext()).setTargetPST(parseInt);
        } catch (Exception unused) {
        }
    }

    public /* synthetic */ void lambda$init$23$IpmFragment(View view) {
        try {
            IpmCore.getInstance(getContext()).setLRPST(Integer.parseInt(this.text_lrpst.getText().toString()));
        } catch (Exception unused) {
        }
    }

    public void onResume() {
        GosTestLog.d(LOG_TAG, "onResume()");
        super.onResume();
        GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
        GlobalFeatureFlagDao globalFeatureFlagDao = DbHelper.getInstance().getGlobalFeatureFlagDao();
        IpmCore instance = IpmCore.getInstance(getContext());
        if ("inherited".equalsIgnoreCase(DbHelper.getInstance().getGlobalFeatureFlagDao().getState("ipm"))) {
            this.swt_server.setBackgroundColor(Color.parseColor("#88FF88"));
            this.swt_local.setEnabled(true);
        } else {
            this.swt_server.setBackgroundColor(Color.parseColor("#FF8888"));
            this.swt_local.setEnabled(false);
        }
        this.swt_server.setEnabled(false);
        this.swt_server.setChecked(globalFeatureFlagDao.isEnabledFlagByUser("ipm"));
        this.swt_local.setChecked(globalFeatureFlagDao.isEnabledFlagByUser("ipm"));
        this.swt_json_pkg.setChecked(IpmFeature.mUseJSONPolicy);
        this.spinner_mode.setSelection(globalDao.getIpmMode());
        this.swt_supertrain.setChecked(TypeConverter.csvToBooleans(globalDao.getIpmFlag())[0]);
        this.swt_loglevel.setChecked(TypeConverter.csvToBooleans(globalDao.getIpmFlag())[1]);
        this.swt_onlyCapture.setChecked(TypeConverter.csvToBooleans(globalDao.getIpmFlag())[4]);
        this.swt_cpu_minlock.setChecked(instance.getEnableCpuMinFreqControl());
        this.swt_gpu_minlock.setChecked(instance.getEnableGpuMinFreqControl());
        this.swt_allow_mloff.setChecked(instance.getEnableAllowMlOff());
        this.swt_enable_lrpst.setChecked(instance.getEnableLRPST());
        this.swt_enable_bus_freq.setChecked(false);
        this.swt_run_any_mode.setChecked(instance.getEnableAnyMode());
        this.swt_stability_mode.setChecked(instance.getDynamicDecisions());
        this.swt_enable_bus_min_freq_control.setChecked(instance.getEnableBusMinFreqControl());
        this.swt_custom_tfps.setChecked(instance.getCustomTfpsFlags() != 0);
        this.swt_enable_frame_interpolation.setChecked(instance.isFrameInterpolationEnabled());
        this.text_frame_interpolation_temperature_offset.setText(Float.toString(instance.getFrameInterpolationTemperatureOffset()));
        this.text_frame_interpolation_frame_rate_offset.setText(Float.toString(instance.getFrameInterpolationFrameRateOffset()));
        this.text_frame_interpolation_decay_half_life.setText(Float.toString(instance.getFrameInterpolationDecayHalfLife()));
        this.text_stats.setText(instance.getStatistics());
        this.text_tc.setText(String.format(Locale.US, "%d", new Object[]{Integer.valueOf(globalDao.getIpmTargetPower())}));
        this.text_pst.setText(String.format(Locale.US, "%d", new Object[]{Integer.valueOf(globalDao.getIpmTargetTemperature())}));
        this.text_lrpst.setText(String.format(Locale.US, "%d", new Object[]{Integer.valueOf(instance.getLRPST())}));
    }

    private void makeDirs() {
        File parentFile = ContextCompat.getExternalFilesDirs(getContext(), (String) null)[0].getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            if (parentFile.mkdirs()) {
                GosTestLog.d(LOG_TAG, "dir was made : " + parentFile.getParent());
            } else {
                GosTestLog.d(LOG_TAG, "The dir could not created. Please check application permission");
                Toast.makeText(getActivity(), "The dir could not created. Please check application permission", 1).show();
            }
        }
        File parentFile2 = ContextCompat.getExternalFilesDirs(getContext(), (String) null)[0].getParentFile();
        if (parentFile2 != null && !parentFile2.exists()) {
            if (parentFile2.mkdirs()) {
                GosTestLog.d(LOG_TAG, "dir was made : " + parentFile2.getParent());
                return;
            }
            GosTestLog.d(LOG_TAG, "dir could not created. Please check application permission");
            Toast.makeText(getActivity(), "dir could not created. Please check application permission", 1).show();
        }
    }

    private String deleteTrainingData() {
        StringBuilder sb = new StringBuilder();
        if (getActivity() == null) {
            sb.append("getActivity() is null!!!");
            return sb.toString();
        }
        for (String str : TRAINING_FILE_NAME) {
            File file = new File(getActivity().getFilesDir().getPath() + "/" + str);
            if (file.exists()) {
                GosTestLog.d(LOG_TAG, "file exists : " + file.getAbsolutePath());
                try {
                    if (FileUtil.delete(file)) {
                        sb.append("File was deleted. : ");
                        sb.append(str);
                    } else {
                        sb.append("Deleting failed. : ");
                        sb.append(str);
                    }
                } catch (SecurityException e) {
                    GosTestLog.w(LOG_TAG, (Throwable) e);
                    sb.append("Deleting failed. : ");
                    sb.append(str);
                    sb.append(", ");
                    sb.append(e.getMessage());
                }
            } else {
                sb.append("File does not exist. : ");
                sb.append(file.getAbsolutePath());
                GosTestLog.d(LOG_TAG, sb.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0063, code lost:
        if (r2.hasNext() == false) goto L_0x0084;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0065, code lost:
        r3 = r2.next();
        com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance().getPackageDao().setIpmPolicy(new com.samsung.android.game.gos.data.model.Package.PkgNameAndIpmPolicy(r3, r1));
        r0.append(r3);
        r0.append("\n");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0088, code lost:
        return r0.toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x008b, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0094, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0044, code lost:
        r0.append("Apply to:\n");
        r2 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(com.samsung.android.game.gos.value.Constants.CategoryCode.GAME);
        r1 = r1.toString();
        r2 = r2.iterator();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String applyAllGamesJSON() {
        /*
            r6 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.io.File r2 = new java.io.File
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.io.File r4 = android.os.Environment.getExternalStorageDirectory()
            java.lang.String r4 = r4.getAbsolutePath()
            r3.append(r4)
            java.lang.String r4 = "/IPM/all_games.json"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0095 }
            java.io.FileReader r4 = new java.io.FileReader     // Catch:{ IOException -> 0x0095 }
            r4.<init>(r2)     // Catch:{ IOException -> 0x0095 }
            r3.<init>(r4)     // Catch:{ IOException -> 0x0095 }
        L_0x0032:
            java.lang.String r2 = r3.readLine()     // Catch:{ all -> 0x0089 }
            if (r2 == 0) goto L_0x0041
            r1.append(r2)     // Catch:{ all -> 0x0089 }
            r2 = 10
            r1.append(r2)     // Catch:{ all -> 0x0089 }
            goto L_0x0032
        L_0x0041:
            r3.close()     // Catch:{ IOException -> 0x0095 }
            java.lang.String r2 = "Apply to:\n"
            r0.append(r2)
            com.samsung.android.game.gos.data.dbhelper.DbHelper r2 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.PackageDao r2 = r2.getPackageDao()
            java.lang.String r3 = "game"
            java.util.List r2 = r2.getPkgNameListByCategory(r3)
            java.lang.String r1 = r1.toString()
            java.util.Iterator r2 = r2.iterator()
        L_0x005f:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0084
            java.lang.Object r3 = r2.next()
            java.lang.String r3 = (java.lang.String) r3
            com.samsung.android.game.gos.data.dbhelper.DbHelper r4 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.PackageDao r4 = r4.getPackageDao()
            com.samsung.android.game.gos.data.model.Package$PkgNameAndIpmPolicy r5 = new com.samsung.android.game.gos.data.model.Package$PkgNameAndIpmPolicy
            r5.<init>(r3, r1)
            r4.setIpmPolicy(r5)
            r0.append(r3)
            java.lang.String r3 = "\n"
            r0.append(r3)
            goto L_0x005f
        L_0x0084:
            java.lang.String r0 = r0.toString()
            return r0
        L_0x0089:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x008b }
        L_0x008b:
            r2 = move-exception
            r3.close()     // Catch:{ all -> 0x0090 }
            goto L_0x0094
        L_0x0090:
            r3 = move-exception
            r1.addSuppressed(r3)     // Catch:{ IOException -> 0x0095 }
        L_0x0094:
            throw r2     // Catch:{ IOException -> 0x0095 }
        L_0x0095:
            r1 = move-exception
            java.lang.String r2 = "IpmFragment"
            java.lang.String r3 = "applyAllGamesJSON(): "
            com.samsung.android.game.gos.test.util.GosTestLog.w(r2, r3, r1)
            java.lang.String r2 = "Exception reading JSON file"
            r0.append(r2)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.fragment.IpmFragment.applyAllGamesJSON():java.lang.String");
    }
}
