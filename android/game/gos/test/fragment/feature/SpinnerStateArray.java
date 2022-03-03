package com.samsung.android.game.gos.test.fragment.feature;

import java.util.Map;

class SpinnerStateArray implements Map.Entry<Integer, String> {
    int mKey;
    String mValue;

    public SpinnerStateArray(int i, String str) {
        this.mKey = i;
        this.mValue = str;
    }

    public Integer getKey() {
        return Integer.valueOf(this.mKey);
    }

    public String getValue() {
        return this.mValue;
    }

    public String setValue(String str) {
        this.mValue = str;
        return str;
    }
}
