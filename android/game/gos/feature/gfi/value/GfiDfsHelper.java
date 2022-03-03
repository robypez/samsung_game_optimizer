package com.samsung.android.game.gos.feature.gfi.value;

import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Package;
import java.util.HashMap;
import java.util.Map;

public class GfiDfsHelper {
    public static final float DFS_MAX = 120.0f;
    private static Map<String, Float> sDfsValues = new HashMap();

    public static void pushDfs(String str, float f) throws NullPointerException {
        if (sDfsValues.get(str) == null) {
            sDfsValues.put(str, Float.valueOf(getDfsDefault(str)));
        }
        setDfsDefault(str, f);
    }

    public static void popDfs(String str) throws NullPointerException {
        Float f = sDfsValues.get(str);
        if (f != null) {
            setDfsDefault(str, f.floatValue());
            sDfsValues.remove(str);
        }
    }

    private static void setDfsDefault(String str, float f) {
        float[] eachModeDfsArray;
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(str);
        if (packageR != null && (eachModeDfsArray = packageR.getEachModeDfsArray()) != null) {
            eachModeDfsArray[1] = f;
            packageR.setEachModeDfs(eachModeDfsArray);
            DbHelper.getInstance().getPackageDao().insertOrUpdate(packageR);
        }
    }

    private static float getDfsDefault(String str) {
        float[] eachModeDfsArray;
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(str);
        if (packageR == null || (eachModeDfsArray = packageR.getEachModeDfsArray()) == null) {
            return 120.0f;
        }
        return eachModeDfsArray[1];
    }
}
