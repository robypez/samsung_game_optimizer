package com.samsung.android.game.gos.ipm;

public enum Profile {
    LOW((String) 0),
    HIGH((String) 1),
    ULTRA((String) 2),
    CUSTOM((String) 3),
    CRITICAL((String) 4);
    
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public static Profile swigToEnum(int i) {
        Class<Profile> cls = Profile.class;
        Profile[] profileArr = (Profile[]) cls.getEnumConstants();
        if (i < profileArr.length && i >= 0 && profileArr[i].swigValue == i) {
            return profileArr[i];
        }
        for (Profile profile : profileArr) {
            if (profile.swigValue == i) {
                return profile;
            }
        }
        throw new IllegalArgumentException("No enum " + cls + " with value " + i);
    }

    private Profile(int i) {
        this.swigValue = i;
        int unused = SwigNext.next = i + 1;
    }

    private Profile(Profile profile) {
        int i = profile.swigValue;
        this.swigValue = i;
        int unused = SwigNext.next = i + 1;
    }

    private static class SwigNext {
        /* access modifiers changed from: private */
        public static int next;

        private SwigNext() {
        }

        static /* synthetic */ int access$008() {
            int i = next;
            next = i + 1;
            return i;
        }
    }

    public static Profile fromInt(int i) {
        return swigToEnum(i);
    }

    public final int toInt() {
        return swigValue();
    }
}
