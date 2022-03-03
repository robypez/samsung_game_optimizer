package com.samsung.android.game.gos.data.model;

public class FeatureFlag {
    private boolean enabled;
    private boolean enabledFlagByServer;
    private boolean enabledFlagByUser;
    private boolean forcedFlag;
    private boolean inheritedFlag;
    private String name;
    private String pkgName;
    private String state = "inherited";

    public FeatureFlag() {
    }

    public FeatureFlag(String str, String str2, String str3) {
        setName(str);
        setPkgName(str2);
        setState(str3);
        setFlag();
        setEnabledFlagByUser(false);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setFlag() {
        /*
            r6 = this;
            java.lang.String r0 = r6.getState()
            int r1 = r0.hashCode()
            r2 = 3
            r3 = 2
            r4 = 1
            r5 = 0
            switch(r1) {
                case -1609594047: goto L_0x0038;
                case -1557378342: goto L_0x002e;
                case 270940796: goto L_0x0024;
                case 520972834: goto L_0x001a;
                case 1894004667: goto L_0x0010;
                default: goto L_0x000f;
            }
        L_0x000f:
            goto L_0x0042
        L_0x0010:
            java.lang.String r1 = "forcibly_disabled"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = 4
            goto L_0x0043
        L_0x001a:
            java.lang.String r1 = "forcibly_enabled"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = r2
            goto L_0x0043
        L_0x0024:
            java.lang.String r1 = "disabled"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = r4
            goto L_0x0043
        L_0x002e:
            java.lang.String r1 = "inherited"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = r3
            goto L_0x0043
        L_0x0038:
            java.lang.String r1 = "enabled"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = r5
            goto L_0x0043
        L_0x0042:
            r0 = -1
        L_0x0043:
            if (r0 == 0) goto L_0x0073
            if (r0 == r4) goto L_0x0069
            if (r0 == r3) goto L_0x005f
            if (r0 == r2) goto L_0x0055
            r6.setInheritedFlag(r5)
            r6.setForcedFlag(r4)
            r6.setEnabledFlagByServer(r5)
            goto L_0x007c
        L_0x0055:
            r6.setInheritedFlag(r5)
            r6.setForcedFlag(r4)
            r6.setEnabledFlagByServer(r4)
            goto L_0x007c
        L_0x005f:
            r6.setInheritedFlag(r4)
            r6.setForcedFlag(r5)
            r6.setEnabledFlagByServer(r5)
            goto L_0x007c
        L_0x0069:
            r6.setInheritedFlag(r5)
            r6.setForcedFlag(r5)
            r6.setEnabledFlagByServer(r5)
            goto L_0x007c
        L_0x0073:
            r6.setInheritedFlag(r5)
            r6.setForcedFlag(r5)
            r6.setEnabledFlagByServer(r4)
        L_0x007c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.data.model.FeatureFlag.setFlag():void");
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String str) {
        this.pkgName = str;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String str) {
        this.state = str;
        setFlag();
    }

    public boolean isInheritedFlag() {
        return this.inheritedFlag;
    }

    public void setInheritedFlag(boolean z) {
        this.inheritedFlag = z;
    }

    public boolean isForcedFlag() {
        return this.forcedFlag;
    }

    public void setForcedFlag(boolean z) {
        this.forcedFlag = z;
    }

    public boolean isEnabledFlagByServer() {
        return this.enabledFlagByServer;
    }

    public void setEnabledFlagByServer(boolean z) {
        this.enabledFlagByServer = z;
    }

    public boolean isEnabledFlagByUser() {
        return this.enabledFlagByUser;
    }

    public void setEnabledFlagByUser(boolean z) {
        this.enabledFlagByUser = z;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
    }
}
