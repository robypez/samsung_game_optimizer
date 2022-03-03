package com.samsung.android.game.gos.test.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.endpoint.MethodsForJsonCommand;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;

public class DeviceInfoFragment extends BaseFragment {
    private static final String LOG_TAG = "DeviceInfoFragment";
    private TextView txt_buildType;
    private TextView txt_deviceName;
    private TextView txt_deviceNameDescription;
    private TextView txt_gameManagerVer;
    private TextView txt_gameOptimizerVer;
    private TextView txt_isDeviceSupported;
    private TextView txt_lastFullyUpdateTime;
    private TextView txt_lastUpdateTime;
    private TextView txt_modelName;
    private TextView txt_osSdkVer;
    private TextView txt_osVerI;
    private TextView txt_osVerR;
    private TextView txt_uuid;

    public int getNavItemId() {
        return R.id.nav_deviceInfo;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        GosTestLog.d(LOG_TAG, "onCreateView()");
        View inflate = layoutInflater.inflate(R.layout.fragment_device_info, viewGroup, false);
        init(inflate);
        return inflate;
    }

    private void init(View view) {
        GosTestLog.d(LOG_TAG, "init()");
        this.txt_deviceNameDescription = (TextView) view.findViewById(R.id.txt_deviceNameDescription);
        this.txt_deviceName = (TextView) view.findViewById(R.id.txt_deviceName);
        this.txt_modelName = (TextView) view.findViewById(R.id.txt_modelName);
        this.txt_osSdkVer = (TextView) view.findViewById(R.id.txt_osSdkVer);
        this.txt_osVerR = (TextView) view.findViewById(R.id.txt_osVerR);
        this.txt_osVerI = (TextView) view.findViewById(R.id.txt_osVerI);
        this.txt_buildType = (TextView) view.findViewById(R.id.txt_buildType);
        this.txt_isDeviceSupported = (TextView) view.findViewById(R.id.txt_isDeviceSupported);
        this.txt_gameManagerVer = (TextView) view.findViewById(R.id.txt_gameManagerVer);
        this.txt_gameOptimizerVer = (TextView) view.findViewById(R.id.txt_gameOptimizerVer);
        this.txt_uuid = (TextView) view.findViewById(R.id.txt_uuid);
        this.txt_lastUpdateTime = (TextView) view.findViewById(R.id.txt_lastUpdateTime);
        this.txt_lastFullyUpdateTime = (TextView) view.findViewById(R.id.txt_lastFullyUpdateTime);
    }

    public void onResume() {
        GosTestLog.d(LOG_TAG, "onResume()");
        super.onResume();
        new AsyncTask<Void, Void, Void>() {
            String mDeviceName;
            float mGmsVersion;
            long mLastFullyUpdateTime;
            long mLastUpdateTime;
            String response;

            /* access modifiers changed from: protected */
            public Void doInBackground(Void[] voidArr) {
                GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
                this.mDeviceName = globalDao.getDeviceName();
                this.mGmsVersion = globalDao.getGmsVersion();
                this.mLastUpdateTime = globalDao.getUpdateTime();
                this.mLastFullyUpdateTime = globalDao.getFullyUpdateTime();
                this.response = MethodsForJsonCommand.respondWithJson(GosInterface.Command.GET_GLOBAL_DATA, (String) null, (String) null);
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                super.onPostExecute(voidR);
                DeviceInfoFragment.this.getInfo(this.mDeviceName, this.mGmsVersion, this.mLastUpdateTime, this.mLastFullyUpdateTime, this.response);
            }
        }.execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x009b A[Catch:{ Exception -> 0x00aa }] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x00a5 A[Catch:{ Exception -> 0x00aa }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getInfo(java.lang.String r6, float r7, long r8, long r10, java.lang.String r12) {
        /*
            r5 = this;
            java.lang.String r0 = "is_device_supported_by_server"
            java.lang.String r1 = "uuid"
            java.lang.String r2 = "DeviceInfoFragment"
            java.lang.String r3 = "getInfo()"
            com.samsung.android.game.gos.test.util.GosTestLog.d(r2, r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Device name"
            r3.append(r4)
            java.lang.String r4 = com.samsung.android.game.gos.value.AppVariable.getOriginalDeviceName()
            boolean r4 = java.util.Objects.equals(r4, r6)
            if (r4 == 0) goto L_0x0022
            java.lang.String r4 = ""
            goto L_0x0024
        L_0x0022:
            java.lang.String r4 = " (fake)"
        L_0x0024:
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.widget.TextView r4 = r5.txt_deviceNameDescription
            r4.setText(r3)
            android.widget.TextView r3 = r5.txt_deviceName
            r3.setText(r6)
            android.widget.TextView r6 = r5.txt_modelName
            java.lang.String r3 = com.samsung.android.game.gos.value.AppVariable.getOriginalModelName()
            r6.setText(r3)
            android.widget.TextView r6 = r5.txt_osSdkVer
            int r3 = android.os.Build.VERSION.SDK_INT
            java.lang.String r3 = java.lang.String.valueOf(r3)
            r6.setText(r3)
            android.widget.TextView r6 = r5.txt_osVerR
            java.lang.String r3 = android.os.Build.VERSION.RELEASE
            r6.setText(r3)
            android.widget.TextView r6 = r5.txt_osVerI
            java.lang.String r3 = android.os.Build.VERSION.INCREMENTAL
            r6.setText(r3)
            android.widget.TextView r6 = r5.txt_buildType
            java.lang.String r3 = android.os.Build.TYPE
            r6.setText(r3)
            android.widget.TextView r6 = r5.txt_gameManagerVer
            java.lang.String r7 = java.lang.String.valueOf(r7)
            r6.setText(r7)
            androidx.fragment.app.FragmentActivity r6 = r5.getActivity()
            java.lang.Object r6 = java.util.Objects.requireNonNull(r6)
            androidx.fragment.app.FragmentActivity r6 = (androidx.fragment.app.FragmentActivity) r6
            android.content.pm.PackageManager r6 = r6.getPackageManager()
            if (r6 == 0) goto L_0x0086
            java.lang.String r7 = "com.samsung.android.game.gos"
            r3 = 128(0x80, float:1.794E-43)
            android.content.pm.PackageInfo r6 = r6.getPackageInfo(r7, r3)     // Catch:{ NameNotFoundException -> 0x0082 }
            java.lang.String r6 = r6.versionName     // Catch:{ NameNotFoundException -> 0x0082 }
            goto L_0x0088
        L_0x0082:
            r6 = move-exception
            com.samsung.android.game.gos.test.util.GosTestLog.w((java.lang.String) r2, (java.lang.Throwable) r6)
        L_0x0086:
            java.lang.String r6 = "3.5.01.18"
        L_0x0088:
            android.widget.TextView r7 = r5.txt_gameOptimizerVer
            r7.setText(r6)
            java.lang.String r6 = "null"
            r7 = 0
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x00aa }
            r3.<init>(r12)     // Catch:{ Exception -> 0x00aa }
            boolean r12 = r3.has(r1)     // Catch:{ Exception -> 0x00aa }
            if (r12 == 0) goto L_0x009f
            java.lang.String r6 = r3.getString(r1)     // Catch:{ Exception -> 0x00aa }
        L_0x009f:
            boolean r12 = r3.has(r0)     // Catch:{ Exception -> 0x00aa }
            if (r12 == 0) goto L_0x00b0
            boolean r7 = r3.getBoolean(r0)     // Catch:{ Exception -> 0x00aa }
            goto L_0x00b0
        L_0x00aa:
            r12 = move-exception
            java.lang.String r0 = "getInfo(): "
            com.samsung.android.game.gos.test.util.GosTestLog.w(r2, r0, r12)
        L_0x00b0:
            android.widget.TextView r12 = r5.txt_uuid
            r12.setText(r6)
            android.widget.TextView r6 = r5.txt_isDeviceSupported
            java.lang.String r7 = java.lang.String.valueOf(r7)
            r6.setText(r7)
            java.text.SimpleDateFormat r6 = new java.text.SimpleDateFormat
            java.util.Locale r7 = java.util.Locale.US
            java.lang.String r12 = "yyyy-MM-dd, hh:mm:ss a"
            r6.<init>(r12, r7)
            android.widget.TextView r7 = r5.txt_lastUpdateTime
            java.util.Date r12 = new java.util.Date
            r12.<init>(r8)
            java.lang.String r8 = r6.format(r12)
            r7.setText(r8)
            android.widget.TextView r7 = r5.txt_lastFullyUpdateTime
            java.util.Date r8 = new java.util.Date
            r8.<init>(r10)
            java.lang.String r6 = r6.format(r8)
            r7.setText(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.fragment.DeviceInfoFragment.getInfo(java.lang.String, float, long, long, java.lang.String):void");
    }
}
