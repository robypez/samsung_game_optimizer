package com.samsung.android.game.gos.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CharacterUtil {
    public static <T, R> Map<T, R> filterOut(Map<T, R> map, List<T> list) {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (T next : map.keySet()) {
            if (!list.contains(next)) {
                arrayList.add(next);
            }
        }
        for (Object remove : arrayList) {
            map.remove(remove);
        }
        return map;
    }

    public static int getByteSize(String str) {
        if (str != null) {
            return str.getBytes(StandardCharsets.UTF_8).length;
        }
        return 0;
    }
}
