package com.samsung.android.game.gos.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryResponse extends BaseResponse {
    @SerializedName("pkg_name")
    @Expose
    private String pkgName;
    @SerializedName("pkg_type")
    @Expose
    private String pkgType;

    public CategoryResponse(String str, String str2) {
        this.pkgName = str;
        this.pkgType = str2;
    }

    public String toString() {
        return String.format("{\"pkg_name\":\"%s\", \"pkg_type\":\"%s\"}", new Object[]{this.pkgName, this.pkgType});
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String str) {
        this.pkgName = str;
    }

    public String getPkgType() {
        return this.pkgType;
    }

    public void setPkgType(String str) {
        this.pkgType = str;
    }
}
