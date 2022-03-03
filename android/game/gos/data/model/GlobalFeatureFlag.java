package com.samsung.android.game.gos.data.model;

import com.samsung.android.game.gos.data.model.Global;

public class GlobalFeatureFlag {
    public boolean available;
    public boolean enabledFlagByServer;
    public boolean enabledFlagByUser;
    public boolean forcedFlag;
    public boolean inheritedFlag;
    public String name;
    private String state = "inherited";
    public boolean usingPkgValue;
    public boolean usingUserValue;

    public GlobalFeatureFlag() {
    }

    public GlobalFeatureFlag(String str, String str2) {
        this.name = str;
        this.available = false;
        this.usingUserValue = false;
        this.usingPkgValue = false;
        setState(str2);
        if (this.inheritedFlag) {
            this.enabledFlagByUser = Global.DefaultGlobalData.isEnabledByDefault(str);
        } else {
            this.enabledFlagByUser = this.enabledFlagByServer;
        }
    }

    public String getState() {
        return this.state;
    }

    public void setState(String str) {
        if (State.isValidV4State(str)) {
            this.state = str;
        } else {
            this.state = "inherited";
        }
        Flags flagsByState = Flags.getFlagsByState(this.name, this.state);
        this.inheritedFlag = flagsByState.inheritedFlag;
        this.forcedFlag = flagsByState.forcedFlag;
        this.enabledFlagByServer = flagsByState.enabledFlagByServer;
    }

    public boolean isPositiveState() {
        return !State.FORCIBLY_DISABLED.equals(this.state);
    }

    private static class Flags {
        public boolean enabledFlagByServer;
        public boolean forcedFlag;
        public boolean inheritedFlag;

        Flags(boolean z, boolean z2, boolean z3) {
            this.inheritedFlag = z;
            this.forcedFlag = z2;
            this.enabledFlagByServer = z3;
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static com.samsung.android.game.gos.data.model.GlobalFeatureFlag.Flags getFlagsByState(java.lang.String r5, java.lang.String r6) {
            /*
                int r0 = r6.hashCode()
                r1 = 3
                r2 = 2
                r3 = 1
                r4 = 0
                switch(r0) {
                    case -1609594047: goto L_0x0034;
                    case -1557378342: goto L_0x002a;
                    case 270940796: goto L_0x0020;
                    case 520972834: goto L_0x0016;
                    case 1894004667: goto L_0x000c;
                    default: goto L_0x000b;
                }
            L_0x000b:
                goto L_0x003e
            L_0x000c:
                java.lang.String r0 = "forcibly_disabled"
                boolean r6 = r6.equals(r0)
                if (r6 == 0) goto L_0x003e
                r6 = 4
                goto L_0x003f
            L_0x0016:
                java.lang.String r0 = "forcibly_enabled"
                boolean r6 = r6.equals(r0)
                if (r6 == 0) goto L_0x003e
                r6 = r1
                goto L_0x003f
            L_0x0020:
                java.lang.String r0 = "disabled"
                boolean r6 = r6.equals(r0)
                if (r6 == 0) goto L_0x003e
                r6 = r3
                goto L_0x003f
            L_0x002a:
                java.lang.String r0 = "inherited"
                boolean r6 = r6.equals(r0)
                if (r6 == 0) goto L_0x003e
                r6 = r2
                goto L_0x003f
            L_0x0034:
                java.lang.String r0 = "enabled"
                boolean r6 = r6.equals(r0)
                if (r6 == 0) goto L_0x003e
                r6 = r4
                goto L_0x003f
            L_0x003e:
                r6 = -1
            L_0x003f:
                if (r6 == 0) goto L_0x005b
                if (r6 == r3) goto L_0x0058
                if (r6 == r2) goto L_0x004e
                if (r6 == r1) goto L_0x004a
                r5 = r4
                r4 = r3
                goto L_0x0059
            L_0x004a:
                r5 = r3
                r3 = r4
                r4 = r5
                goto L_0x005d
            L_0x004e:
                boolean r6 = com.samsung.android.game.gos.data.model.Global.DefaultGlobalData.isForcedByDefault(r5)
                boolean r5 = com.samsung.android.game.gos.data.model.Global.DefaultGlobalData.isEnabledByDefault(r5)
                r4 = r6
                goto L_0x005d
            L_0x0058:
                r5 = r4
            L_0x0059:
                r3 = r5
                goto L_0x005d
            L_0x005b:
                r5 = r3
                r3 = r4
            L_0x005d:
                com.samsung.android.game.gos.data.model.GlobalFeatureFlag$Flags r6 = new com.samsung.android.game.gos.data.model.GlobalFeatureFlag$Flags
                r6.<init>(r3, r4, r5)
                return r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.data.model.GlobalFeatureFlag.Flags.getFlagsByState(java.lang.String, java.lang.String):com.samsung.android.game.gos.data.model.GlobalFeatureFlag$Flags");
        }
    }

    public static class NameAndState {
        public boolean enabledFlagByServer;
        public boolean forcedFlag;
        public boolean inheritedFlag;
        public String name;
        public String state;

        public NameAndState(String str, String str2) {
            this.name = str;
            this.state = str2;
            Flags flagsByState = Flags.getFlagsByState(str, str2);
            this.inheritedFlag = flagsByState.inheritedFlag;
            this.forcedFlag = flagsByState.forcedFlag;
            this.enabledFlagByServer = flagsByState.enabledFlagByServer;
        }
    }

    public static class NameAndEnabledFlagByUser {
        public boolean enabledFlagByUser;
        public String name;

        public NameAndEnabledFlagByUser(String str, boolean z) {
            this.name = str;
            this.enabledFlagByUser = z;
        }
    }

    public static class NameAndUsingUserValue {
        public String name;
        public boolean usingUserValue;

        public NameAndUsingUserValue(String str, boolean z) {
            this.name = str;
            this.usingUserValue = z;
        }
    }

    public static class NameAndUsingPkgValue {
        public String name;
        public boolean usingPkgValue;

        public NameAndUsingPkgValue(String str, boolean z) {
            this.name = str;
            this.usingPkgValue = z;
        }
    }
}
