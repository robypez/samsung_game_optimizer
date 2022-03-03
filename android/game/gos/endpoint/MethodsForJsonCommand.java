package com.samsung.android.game.gos.endpoint;

import com.samsung.android.game.gos.util.GosLog;

public class MethodsForJsonCommand {
    private static final String LOG_TAG = "MethodsForJsonCommand";

    public static String respondWithJson(String str, String str2, String str3) {
        GosLog.i(LOG_TAG, "respondWithJson(), " + str + ", param: " + str2);
        if (str == null) {
            return null;
        }
        String onGosCommand = onGosCommand(str, str2, str3);
        if (onGosCommand == null) {
            onGosCommand = onGppCommand(str, str2, str3);
        }
        if (onGosCommand == null) {
            onGosCommand = onEtcCommand(str, str2, str3);
        }
        if (onGosCommand == null) {
            GosLog.e(LOG_TAG, "Response is null. Check if it is wrong command: " + str);
        }
        return onGosCommand;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String onGosCommand(java.lang.String r6, java.lang.String r7, java.lang.String r8) {
        /*
            com.samsung.android.game.gos.endpoint.GlobalCommand r0 = new com.samsung.android.game.gos.endpoint.GlobalCommand
            r0.<init>()
            com.samsung.android.game.gos.endpoint.PackageCommand r1 = new com.samsung.android.game.gos.endpoint.PackageCommand
            r1.<init>()
            com.samsung.android.game.gos.endpoint.GosCommand r2 = new com.samsung.android.game.gos.endpoint.GosCommand
            r2.<init>()
            com.samsung.android.game.gos.endpoint.EventCommand r3 = new com.samsung.android.game.gos.endpoint.EventCommand
            r3.<init>()
            com.samsung.android.game.gos.endpoint.DbCommand r4 = new com.samsung.android.game.gos.endpoint.DbCommand
            r4.<init>()
            int r5 = r6.hashCode()
            switch(r5) {
                case -1917036114: goto L_0x0089;
                case -1602738612: goto L_0x007f;
                case -1831075: goto L_0x0075;
                case 860785856: goto L_0x006b;
                case 908921449: goto L_0x0061;
                case 1053922046: goto L_0x0057;
                case 1120690314: goto L_0x004d;
                case 1146156605: goto L_0x0042;
                case 1218465704: goto L_0x0038;
                case 1863939302: goto L_0x002e;
                case 1905200373: goto L_0x0022;
                default: goto L_0x0020;
            }
        L_0x0020:
            goto L_0x0094
        L_0x0022:
            java.lang.String r5 = "set_monitored_apps"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 8
            goto L_0x0095
        L_0x002e:
            java.lang.String r5 = "get_package_names"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 2
            goto L_0x0095
        L_0x0038:
            java.lang.String r5 = "set_feature_accessibility"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 6
            goto L_0x0095
        L_0x0042:
            java.lang.String r5 = "get_report"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 10
            goto L_0x0095
        L_0x004d:
            java.lang.String r5 = "stop_packages"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 5
            goto L_0x0095
        L_0x0057:
            java.lang.String r5 = "set_fps_value"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 7
            goto L_0x0095
        L_0x0061:
            java.lang.String r5 = "set_global_data"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 1
            goto L_0x0095
        L_0x006b:
            java.lang.String r5 = "set_package_data"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 4
            goto L_0x0095
        L_0x0075:
            java.lang.String r5 = "get_global_data"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 0
            goto L_0x0095
        L_0x007f:
            java.lang.String r5 = "get_package_data"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 3
            goto L_0x0095
        L_0x0089:
            java.lang.String r5 = "subscribe_events"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x0094
            r6 = 9
            goto L_0x0095
        L_0x0094:
            r6 = -1
        L_0x0095:
            r5 = 0
            switch(r6) {
                case 0: goto L_0x00d5;
                case 1: goto L_0x00cd;
                case 2: goto L_0x00c8;
                case 3: goto L_0x00c0;
                case 4: goto L_0x00b8;
                case 5: goto L_0x00b3;
                case 6: goto L_0x00ae;
                case 7: goto L_0x00a9;
                case 8: goto L_0x00a4;
                case 9: goto L_0x009f;
                case 10: goto L_0x009a;
                default: goto L_0x0099;
            }
        L_0x0099:
            goto L_0x00d9
        L_0x009a:
            java.lang.String r5 = r4.getReport(r7)
            goto L_0x00d9
        L_0x009f:
            java.lang.String r5 = r3.subscribeEvents(r7, r8)
            goto L_0x00d9
        L_0x00a4:
            java.lang.String r5 = r3.setMonitoredApps(r7, r8)
            goto L_0x00d9
        L_0x00a9:
            java.lang.String r5 = r2.setFpsValue(r7)
            goto L_0x00d9
        L_0x00ae:
            java.lang.String r5 = r2.setFeatureAccessibility(r7, r8)
            goto L_0x00d9
        L_0x00b3:
            java.lang.String r5 = r1.stopPackages(r7)
            goto L_0x00d9
        L_0x00b8:
            if (r7 != 0) goto L_0x00bb
            return r5
        L_0x00bb:
            java.lang.String r5 = r1.setPkgDataWithJson(r7, r8)
            goto L_0x00d9
        L_0x00c0:
            if (r7 != 0) goto L_0x00c3
            return r5
        L_0x00c3:
            java.lang.String r5 = r1.getPkgDataWithJson(r7)
            goto L_0x00d9
        L_0x00c8:
            java.lang.String r5 = r1.getPackageNames(r7)
            goto L_0x00d9
        L_0x00cd:
            if (r7 != 0) goto L_0x00d0
            return r5
        L_0x00d0:
            java.lang.String r5 = r0.setGlobalDataWithJson(r7, r8)
            goto L_0x00d9
        L_0x00d5:
            java.lang.String r5 = r0.getGlobalDataWithJson()
        L_0x00d9:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.endpoint.MethodsForJsonCommand.onGosCommand(java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String onGppCommand(java.lang.String r6, java.lang.String r7, java.lang.String r8) {
        /*
            int r0 = r6.hashCode()
            r1 = 5
            r2 = 4
            r3 = 3
            r4 = 2
            r5 = 1
            switch(r0) {
                case -1472765983: goto L_0x003f;
                case -650158456: goto L_0x0035;
                case -574953278: goto L_0x002b;
                case 102577488: goto L_0x0021;
                case 1099588506: goto L_0x0017;
                case 2038561979: goto L_0x000d;
                default: goto L_0x000c;
            }
        L_0x000c:
            goto L_0x0049
        L_0x000d:
            java.lang.String r0 = "perf_data_get_available_sessions"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0049
            r6 = r4
            goto L_0x004a
        L_0x0017:
            java.lang.String r0 = "perf_data_get_system_status"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0049
            r6 = r2
            goto L_0x004a
        L_0x0021:
            java.lang.String r0 = "get_gpp_state"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0049
            r6 = r1
            goto L_0x004a
        L_0x002b:
            java.lang.String r0 = "perf_data_register"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0049
            r6 = 0
            goto L_0x004a
        L_0x0035:
            java.lang.String r0 = "perf_data_get_available_parameters"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0049
            r6 = r5
            goto L_0x004a
        L_0x003f:
            java.lang.String r0 = "perf_data_simple_request"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0049
            r6 = r3
            goto L_0x004a
        L_0x0049:
            r6 = -1
        L_0x004a:
            if (r6 == 0) goto L_0x0081
            if (r6 == r5) goto L_0x0078
            if (r6 == r4) goto L_0x006f
            if (r6 == r3) goto L_0x0066
            if (r6 == r2) goto L_0x005d
            if (r6 == r1) goto L_0x0058
            r6 = 0
            goto L_0x0089
        L_0x0058:
            java.lang.String r6 = com.samsung.android.game.gos.feature.autocontrol.AutoControlFeature.getGppStateJSON()
            goto L_0x0089
        L_0x005d:
            android.app.Application r6 = com.samsung.android.game.gos.context.AppContext.get()
            java.lang.String r6 = com.samsung.android.game.gos.feature.ringlog.SystemStatusUtil.getSystemStatusJSON((android.content.Context) r6, (java.lang.String) r7)
            goto L_0x0089
        L_0x0066:
            com.samsung.android.game.gos.feature.ringlog.RinglogInterface r6 = com.samsung.android.game.gos.feature.ringlog.RinglogInterface.getInstance()
            java.lang.String r6 = r6.readDataSimpleRequestJSON(r8, r7)
            goto L_0x0089
        L_0x006f:
            com.samsung.android.game.gos.feature.ringlog.RinglogInterface r6 = com.samsung.android.game.gos.feature.ringlog.RinglogInterface.getInstance()
            java.lang.String r6 = r6.getAvailableSessionsJSON(r8, r7)
            goto L_0x0089
        L_0x0078:
            com.samsung.android.game.gos.feature.ringlog.RinglogInterface r6 = com.samsung.android.game.gos.feature.ringlog.RinglogInterface.getInstance()
            java.lang.String r6 = r6.getAvailableParametersJSON()
            goto L_0x0089
        L_0x0081:
            com.samsung.android.game.gos.feature.ringlog.RinglogInterface r6 = com.samsung.android.game.gos.feature.ringlog.RinglogInterface.getInstance()
            java.lang.String r6 = r6.handshakeJSON()
        L_0x0089:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.endpoint.MethodsForJsonCommand.onGppCommand(java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String onEtcCommand(java.lang.String r4, java.lang.String r5, java.lang.String r6) {
        /*
            int r0 = r4.hashCode()
            r1 = 3
            r2 = 2
            r3 = 1
            switch(r0) {
                case -660561061: goto L_0x0029;
                case -445176780: goto L_0x001f;
                case -200639864: goto L_0x0015;
                case 428797977: goto L_0x000b;
                default: goto L_0x000a;
            }
        L_0x000a:
            goto L_0x0033
        L_0x000b:
            java.lang.String r0 = "set_lru_num"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = r2
            goto L_0x0034
        L_0x0015:
            java.lang.String r0 = "set_survive_list"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = r1
            goto L_0x0034
        L_0x001f:
            java.lang.String r0 = "set_limit_bg_network_data"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = 0
            goto L_0x0034
        L_0x0029:
            java.lang.String r0 = "set_block_md_wifi_data"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = r3
            goto L_0x0034
        L_0x0033:
            r4 = -1
        L_0x0034:
            if (r4 == 0) goto L_0x0065
            if (r4 == r3) goto L_0x0060
            java.lang.String r0 = "clear_bg_process"
            if (r4 == r2) goto L_0x0050
            if (r4 == r1) goto L_0x0040
            r4 = 0
            goto L_0x0069
        L_0x0040:
            java.lang.String r4 = com.samsung.android.game.gos.feature.clearbgprocess.ClearBGProcessUtil.setSurviveList(r5, r6)
            com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper r5 = com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper.getInstance()
            java.util.Set r0 = java.util.Collections.singleton(r0)
            r5.setSettingAccessiblePackage(r6, r0)
            goto L_0x0069
        L_0x0050:
            java.lang.String r4 = com.samsung.android.game.gos.feature.clearbgprocess.ClearBGProcessUtil.setLRU_num(r5)
            com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper r5 = com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper.getInstance()
            java.util.Set r0 = java.util.Collections.singleton(r0)
            r5.setSettingAccessiblePackage(r6, r0)
            goto L_0x0069
        L_0x0060:
            java.lang.String r4 = com.samsung.android.game.gos.feature.mdswitchwifiblock.MDSwitchWifiBlockUtil.setBlockSwitchStatus(r5)
            goto L_0x0069
        L_0x0065:
            java.lang.String r4 = com.samsung.android.game.gos.feature.limitbgnetwork.LimitBGNetworkUtil.setLimitBGNetwork(r5)
        L_0x0069:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.endpoint.MethodsForJsonCommand.onEtcCommand(java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }
}
