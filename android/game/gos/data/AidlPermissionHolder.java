package com.samsung.android.game.gos.data;

import android.util.Pair;
import java.util.HashMap;
import java.util.Map;

public class AidlPermissionHolder {
    private final Map<String, AidlPermission> mapPkgName;
    private final Map<Integer, AidlPermission> mapUid;

    private AidlPermissionHolder() {
        this.mapUid = new HashMap();
        this.mapPkgName = new HashMap();
    }

    public static AidlPermissionHolder getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean add(String str, int i, boolean z) {
        if (i < 10000) {
            return false;
        }
        AidlPermission aidlPermission = new AidlPermission(str, i, z);
        this.mapUid.put(Integer.valueOf(i), aidlPermission);
        this.mapPkgName.put(str, aidlPermission);
        return true;
    }

    public boolean remove(String str) {
        AidlPermission aidlPermission = this.mapPkgName.get(str);
        if (aidlPermission == null) {
            return false;
        }
        int i = aidlPermission.uid;
        this.mapPkgName.remove(str);
        this.mapUid.remove(Integer.valueOf(i));
        return true;
    }

    public Pair<String, Boolean> getInfo(int i) {
        AidlPermission aidlPermission = this.mapUid.get(Integer.valueOf(i));
        if (aidlPermission != null) {
            return new Pair<>(aidlPermission.pkgName, Boolean.valueOf(aidlPermission.isAllowedToGosAidl));
        }
        return null;
    }

    private static class AidlPermission {
        final boolean isAllowedToGosAidl;
        final String pkgName;
        final int uid;

        AidlPermission(String str, int i, boolean z) {
            this.uid = i;
            this.pkgName = str;
            this.isAllowedToGosAidl = z;
        }
    }

    private static class SingletonHolder {
        static final AidlPermissionHolder INSTANCE = new AidlPermissionHolder();

        private SingletonHolder() {
        }
    }
}
