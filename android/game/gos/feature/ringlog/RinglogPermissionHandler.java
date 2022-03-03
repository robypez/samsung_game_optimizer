package com.samsung.android.game.gos.feature.ringlog;

import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.type.PerfPermissionData;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PackageUtil;
import com.samsung.android.game.gos.value.RinglogConstants;
import com.samsung.android.game.gos.value.RinglogPermission;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RinglogPermissionHandler {
    private static final String TAG = "RinglogPerm";
    private static RinglogPermissionHandler sRinglogPermissionHandler;

    /* access modifiers changed from: package-private */
    public boolean match_signature() {
        return true;
    }

    private RinglogPermissionHandler() {
    }

    public static RinglogPermissionHandler getInstance() {
        if (sRinglogPermissionHandler == null) {
            sRinglogPermissionHandler = new RinglogPermissionHandler();
        }
        return sRinglogPermissionHandler;
    }

    private PerfPermissionData getPermission(String str) {
        return GlobalDbHelper.getInstance().getPermissionsForPkg(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x005b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isPermissionGranted(java.lang.String r6, com.samsung.android.game.gos.data.type.PerfPermissionData r7) {
        /*
            r5 = this;
            r0 = 1
            r1 = 0
            if (r7 != 0) goto L_0x0006
        L_0x0004:
            r0 = r1
            goto L_0x0057
        L_0x0006:
            com.samsung.android.game.gos.value.RinglogPermission$PERM_TYPES r2 = r7.getPermType()
            com.samsung.android.game.gos.value.RinglogPermission$PERM_TYPES r3 = com.samsung.android.game.gos.value.RinglogPermission.PERM_TYPES.ALLOW_ALL
            if (r2 != r3) goto L_0x000f
            goto L_0x0057
        L_0x000f:
            com.samsung.android.game.gos.value.RinglogPermission$PERM_TYPES r2 = r7.getPermType()
            com.samsung.android.game.gos.value.RinglogPermission$PERM_TYPES r3 = com.samsung.android.game.gos.value.RinglogPermission.PERM_TYPES.ALLOW_SOME
            java.lang.String r4 = ","
            if (r2 != r3) goto L_0x0030
            java.lang.String r2 = r7.getParamListCsv()
            if (r2 == 0) goto L_0x0030
            java.lang.String r7 = r7.getParamListCsv()
            java.lang.String[] r7 = r7.split(r4)
            java.util.List r7 = java.util.Arrays.asList(r7)
            boolean r0 = r7.contains(r6)
            goto L_0x0057
        L_0x0030:
            com.samsung.android.game.gos.value.RinglogPermission$PERM_TYPES r2 = r7.getPermType()
            com.samsung.android.game.gos.value.RinglogPermission$PERM_TYPES r3 = com.samsung.android.game.gos.value.RinglogPermission.PERM_TYPES.DENY_SOME
            if (r2 != r3) goto L_0x0050
            java.lang.String r2 = r7.getParamListCsv()
            if (r2 == 0) goto L_0x0050
            java.lang.String r7 = r7.getParamListCsv()
            java.lang.String[] r7 = r7.split(r4)
            java.util.List r7 = java.util.Arrays.asList(r7)
            boolean r6 = r7.contains(r6)
            r0 = r0 ^ r6
            goto L_0x0057
        L_0x0050:
            com.samsung.android.game.gos.value.RinglogPermission$PERM_TYPES r6 = r7.getPermType()
            com.samsung.android.game.gos.value.RinglogPermission$PERM_TYPES r7 = com.samsung.android.game.gos.value.RinglogPermission.PERM_TYPES.DENY_ALL
            goto L_0x0004
        L_0x0057:
            boolean r6 = com.samsung.android.game.gos.value.RinglogConstants.DEBUG
            if (r6 == 0) goto L_0x0071
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "isPermissionGranted isAllowed= "
            r6.append(r7)
            r6.append(r0)
            java.lang.String r6 = r6.toString()
            java.lang.String r7 = "RinglogPerm"
            com.samsung.android.game.gos.util.GosLog.i(r7, r6)
        L_0x0071:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.feature.ringlog.RinglogPermissionHandler.isPermissionGranted(java.lang.String, com.samsung.android.game.gos.data.type.PerfPermissionData):boolean");
    }

    private boolean isPermissionGranted(RinglogConstants.PerfParams perfParams, PerfPermissionData perfPermissionData) {
        return isPermissionGranted(perfParams.getName(), perfPermissionData);
    }

    /* access modifiers changed from: package-private */
    public boolean isCallerDisallowed() {
        return isCallerDisallowed((String) null);
    }

    /* access modifiers changed from: package-private */
    public boolean isCallerDisallowed(String str) {
        if ((str != null || (str = getCallerPkgName()) != null) && getPermission(str) == null && !str.equals("com.samsung.android.game.gos")) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean assignPermission(String str, RinglogPermission.PERM_POLICY perm_policy, RinglogPermission.PERM_TYPES perm_types, List<RinglogConstants.PerfParams> list) {
        if (perm_policy == RinglogPermission.PERM_POLICY.SIGNATURE) {
            GlobalDbHelper.getInstance().addPermissionData(str, RinglogPermission.PERM_POLICY.SIGNATURE, RinglogPermission.PERM_TYPES.ALLOW_ALL, (String) null, System.currentTimeMillis());
            return true;
        }
        String str2 = str;
        RinglogPermission.PERM_POLICY perm_policy2 = perm_policy;
        RinglogPermission.PERM_TYPES perm_types2 = perm_types;
        GlobalDbHelper.getInstance().addPermissionData(str2, perm_policy2, perm_types2, RinglogUtil.paramListToCsv(list), System.currentTimeMillis());
        return true;
    }

    /* access modifiers changed from: package-private */
    public Set<String> getAllowedParamsStr(String str) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        PerfPermissionData permissionsForPkg = GlobalDbHelper.getInstance().getPermissionsForPkg(str);
        boolean equals = "com.samsung.android.game.gos".equals(str);
        for (RinglogConstants.PerfParams perfParams : RinglogConstants.PerfParams.values()) {
            if (equals) {
                linkedHashSet.add(perfParams.getName());
            } else if (isPermissionGranted(perfParams, permissionsForPkg)) {
                linkedHashSet.add(perfParams.getName());
            }
        }
        return linkedHashSet;
    }

    /* access modifiers changed from: package-private */
    public Set<RinglogConstants.PerfParams> getAllowedParams(String str) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        PerfPermissionData permissionsForPkg = GlobalDbHelper.getInstance().getPermissionsForPkg(str);
        boolean equals = "com.samsung.android.game.gos".equals(str);
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getAllowedParams permData= " + permissionsForPkg);
        }
        for (RinglogConstants.PerfParams perfParams : RinglogConstants.PerfParams.values()) {
            if (equals) {
                linkedHashSet.add(perfParams);
            } else if (isPermissionGranted(perfParams, permissionsForPkg)) {
                linkedHashSet.add(perfParams);
            }
        }
        if (RinglogConstants.DEBUG) {
            GosLog.i(TAG, "getAllowedParams allowedParams= " + linkedHashSet.size());
        }
        return linkedHashSet;
    }

    /* access modifiers changed from: package-private */
    public boolean isPermissionGranted(String str, RinglogConstants.PerfParams perfParams) {
        PerfPermissionData permissionsForPkg = GlobalDbHelper.getInstance().getPermissionsForPkg(str);
        if ("com.samsung.android.game.gos".equals(str)) {
            return true;
        }
        return isPermissionGranted(perfParams, permissionsForPkg);
    }

    /* access modifiers changed from: package-private */
    public boolean isPermissionGrantedSystemStatus(String str) {
        PerfPermissionData permissionsForPkg = GlobalDbHelper.getInstance().getPermissionsForPkg(str);
        if ("com.samsung.android.game.gos".equals(str)) {
            return true;
        }
        return isPermissionGranted("system_status", permissionsForPkg);
    }

    static String getCallerPkgName() {
        return PackageUtil.getCallerPkgName();
    }
}
