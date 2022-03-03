package com.samsung.android.game.gos.ipm;

import java.util.Map;

public class WrapUtility {
    public static String[] toArray(VectorString vectorString) {
        String[] strArr = new String[vectorString.size()];
        for (int i = 0; i < vectorString.size(); i++) {
            strArr[i] = vectorString.get((long) i);
        }
        return strArr;
    }

    public static VectorString toVectorString(String[] strArr) {
        VectorString vectorString = new VectorString();
        vectorString.ensureCapacity((long) strArr.length);
        for (String add : strArr) {
            vectorString.add(add);
        }
        return vectorString;
    }

    public static MapLongFloat toMapLongFloat(Map<Long, Float> map) {
        MapLongFloat mapLongFloat = new MapLongFloat();
        for (Map.Entry next : map.entrySet()) {
            mapLongFloat.put(((Long) next.getKey()).longValue(), ((Float) next.getValue()).floatValue());
        }
        return mapLongFloat;
    }
}
