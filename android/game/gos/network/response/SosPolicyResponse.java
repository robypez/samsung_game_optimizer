package com.samsung.android.game.gos.network.response;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SosPolicyResponse extends BaseResponse {
    @SerializedName("pkg_name")
    @Expose
    private String pkgName;
    @SerializedName("pkg_policy")
    @Expose
    private SosPkgPolicy sosPkgPolicy;

    public SosPolicyResponse() {
    }

    public SosPolicyResponse(String str) {
        this.pkgName = str;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String str) {
        this.pkgName = str;
    }

    public SosPkgPolicy getSosPkgPolicy() {
        return this.sosPkgPolicy;
    }

    public void setSosPkgPolicy(SosPkgPolicy sosPkgPolicy2) {
        this.sosPkgPolicy = sosPkgPolicy2;
    }

    public static class SosPkgPolicy extends BaseResponse {
        @SerializedName("boost_settings")
        @Expose
        private String boostSettings;
        @SerializedName("governor_settings")
        @Expose
        private String governorSettings;
        @SerializedName("scheduler_settings")
        @Expose
        private String schedulerSettings;
        @SerializedName("touch_settings")
        @Expose
        private String touchSettings;

        public String getGovernorSettings() {
            return this.governorSettings;
        }

        public void setGovernorSettings(String str) {
            this.governorSettings = str;
        }

        public String toString() {
            return new Gson().toJson((Object) this);
        }
    }
}
