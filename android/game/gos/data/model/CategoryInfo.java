package com.samsung.android.game.gos.data.model;

import com.samsung.android.game.gos.value.Constants;

public class CategoryInfo {
    public String category = Constants.CategoryCode.UNDEFINED;
    public int fixed = 0;
    public String pkgName;

    public CategoryInfo() {
    }

    public CategoryInfo(String str, String str2, int i) {
        this.pkgName = str;
        this.category = str2;
        this.fixed = i;
    }
}
