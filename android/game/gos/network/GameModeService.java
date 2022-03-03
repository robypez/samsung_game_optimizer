package com.samsung.android.game.gos.network;

import com.google.gson.JsonObject;
import com.samsung.android.game.gos.network.response.CategoryResponse;
import com.samsung.android.game.gos.network.response.DeviceGroupResponse;
import com.samsung.android.game.gos.network.response.PerfPolicyResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface GameModeService {
    public static final String DEV_URL = "https://gos-api.dev-gos-gsp.io/";
    public static final String PRD_URL = "https://gos-api.gos-gsp.io/";
    public static final String STG_URL = "https://gos-api.qa-gos-gsp.io/";

    @GET("mode/value/global")
    Call<JsonObject> global(@Query("device_name") String str);

    @GET("v3/packages/?type=install")
    Call<JsonObject> packageInstall(@Query("device_name") String str, @Query("package_names") String str2, @Query("installer_package_names") String str3);

    @GET("v3/packages/?type=select")
    Call<JsonObject> packages(@Query("device_name") String str, @Query("package_names") String str2, @Query("installer_package_names") String str3);

    @GET("v4/devices/{deviceName}")
    @Headers({"Accept: application/json;charset=UTF-8"})
    Call<DeviceGroupResponse> v4GetDeviceGroup(@Header("X-Samsung-Trace-Id") String str, @Header("et") long j, @Header("ct") String str2, @Path("deviceName") String str3);

    @GET("v4/gos/devices/{device_name}/policy")
    @Headers({"Accept: application/json;charset=UTF-8"})
    Call<PerfPolicyResponse> v4GetDevicePolicy(@Header("X-Samsung-Trace-Id") String str, @Header("et") long j, @Header("ct") String str2, @Path("device_name") String str3, @Query("os_sdk_version") int i, @Query("gms_version") float f, @Query("gos_version") long j2);

    @GET("v4/gos/packages/policy")
    @Headers({"Accept: application/json;charset=UTF-8"})
    Call<List<PerfPolicyResponse>> v4GetGamePolicies(@Header("X-Samsung-Trace-Id") String str, @Header("et") long j, @Header("ct") String str2, @Query("device_name") String str3, @Query(encoded = true, value = "package_names") String str4, @Query("os_sdk_version") int i, @Query("gms_version") float f, @Query("gos_version") long j2);

    @GET("v4/gos/packages/{package_name}/policy")
    @Headers({"Accept: application/json;charset=UTF-8"})
    Call<PerfPolicyResponse> v4GetGamePolicy(@Header("X-Samsung-Trace-Id") String str, @Header("et") long j, @Header("ct") String str2, @Path("package_name") String str3, @Query("device_name") String str4, @Query("os_sdk_version") int i, @Query("gms_version") float f, @Query("gos_version") long j2);

    @GET("v4/packages/{packageName}")
    @Headers({"Accept: application/json;charset=UTF-8"})
    Call<CategoryResponse> v4GetPackageCategoryOnInstall(@Header("X-Samsung-Trace-Id") String str, @Header("et") long j, @Header("ct") String str2, @Path("packageName") String str3);

    @GET("v4/packages")
    @Headers({"Accept: application/json;charset=UTF-8"})
    Call<List<CategoryResponse>> v4GetPackagesCategories(@Header("X-Samsung-Trace-Id") String str, @Header("et") long j, @Header("ct") String str2, @Query(encoded = true, value = "package_names") String str3);

    @GET("v4/gos/devices/{uuid}/selective-target-pkg-policies")
    @Headers({"Accept: application/json;charset=UTF-8"})
    Call<List<PerfPolicyResponse>> v4GetSelectvieTargetPolicies(@Header("X-Samsung-Trace-Id") String str, @Header("et") long j, @Header("ct") String str2, @Path("uuid") String str3);
}
