package com.samsung.android.game.gos.test.fragment.feature;

import java.util.Map;

public class FeatureInfo implements Map.Entry<String, String> {
    String mKey;
    String mValue;

    public FeatureInfo(String str, String str2) {
        this.mKey = str;
        this.mValue = str2;
    }

    public String getKey() {
        return this.mKey;
    }

    public String getValue() {
        return this.mValue;
    }

    public String setValue(String str) {
        this.mValue = str;
        return str;
    }
}
