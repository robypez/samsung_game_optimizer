package com.samsung.android.game.gos.network.response;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import kotlin.text.Typography;

public class BaseResponse {
    public static String getCircuitLog(Object obj) {
        StringBuilder sb = new StringBuilder();
        if (obj != null) {
            try {
                Field[] declaredFields = obj.getClass().getDeclaredFields();
                ArrayList arrayList = new ArrayList();
                for (Field field : declaredFields) {
                    String name = field.getName();
                    if (!name.startsWith("this") && !name.startsWith("FIELD_NAME_")) {
                        if (!name.equals("LOG_TAG")) {
                            arrayList.add(field);
                        }
                    }
                }
                if (obj.getClass() != FeatureResponse.class && (obj instanceof FeatureResponse)) {
                    arrayList.add(FeatureResponse.class.getField("state"));
                }
                if (obj.getClass() != EachModeFeatureResponse.class && (obj instanceof EachModeFeatureResponse)) {
                    arrayList.add(EachModeFeatureResponse.class.getField("modeValues"));
                }
                sb.append("{");
                for (int i = 0; i < arrayList.size(); i++) {
                    Field field2 = (Field) arrayList.get(i);
                    field2.setAccessible(true);
                    String name2 = field2.getName();
                    Object obj2 = field2.get(obj);
                    if (obj2 instanceof String) {
                        obj2 = "\"" + obj2 + "\"";
                    }
                    sb.append(Typography.quote);
                    sb.append(name2);
                    sb.append("\":");
                    sb.append(obj2);
                    if (arrayList.size() - 1 > i) {
                        sb.append(",");
                    }
                }
                sb.append("}");
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return sb.toString();
    }

    public String toString() {
        String circuitLog = getCircuitLog(this);
        if (circuitLog == null || circuitLog.isEmpty()) {
            return null;
        }
        return circuitLog;
    }

    public static String loadJSONFromAsset(Context context, String str) {
        if (str == null) {
            str = "ex_json.json";
        }
        try {
            InputStream open = context.getAssets().open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
