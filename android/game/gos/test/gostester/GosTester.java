package com.samsung.android.game.gos.test.gostester;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.DataUploader;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.data.PkgData;
import com.samsung.android.game.gos.data.SystemDataHelper;
import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.FeatureHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.dbhelper.PackageDbHelper;
import com.samsung.android.game.gos.data.dbhelper.ReportDbHelper;
import com.samsung.android.game.gos.data.model.EventSubscription;
import com.samsung.android.game.gos.data.model.FeatureFlag;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.GlobalFeatureFlag;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.data.model.State;
import com.samsung.android.game.gos.feature.dfs.DfsFeature;
import com.samsung.android.game.gos.feature.dss.Dss;
import com.samsung.android.game.gos.feature.dss.DssFeature;
import com.samsung.android.game.gos.feature.dss.TssCore;
import com.samsung.android.game.gos.feature.gfi.GfiFeature;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicy;
import com.samsung.android.game.gos.feature.gfi.value.GfiPolicyException;
import com.samsung.android.game.gos.feature.gfi.value.GfiSettings;
import com.samsung.android.game.gos.feature.ipm.IpmCore;
import com.samsung.android.game.gos.feature.ipm.IpmFeature;
import com.samsung.android.game.gos.feature.resumeboost.LaunchBoostFeature;
import com.samsung.android.game.gos.feature.resumeboost.ResumeBoostCore;
import com.samsung.android.game.gos.feature.resumeboost.ResumeBoostFeature;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.ipm.IntelMode;
import com.samsung.android.game.gos.ipm.Profile;
import com.samsung.android.game.gos.network.NetworkConnector;
import com.samsung.android.game.gos.network.response.PerfPolicyResponse;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.selibrary.SeDvfsInterfaceImpl;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.selibrary.SeSysProp;
import com.samsung.android.game.gos.test.gostester.entity.FeatureDataEntity;
import com.samsung.android.game.gos.test.gostester.entity.GfiPackageDataEntity;
import com.samsung.android.game.gos.test.gostester.entity.GlobalDataEntity;
import com.samsung.android.game.gos.test.gostester.entity.GlobalDataItemEntity;
import com.samsung.android.game.gos.test.gostester.entity.IpmFeatureEntity;
import com.samsung.android.game.gos.test.gostester.entity.PackageDataEntity;
import com.samsung.android.game.gos.test.gostester.entity.ResumeBoostDataEntity;
import com.samsung.android.game.gos.test.gostester.value.GosTesterConstants;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.test.util.TestDataSetter;
import com.samsung.android.game.gos.test.value.IpmFragmentConst;
import com.samsung.android.game.gos.util.FileUtil;
import com.samsung.android.game.gos.util.PackageUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.jsoninterface.GosInterface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

public class GosTester {
    private static final String TAG = GosTester.class.getSimpleName();

    private GosTester() {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00ef, code lost:
        if (r0.equals(com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.TESTER_COMMAND_GET_GLOBAL_DATA) != false) goto L_0x0128;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getTestApiResponse(android.content.Context r8, java.lang.String r9) throws org.json.JSONException, com.samsung.android.game.gos.feature.gfi.value.GfiPolicyException {
        /*
            org.json.JSONObject r6 = new org.json.JSONObject
            r6.<init>()
            org.json.JSONObject r2 = new org.json.JSONObject
            r2.<init>(r9)
            java.lang.String r9 = "tester_command_id"
            boolean r0 = r2.has(r9)
            r1 = 0
            java.lang.String r3 = "successful"
            if (r0 != 0) goto L_0x001d
            r6.put(r3, r1)
            java.lang.String r8 = r6.toString()
            return r8
        L_0x001d:
            java.lang.String r0 = r2.getString(r9)
            com.samsung.android.game.gos.data.dbhelper.DbHelper r9 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.GlobalDao r4 = r9.getGlobalDao()
            r9 = 1
            r6.put(r3, r9)
            java.lang.String r3 = TAG
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r7 = "getTestApiResponse(), command: "
            r5.append(r7)
            r5.append(r0)
            java.lang.String r5 = r5.toString()
            com.samsung.android.game.gos.test.util.GosTestLog.d(r3, r5)
            com.samsung.android.game.gos.data.dbhelper.DbHelper r3 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao r5 = r3.getGlobalFeatureFlagDao()
            r3 = -1
            int r7 = r0.hashCode()
            switch(r7) {
                case -1885251322: goto L_0x011d;
                case -1509783555: goto L_0x0112;
                case -1503543474: goto L_0x0107;
                case -1378437262: goto L_0x00fc;
                case -1262691412: goto L_0x00f2;
                case -1156999709: goto L_0x00e9;
                case -746207680: goto L_0x00df;
                case -102562704: goto L_0x00d4;
                case 224115358: goto L_0x00c9;
                case 268192730: goto L_0x00be;
                case 329465688: goto L_0x00b3;
                case 368530225: goto L_0x00a7;
                case 483103770: goto L_0x009c;
                case 686555694: goto L_0x0090;
                case 979724798: goto L_0x0084;
                case 1267034597: goto L_0x0078;
                case 1777023412: goto L_0x006d;
                case 1957239231: goto L_0x0061;
                case 2111881032: goto L_0x0055;
                default: goto L_0x0053;
            }
        L_0x0053:
            goto L_0x0127
        L_0x0055:
            java.lang.String r9 = "setIpmGamePolicy"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 10
            goto L_0x0128
        L_0x0061:
            java.lang.String r9 = "deleteIpmTrainingData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 13
            goto L_0x0128
        L_0x006d:
            java.lang.String r9 = "setResumeBoost"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 6
            goto L_0x0128
        L_0x0078:
            java.lang.String r9 = "pullIpmTrainingData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 11
            goto L_0x0128
        L_0x0084:
            java.lang.String r9 = "getGfiData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 14
            goto L_0x0128
        L_0x0090:
            java.lang.String r9 = "setIpmData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 9
            goto L_0x0128
        L_0x009c:
            java.lang.String r9 = "getDeviceInfo"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 4
            goto L_0x0128
        L_0x00a7:
            java.lang.String r9 = "saveRinglogDumpToExternal"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 18
            goto L_0x0128
        L_0x00b3:
            java.lang.String r9 = "restoreData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 2
            goto L_0x0128
        L_0x00be:
            java.lang.String r9 = "getPackageData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 7
            goto L_0x0128
        L_0x00c9:
            java.lang.String r9 = "moveGosDbToExternal"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 16
            goto L_0x0128
        L_0x00d4:
            java.lang.String r9 = "pushIpmTrainingData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 12
            goto L_0x0128
        L_0x00df:
            java.lang.String r9 = "getResumeBoost"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 5
            goto L_0x0128
        L_0x00e9:
            java.lang.String r9 = "getGlobalData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            goto L_0x0128
        L_0x00f2:
            java.lang.String r9 = "uploadCombinationReportData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 3
            goto L_0x0128
        L_0x00fc:
            java.lang.String r9 = "setGfiData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 15
            goto L_0x0128
        L_0x0107:
            java.lang.String r9 = "setPackageData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 8
            goto L_0x0128
        L_0x0112:
            java.lang.String r9 = "getSysFileData"
            boolean r9 = r0.equals(r9)
            if (r9 == 0) goto L_0x0127
            r1 = 17
            goto L_0x0128
        L_0x011d:
            java.lang.String r1 = "getServerGlobalData"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0127
            r1 = r9
            goto L_0x0128
        L_0x0127:
            r1 = r3
        L_0x0128:
            switch(r1) {
                case 0: goto L_0x0179;
                case 1: goto L_0x0175;
                case 2: goto L_0x0171;
                case 3: goto L_0x016d;
                case 4: goto L_0x0169;
                case 5: goto L_0x0165;
                case 6: goto L_0x0161;
                case 7: goto L_0x015d;
                case 8: goto L_0x0159;
                case 9: goto L_0x0155;
                case 10: goto L_0x0151;
                case 11: goto L_0x014d;
                case 12: goto L_0x0149;
                case 13: goto L_0x0145;
                case 14: goto L_0x0141;
                case 15: goto L_0x013d;
                case 16: goto L_0x0139;
                case 17: goto L_0x0135;
                case 18: goto L_0x0131;
                default: goto L_0x012b;
            }
        L_0x012b:
            r1 = r6
            r3 = r8
            onEtcRequest(r0, r1, r2, r3, r4, r5)
            goto L_0x017c
        L_0x0131:
            onSaveRingLogDumpToExternal(r6, r8)
            goto L_0x017c
        L_0x0135:
            onGetSysFileData(r6, r2)
            goto L_0x017c
        L_0x0139:
            onMoveGosDbToExternal(r6, r8)
            goto L_0x017c
        L_0x013d:
            onSetGfiPackageData(r6, r2)
            goto L_0x017c
        L_0x0141:
            onGetGfiPackageData(r6, r2)
            goto L_0x017c
        L_0x0145:
            onDeleteIpmTrainingData(r6, r2, r8)
            goto L_0x017c
        L_0x0149:
            onPushIpmTrainingData(r6, r2, r8)
            goto L_0x017c
        L_0x014d:
            onPullIpmTrainingData(r6, r2, r8)
            goto L_0x017c
        L_0x0151:
            onSetIpmGamePolicy(r6, r8)
            goto L_0x017c
        L_0x0155:
            onSetIpmData(r6, r2, r8)
            goto L_0x017c
        L_0x0159:
            onSetPackageData(r6, r2)
            goto L_0x017c
        L_0x015d:
            onGetPackageData(r6, r2)
            goto L_0x017c
        L_0x0161:
            onSetResumeBoostData(r2)
            goto L_0x017c
        L_0x0165:
            onGetResumeBoostData(r6, r2, r5)
            goto L_0x017c
        L_0x0169:
            onGetDeviceInfo(r6, r8, r4)
            goto L_0x017c
        L_0x016d:
            onUploadCombinationReportData(r8)
            goto L_0x017c
        L_0x0171:
            onRestoreData(r6, r2, r8)
            goto L_0x017c
        L_0x0175:
            onGetServerGlobalData(r6, r8, r5)
            goto L_0x017c
        L_0x0179:
            onGetGlobalData(r6)
        L_0x017c:
            java.lang.String r8 = r6.toString()
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.gostester.GosTester.getTestApiResponse(android.content.Context, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void onEtcRequest(java.lang.String r6, org.json.JSONObject r7, org.json.JSONObject r8, android.content.Context r9, com.samsung.android.game.gos.data.dao.GlobalDao r10, com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao r11) throws org.json.JSONException {
        /*
            int r0 = r6.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case -1759627758: goto L_0x00a4;
                case -1431618486: goto L_0x009a;
                case -1250249542: goto L_0x008f;
                case -1221769105: goto L_0x0085;
                case -971712988: goto L_0x007b;
                case -893759833: goto L_0x0071;
                case -221147603: goto L_0x0066;
                case -149026212: goto L_0x005c;
                case 421743611: goto L_0x0051;
                case 469035952: goto L_0x0046;
                case 483240439: goto L_0x003a;
                case 1051770152: goto L_0x002f;
                case 1072098986: goto L_0x0023;
                case 1257371651: goto L_0x0017;
                case 1982851510: goto L_0x000b;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x00ae
        L_0x000b:
            java.lang.String r0 = "setTargetServer"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 12
            goto L_0x00af
        L_0x0017:
            java.lang.String r0 = "setDeviceName"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 11
            goto L_0x00af
        L_0x0023:
            java.lang.String r0 = "getTargetServer"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 9
            goto L_0x00af
        L_0x002f:
            java.lang.String r0 = "setDefinedEnabledFeatureFlag"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 7
            goto L_0x00af
        L_0x003a:
            java.lang.String r0 = "getDeviceName"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 10
            goto L_0x00af
        L_0x0046:
            java.lang.String r0 = "setServerDefinedFeatureFlagPolicy"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 6
            goto L_0x00af
        L_0x0051:
            java.lang.String r0 = "applyGlobalData"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 13
            goto L_0x00af
        L_0x005c:
            java.lang.String r0 = "isAutomaticSync"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = r2
            goto L_0x00af
        L_0x0066:
            java.lang.String r0 = "getGamePkgNameList"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 8
            goto L_0x00af
        L_0x0071:
            java.lang.String r0 = "isUsingServerData"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 5
            goto L_0x00af
        L_0x007b:
            java.lang.String r0 = "setAutomaticSync"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 3
            goto L_0x00af
        L_0x0085:
            java.lang.String r0 = "setUsingServerData"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 4
            goto L_0x00af
        L_0x008f:
            java.lang.String r0 = "getIpmData"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 14
            goto L_0x00af
        L_0x009a:
            java.lang.String r0 = "isAutomaticUpdate"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = r1
            goto L_0x00af
        L_0x00a4:
            java.lang.String r0 = "setAutomaticUpdate"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x00ae
            r6 = 2
            goto L_0x00af
        L_0x00ae:
            r6 = -1
        L_0x00af:
            java.lang.String r0 = "parameter_int"
            java.lang.String r3 = "parameter_feature_flag"
            java.lang.String r4 = "parameter_string"
            java.lang.String r5 = "parameter_boolean"
            switch(r6) {
                case 0: goto L_0x01db;
                case 1: goto L_0x01db;
                case 2: goto L_0x01ae;
                case 3: goto L_0x01a1;
                case 4: goto L_0x017c;
                case 5: goto L_0x0155;
                case 6: goto L_0x0143;
                case 7: goto L_0x012d;
                case 8: goto L_0x0116;
                case 9: goto L_0x010d;
                case 10: goto L_0x0104;
                case 11: goto L_0x00f6;
                case 12: goto L_0x00e0;
                case 13: goto L_0x00db;
                case 14: goto L_0x00c1;
                default: goto L_0x00ba;
            }
        L_0x00ba:
            java.lang.String r6 = "successful"
            r7.put(r6, r1)
            goto L_0x01e2
        L_0x00c1:
            com.samsung.android.game.gos.test.gostester.entity.IpmFeatureEntity r6 = new com.samsung.android.game.gos.test.gostester.entity.IpmFeatureEntity
            r6.<init>()
            com.samsung.android.game.gos.feature.ipm.IpmCore r8 = com.samsung.android.game.gos.feature.ipm.IpmCore.getInstance(r9)
            populateIPMEntity(r8, r6)
            com.google.gson.Gson r8 = new com.google.gson.Gson
            r8.<init>()
            java.lang.String r6 = r8.toJson((java.lang.Object) r6)
            r7.put(r4, r6)
            goto L_0x01e2
        L_0x00db:
            com.samsung.android.game.gos.test.util.TestDataSetter.applyGlobalData()
            goto L_0x01e2
        L_0x00e0:
            int r6 = r8.getInt(r0)
            com.samsung.android.game.gos.data.model.Global$IdAndRegisteredDevice r7 = new com.samsung.android.game.gos.data.model.Global$IdAndRegisteredDevice
            r7.<init>(r1)
            r10.setRegisteredDevice(r7)
            com.samsung.android.game.gos.network.NetworkConnector r7 = new com.samsung.android.game.gos.network.NetworkConnector
            r7.<init>(r9)
            r7.setTargetServer(r6)
            goto L_0x01e2
        L_0x00f6:
            java.lang.String r6 = r8.getString(r4)
            com.samsung.android.game.gos.data.model.Global$IdAndDeviceName r7 = new com.samsung.android.game.gos.data.model.Global$IdAndDeviceName
            r7.<init>(r6)
            r10.setDeviceName(r7)
            goto L_0x01e2
        L_0x0104:
            java.lang.String r6 = r10.getDeviceName()
            r7.put(r4, r6)
            goto L_0x01e2
        L_0x010d:
            int r6 = r10.getTargetServer()
            r7.put(r0, r6)
            goto L_0x01e2
        L_0x0116:
            com.samsung.android.game.gos.data.dbhelper.DbHelper r6 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.PackageDao r6 = r6.getPackageDao()
            java.lang.String r8 = "game"
            java.util.List r6 = r6.getPkgNameListByCategory(r8)
            java.lang.String r6 = com.samsung.android.game.gos.util.TypeConverter.stringsToCsv((java.lang.Iterable<java.lang.String>) r6)
            r7.put(r4, r6)
            goto L_0x01e2
        L_0x012d:
            boolean r6 = r8.getBoolean(r5)
            java.lang.String r7 = r8.getString(r3)
            com.samsung.android.game.gos.data.model.GlobalFeatureFlag$NameAndEnabledFlagByUser[] r8 = new com.samsung.android.game.gos.data.model.GlobalFeatureFlag.NameAndEnabledFlagByUser[r2]
            com.samsung.android.game.gos.data.model.GlobalFeatureFlag$NameAndEnabledFlagByUser r9 = new com.samsung.android.game.gos.data.model.GlobalFeatureFlag$NameAndEnabledFlagByUser
            r9.<init>(r7, r6)
            r8[r1] = r9
            r11.setEnabledFlagByUser(r8)
            goto L_0x01e2
        L_0x0143:
            java.lang.String r6 = r8.getString(r4)
            java.lang.String r7 = r8.getString(r3)
            com.samsung.android.game.gos.data.model.GlobalFeatureFlag$NameAndState r8 = new com.samsung.android.game.gos.data.model.GlobalFeatureFlag$NameAndState
            r8.<init>(r7, r6)
            r11.setState(r8)
            goto L_0x01e2
        L_0x0155:
            com.samsung.android.game.gos.data.dbhelper.DbHelper r6 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao r6 = r6.getGlobalFeatureFlagDao()
            com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$GlobalFeatureFlag[] r8 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.GlobalFeatureFlag.values()
            int r9 = r8.length
            r10 = r1
        L_0x0163:
            if (r10 >= r9) goto L_0x0176
            r11 = r8[r10]
            java.lang.String r11 = r11.getFeatureName()
            boolean r11 = r6.isUsingUserValue(r11)
            if (r11 == 0) goto L_0x0173
            r1 = r2
            goto L_0x0176
        L_0x0173:
            int r10 = r10 + 1
            goto L_0x0163
        L_0x0176:
            r6 = r1 ^ 1
            r7.put(r5, r6)
            goto L_0x01e2
        L_0x017c:
            boolean r6 = r8.getBoolean(r5)
            com.samsung.android.game.gos.data.dbhelper.DbHelper r7 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao r7 = r7.getGlobalFeatureFlagDao()
            r6 = r6 ^ r2
            com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$GlobalFeatureFlag[] r8 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.GlobalFeatureFlag.values()
            int r9 = r8.length
        L_0x018e:
            if (r1 >= r9) goto L_0x01e2
            r10 = r8[r1]
            com.samsung.android.game.gos.data.model.GlobalFeatureFlag$NameAndUsingUserValue r11 = new com.samsung.android.game.gos.data.model.GlobalFeatureFlag$NameAndUsingUserValue
            java.lang.String r10 = r10.getFeatureName()
            r11.<init>(r10, r6)
            r7.setUsingUserValue(r11)
            int r1 = r1 + 1
            goto L_0x018e
        L_0x01a1:
            boolean r6 = r8.getBoolean(r5)
            com.samsung.android.game.gos.data.model.Global$IdAndAutomaticUpdate r7 = new com.samsung.android.game.gos.data.model.Global$IdAndAutomaticUpdate
            r7.<init>(r6)
            r10.setAutomaticUpdate(r7)
            goto L_0x01e2
        L_0x01ae:
            boolean r6 = r8.getBoolean(r5)
            com.samsung.android.game.gos.data.model.Global$IdAndAutomaticUpdate r7 = new com.samsung.android.game.gos.data.model.Global$IdAndAutomaticUpdate
            r7.<init>(r6)
            r10.setAutomaticUpdate(r7)
            com.samsung.android.game.gos.data.dbhelper.DbHelper r7 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
            com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao r7 = r7.getGlobalFeatureFlagDao()
            r6 = r6 ^ r2
            com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$GlobalFeatureFlag[] r8 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.GlobalFeatureFlag.values()
            int r9 = r8.length
        L_0x01c8:
            if (r1 >= r9) goto L_0x01e2
            r10 = r8[r1]
            com.samsung.android.game.gos.data.model.GlobalFeatureFlag$NameAndUsingUserValue r11 = new com.samsung.android.game.gos.data.model.GlobalFeatureFlag$NameAndUsingUserValue
            java.lang.String r10 = r10.getFeatureName()
            r11.<init>(r10, r6)
            r7.setUsingUserValue(r11)
            int r1 = r1 + 1
            goto L_0x01c8
        L_0x01db:
            boolean r6 = r10.isAutomaticUpdate()
            r7.put(r5, r6)
        L_0x01e2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.gostester.GosTester.onEtcRequest(java.lang.String, org.json.JSONObject, org.json.JSONObject, android.content.Context, com.samsung.android.game.gos.data.dao.GlobalDao, com.samsung.android.game.gos.data.dao.GlobalFeatureFlagDao):void");
    }

    private static void onRestoreData(JSONObject jSONObject, JSONObject jSONObject2, final Context context) throws JSONException {
        String[] strArr = null;
        String string = jSONObject2.has(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING) ? jSONObject2.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING) : null;
        if (string != null) {
            strArr = TypeConverter.csvToStrings(string);
        }
        final String callerPkgName = PackageUtil.getCallerPkgName();
        if (strArr == null || strArr.length == 0 || callerPkgName == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            jSONObject.put(GosInterface.KeyName.REPORT_MSG, "wrong params");
            return;
        }
        TestDataSetter.DataRangeToBeSet valueOf = TestDataSetter.DataRangeToBeSet.valueOf(strArr[0]);
        String str = strArr[1];
        String str2 = strArr[2];
        final EventSubscription eventSubscription = new EventSubscription("dummy", callerPkgName);
        eventSubscription.intentActionName = str2;
        eventSubscription.flags = 0;
        new TestDataSetter(context, new TestDataSetter.DataSettingFeedBack() {
            public void onRestore(TestDataSetter.DataRangeToBeSet dataRangeToBeSet, String str) {
                String str2;
                HashMap hashMap = new HashMap();
                hashMap.put("success", "true");
                if (dataRangeToBeSet == null) {
                    str2 = BuildConfig.VERSION_NAME;
                } else {
                    str2 = dataRangeToBeSet.name();
                }
                hashMap.put("message", str2);
                EventPublisher.publishEvent(context, eventSubscription, "callback", callerPkgName, (Map<String, String>) hashMap);
            }

            public void onRestoreFailed(String str) {
                HashMap hashMap = new HashMap();
                hashMap.put("success", "false");
                hashMap.put("message", str);
                EventPublisher.publishEvent(context, eventSubscription, "callback", callerPkgName, (Map<String, String>) hashMap);
            }
        }).restoreData(valueOf, str);
    }

    private static void onUploadCombinationReportData(Context context) {
        if (SystemDataHelper.isCollectingAgreedByUser(context)) {
            DataUploader.uploadCombinationReportData(context, new NetworkConnector(AppContext.get()));
            return;
        }
        boolean removeAll = ReportDbHelper.getInstance().removeAll();
        String str = TAG;
        GosTestLog.d(str, "User not agreed, remove Ringlog data. successful: " + removeAll);
    }

    private static void onGetDeviceInfo(JSONObject jSONObject, Context context, GlobalDao globalDao) throws JSONException {
        String str;
        String deviceName = globalDao.getDeviceName();
        StringBuilder sb = new StringBuilder();
        sb.append("Device name");
        sb.append(Objects.equals(AppVariable.getOriginalDeviceName(), deviceName) ? BuildConfig.VERSION_NAME : " (fake)");
        String sb2 = sb.toString();
        jSONObject.put(GosInterface.KeyName.DEVICE_NAME, deviceName);
        jSONObject.put("device_name_description", sb2);
        jSONObject.put("device_model", AppVariable.getOriginalModelName());
        jSONObject.put("ver_os_sdk", Build.VERSION.SDK_INT);
        jSONObject.put("ver_release", Build.VERSION.RELEASE);
        jSONObject.put("ver_incremental", Build.VERSION.INCREMENTAL);
        jSONObject.put("build_type", Build.TYPE);
        jSONObject.put("ver_gms", (double) globalDao.getGmsVersion());
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            try {
                str = packageManager.getPackageInfo("com.samsung.android.game.gos", 128).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                GosTestLog.w(TAG, (Throwable) e);
            }
            jSONObject.put("ver_game_optimizer", str);
            jSONObject.put(GosInterface.KeyName.UUID, GlobalDbHelper.getInstance().getUUID());
            jSONObject.put("is_device_supported", globalDao.isRegisteredDevice());
            jSONObject.put(GosInterface.KeyName.LAST_UPDATE_TIME, globalDao.getUpdateTime());
            jSONObject.put("last_full_update_time", globalDao.getFullyUpdateTime());
        }
        str = com.samsung.android.game.gos.BuildConfig.VERSION_NAME;
        jSONObject.put("ver_game_optimizer", str);
        jSONObject.put(GosInterface.KeyName.UUID, GlobalDbHelper.getInstance().getUUID());
        jSONObject.put("is_device_supported", globalDao.isRegisteredDevice());
        jSONObject.put(GosInterface.KeyName.LAST_UPDATE_TIME, globalDao.getUpdateTime());
        jSONObject.put("last_full_update_time", globalDao.getFullyUpdateTime());
    }

    private static void onGetResumeBoostData(JSONObject jSONObject, JSONObject jSONObject2, GlobalFeatureFlagDao globalFeatureFlagDao) throws JSONException {
        String string = jSONObject2.has(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING) ? jSONObject2.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING) : null;
        ResumeBoostDataEntity resumeBoostDataEntity = new ResumeBoostDataEntity();
        ResumeBoostDataEntity.BoostDataEntity boostDataEntity = new ResumeBoostDataEntity.BoostDataEntity();
        ResumeBoostDataEntity.BoostDataEntity boostDataEntity2 = new ResumeBoostDataEntity.BoostDataEntity();
        boostDataEntity.isAvailable = globalFeatureFlagDao.isAvailable(Constants.V4FeatureFlag.RESUME_BOOST);
        if (boostDataEntity.isAvailable) {
            boostDataEntity.isEnable = globalFeatureFlagDao.isEnabledFlagByUser(Constants.V4FeatureFlag.RESUME_BOOST);
        }
        boostDataEntity2.isAvailable = globalFeatureFlagDao.isAvailable(Constants.V4FeatureFlag.LAUNCH_BOOST);
        if (boostDataEntity2.isAvailable) {
            boostDataEntity2.isEnable = globalFeatureFlagDao.isEnabledFlagByUser(Constants.V4FeatureFlag.LAUNCH_BOOST);
        }
        resumeBoostDataEntity.feature.put(Integer.valueOf(Constants.BoostType.Resume.ordinal()), boostDataEntity);
        resumeBoostDataEntity.feature.put(Integer.valueOf(Constants.BoostType.Launch.ordinal()), boostDataEntity2);
        if (!TextUtils.isEmpty(string)) {
            populatePackageResumeBoostEntity(string, boostDataEntity, boostDataEntity2);
        } else {
            populateGlobalResumeBoostEntity(boostDataEntity, boostDataEntity2);
        }
        jSONObject.put(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING, new Gson().toJson((Object) resumeBoostDataEntity));
    }

    private static void onSetResumeBoostData(JSONObject jSONObject) throws JSONException {
        String string = jSONObject.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING);
        String string2 = jSONObject.has(GosTesterConstants.TESTER_COMMAND_PARAMETER_PACKAGE) ? jSONObject.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_PACKAGE) : null;
        int i = jSONObject.getInt(GosTesterConstants.TESTER_COMMAND_PARAMETER_INT);
        ResumeBoostDataEntity.BoostDataEntity boostDataEntity = (ResumeBoostDataEntity.BoostDataEntity) new Gson().fromJson(string, ResumeBoostDataEntity.BoostDataEntity.class);
        if (boostDataEntity == null) {
            return;
        }
        if (!TextUtils.isEmpty(string2)) {
            setPackageBoostEntity(Constants.BoostType.values()[i], string2, boostDataEntity);
        } else {
            ResumeBoostFeature.getInstance().changeSettings(i, boostDataEntity.durationSec, boostDataEntity.cpuIndex, boostDataEntity.busIndex);
        }
    }

    private static void onGetPackageData(JSONObject jSONObject, JSONObject jSONObject2) throws JSONException {
        String string = jSONObject2.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING);
        PkgData pkgData = PackageDbHelper.getInstance().getPkgData(string);
        if (pkgData == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            return;
        }
        PackageDataEntity packageDataEntity = new PackageDataEntity();
        packageDataEntity.package_name = string;
        packageDataEntity.category_code = pkgData.getCategoryCode();
        for (GosTesterConstants.PackageFeatureFlag packageFeatureFlag : GosTesterConstants.PackageFeatureFlag.values()) {
            packageDataEntity.features.put(Integer.valueOf(packageFeatureFlag.ordinal()), createFeature(packageFeatureFlag, pkgData));
        }
        jSONObject.put(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING, new Gson().toJson((Object) packageDataEntity));
    }

    private static void onSetPackageData(JSONObject jSONObject, JSONObject jSONObject2) throws JSONException {
        String string = jSONObject2.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_PACKAGE);
        FeatureDataEntity featureDataEntity = (FeatureDataEntity) new Gson().fromJson(jSONObject2.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING), FeatureDataEntity.class);
        if (featureDataEntity == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            return;
        }
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(string);
        if (packageR == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            return;
        }
        applyFeature(packageR, GosTesterConstants.PackageFeatureFlag.values()[featureDataEntity.featureIndex], (String) featureDataEntity.current_value, (String) featureDataEntity.default_value);
        DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
        GmsGlobalPackageDataSetter.getInstance().applySingleGame(string);
        SeActivityManager.getInstance().forceStopPackage(string);
    }

    private static void onSetIpmData(JSONObject jSONObject, JSONObject jSONObject2, Context context) throws JSONException {
        IpmFeatureEntity ipmFeatureEntity = (IpmFeatureEntity) new Gson().fromJson(jSONObject2.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING), IpmFeatureEntity.class);
        if (ipmFeatureEntity != null) {
            applyIPMFeatures(IpmCore.getInstance(context), ipmFeatureEntity);
        } else {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
        }
    }

    private static void onSetIpmGamePolicy(JSONObject jSONObject, Context context) throws JSONException {
        File backupFolderExist = getBackupFolderExist(context, context.getFilesDir());
        if (backupFolderExist == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            return;
        }
        File file = new File(backupFolderExist.getAbsolutePath() + IpmFragmentConst.FILE_JSON_APPLY);
        if (!file.exists()) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            jSONObject.put(GosInterface.KeyName.COMMENT, "File " + file.getPath() + " not exist");
            return;
        }
        StringBuilder sb = new StringBuilder();
        String string = FileUtil.getString(file);
        if (string == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            jSONObject.put(GosInterface.KeyName.COMMENT, "Can't read file " + file.getPath());
            return;
        }
        sb.append("Apply to:\n");
        for (String next : DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME)) {
            DbHelper.getInstance().getPackageDao().setIpmPolicy(new Package.PkgNameAndIpmPolicy(next, string));
            sb.append(next);
            sb.append("\n");
        }
        jSONObject.put(GosInterface.KeyName.SUCCESSFUL_ITEMS, sb);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00eb, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ec, code lost:
        if (r4 != null) goto L_0x00ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00f2, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        r1.addSuppressed(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00f6, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00f9, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00fa, code lost:
        if (r2 != null) goto L_0x00fc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0104, code lost:
        throw r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void onPullIpmTrainingData(org.json.JSONObject r12, org.json.JSONObject r13, android.content.Context r14) throws org.json.JSONException {
        /*
            java.io.File r0 = r14.getFilesDir()
            java.io.File r14 = getBackupFolderExist(r14, r0)
            r1 = 0
            java.lang.String r2 = "successful"
            if (r14 != 0) goto L_0x0011
            r12.put(r2, r1)
            return
        L_0x0011:
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r14 = r14.getAbsolutePath()
            r4.append(r14)
            java.lang.String r14 = "/IPM/PulledTrainingData"
            r4.append(r14)
            java.lang.String r14 = r4.toString()
            r3.<init>(r14)
            boolean r14 = r3.exists()
            if (r14 == 0) goto L_0x0034
            com.samsung.android.game.gos.util.FileUtil.deleteRecursive(r3)
        L_0x0034:
            java.lang.String r14 = "parameter_string"
            java.lang.String r13 = r13.getString(r14)
            java.util.List r13 = com.samsung.android.game.gos.util.TypeConverter.csvToStringList(r13)
            boolean r14 = r3.mkdirs()
            if (r14 == 0) goto L_0x0131
            boolean r14 = r0.exists()
            if (r14 == 0) goto L_0x0131
            boolean r14 = r0.isDirectory()
            if (r14 != 0) goto L_0x0052
            goto L_0x0131
        L_0x0052:
            java.lang.String[] r14 = r0.list()
            if (r14 != 0) goto L_0x005c
            r12.put(r2, r1)
            return
        L_0x005c:
            if (r13 == 0) goto L_0x0064
            boolean r1 = r13.isEmpty()
            if (r1 == 0) goto L_0x0068
        L_0x0064:
            java.util.List r13 = java.util.Arrays.asList(r14)
        L_0x0068:
            java.util.ArrayList r14 = new java.util.ArrayList
            r14.<init>()
            java.util.Iterator r13 = r13.iterator()
        L_0x0071:
            boolean r1 = r13.hasNext()
            if (r1 == 0) goto L_0x010b
            java.lang.Object r1 = r13.next()
            java.lang.String r1 = (java.lang.String) r1
            java.io.File r2 = new java.io.File
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = r0.getAbsolutePath()
            r4.append(r5)
            java.lang.String r5 = java.io.File.separator
            r4.append(r5)
            r4.append(r1)
            java.lang.String r4 = r4.toString()
            r2.<init>(r4)
            java.io.File r4 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = r3.getAbsolutePath()
            r5.append(r6)
            java.lang.String r6 = java.io.File.separator
            r5.append(r6)
            r5.append(r1)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            boolean r5 = r2.isDirectory()
            if (r5 == 0) goto L_0x00be
            goto L_0x0071
        L_0x00be:
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0105 }
            r5.<init>(r2)     // Catch:{ IOException -> 0x0105 }
            java.nio.channels.FileChannel r2 = r5.getChannel()     // Catch:{ IOException -> 0x0105 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ all -> 0x00f7 }
            r5.<init>(r4)     // Catch:{ all -> 0x00f7 }
            java.nio.channels.FileChannel r4 = r5.getChannel()     // Catch:{ all -> 0x00f7 }
            r8 = 0
            long r10 = r4.size()     // Catch:{ all -> 0x00e9 }
            r6 = r4
            r7 = r2
            r6.transferFrom(r7, r8, r10)     // Catch:{ all -> 0x00e9 }
            r14.add(r1)     // Catch:{ all -> 0x00e9 }
            if (r4 == 0) goto L_0x00e3
            r4.close()     // Catch:{ all -> 0x00f7 }
        L_0x00e3:
            if (r2 == 0) goto L_0x0071
            r2.close()     // Catch:{ IOException -> 0x0105 }
            goto L_0x0071
        L_0x00e9:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x00eb }
        L_0x00eb:
            r5 = move-exception
            if (r4 == 0) goto L_0x00f6
            r4.close()     // Catch:{ all -> 0x00f2 }
            goto L_0x00f6
        L_0x00f2:
            r4 = move-exception
            r1.addSuppressed(r4)     // Catch:{ all -> 0x00f7 }
        L_0x00f6:
            throw r5     // Catch:{ all -> 0x00f7 }
        L_0x00f7:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x00f9 }
        L_0x00f9:
            r4 = move-exception
            if (r2 == 0) goto L_0x0104
            r2.close()     // Catch:{ all -> 0x0100 }
            goto L_0x0104
        L_0x0100:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch:{ IOException -> 0x0105 }
        L_0x0104:
            throw r4     // Catch:{ IOException -> 0x0105 }
        L_0x0105:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0071
        L_0x010b:
            boolean r13 = r14.isEmpty()
            if (r13 == 0) goto L_0x0114
            java.lang.String r13 = "Nothing to pull"
            goto L_0x012b
        L_0x0114:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r0 = "Pulled: "
            r13.append(r0)
            java.lang.String r0 = ", "
            java.lang.String r14 = android.text.TextUtils.join(r0, r14)
            r13.append(r14)
            java.lang.String r13 = r13.toString()
        L_0x012b:
            java.lang.String r14 = "successful_items"
            r12.put(r14, r13)
            return
        L_0x0131:
            r12.put(r2, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.gostester.GosTester.onPullIpmTrainingData(org.json.JSONObject, org.json.JSONObject, android.content.Context):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x010b, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x010c, code lost:
        if (r4 != null) goto L_0x010e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0112, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        r1.addSuppressed(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0116, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0119, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x011a, code lost:
        if (r2 != null) goto L_0x011c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0124, code lost:
        throw r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void onPushIpmTrainingData(org.json.JSONObject r12, org.json.JSONObject r13, android.content.Context r14) throws org.json.JSONException {
        /*
            java.io.File r0 = r14.getFilesDir()
            java.io.File r14 = getBackupFolderExist(r14, r0)
            r1 = 0
            java.lang.String r2 = "successful"
            if (r14 != 0) goto L_0x0011
            r12.put(r2, r1)
            return
        L_0x0011:
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r14 = r14.getAbsolutePath()
            r4.append(r14)
            java.lang.String r14 = "/IPM/TrainingDataToBePushed"
            r4.append(r14)
            java.lang.String r14 = r4.toString()
            r3.<init>(r14)
            boolean r14 = r3.exists()
            if (r14 != 0) goto L_0x0054
            r12.put(r2, r1)
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "Folder "
            r13.append(r14)
            java.lang.String r14 = r3.getPath()
            r13.append(r14)
            java.lang.String r14 = " not exist"
            r13.append(r14)
            java.lang.String r13 = r13.toString()
            java.lang.String r14 = "comment"
            r12.put(r14, r13)
            return
        L_0x0054:
            java.lang.String r14 = "parameter_string"
            java.lang.String r13 = r13.getString(r14)
            java.util.List r13 = com.samsung.android.game.gos.util.TypeConverter.csvToStringList(r13)
            boolean r14 = r3.isDirectory()
            if (r14 != 0) goto L_0x0068
            r12.put(r2, r1)
            return
        L_0x0068:
            java.lang.String[] r14 = r3.list()
            if (r14 != 0) goto L_0x0072
            r12.put(r2, r1)
            return
        L_0x0072:
            if (r13 == 0) goto L_0x007a
            boolean r1 = r13.isEmpty()
            if (r1 == 0) goto L_0x007e
        L_0x007a:
            java.util.List r13 = java.util.Arrays.asList(r14)
        L_0x007e:
            java.util.ArrayList r14 = new java.util.ArrayList
            r14.<init>()
            java.util.Iterator r13 = r13.iterator()
        L_0x0087:
            boolean r1 = r13.hasNext()
            if (r1 == 0) goto L_0x012b
            java.lang.Object r1 = r13.next()
            java.lang.String r1 = (java.lang.String) r1
            java.io.File r2 = new java.io.File
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = r3.getAbsolutePath()
            r4.append(r5)
            java.lang.String r5 = java.io.File.separator
            r4.append(r5)
            r4.append(r1)
            java.lang.String r4 = r4.toString()
            r2.<init>(r4)
            boolean r4 = r2.isDirectory()
            if (r4 == 0) goto L_0x00b7
            goto L_0x0087
        L_0x00b7:
            java.io.File r4 = new java.io.File
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = r0.getAbsolutePath()
            r5.append(r6)
            java.lang.String r6 = java.io.File.separator
            r5.append(r6)
            r5.append(r1)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            boolean r5 = r4.exists()
            if (r5 == 0) goto L_0x00dd
            com.samsung.android.game.gos.util.FileUtil.delete(r4)
        L_0x00dd:
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0125 }
            r5.<init>(r2)     // Catch:{ IOException -> 0x0125 }
            java.nio.channels.FileChannel r2 = r5.getChannel()     // Catch:{ IOException -> 0x0125 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ all -> 0x0117 }
            r5.<init>(r4)     // Catch:{ all -> 0x0117 }
            java.nio.channels.FileChannel r4 = r5.getChannel()     // Catch:{ all -> 0x0117 }
            r8 = 0
            long r10 = r4.size()     // Catch:{ all -> 0x0109 }
            r6 = r4
            r7 = r2
            r6.transferFrom(r7, r8, r10)     // Catch:{ all -> 0x0109 }
            r14.add(r1)     // Catch:{ all -> 0x0109 }
            if (r4 == 0) goto L_0x0102
            r4.close()     // Catch:{ all -> 0x0117 }
        L_0x0102:
            if (r2 == 0) goto L_0x0087
            r2.close()     // Catch:{ IOException -> 0x0125 }
            goto L_0x0087
        L_0x0109:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x010b }
        L_0x010b:
            r5 = move-exception
            if (r4 == 0) goto L_0x0116
            r4.close()     // Catch:{ all -> 0x0112 }
            goto L_0x0116
        L_0x0112:
            r4 = move-exception
            r1.addSuppressed(r4)     // Catch:{ all -> 0x0117 }
        L_0x0116:
            throw r5     // Catch:{ all -> 0x0117 }
        L_0x0117:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0119 }
        L_0x0119:
            r4 = move-exception
            if (r2 == 0) goto L_0x0124
            r2.close()     // Catch:{ all -> 0x0120 }
            goto L_0x0124
        L_0x0120:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch:{ IOException -> 0x0125 }
        L_0x0124:
            throw r4     // Catch:{ IOException -> 0x0125 }
        L_0x0125:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0087
        L_0x012b:
            boolean r13 = r14.isEmpty()
            if (r13 == 0) goto L_0x0134
            java.lang.String r13 = "Nothing to push"
            goto L_0x014b
        L_0x0134:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r0 = "Pushed: "
            r13.append(r0)
            java.lang.String r0 = ", "
            java.lang.String r14 = android.text.TextUtils.join(r0, r14)
            r13.append(r14)
            java.lang.String r13 = r13.toString()
        L_0x014b:
            java.lang.String r14 = "successful_items"
            r12.put(r14, r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.gostester.GosTester.onPushIpmTrainingData(org.json.JSONObject, org.json.JSONObject, android.content.Context):void");
    }

    private static void onDeleteIpmTrainingData(JSONObject jSONObject, JSONObject jSONObject2, Context context) throws JSONException {
        String str;
        File filesDir = context.getFilesDir();
        List<String> csvToStringList = TypeConverter.csvToStringList(jSONObject2.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING));
        if (!filesDir.exists() || !filesDir.isDirectory()) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            return;
        }
        String[] list = filesDir.list();
        if (list == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            return;
        }
        if (csvToStringList == null || csvToStringList.isEmpty()) {
            csvToStringList = Arrays.asList(list);
        }
        ArrayList arrayList = new ArrayList();
        for (String next : csvToStringList) {
            File file = new File(filesDir.getAbsolutePath() + File.separator + next);
            if (!file.isDirectory() && FileUtil.delete(file)) {
                arrayList.add(next);
            }
        }
        if (arrayList.isEmpty()) {
            str = "Nothing to delete";
        } else {
            str = "Deleted: " + TextUtils.join(", ", arrayList);
        }
        jSONObject.put(GosInterface.KeyName.SUCCESSFUL_ITEMS, str);
    }

    private static void onGetGfiPackageData(JSONObject jSONObject, JSONObject jSONObject2) throws JSONException {
        String string = jSONObject2.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING);
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(string);
        if (packageR == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            return;
        }
        GfiPackageDataEntity gfiPackageDataEntity = new GfiPackageDataEntity();
        gfiPackageDataEntity.package_name = string;
        populateGfiEntity(packageR, initGfiPolicy(packageR), gfiPackageDataEntity);
        jSONObject.put(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING, new Gson().toJson((Object) gfiPackageDataEntity));
    }

    private static void onSetGfiPackageData(JSONObject jSONObject, JSONObject jSONObject2) throws JSONException, GfiPolicyException {
        GfiPackageDataEntity gfiPackageDataEntity = (GfiPackageDataEntity) new Gson().fromJson(jSONObject2.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING), GfiPackageDataEntity.class);
        if (gfiPackageDataEntity == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            return;
        }
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(gfiPackageDataEntity.package_name);
        if (packageR == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
        } else {
            applyGfiFeatures(packageR, gfiPackageDataEntity);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00da, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00db, code lost:
        if (r5 != null) goto L_0x00dd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00e1, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        r7.addSuppressed(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00e5, code lost:
        throw r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00e8, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00e9, code lost:
        if (r6 != null) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00f3, code lost:
        throw r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void onMoveGosDbToExternal(org.json.JSONObject r13, android.content.Context r14) throws org.json.JSONException {
        /*
            java.io.File r0 = r14.getFilesDir()
            java.io.File r0 = r0.getParentFile()
            java.lang.String r1 = "successful"
            r2 = 0
            if (r0 != 0) goto L_0x0011
            r13.put(r1, r2)
            return
        L_0x0011:
            java.lang.String r3 = android.os.Environment.getExternalStorageState()
            java.lang.String r4 = "mounted"
            boolean r3 = java.util.Objects.equals(r3, r4)
            if (r3 != 0) goto L_0x0021
            r13.put(r1, r2)
            return
        L_0x0021:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r0 = r0.getAbsolutePath()
            r3.append(r0)
            java.lang.String r0 = "/databases/"
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            java.io.File r3 = new java.io.File
            r3.<init>(r0)
            r0 = 0
            java.io.File[] r14 = androidx.core.content.ContextCompat.getExternalFilesDirs(r14, r0)
            int r14 = r14.length
            if (r14 != 0) goto L_0x0047
            r13.put(r1, r2)
            return
        L_0x0047:
            java.lang.String r14 = android.os.Environment.DIRECTORY_DOWNLOADS
            java.io.File r14 = android.os.Environment.getExternalStoragePublicDirectory(r14)
            if (r14 != 0) goto L_0x0053
            r13.put(r1, r2)
            return
        L_0x0053:
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r14 = r14.getAbsolutePath()
            r4.append(r14)
            java.lang.String r14 = "/gos_db/"
            r4.append(r14)
            java.lang.String r14 = r4.toString()
            r0.<init>(r14)
            boolean r14 = r0.exists()
            if (r14 == 0) goto L_0x0076
            com.samsung.android.game.gos.util.FileUtil.deleteRecursive(r0)
        L_0x0076:
            boolean r14 = r0.mkdirs()
            if (r14 == 0) goto L_0x010c
            boolean r14 = r3.exists()
            if (r14 == 0) goto L_0x010c
            boolean r14 = r3.isDirectory()
            if (r14 == 0) goto L_0x010c
            java.io.File[] r14 = r3.listFiles()
            if (r14 == 0) goto L_0x0108
            int r3 = r14.length
            r4 = r2
        L_0x0090:
            if (r4 >= r3) goto L_0x00fe
            r5 = r14[r4]
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ IOException -> 0x00f4 }
            r6.<init>(r5)     // Catch:{ IOException -> 0x00f4 }
            java.nio.channels.FileChannel r6 = r6.getChannel()     // Catch:{ IOException -> 0x00f4 }
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ all -> 0x00e6 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e6 }
            r8.<init>()     // Catch:{ all -> 0x00e6 }
            java.lang.String r9 = r0.getAbsolutePath()     // Catch:{ all -> 0x00e6 }
            r8.append(r9)     // Catch:{ all -> 0x00e6 }
            java.lang.String r9 = java.io.File.separator     // Catch:{ all -> 0x00e6 }
            r8.append(r9)     // Catch:{ all -> 0x00e6 }
            java.lang.String r5 = r5.getName()     // Catch:{ all -> 0x00e6 }
            r8.append(r5)     // Catch:{ all -> 0x00e6 }
            java.lang.String r5 = r8.toString()     // Catch:{ all -> 0x00e6 }
            r7.<init>(r5)     // Catch:{ all -> 0x00e6 }
            java.nio.channels.FileChannel r5 = r7.getChannel()     // Catch:{ all -> 0x00e6 }
            r9 = 0
            long r11 = r6.size()     // Catch:{ all -> 0x00d8 }
            r7 = r5
            r8 = r6
            r7.transferFrom(r8, r9, r11)     // Catch:{ all -> 0x00d8 }
            if (r5 == 0) goto L_0x00d2
            r5.close()     // Catch:{ all -> 0x00e6 }
        L_0x00d2:
            if (r6 == 0) goto L_0x00fb
            r6.close()     // Catch:{ IOException -> 0x00f4 }
            goto L_0x00fb
        L_0x00d8:
            r7 = move-exception
            throw r7     // Catch:{ all -> 0x00da }
        L_0x00da:
            r8 = move-exception
            if (r5 == 0) goto L_0x00e5
            r5.close()     // Catch:{ all -> 0x00e1 }
            goto L_0x00e5
        L_0x00e1:
            r5 = move-exception
            r7.addSuppressed(r5)     // Catch:{ all -> 0x00e6 }
        L_0x00e5:
            throw r8     // Catch:{ all -> 0x00e6 }
        L_0x00e6:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x00e8 }
        L_0x00e8:
            r7 = move-exception
            if (r6 == 0) goto L_0x00f3
            r6.close()     // Catch:{ all -> 0x00ef }
            goto L_0x00f3
        L_0x00ef:
            r6 = move-exception
            r5.addSuppressed(r6)     // Catch:{ IOException -> 0x00f4 }
        L_0x00f3:
            throw r7     // Catch:{ IOException -> 0x00f4 }
        L_0x00f4:
            r5 = move-exception
            r13.put(r1, r2)
            r5.printStackTrace()
        L_0x00fb:
            int r4 = r4 + 1
            goto L_0x0090
        L_0x00fe:
            java.lang.String r14 = r0.getAbsolutePath()
            java.lang.String r0 = "successful_items"
            r13.put(r0, r14)
            goto L_0x010f
        L_0x0108:
            r13.put(r1, r2)
            goto L_0x010f
        L_0x010c:
            r13.put(r1, r2)
        L_0x010f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.gostester.GosTester.onMoveGosDbToExternal(org.json.JSONObject, android.content.Context):void");
    }

    private static void onGetSysFileData(JSONObject jSONObject, JSONObject jSONObject2) throws JSONException {
        jSONObject.put(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING, SeGameManager.getInstance().readSysFile(jSONObject2.getString(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING)));
    }

    private static void onSaveRingLogDumpToExternal(JSONObject jSONObject, Context context) throws JSONException {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (externalStoragePublicDirectory == null) {
            jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
            return;
        }
        File file = new File(externalStoragePublicDirectory.getAbsolutePath() + "/gos_ringlog_dump/");
        boolean exists = file.exists();
        if (!exists) {
            exists = file.mkdirs();
        }
        if (exists) {
            String encodedRinglog = IpmCore.getInstance(context).getEncodedRinglog();
            String readableRinglog = IpmCore.getInstance(context).getReadableRinglog();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss", Locale.US);
            File file2 = new File(file, "ringlog_dump_" + simpleDateFormat.format(new Date()) + ".txt");
            if (file2.exists()) {
                file2.delete();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(("IPM:\n" + encodedRinglog + "\n\nRingLog:\n" + readableRinglog).getBytes());
                fileOutputStream.close();
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL_ITEMS, file2.getAbsolutePath());
            } catch (IOException e) {
                jSONObject.put(GosInterface.KeyName.SUCCESSFUL, false);
                e.printStackTrace();
            }
        }
    }

    private static void onGetGlobalData(JSONObject jSONObject) throws JSONException {
        Map<String, GlobalFeatureFlag> featureFlagMap = GlobalDbHelper.getInstance().getFeatureFlagMap();
        if (!featureFlagMap.isEmpty()) {
            GlobalDataEntity globalDataEntity = new GlobalDataEntity();
            for (GosTesterConstants.GlobalFeatureFlag globalFeatureFlag : GosTesterConstants.GlobalFeatureFlag.values()) {
                GlobalFeatureFlag globalFeatureFlag2 = featureFlagMap.get(globalFeatureFlag.getFeatureName());
                if (globalFeatureFlag2 != null) {
                    GlobalDataItemEntity globalDataItemEntity = new GlobalDataItemEntity();
                    globalDataItemEntity.isAvailable = globalFeatureFlag2.available;
                    globalDataItemEntity.state = globalFeatureFlag2.getState();
                    globalDataItemEntity.inheritedFlag = globalFeatureFlag2.inheritedFlag;
                    globalDataItemEntity.forcedFlag = globalFeatureFlag2.inheritedFlag ? Global.DefaultGlobalData.isForcedByDefault(globalFeatureFlag.getFeatureName()) : globalFeatureFlag2.forcedFlag;
                    globalDataItemEntity.enabledFlagByServer = globalFeatureFlag2.enabledFlagByServer;
                    globalDataItemEntity.enabledFlagByUser = globalFeatureFlag2.enabledFlagByUser;
                    globalDataItemEntity.defaultEnable = globalFeatureFlag2.inheritedFlag ? Global.DefaultGlobalData.isEnabledByDefault(globalFeatureFlag.getFeatureName()) : globalFeatureFlag2.enabledFlagByServer;
                    globalDataItemEntity.usingUserValue = globalFeatureFlag2.usingUserValue;
                    globalDataItemEntity.usingPkgValue = globalFeatureFlag2.usingPkgValue;
                    globalDataEntity.globalDataItemEntities.put(globalFeatureFlag.getFeatureName(), globalDataItemEntity);
                }
            }
            jSONObject.put(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING, new Gson().toJson((Object) globalDataEntity));
        }
    }

    private static void onGetServerGlobalData(JSONObject jSONObject, Context context, GlobalFeatureFlagDao globalFeatureFlagDao) throws JSONException {
        PerfPolicyResponse globalPerfPolicyData = new NetworkConnector(context).getGlobalPerfPolicyData(Constants.CallTrigger.TEST);
        if (globalPerfPolicyData != null) {
            GosTestLog.d(TAG, "getTestApiResponse: getServerGlobalData : " + globalPerfPolicyData);
            GlobalDataEntity globalDataEntity = new GlobalDataEntity();
            for (GosTesterConstants.GlobalFeatureFlag featureName : GosTesterConstants.GlobalFeatureFlag.values()) {
                String featureName2 = featureName.getFeatureName();
                GlobalDataItemEntity globalDataItemEntity = new GlobalDataItemEntity();
                FeatureFlag featureFlag = globalPerfPolicyData.getServerFeatureFlag().get(featureName2);
                if (featureFlag != null) {
                    globalDataItemEntity.isAvailable = globalFeatureFlagDao.isAvailable(featureName2);
                    globalDataItemEntity.state = featureFlag.getState();
                    globalDataItemEntity.inheritedFlag = featureFlag.isInheritedFlag();
                    globalDataItemEntity.forcedFlag = featureFlag.isInheritedFlag() ? Global.DefaultGlobalData.isForcedByDefault(featureName2) : featureFlag.isForcedFlag();
                    globalDataItemEntity.enabledFlagByServer = featureFlag.isEnabledFlagByServer();
                    globalDataItemEntity.enabledFlagByUser = featureFlag.isEnabledFlagByUser();
                    globalDataItemEntity.usingUserValue = globalFeatureFlagDao.isUsingUserValue(featureName2);
                    globalDataItemEntity.usingPkgValue = globalFeatureFlagDao.isUsingPkgValue(featureName2);
                    globalDataItemEntity.defaultEnable = featureFlag.isInheritedFlag() ? Global.DefaultGlobalData.isEnabledByDefault(featureName2) : featureFlag.isEnabledFlagByServer();
                    globalDataEntity.globalDataItemEntities.put(featureName2, globalDataItemEntity);
                }
            }
            jSONObject.put(GosTesterConstants.TESTER_COMMAND_PARAMETER_STRING, new Gson().toJson((Object) globalDataEntity));
        }
    }

    private static void applyFeature(Package packageR, GosTesterConstants.PackageFeatureFlag packageFeatureFlag, String str, String str2) {
        String str3 = TAG;
        GosTestLog.d(str3, "applyFeature - " + packageFeatureFlag.name() + ": " + str);
        switch (AnonymousClass2.$SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag[packageFeatureFlag.ordinal()]) {
            case 1:
                float[] eachModeDfsArray = packageR.getEachModeDfsArray();
                eachModeDfsArray[1] = Float.valueOf(str).floatValue();
                packageR.setEachModeDfs(TypeConverter.floatsToCsv(eachModeDfsArray));
                return;
            case 2:
                float[] eachModeDssArray = packageR.getEachModeDssArray();
                eachModeDssArray[1] = Float.valueOf(str).floatValue();
                packageR.setEachModeDss(eachModeDssArray);
                return;
            case 3:
                int[] csvToInts = TypeConverter.csvToInts(packageR.getEachModeTargetShortSide());
                csvToInts[1] = Integer.valueOf(str).intValue();
                packageR.setEachModeTargetShortSide(TypeConverter.intsToCsv(csvToInts));
                return;
            case 4:
                packageR.setDefaultCpuLevel(Integer.valueOf(str).intValue());
                return;
            case 5:
                packageR.setDefaultGpuLevel(Integer.valueOf(str).intValue());
                return;
            case 6:
                packageR.setShiftTemperature(Integer.valueOf(str).intValue());
                return;
            case 7:
                packageR.setSiopModePolicy(str);
                return;
            case 8:
                packageR.setVrrMinValue(Integer.valueOf(str).intValue());
                return;
            case 9:
                packageR.setVrrMaxValue(Integer.valueOf(str).intValue());
                return;
            case 10:
                if (!str.equals("null") && str.length() >= 5) {
                    packageR.tspPolicy = str;
                    return;
                }
                return;
            default:
                return;
        }
    }

    private static void createFeatureTss(FeatureDataEntity featureDataEntity, boolean z, PkgData pkgData) {
        featureDataEntity.is_available = z && TssCore.isAvailable();
        featureDataEntity.current_value = Integer.valueOf(getCurrentTSS(pkgData));
        if (TssCore.isAvailable()) {
            featureDataEntity.default_value = Integer.valueOf(TssCore.getDefaultTss(pkgData));
        } else {
            featureDataEntity.default_value = -1;
        }
        featureDataEntity.range_value = toString(new Integer[]{1, Integer.valueOf(DssFeature.getInstance().getDisplayShortSide())});
    }

    private static void createFeatureBoost(GosTesterConstants.PackageFeatureFlag packageFeatureFlag, FeatureDataEntity featureDataEntity, PkgData pkgData) {
        int i = AnonymousClass2.$SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag[packageFeatureFlag.ordinal()];
        if (i == 11) {
            featureDataEntity.is_available = isResumeLaunchAvailable(Constants.V4FeatureFlag.RESUME_BOOST);
            featureDataEntity.current_value = pkgData.getPkg().getResumeBoostPolicy();
            if (featureDataEntity.current_value == null) {
                featureDataEntity.current_value = "null";
            }
        } else if (i == 12) {
            featureDataEntity.is_available = isResumeLaunchAvailable(Constants.V4FeatureFlag.LAUNCH_BOOST);
            featureDataEntity.current_value = pkgData.getPkg().getLaunchBoostPolicy();
            if (featureDataEntity.current_value == null) {
                featureDataEntity.current_value = "null";
            }
        }
    }

    private static FeatureDataEntity createFeature(GosTesterConstants.PackageFeatureFlag packageFeatureFlag, PkgData pkgData) {
        PkgData pkgData2 = pkgData;
        boolean isAvailable = FeatureHelper.isAvailable(packageFeatureFlag.getFeatureName());
        boolean isEnabledFlagByUser = FeatureHelper.isEnabledFlagByUser(packageFeatureFlag.getFeatureName());
        GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
        FeatureDataEntity featureDataEntity = new FeatureDataEntity();
        featureDataEntity.is_available = isAvailable;
        featureDataEntity.is_global_enable = isEnabledFlagByUser;
        featureDataEntity.featureIndex = packageFeatureFlag.ordinal();
        int i = AnonymousClass2.$SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag[packageFeatureFlag.ordinal()];
        Float valueOf = Float.valueOf(1.0f);
        boolean z = true;
        switch (i) {
            case 1:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag2 = packageFeatureFlag;
                featureDataEntity.current_value = Float.valueOf(getCurrentDfs(pkgData));
                featureDataEntity.default_value = Float.valueOf(120.0f);
                featureDataEntity.range_value = toString(new Float[]{valueOf, Float.valueOf(120.0f)});
                break;
            case 2:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag3 = packageFeatureFlag;
                featureDataEntity.is_available = isAvailable && !TssCore.isAvailable();
                featureDataEntity.current_value = Float.valueOf(getCurrentDss(pkgData));
                featureDataEntity.default_value = Float.valueOf(100.0f);
                featureDataEntity.range_value = toString(new Float[]{valueOf, Float.valueOf(100.0f)});
                break;
            case 3:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag4 = packageFeatureFlag;
                createFeatureTss(featureDataEntity, isAvailable, pkgData2);
                break;
            case 4:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag5 = packageFeatureFlag;
                if (!isAvailable || SeSysProp.getCpuGpuLevelInstance().getCpuLevelsCsv() == null) {
                    z = false;
                }
                featureDataEntity.is_available = z;
                featureDataEntity.current_value = Integer.valueOf(pkgData.getDefaultCpuLevel());
                featureDataEntity.default_value = Integer.valueOf(globalDao.getDefaultCpuLevel());
                featureDataEntity.range_value = SeSysProp.getCpuGpuLevelInstance().getCpuLevelsCsv();
                break;
            case 5:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag6 = packageFeatureFlag;
                if (!isAvailable || SeSysProp.getCpuGpuLevelInstance().getGpuLevelsCsv() == null) {
                    z = false;
                }
                featureDataEntity.is_available = z;
                featureDataEntity.current_value = Integer.valueOf(pkgData.getDefaultGpuLevel());
                featureDataEntity.default_value = Integer.valueOf(globalDao.getDefaultGpuLevel());
                featureDataEntity.range_value = SeSysProp.getCpuGpuLevelInstance().getGpuLevelsCsv();
                break;
            case 6:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag7 = packageFeatureFlag;
                featureDataEntity.is_available = isAvailable && SeSysProp.getCpuGpuLevelInstance().getCpuLevelsCsv() != null;
                featureDataEntity.current_value = Integer.valueOf(pkgData.getPkg().getShiftTemperature());
                featureDataEntity.default_value = -1000;
                featureDataEntity.range_value = toString(new Integer[]{-10, 10});
                break;
            case 7:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag8 = packageFeatureFlag;
                featureDataEntity.current_value = pkgData.getSiopModePolicy();
                if (featureDataEntity.current_value == null) {
                    featureDataEntity.current_value = "null";
                    break;
                }
                break;
            case 8:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag9 = packageFeatureFlag;
                featureDataEntity.global_value = Integer.valueOf(globalDao.getVrrMinValue());
                featureDataEntity.current_value = Integer.valueOf(pkgData.getPkg().getVrrMinValue());
                featureDataEntity.range_value = toString(new Integer[]{48, 60, 96, 120});
                break;
            case 9:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag10 = packageFeatureFlag;
                featureDataEntity.global_value = Integer.valueOf(globalDao.getVrrMaxValue());
                featureDataEntity.current_value = Integer.valueOf(pkgData.getPkg().getVrrMaxValue());
                featureDataEntity.range_value = toString(new Integer[]{48, 60, 96, 120});
                break;
            case 10:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag11 = packageFeatureFlag;
                featureDataEntity.current_value = pkgData.getPkg().tspPolicy;
                if (featureDataEntity.current_value == null) {
                    featureDataEntity.current_value = "null";
                    break;
                }
                break;
            case 11:
            case 12:
                createFeatureBoost(packageFeatureFlag, featureDataEntity, pkgData2);
                break;
            default:
                GosTesterConstants.PackageFeatureFlag packageFeatureFlag12 = packageFeatureFlag;
                break;
        }
        GosTestLog.d(TAG, "createFeature - " + packageFeatureFlag.name() + ": " + featureDataEntity.current_value);
        return featureDataEntity;
    }

    private static boolean isResumeLaunchAvailable(String str) {
        return FeatureHelper.isAvailable(str) && FeatureHelper.isEnabledFlagByUser(str);
    }

    private static int getCurrentTSS(PkgData pkgData) {
        return TssCore.getTssValueByMode(Dss.getActualResolutionMode(pkgData, FeatureHelper.isUsingUserValue(Constants.V4FeatureFlag.RESOLUTION), FeatureHelper.isUsingPkgValue(Constants.V4FeatureFlag.RESOLUTION)), pkgData);
    }

    private static float getCurrentDss(PkgData pkgData) {
        boolean isUsingUserValue = FeatureHelper.isUsingUserValue(Constants.V4FeatureFlag.RESOLUTION);
        boolean isUsingPkgValue = FeatureHelper.isUsingPkgValue(Constants.V4FeatureFlag.RESOLUTION);
        return Dss.getDssByMode(Dss.getActualResolutionMode(pkgData, isUsingUserValue, isUsingPkgValue), pkgData, isUsingUserValue, isUsingPkgValue);
    }

    private static float getCurrentDfs(PkgData pkgData) {
        boolean isUsingUserValue = FeatureHelper.isUsingUserValue(Constants.V4FeatureFlag.RESOLUTION);
        boolean isUsingPkgValue = FeatureHelper.isUsingPkgValue(Constants.V4FeatureFlag.RESOLUTION);
        return DfsFeature.getDfsValueByMode(DfsFeature.getActualDfsMode(pkgData, isUsingUserValue, isUsingPkgValue), pkgData, isUsingUserValue, isUsingPkgValue);
    }

    private static <T> String toString(T[] tArr) {
        StringBuilder sb = new StringBuilder();
        int length = tArr.length;
        int i = 0;
        for (T append : tArr) {
            i++;
            sb.append(append);
            if (i < length) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private static String initGfiPolicy(Package packageR) {
        String str = packageR.gfiPolicy;
        if (str == null) {
            str = "{}";
            packageR.gfiPolicy = str;
        }
        DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
        return str;
    }

    private static void populateGfiEntity(Package packageR, String str, GfiPackageDataEntity gfiPackageDataEntity) throws JSONException {
        GfiPolicy.GfiMode gfiMode;
        GfiPackageDataEntity gfiPackageDataEntity2 = gfiPackageDataEntity;
        Application application = AppContext.get();
        JSONObject jSONObject = new JSONObject(str);
        JSONObject jSONObject2 = null;
        JSONObject jSONObject3 = jSONObject.has(GfiPolicy.KEY_DFS_OFFSET) ? jSONObject.getJSONObject(GfiPolicy.KEY_DFS_OFFSET) : null;
        if (jSONObject.has(GfiPolicy.KEY_GFPS_OFFSET)) {
            jSONObject2 = jSONObject.getJSONObject(GfiPolicy.KEY_GFPS_OFFSET);
        }
        gfiPackageDataEntity2.keepTwoHwcLayers = GfiFeature.getInstance(application).getKeepTwoHwcLayers();
        gfiPackageDataEntity2.watchdogExpire = GfiSettings.getInstance(application).getWatchdogExpireDuration();
        gfiPackageDataEntity2.useInterpolation = jSONObject.optBoolean("enabled", false);
        gfiPackageDataEntity2.minVersion = jSONObject.optString(GfiPolicy.KEY_MINIMUM_VERSION, GfiPolicy.DEFAULT_MIN_VERSION);
        gfiPackageDataEntity2.version = GfiFeature.getInstance(application).getVersionString();
        gfiPackageDataEntity2.logLevel = GfiFeature.getInstance(application).getLogLevel().level;
        gfiPackageDataEntity2.gameFPS.current_value = BuildConfig.VERSION_NAME + jSONObject.optInt(GfiPolicy.KEY_INTERPOLATION_FPS, 0);
        String str2 = BuildConfig.VERSION_NAME + DfsFeature.getDefaultDfs(packageR);
        if (jSONObject.has(GfiPolicy.KEY_TARGET_DFS)) {
            str2 = BuildConfig.VERSION_NAME + jSONObject.get(GfiPolicy.KEY_TARGET_DFS);
        }
        gfiPackageDataEntity2.gameDFS.current_value = str2;
        gfiPackageDataEntity2.gameDFS.range_value = toString(new Integer[]{15, 60});
        gfiPackageDataEntity2.useSmartDelay = jSONObject.optBoolean(GfiPolicy.KEY_AUTODELAY_ENABLED, true);
        gfiPackageDataEntity2.useWriteToFrameTracker = jSONObject.optBoolean(GfiPolicy.KEY_WRITE_TO_FRAMETRACKER, true);
        gfiPackageDataEntity2.enableExternalControl = jSONObject.optBoolean(GfiPolicy.KEY_EXTERNAL_CONTROL, false);
        gfiPackageDataEntity2.minRegalStability.current_value = Double.valueOf(jSONObject.optDouble(GfiPolicy.KEY_MINIMUM_REGAL_STABILITY, 0.9d));
        gfiPackageDataEntity2.minRegalStability.range_value = toString(new Float[]{Float.valueOf(0.0f), Float.valueOf(1.0f)});
        gfiPackageDataEntity2.maxConsecutiveGlComposition = jSONObject.optInt(GfiPolicy.KEY_MAX_CONSECUTIVE_GL_COMPOSITIONS, GfiPolicy.DEFAULT_MAX_CONSECUTIVE_GL_COMPOSITIONS);
        gfiPackageDataEntity2.maxGLComposition.current_value = Double.valueOf(jSONObject.optDouble(GfiPolicy.KEY_MAX_PERCENT_GL_COMPOSITIONS, 1.0d));
        gfiPackageDataEntity2.maxGLComposition.range_value = toString(new Float[]{Float.valueOf(0.0f), Float.valueOf(1.0f)});
        gfiPackageDataEntity2.minAcceptableFps = jSONObject.optInt(GfiPolicy.KEY_MIN_ACCEPTABLE_FPS, 0);
        gfiPackageDataEntity2.enableSessionRecording = GfiSettings.getInstance(application).isSessionRecordingEnabled();
        try {
            gfiMode = GfiPolicy.GfiMode.fromString(jSONObject.optString(GfiPolicy.KEY_INTERPOLATION_MODE, "retime"));
        } catch (Exception unused) {
            gfiMode = GfiPolicy.GfiMode.RETIME;
        }
        gfiPackageDataEntity2.interpolationMode = gfiMode.mode;
        if (jSONObject3 != null) {
            gfiPackageDataEntity2.dfsOffset.current_value = Integer.valueOf(jSONObject3.optInt("value", 10));
            gfiPackageDataEntity2.dfsOffsetMax.current_value = Integer.valueOf(jSONObject3.optInt("maximum", 60));
            gfiPackageDataEntity2.useDFSOffset = jSONObject3.optBoolean("enabled", false);
            gfiPackageDataEntity2.dfsSmoothness.current_value = Double.valueOf(jSONObject3.optDouble(GfiPolicy.DfsOffset.KEY_SMOOTHNESS, 0.8d));
            gfiPackageDataEntity2.dfsSmoothness.range_value = toString(new Float[]{Float.valueOf(0.0f), Float.valueOf(1.0f)});
        }
        if (jSONObject2 != null) {
            gfiPackageDataEntity2.useGFPSOffset = jSONObject2.optBoolean("enabled", false);
            gfiPackageDataEntity2.GFPSOffset.current_value = Integer.valueOf(jSONObject2.optInt("value", 0));
            gfiPackageDataEntity2.GFPSOffsetMin.current_value = Integer.valueOf(jSONObject2.optInt("minimum", 15));
        }
    }

    public static void applyGfiFeatures(Package packageR, GfiPackageDataEntity gfiPackageDataEntity) throws JSONException, GfiPolicyException {
        JSONObject jSONObject = packageR.gfiPolicy != null ? new JSONObject(packageR.gfiPolicy) : new JSONObject();
        String str = "enabled";
        jSONObject.put(str, gfiPackageDataEntity.useInterpolation);
        jSONObject.put(GfiPolicy.KEY_MINIMUM_VERSION, gfiPackageDataEntity.minVersion);
        jSONObject.put(GfiPolicy.KEY_INTERPOLATION_FPS, gfiPackageDataEntity.gameFPS.current_value);
        jSONObject.put(GfiPolicy.KEY_AUTODELAY_ENABLED, gfiPackageDataEntity.useSmartDelay);
        jSONObject.put(GfiPolicy.KEY_WRITE_TO_FRAMETRACKER, gfiPackageDataEntity.useWriteToFrameTracker);
        jSONObject.put(GfiPolicy.KEY_EXTERNAL_CONTROL, gfiPackageDataEntity.enableExternalControl);
        jSONObject.put(GfiPolicy.KEY_MINIMUM_REGAL_STABILITY, gfiPackageDataEntity.minRegalStability.current_value);
        jSONObject.put(GfiPolicy.KEY_MAX_CONSECUTIVE_GL_COMPOSITIONS, gfiPackageDataEntity.maxConsecutiveGlComposition);
        jSONObject.put(GfiPolicy.KEY_MAX_PERCENT_GL_COMPOSITIONS, gfiPackageDataEntity.maxGLComposition.current_value);
        jSONObject.put(GfiPolicy.KEY_MIN_ACCEPTABLE_FPS, gfiPackageDataEntity.minAcceptableFps);
        GfiPolicy.GfiMode fromInt = GfiPolicy.GfiMode.fromInt(gfiPackageDataEntity.interpolationMode);
        if (fromInt != null) {
            switch (AnonymousClass2.$SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode[fromInt.ordinal()]) {
                case 1:
                    jSONObject.put(GfiPolicy.KEY_INTERPOLATION_MODE, "retime");
                    break;
                case 2:
                    jSONObject.put(GfiPolicy.KEY_INTERPOLATION_MODE, "fillin");
                    break;
                case 3:
                    jSONObject.put(GfiPolicy.KEY_INTERPOLATION_MODE, "latred");
                    break;
                case 4:
                    jSONObject.put(GfiPolicy.KEY_INTERPOLATION_MODE, "mllatred");
                    break;
                case 5:
                    jSONObject.put(GfiPolicy.KEY_INTERPOLATION_MODE, "none");
                    break;
                case 6:
                    jSONObject.put(GfiPolicy.KEY_INTERPOLATION_MODE, "unlimited");
                    break;
            }
        }
        if (Float.parseFloat((String) gfiPackageDataEntity.gameDFS.current_value) < 0.5f) {
            jSONObject.remove(GfiPolicy.KEY_TARGET_DFS);
        } else {
            jSONObject.put(GfiPolicy.KEY_TARGET_DFS, gfiPackageDataEntity.gameDFS.current_value);
        }
        if (gfiPackageDataEntity.useDFSOffset) {
            applyNestedGfiFeature(jSONObject, GfiPolicy.KEY_DFS_OFFSET, "value", gfiPackageDataEntity.dfsOffset.current_value);
            applyNestedGfiFeature(jSONObject, GfiPolicy.KEY_DFS_OFFSET, "maximum", gfiPackageDataEntity.dfsOffsetMax.current_value);
            applyNestedGfiFeature(jSONObject, GfiPolicy.KEY_DFS_OFFSET, GfiPolicy.DfsOffset.KEY_SMOOTHNESS, gfiPackageDataEntity.dfsSmoothness.current_value);
            applyNestedGfiFeature(jSONObject, GfiPolicy.KEY_DFS_OFFSET, str, true);
        } else {
            jSONObject.remove(GfiPolicy.KEY_DFS_OFFSET);
            applyNestedGfiFeature(jSONObject, GfiPolicy.KEY_DFS_OFFSET, str, false);
        }
        if (gfiPackageDataEntity.useGFPSOffset) {
            applyNestedGfiFeature(jSONObject, GfiPolicy.KEY_GFPS_OFFSET, "value", gfiPackageDataEntity.GFPSOffset.current_value);
            applyNestedGfiFeature(jSONObject, GfiPolicy.KEY_GFPS_OFFSET, "minimum", gfiPackageDataEntity.GFPSOffsetMin.current_value);
            applyNestedGfiFeature(jSONObject, GfiPolicy.KEY_GFPS_OFFSET, str, true);
        } else {
            jSONObject.remove(GfiPolicy.KEY_GFPS_OFFSET);
            applyNestedGfiFeature(jSONObject, GfiPolicy.KEY_GFPS_OFFSET, str, false);
        }
        GfiSettings.getInstance(AppContext.get()).setWatchdogExpireDuration(gfiPackageDataEntity.watchdogExpire);
        GfiSettings.getInstance(AppContext.get()).setSessionRecordingEnabled(gfiPackageDataEntity.enableSessionRecording);
        GfiFeature instance = GfiFeature.getInstance(AppContext.get());
        instance.setLogLevel(GfiFeature.LogLevel.fromInteger(gfiPackageDataEntity.logLevel));
        instance.setKeepTwoHwcLayers(gfiPackageDataEntity.keepTwoHwcLayers);
        packageR.gfiPolicy = jSONObject.toString();
        if (!gfiPackageDataEntity.useInterpolation) {
            str = State.DISABLED;
        }
        PackageDbHelper.getInstance().setServerPolicy(Constants.V4FeatureFlag.GFI, packageR.pkgName, str);
        DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
    }

    private static <T> void applyNestedGfiFeature(JSONObject jSONObject, String str, String str2, T t) throws JSONException {
        JSONObject jSONObject2 = jSONObject.has(str) ? jSONObject.getJSONObject(str) : new JSONObject();
        jSONObject2.put(str2, t);
        jSONObject.put(str, jSONObject2);
    }

    private static void populateIPMEntity(IpmCore ipmCore, IpmFeatureEntity ipmFeatureEntity) {
        GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
        ipmFeatureEntity.ipmMode = globalDao.getIpmMode();
        ipmFeatureEntity.useJSONPkgConfig = IpmFeature.mUseJSONPolicy;
        boolean[] csvToBooleans = TypeConverter.csvToBooleans(globalDao.getIpmFlag());
        boolean z = true;
        if (csvToBooleans != null) {
            ipmFeatureEntity.useSuperTrainMode = csvToBooleans[0];
            ipmFeatureEntity.useVerboseLogs = csvToBooleans[1];
            ipmFeatureEntity.useCaptureOnly = csvToBooleans[4];
        }
        ipmFeatureEntity.useDynamicDecisions = ipmCore.getDynamicDecisions();
        ipmFeatureEntity.useRunInAnyMode = ipmCore.getEnableAnyMode();
        ipmFeatureEntity.useCPUMinFreqControl = ipmCore.getEnableCpuMinFreqControl();
        ipmFeatureEntity.useGPUMinFreqControl = ipmCore.getEnableGpuMinFreqControl();
        ipmFeatureEntity.useLRPST = ipmCore.getEnableLRPST();
        ipmFeatureEntity.useMLOff = ipmCore.getEnableAllowMlOff();
        if (ipmCore.getCustomTfpsFlags() == 0) {
            z = false;
        }
        ipmFeatureEntity.useCustomTfpsFlags = z;
        ipmFeatureEntity.statistics = ipmCore.getStatistics();
        ipmFeatureEntity.ipmTargetPower = globalDao.getIpmTargetPower();
        ipmFeatureEntity.ipmTargetTemperature = globalDao.getIpmTargetTemperature();
        ipmFeatureEntity.ipmDefaultTemperature = GlobalDbHelper.getInstance().getIpmDefaultTemperature();
        ipmFeatureEntity.lrpst = ipmCore.getLRPST();
    }

    private static void applyIPMFeatures(IpmCore ipmCore, IpmFeatureEntity ipmFeatureEntity) {
        GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
        IpmFeature.mUseJSONPolicy = ipmFeatureEntity.useJSONPkgConfig;
        globalDao.setIpmMode(new Global.IdAndIpmMode(ipmFeatureEntity.ipmMode));
        ipmCore.setProfile(Profile.fromInt(ipmFeatureEntity.ipmMode));
        boolean[] csvToBooleans = TypeConverter.csvToBooleans(globalDao.getIpmFlag());
        int customTfpsFlags = ipmCore.getCustomTfpsFlags();
        if (customTfpsFlags == 0) {
            customTfpsFlags = 2300;
        }
        if (!ipmFeatureEntity.useCustomTfpsFlags) {
            customTfpsFlags = 0;
        }
        ipmCore.setCustomTfpsFlags(customTfpsFlags);
        if (csvToBooleans != null) {
            csvToBooleans[0] = ipmFeatureEntity.useSuperTrainMode;
        }
        ipmCore.setSupertrain(ipmFeatureEntity.useSuperTrainMode);
        if (csvToBooleans != null) {
            csvToBooleans[1] = ipmFeatureEntity.useVerboseLogs;
        }
        ipmCore.setLogLevel(ipmCore.getDefaultLogLevel());
        if (csvToBooleans != null) {
            csvToBooleans[4] = ipmFeatureEntity.useCaptureOnly;
        }
        ipmCore.setOnlyCapture(ipmFeatureEntity.useCaptureOnly);
        globalDao.setIpmFlag(new Global.IdAndIpmFlag(csvToBooleans));
        ipmCore.setDynamicDecisions(ipmFeatureEntity.useDynamicDecisions);
        ipmCore.setEnableAnyMode(ipmFeatureEntity.useRunInAnyMode);
        ipmCore.setEnableCpuMinFreqControl(ipmFeatureEntity.useCPUMinFreqControl);
        int i = -2;
        ipmCore.setCpuGap(ipmFeatureEntity.useCPUMinFreqControl ? -2 : -1);
        ipmCore.setEnableGpuMinFreqControl(ipmFeatureEntity.useGPUMinFreqControl);
        if (!ipmFeatureEntity.useGPUMinFreqControl) {
            i = -1;
        }
        ipmCore.setGpuGap(i);
        ipmCore.setEnableLRPST(ipmFeatureEntity.useLRPST);
        ipmCore.setEnableAllowMlOff(ipmFeatureEntity.useMLOff);
        ipmCore.setAllowMlOff(ipmFeatureEntity.useMLOff);
        if (ipmFeatureEntity.intelMode != -100) {
            GosTestLog.d(TAG, "applyIPMFeatures(), dataEntity.intelMode = " + ipmFeatureEntity.intelMode);
            ipmCore.setIntelMode(IntelMode.fromInt(ipmFeatureEntity.intelMode));
        }
        if (ipmFeatureEntity.gpuMinBoost != -100) {
            GosTestLog.d(TAG, "applyIPMFeatures(), dataEntity.gpuMinBoost = " + ipmFeatureEntity.gpuMinBoost);
            ipmCore.setGpuMinBoost(ipmFeatureEntity.gpuMinBoost);
        }
        if (!(ipmFeatureEntity.minFreqGpu == -100 || ipmFeatureEntity.minFreqCpu == -100)) {
            GosTestLog.d(TAG, "applyIPMFeatures(), dataEntity.minFreqGpu = " + ipmFeatureEntity.minFreqGpu + " dataEntity.minFreqCpu = " + ipmFeatureEntity.minFreqCpu);
            ipmCore.setMinFreqs(ipmFeatureEntity.minFreqGpu, ipmFeatureEntity.minFreqCpu);
        }
        if (!(ipmFeatureEntity.maxFreqGpu == -100 || ipmFeatureEntity.maxFreqCpu == -100)) {
            GosTestLog.d(TAG, "applyIPMFeatures(), dataEntity.maxFreqGpu = " + ipmFeatureEntity.maxFreqGpu + " dataEntity.maxFreqCpu = " + ipmFeatureEntity.maxFreqCpu);
            ipmCore.setMaxFreqs(ipmFeatureEntity.maxFreqGpu, ipmFeatureEntity.maxFreqCpu);
        }
        if (globalDao.getIpmTargetPower() != ipmFeatureEntity.ipmTargetPower) {
            globalDao.setIpmTargetPower(new Global.IdAndIpmTargetPower(ipmFeatureEntity.ipmTargetPower));
        }
        if (globalDao.getIpmTargetTemperature() != ipmFeatureEntity.ipmTargetTemperature) {
            ipmCore.setTargetPST(ipmFeatureEntity.ipmTargetTemperature);
            globalDao.setIpmTargetTemperature(new Global.IdAndIpmTargetTemperature(ipmFeatureEntity.ipmTargetTemperature));
        }
        ipmCore.setLRPST(ipmFeatureEntity.lrpst);
    }

    private static void populateGlobalResumeBoostEntity(ResumeBoostDataEntity.BoostDataEntity boostDataEntity, ResumeBoostDataEntity.BoostDataEntity boostDataEntity2) {
        boostDataEntity.durationSec = ResumeBoostFeature.getInstance().getResumeBoostDuration(Constants.BoostType.Resume);
        boostDataEntity.cpuIndex = ResumeBoostFeature.getInstance().getCpuIndexFromServer(Constants.BoostType.Resume);
        boostDataEntity.busIndex = ResumeBoostFeature.getInstance().getBusIndexFromServer(Constants.BoostType.Resume);
        boostDataEntity.cpuFreq = ResumeBoostFeature.getInstance().getSupportedFreq(SeDvfsInterfaceImpl.TYPE_CPU_MIN);
        boostDataEntity.busFreq = ResumeBoostFeature.getInstance().getSupportedFreq(SeDvfsInterfaceImpl.TYPE_BUS_MIN);
        boostDataEntity2.durationSec = ResumeBoostFeature.getInstance().getResumeBoostDuration(Constants.BoostType.Launch);
        boostDataEntity2.cpuIndex = ResumeBoostFeature.getInstance().getCpuIndexFromServer(Constants.BoostType.Launch);
        boostDataEntity2.busIndex = ResumeBoostFeature.getInstance().getBusIndexFromServer(Constants.BoostType.Launch);
        boostDataEntity2.cpuFreq = ResumeBoostFeature.getInstance().getSupportedFreq(SeDvfsInterfaceImpl.TYPE_CPU_MIN);
        boostDataEntity2.busFreq = ResumeBoostFeature.getInstance().getSupportedFreq(SeDvfsInterfaceImpl.TYPE_BUS_MIN);
    }

    private static void populatePackageResumeBoostEntity(String str, ResumeBoostDataEntity.BoostDataEntity boostDataEntity, ResumeBoostDataEntity.BoostDataEntity boostDataEntity2) {
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(str);
        if (packageR != null) {
            populateBoostEntity(packageR.resumeBoostPolicy, boostDataEntity);
        }
        if (packageR != null) {
            populateBoostEntity(packageR.launchBoostPolicy, boostDataEntity2);
        }
    }

    private static void populateBoostEntity(String str, ResumeBoostDataEntity.BoostDataEntity boostDataEntity) {
        JSONObject jSONObject;
        if (str != null) {
            try {
                jSONObject = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            jSONObject = new JSONObject();
        }
        if (jSONObject.has(ResumeBoostCore.Policy.CPU_FREQ)) {
            boostDataEntity.cpuIndex = ResumeBoostCore.getInstance().getCpuIndexFromServer(jSONObject.optInt(ResumeBoostCore.Policy.CPU_FREQ));
        }
        if (jSONObject.has(ResumeBoostCore.Policy.BUS_FREQ)) {
            boostDataEntity.busIndex = ResumeBoostCore.getInstance().getBusIndexFromServer(jSONObject.optInt(ResumeBoostCore.Policy.BUS_FREQ));
        }
        if (jSONObject.has("duration")) {
            boostDataEntity.durationSec = jSONObject.optInt("duration", 10);
        }
        boostDataEntity.cpuFreq = ResumeBoostFeature.getInstance().getSupportedFreq(SeDvfsInterfaceImpl.TYPE_CPU_MIN);
        boostDataEntity.busFreq = ResumeBoostFeature.getInstance().getSupportedFreq(SeDvfsInterfaceImpl.TYPE_BUS_MIN);
    }

    /* renamed from: com.samsung.android.game.gos.test.gostester.GosTester$2  reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode;
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag;
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$value$Constants$BoostType;

        /* JADX WARNING: Can't wrap try/catch for region: R(42:0|(2:1|2)|3|(2:5|6)|7|9|10|(2:11|12)|13|(2:15|16)|17|(2:19|20)|21|(2:23|24)|25|(2:27|28)|29|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|56) */
        /* JADX WARNING: Can't wrap try/catch for region: R(44:0|(2:1|2)|3|(2:5|6)|7|9|10|(2:11|12)|13|15|16|17|(2:19|20)|21|(2:23|24)|25|27|28|29|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|56) */
        /* JADX WARNING: Can't wrap try/catch for region: R(45:0|(2:1|2)|3|5|6|7|9|10|(2:11|12)|13|15|16|17|(2:19|20)|21|(2:23|24)|25|27|28|29|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|56) */
        /* JADX WARNING: Can't wrap try/catch for region: R(48:0|1|2|3|5|6|7|9|10|11|12|13|15|16|17|(2:19|20)|21|23|24|25|27|28|29|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|56) */
        /* JADX WARNING: Can't wrap try/catch for region: R(49:0|1|2|3|5|6|7|9|10|11|12|13|15|16|17|19|20|21|23|24|25|27|28|29|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|56) */
        /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x002e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x0075 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x007f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x0089 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x0093 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x009d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00a7 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x00b2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:47:0x00be */
        /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x00ca */
        /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x00d6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x00e2 */
        static {
            /*
                com.samsung.android.game.gos.value.Constants$BoostType[] r0 = com.samsung.android.game.gos.value.Constants.BoostType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$value$Constants$BoostType = r0
                r1 = 1
                com.samsung.android.game.gos.value.Constants$BoostType r2 = com.samsung.android.game.gos.value.Constants.BoostType.Resume     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$com$samsung$android$game$gos$value$Constants$BoostType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.value.Constants$BoostType r3 = com.samsung.android.game.gos.value.Constants.BoostType.Launch     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode[] r2 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.values()
                int r2 = r2.length
                int[] r2 = new int[r2]
                $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode = r2
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r3 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.RETIME     // Catch:{ NoSuchFieldError -> 0x002e }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x002e }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x002e }
            L_0x002e:
                int[] r2 = $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode     // Catch:{ NoSuchFieldError -> 0x0038 }
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r3 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.FILLIN     // Catch:{ NoSuchFieldError -> 0x0038 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0038 }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x0038 }
            L_0x0038:
                r2 = 3
                int[] r3 = $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode     // Catch:{ NoSuchFieldError -> 0x0043 }
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r4 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.LATENCY_REDUCTION     // Catch:{ NoSuchFieldError -> 0x0043 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0043 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0043 }
            L_0x0043:
                r3 = 4
                int[] r4 = $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode     // Catch:{ NoSuchFieldError -> 0x004e }
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r5 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.ML_LATENCY_REDUCTION     // Catch:{ NoSuchFieldError -> 0x004e }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x004e }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x004e }
            L_0x004e:
                r4 = 5
                int[] r5 = $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode     // Catch:{ NoSuchFieldError -> 0x0059 }
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r6 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.NONE     // Catch:{ NoSuchFieldError -> 0x0059 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0059 }
                r5[r6] = r4     // Catch:{ NoSuchFieldError -> 0x0059 }
            L_0x0059:
                r5 = 6
                int[] r6 = $SwitchMap$com$samsung$android$game$gos$feature$gfi$value$GfiPolicy$GfiMode     // Catch:{ NoSuchFieldError -> 0x0064 }
                com.samsung.android.game.gos.feature.gfi.value.GfiPolicy$GfiMode r7 = com.samsung.android.game.gos.feature.gfi.value.GfiPolicy.GfiMode.UNLIMITED     // Catch:{ NoSuchFieldError -> 0x0064 }
                int r7 = r7.ordinal()     // Catch:{ NoSuchFieldError -> 0x0064 }
                r6[r7] = r5     // Catch:{ NoSuchFieldError -> 0x0064 }
            L_0x0064:
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag[] r6 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.values()
                int r6 = r6.length
                int[] r6 = new int[r6]
                $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag = r6
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r7 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.Dfs     // Catch:{ NoSuchFieldError -> 0x0075 }
                int r7 = r7.ordinal()     // Catch:{ NoSuchFieldError -> 0x0075 }
                r6[r7] = r1     // Catch:{ NoSuchFieldError -> 0x0075 }
            L_0x0075:
                int[] r1 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x007f }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r6 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.Dss     // Catch:{ NoSuchFieldError -> 0x007f }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x007f }
                r1[r6] = r0     // Catch:{ NoSuchFieldError -> 0x007f }
            L_0x007f:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x0089 }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r1 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.Tss     // Catch:{ NoSuchFieldError -> 0x0089 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0089 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0089 }
            L_0x0089:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x0093 }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r1 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.CpuLevel     // Catch:{ NoSuchFieldError -> 0x0093 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0093 }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x0093 }
            L_0x0093:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x009d }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r1 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.GpuLevel     // Catch:{ NoSuchFieldError -> 0x009d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009d }
                r0[r1] = r4     // Catch:{ NoSuchFieldError -> 0x009d }
            L_0x009d:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x00a7 }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r1 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.ShiftTemp     // Catch:{ NoSuchFieldError -> 0x00a7 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a7 }
                r0[r1] = r5     // Catch:{ NoSuchFieldError -> 0x00a7 }
            L_0x00a7:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x00b2 }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r1 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.LaunchMode     // Catch:{ NoSuchFieldError -> 0x00b2 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b2 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00b2 }
            L_0x00b2:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x00be }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r1 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.VRR_MIN     // Catch:{ NoSuchFieldError -> 0x00be }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00be }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00be }
            L_0x00be:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x00ca }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r1 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.VRR_MAX     // Catch:{ NoSuchFieldError -> 0x00ca }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ca }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00ca }
            L_0x00ca:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x00d6 }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r1 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.TSP     // Catch:{ NoSuchFieldError -> 0x00d6 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00d6 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00d6 }
            L_0x00d6:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x00e2 }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r1 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.ResumeBoost     // Catch:{ NoSuchFieldError -> 0x00e2 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00e2 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00e2 }
            L_0x00e2:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$gostester$value$GosTesterConstants$PackageFeatureFlag     // Catch:{ NoSuchFieldError -> 0x00ee }
                com.samsung.android.game.gos.test.gostester.value.GosTesterConstants$PackageFeatureFlag r1 = com.samsung.android.game.gos.test.gostester.value.GosTesterConstants.PackageFeatureFlag.LaunchBoost     // Catch:{ NoSuchFieldError -> 0x00ee }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00ee }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00ee }
            L_0x00ee:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.gostester.GosTester.AnonymousClass2.<clinit>():void");
        }
    }

    private static void setPackageBoostEntity(Constants.BoostType boostType, String str, ResumeBoostDataEntity.BoostDataEntity boostDataEntity) {
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(str);
        int i = AnonymousClass2.$SwitchMap$com$samsung$android$game$gos$value$Constants$BoostType[boostType.ordinal()];
        if (i == 1) {
            ResumeBoostFeature.getInstance().changePackageSettings(packageR, boostDataEntity.durationSec, boostDataEntity.cpuIndex, boostDataEntity.busIndex);
        } else if (i == 2) {
            LaunchBoostFeature.getInstance().changePackageSettings(packageR, boostDataEntity.durationSec, boostDataEntity.cpuIndex, boostDataEntity.busIndex);
        }
        DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
        SeActivityManager.getInstance().forceStopPackage(str);
    }

    private static File getBackupFolderExist(Context context, File file) {
        if (file == null || !Objects.equals(Environment.getExternalStorageState(), "mounted")) {
            return null;
        }
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, (String) null);
        if (externalFilesDirs.length == 0) {
            return null;
        }
        return externalFilesDirs[0].getParentFile();
    }
}
