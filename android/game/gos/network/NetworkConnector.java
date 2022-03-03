package com.samsung.android.game.gos.network;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import com.google.gson.JsonObject;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.PreferenceHelper;
import com.samsung.android.game.gos.data.dbhelper.CategoryInfoDbHelper;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.dbhelper.LocalLogDbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.data.model.Package;
import com.samsung.android.game.gos.data.type.ReportData;
import com.samsung.android.game.gos.network.response.BaseResponse;
import com.samsung.android.game.gos.network.response.CategoryResponse;
import com.samsung.android.game.gos.network.response.DeviceGroupResponse;
import com.samsung.android.game.gos.network.response.PerfPolicyResponse;
import com.samsung.android.game.gos.network.response.PostResponse;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PackageUtil;
import com.samsung.android.game.gos.util.TypeConverter;
import com.samsung.android.game.gos.util.ValidationUtil;
import com.samsung.android.game.gos.value.Constants;
import com.samsung.android.game.gos.value.RinglogConstants;
import datautil.IDataUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkConnector {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = NetworkConnector.class.getSimpleName();
    private static final String NETWORK_POLICY_SERVICE = "netpolicy";
    private static final int POLICY_REJECT_METERED_BACKGROUND = 1;
    static String sBaseUrl;
    static String sBaseUrlForPost;
    private GameModePostService gameModePostService;
    private GameModeService gameModeService;
    private OkHttpClient.Builder httpClient;
    private OkHttpClient.Builder httpPostClient;
    private RequestHeader mRequestHeader;
    private int mTargetServer;

    public NetworkConnector(Context context) {
        this.mRequestHeader = new RequestHeader(context);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        this.httpClient = builder;
        builder.addInterceptor(new Interceptor() {
            public final Response intercept(Interceptor.Chain chain) {
                return NetworkConnector.this.lambda$new$0$NetworkConnector(chain);
            }
        });
        this.httpClient.connectTimeout(Duration.ofMillis(RinglogConstants.SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS));
        OkHttpClient.Builder builder2 = new OkHttpClient.Builder();
        this.httpPostClient = builder2;
        builder2.addInterceptor(new Interceptor() {
            public final Response intercept(Interceptor.Chain chain) {
                return NetworkConnector.this.lambda$new$1$NetworkConnector(chain);
            }
        });
        this.httpPostClient.connectTimeout(Duration.ofMillis(RinglogConstants.SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS));
        int targetServer = DbHelper.getInstance().getGlobalDao().getTargetServer();
        this.mTargetServer = targetServer;
        setTargetServer(targetServer);
        String str = LOG_TAG;
        GosLog.v(str, "RequestHeader: " + this.mRequestHeader.getUserAgent());
    }

    public /* synthetic */ Response lambda$new$0$NetworkConnector(Interceptor.Chain chain) throws IOException {
        Request.Builder newBuilder = chain.request().newBuilder();
        String userAgent = this.mRequestHeader.getUserAgent();
        String str = LOG_TAG;
        GosLog.v(str, "userAgent: " + userAgent);
        return chain.proceed(newBuilder.header("User-Agent", userAgent).build());
    }

    public /* synthetic */ Response lambda$new$1$NetworkConnector(Interceptor.Chain chain) throws IOException {
        return chain.proceed(chain.request().newBuilder().header("User-Agent", this.mRequestHeader.getUserAgent()).header(IDataUtil.DATA_SCHEME_VERSION_NAME, IDataUtil.DATA_SCHEME_VERSION_VALUE).build());
    }

    public void setTargetServer(int i) {
        if (i == 0) {
            sBaseUrl = GameModeService.DEV_URL;
            sBaseUrlForPost = GameModePostService.POST_DEV_URL;
        } else if (i == 1) {
            sBaseUrl = GameModeService.STG_URL;
            sBaseUrlForPost = GameModePostService.POST_STG_URL;
        } else if (i != 2) {
            String str = LOG_TAG;
            GosLog.e(str, "setTargetServer, unexpected targetServer: " + i);
            sBaseUrl = GameModeService.PRD_URL;
            sBaseUrlForPost = GameModePostService.POST_PRD_URL;
            i = 2;
        } else {
            sBaseUrl = GameModeService.PRD_URL;
            sBaseUrlForPost = GameModePostService.POST_PRD_URL;
        }
        if (i != this.mTargetServer) {
            this.mTargetServer = i;
            DbHelper.getInstance().getGlobalDao().setTargetServer(new Global.IdAndTargetServer(i));
        }
        String str2 = LOG_TAG;
        GosLog.v(str2, "sBaseUrl: " + replaceUrl(sBaseUrl));
        String str3 = LOG_TAG;
        GosLog.v(str3, "sBaseUrlForPost: " + replaceUrl(sBaseUrlForPost));
        setServiceWithWebServerUrl(sBaseUrl, sBaseUrlForPost);
    }

    /* access modifiers changed from: package-private */
    public void setServiceWithWebServerUrl(String str, String str2) {
        if (str == null) {
            str = sBaseUrl;
        }
        if (str2 == null) {
            str2 = sBaseUrlForPost;
        }
        this.gameModeService = (GameModeService) new Retrofit.Builder().baseUrl(str).addConverterFactory(GsonConverterFactory.create()).client(this.httpClient.build()).build().create(GameModeService.class);
        this.gameModePostService = (GameModePostService) new Retrofit.Builder().baseUrl(str2).addConverterFactory(GsonConverterFactory.create()).client(this.httpPostClient.build()).build().create(GameModePostService.class);
    }

    public int isSupportedDeviceFromServer(String str) {
        DeviceGroupResponse deviceGroupResponse;
        int i;
        GosLog.d(LOG_TAG, "isSupportedDeviceFromServer()");
        Call<DeviceGroupResponse> v4GetDeviceGroup = this.gameModeService.v4GetDeviceGroup(XAmznTraceIdCreator.create(), SystemClock.elapsedRealtime(), str, getDeviceName());
        V4Result access$000 = new V4Result().getV4Response(this.mRequestHeader, v4GetDeviceGroup);
        String str2 = null;
        if (access$000 != null) {
            i = access$000.getCode();
            deviceGroupResponse = i == 200 ? (DeviceGroupResponse) access$000.getBody() : null;
            if (i >= 200 && i < 300) {
                new PreferenceHelper(AppContext.get().createDeviceProtectedStorageContext()).put(PreferenceHelper.PREF_HAS_CHECKED_DEVICE_REGISTRATION_TO_SERVER, true);
            }
        } else {
            i = 0;
            deviceGroupResponse = null;
        }
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isSupportedDeviceFromServer(), request: ");
        sb.append(replaceUrl(v4GetDeviceGroup.request().url().toString()));
        sb.append(", Code: ");
        sb.append(access$000 != null ? Integer.valueOf(i) : null);
        sb.append(", v4DeviceResponse: ");
        if (deviceGroupResponse != null) {
            str2 = deviceGroupResponse.toString();
        }
        sb.append(str2);
        GosLog.i(str3, sb.toString());
        return i;
    }

    public PerfPolicyResponse getGlobalPerfPolicyData(String str) {
        PerfPolicyResponse perfPolicyResponse;
        GosLog.d(LOG_TAG, "getGlobalPerfPolicyData()");
        Call<PerfPolicyResponse> v4GetDevicePolicy = this.gameModeService.v4GetDevicePolicy(XAmznTraceIdCreator.create(), SystemClock.elapsedRealtime(), str, getDeviceName(), getOsVersion(), getGmsVersion(), getGosVersion());
        V4Result access$000 = new V4Result().getV4Response(this.mRequestHeader, v4GetDevicePolicy);
        String str2 = null;
        if (access$000 != null) {
            perfPolicyResponse = access$000.getCode() == 204 ? new PerfPolicyResponse() : (PerfPolicyResponse) access$000.getBody();
        } else {
            perfPolicyResponse = null;
        }
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getGlobalPerfPolicyData(), request: ");
        sb.append(replaceUrl(v4GetDevicePolicy.request().url().toString()));
        sb.append(", v4PolicyResponse: ");
        if (perfPolicyResponse != null) {
            str2 = perfPolicyResponse.toString();
        }
        sb.append(str2);
        GosLog.i(str3, sb.toString());
        return perfPolicyResponse;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: com.samsung.android.game.gos.network.response.CategoryResponse} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.samsung.android.game.gos.network.response.CategoryResponse getSingleCategoryResponse(java.lang.String r8, java.lang.String r9) {
        /*
            r7 = this;
            java.lang.String r0 = LOG_TAG
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "getSingleCategoryResponse(), packageName : "
            r1.append(r2)
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            com.samsung.android.game.gos.util.GosLog.d(r0, r1)
            r0 = 0
            if (r8 != 0) goto L_0x001a
            return r0
        L_0x001a:
            java.lang.String r2 = com.samsung.android.game.gos.network.XAmznTraceIdCreator.create()
            long r3 = android.os.SystemClock.elapsedRealtime()
            com.samsung.android.game.gos.network.GameModeService r1 = r7.gameModeService
            r5 = r9
            r6 = r8
            retrofit2.Call r8 = r1.v4GetPackageCategoryOnInstall(r2, r3, r5, r6)
            com.samsung.android.game.gos.network.NetworkConnector$V4Result r9 = new com.samsung.android.game.gos.network.NetworkConnector$V4Result
            r9.<init>()
            com.samsung.android.game.gos.network.RequestHeader r1 = r7.mRequestHeader
            com.samsung.android.game.gos.network.NetworkConnector$V4Result r9 = r9.getV4Response(r1, r8)
            if (r9 == 0) goto L_0x003e
            java.lang.Object r9 = r9.getBody()
            r0 = r9
            com.samsung.android.game.gos.network.response.CategoryResponse r0 = (com.samsung.android.game.gos.network.response.CategoryResponse) r0
        L_0x003e:
            java.lang.String r9 = LOG_TAG
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "getSingleCategoryResponse(), request: "
            r1.append(r2)
            okhttp3.Request r2 = r8.request()
            okhttp3.HttpUrl r2 = r2.url()
            java.lang.String r2 = r2.toString()
            java.lang.String r2 = replaceUrl(r2)
            r1.append(r2)
            java.lang.String r2 = ", v4PkgResponse: "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r1 = r1.toString()
            com.samsung.android.game.gos.util.GosLog.i(r9, r1)
            okhttp3.Request r8 = r8.request()
            okhttp3.HttpUrl r8 = r8.url()
            java.lang.String r8 = r8.toString()
            com.samsung.android.game.gos.network.response.CategoryResponse r8 = r7.validateCategoryResponse(r0, r8)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.network.NetworkConnector.getSingleCategoryResponse(java.lang.String, java.lang.String):com.samsung.android.game.gos.network.response.CategoryResponse");
    }

    /* access modifiers changed from: package-private */
    public CategoryResponse validateCategoryResponse(CategoryResponse categoryResponse, String str) {
        if (categoryResponse == null) {
            return null;
        }
        String pkgName = categoryResponse.getPkgName();
        String pkgType = categoryResponse.getPkgType();
        if (!isCategoryResponseContentsNull(categoryResponse)) {
            return categoryResponse;
        }
        String str2 = "validateCategoryResponse(), wrong body data: pkgName: " + pkgName + ", pkgType: " + pkgType;
        GosLog.e(LOG_TAG, str2);
        reportException(this.mRequestHeader.getUserAgent(), str, (String) null, 0, (Exception) null, str2);
        return null;
    }

    public BaseResponse getSinglePkgPolicyResponse(CategoryResponse categoryResponse, String str) {
        if (categoryResponse == null) {
            return null;
        }
        String pkgName = categoryResponse.getPkgName();
        String pkgType = categoryResponse.getPkgType();
        String str2 = LOG_TAG;
        GosLog.v(str2, "getSinglePkgPolicyResponse(), packageName : " + pkgName);
        if (pkgName == null) {
            return null;
        }
        if (!pkgType.equals(Constants.CategoryCode.GAME)) {
            return categoryResponse;
        }
        Call<PerfPolicyResponse> v4GetGamePolicy = this.gameModeService.v4GetGamePolicy(XAmznTraceIdCreator.create(), SystemClock.elapsedRealtime(), str, pkgName, getDeviceName(), getOsVersion(), getGmsVersion(), getGosVersion());
        return parsePerfPolicyResponseResult(pkgName, new V4Result().getV4Response(this.mRequestHeader, v4GetGamePolicy), v4GetGamePolicy.request().url().toString());
    }

    /* access modifiers changed from: package-private */
    public PerfPolicyResponse parsePerfPolicyResponseResult(String str, V4Result<PerfPolicyResponse> v4Result, String str2) {
        PerfPolicyResponse perfPolicyResponse;
        if (!(str == null || v4Result == null)) {
            if (v4Result.getCode() == 204) {
                perfPolicyResponse = new PerfPolicyResponse(str);
            } else {
                perfPolicyResponse = v4Result.getBody();
            }
            GosLog.d(LOG_TAG, "getSinglePkgPolicyResponse(), request: " + replaceUrl(str2) + ", v4PolicyResponse: " + perfPolicyResponse);
            if (perfPolicyResponse != null) {
                if (perfPolicyResponse.getPkgName() != null && perfPolicyResponse.getPkgName().length() >= 1) {
                    return perfPolicyResponse;
                }
                String str3 = "getSinglePkgPolicyResponse(), wrong body data: pkgName: " + perfPolicyResponse.getPkgName();
                GosLog.e(LOG_TAG, str3);
                reportException(this.mRequestHeader.getUserAgent(), str2, (String) null, 0, (Exception) null, str3);
            }
        }
        return null;
    }

    public List<BaseResponse> getPkgResponseList(List<String> list, String str) {
        if (list == null || list.isEmpty()) {
            GosLog.w(LOG_TAG, "getPkgResponseList(), packageNameList is null or empty.");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        getCategoryFromAssets(arrayList, list, arrayList2);
        if (list.isEmpty()) {
            GosLog.w(LOG_TAG, "getPkgResponseList(), packageNameList is empty.");
            return null;
        }
        String str2 = LOG_TAG;
        GosLog.d(str2, "getPkgResponseList(), packageNameList.size(): " + list.size() + ", packageNameList: " + list);
        for (String categoryFromServer : getPackageNamesForQuery(list)) {
            getCategoryFromServer(arrayList, categoryFromServer, arrayList2, str);
        }
        getPolicyFromServer(arrayList, arrayList2, str);
        return arrayList;
    }

    public List<PerfPolicyResponse> getSelectiveTargetPolicyFromServer(String str) {
        StringBuilder sb = new StringBuilder();
        ArrayList arrayList = new ArrayList();
        try {
            String create = XAmznTraceIdCreator.create();
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Call<List<PerfPolicyResponse>> v4GetSelectvieTargetPolicies = this.gameModeService.v4GetSelectvieTargetPolicies(create, elapsedRealtime, str, GlobalDbHelper.getInstance().getUUID());
            List<PerfPolicyResponse> list = null;
            V4Result access$000 = new V4Result().getV4Response(this.mRequestHeader, v4GetSelectvieTargetPolicies);
            if (access$000 != null) {
                list = (List) access$000.getBody();
                if (access$000.getCode() == 204 && list == null) {
                    GosLog.e(LOG_TAG, "getSelectiveTargetPolicyFromServer error case");
                }
            }
            sb.append(", request: ");
            sb.append(replaceUrl(v4GetSelectvieTargetPolicies.request().url().toString()));
            sb.append(", v4PolicyResponse: ");
            sb.append(list);
            String str2 = LOG_TAG;
            GosLog.d(str2, "getSelectiveTargetPolicyFromServer : " + sb);
            if (list != null) {
                for (PerfPolicyResponse perfPolicyResponse : list) {
                    if (perfPolicyResponse.getPkgName() != null) {
                        if (perfPolicyResponse.getPkgName().length() >= 1) {
                            arrayList.add(perfPolicyResponse);
                        }
                    }
                    reportException(this.mRequestHeader.getUserAgent(), v4GetSelectvieTargetPolicies.request().url().toString(), (String) null, 0, (Exception) null, "getPolicyFromServer(), wrong body data: pkgName: " + perfPolicyResponse.pkgName);
                }
            }
        } catch (Exception e) {
            GosLog.e(LOG_TAG, Log.getStackTraceString(e));
        }
        return arrayList;
    }

    private void getCategoryFromAssets(List<BaseResponse> list, List<String> list2, List<String> list3) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String next : list2) {
            if (PackageUtil.isNonGamePackage(next) || CategoryInfoDbHelper.getInstance().isFixed(next)) {
                putToPackageList(list, list3, arrayList, next, Constants.CategoryCode.NON_GAME);
            }
        }
        if (arrayList.size() > 0) {
            for (String remove : arrayList) {
                list2.remove(remove);
            }
            String str = LOG_TAG;
            GosLog.d(str, "getPkgResponseList(), skipCheckPackageList.size(): " + arrayList.size() + ", skipCheckPackageList: " + arrayList);
        }
    }

    private void putToPackageList(List<BaseResponse> list, List<String> list2, List<String> list3, String str, String str2) {
        Package packageR = DbHelper.getInstance().getPackageDao().getPackage(str);
        if (!(packageR == null || packageR.getCategoryCode() == null || packageR.getCategoryCode().equals(str2))) {
            str2 = packageR.getCategoryCode();
        }
        if (str2.equals(Constants.CategoryCode.GAME)) {
            list2.add(str);
        } else {
            list.add(new CategoryResponse(str, str2));
        }
        list3.add(str);
    }

    public List<String> getPackageNamesForQuery(List<String> list) {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 <= size; i2++) {
            if (i2 == size || i >= 100 || sb.toString().getBytes().length > 3500) {
                arrayList2.add(String.format(Locale.US, "packageCount: %d, countSum: %d, length: %,dbyte, packageNames: %s", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(sb.toString().getBytes().length), sb.toString()}));
                arrayList.add(sb.toString());
                if (i2 == size) {
                    break;
                }
                sb = new StringBuilder();
                i = 0;
            }
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(ValidationUtil.getValidPkgName(list.get(i2)));
            i++;
        }
        if (!arrayList2.isEmpty()) {
            GosLog.d(LOG_TAG, "getPackageNamesForQuery(), log:" + arrayList2.toString());
        }
        return arrayList;
    }

    public List<CategoryResponse> getCategoryFromServer(String str, String str2) {
        try {
            V4Result access$000 = new V4Result().getV4Response(this.mRequestHeader, this.gameModeService.v4GetPackagesCategories(XAmznTraceIdCreator.create(), SystemClock.elapsedRealtime(), str2, str));
            if (access$000 != null) {
                return (List) access$000.getBody();
            }
            return null;
        } catch (Exception e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void getCategoryFromServer(List<BaseResponse> list, String str, List<String> list2, String str2) {
        try {
            Call<List<CategoryResponse>> v4GetPackagesCategories = this.gameModeService.v4GetPackagesCategories(XAmznTraceIdCreator.create(), SystemClock.elapsedRealtime(), str2, str);
            List<CategoryResponse> list3 = null;
            V4Result access$000 = new V4Result().getV4Response(this.mRequestHeader, v4GetPackagesCategories);
            if (access$000 != null) {
                list3 = (List) access$000.getBody();
            }
            String str3 = LOG_TAG;
            GosLog.d(str3, "getCategoryFromServer(), request: " + replaceUrl(v4GetPackagesCategories.request().url().toString()) + ", v4PkgResponse: " + list3);
            if (list3 != null) {
                ArrayList arrayList = new ArrayList();
                for (CategoryResponse categoryResponse : list3) {
                    if (isCategoryResponseContentsNull(categoryResponse)) {
                        reportException(this.mRequestHeader.getUserAgent(), v4GetPackagesCategories.request().url().toString(), (String) null, 0, (Exception) null, "getCategoryFromServer(), wrong result data: pkgName: " + categoryResponse.getPkgName() + ", pkgType: " + categoryResponse.getPkgType());
                    } else if (Constants.CategoryCode.GAME.equals(categoryResponse.getPkgType())) {
                        list2.add(categoryResponse.getPkgName());
                    } else {
                        arrayList.add(categoryResponse.toString());
                        list.add(categoryResponse);
                    }
                }
                if (!arrayList.isEmpty()) {
                    String str4 = LOG_TAG;
                    GosLog.d(str4, "getCategoryFromServer(): " + arrayList.toString());
                }
            }
        } catch (Exception e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    /* access modifiers changed from: package-private */
    public void getPolicyFromServer(List<BaseResponse> list, List<String> list2, String str) {
        int i;
        String str2;
        int i2;
        int i3;
        ArrayList arrayList;
        boolean z;
        ArrayList arrayList2;
        StringBuilder sb;
        boolean z2 = true;
        if (list2.size() >= 1) {
            int size = ((list2.size() - 1) / 100) + 1;
            String deviceName = getDeviceName();
            StringBuilder sb2 = new StringBuilder();
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            StringBuilder sb3 = sb2;
            int i4 = 0;
            while (i4 < size) {
                int i5 = i4 * 100;
                if (i4 < size - 1) {
                    i = i5 + 100;
                } else {
                    i = list2.size();
                }
                List<String> subList = list2.subList(i5, i);
                sb3.append("start:");
                sb3.append(i5);
                sb3.append(",end:");
                sb3.append(i);
                try {
                    String str3 = deviceName;
                    i2 = i4;
                    sb = sb3;
                    i3 = size;
                    arrayList2 = arrayList3;
                    str2 = deviceName;
                    arrayList = arrayList4;
                    try {
                        Call<List<PerfPolicyResponse>> v4GetGamePolicies = this.gameModeService.v4GetGamePolicies(XAmznTraceIdCreator.create(), SystemClock.elapsedRealtime(), str, str3, TypeConverter.stringsToCsv((Iterable<String>) subList), getOsVersion(), getGmsVersion(), getGosVersion());
                        List<PerfPolicyResponse> list3 = null;
                        V4Result access$000 = new V4Result().getV4Response(this.mRequestHeader, v4GetGamePolicies);
                        if (access$000 != null) {
                            list3 = (List) access$000.getBody();
                            if (access$000.getCode() == 204 && list3 == null) {
                                list3 = new ArrayList<>();
                                for (String perfPolicyResponse : subList) {
                                    list3.add(new PerfPolicyResponse(perfPolicyResponse));
                                }
                            } else if (list3 != null) {
                                for (String next : subList) {
                                    boolean z3 = false;
                                    for (PerfPolicyResponse pkgName : list3) {
                                        if (pkgName.getPkgName().equals(next)) {
                                            z3 = true;
                                        }
                                    }
                                    if (!z3) {
                                        list3.add(new PerfPolicyResponse(next));
                                    }
                                }
                            }
                        }
                        sb.append(", request: ");
                        sb.append(replaceUrl(v4GetGamePolicies.request().url().toString()));
                        sb.append(", v4PolicyResponse: ");
                        sb.append(list3);
                        if (list3 != null) {
                            for (PerfPolicyResponse perfPolicyResponse2 : list3) {
                                if (perfPolicyResponse2.getPkgName() != null) {
                                    z = true;
                                    if (perfPolicyResponse2.getPkgName().length() < 1) {
                                        List<BaseResponse> list4 = list;
                                    } else {
                                        try {
                                            list.add(perfPolicyResponse2);
                                        } catch (Exception e) {
                                            e = e;
                                            arrayList.add(Log.getStackTraceString(e));
                                            arrayList2.add(sb.toString());
                                            sb3 = new StringBuilder();
                                            i4 = i2 + 1;
                                            arrayList3 = arrayList2;
                                            z2 = z;
                                            arrayList4 = arrayList;
                                            size = i3;
                                            deviceName = str2;
                                        }
                                    }
                                } else {
                                    List<BaseResponse> list5 = list;
                                }
                                reportException(this.mRequestHeader.getUserAgent(), v4GetGamePolicies.request().url().toString(), (String) null, 0, (Exception) null, "getPolicyFromServer(), wrong body data: pkgName: " + perfPolicyResponse2.pkgName);
                            }
                        }
                        List<BaseResponse> list6 = list;
                        z = true;
                    } catch (Exception e2) {
                        e = e2;
                        List<BaseResponse> list7 = list;
                        z = true;
                        arrayList.add(Log.getStackTraceString(e));
                        arrayList2.add(sb.toString());
                        sb3 = new StringBuilder();
                        i4 = i2 + 1;
                        arrayList3 = arrayList2;
                        z2 = z;
                        arrayList4 = arrayList;
                        size = i3;
                        deviceName = str2;
                    }
                } catch (Exception e3) {
                    e = e3;
                    List<BaseResponse> list8 = list;
                    z = z2;
                    i3 = size;
                    i2 = i4;
                    sb = sb3;
                    arrayList2 = arrayList3;
                    str2 = deviceName;
                    arrayList = arrayList4;
                    arrayList.add(Log.getStackTraceString(e));
                    arrayList2.add(sb.toString());
                    sb3 = new StringBuilder();
                    i4 = i2 + 1;
                    arrayList3 = arrayList2;
                    z2 = z;
                    arrayList4 = arrayList;
                    size = i3;
                    deviceName = str2;
                }
                arrayList2.add(sb.toString());
                sb3 = new StringBuilder();
                i4 = i2 + 1;
                arrayList3 = arrayList2;
                z2 = z;
                arrayList4 = arrayList;
                size = i3;
                deviceName = str2;
            }
            ArrayList arrayList5 = arrayList3;
            ArrayList arrayList6 = arrayList4;
            if (!arrayList5.isEmpty()) {
                GosLog.d(LOG_TAG, "getPolicyFromServer(), logs: " + arrayList5.toString());
            }
            if (!arrayList6.isEmpty()) {
                GosLog.w(LOG_TAG, "getPolicyFromServer(), exceptions: " + arrayList6.toString());
            }
        }
    }

    public boolean postJson(JSONObject jSONObject) {
        String str;
        GosLog.d(LOG_TAG, "postJson(), begin");
        boolean z = false;
        try {
            String jSONObject2 = jSONObject.toString();
            if (jSONObject2.length() < 3) {
                return true;
            }
            Call<JsonObject> post = this.gameModePostService.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jSONObject2));
            if (isMainLooper()) {
                GosLog.d(LOG_TAG, "postJson(), creating an AsyncTask because of unsuitable performing network operations on the UI thread");
                str = (String) new V4PostNetworkTask(this.mRequestHeader, post, jSONObject2).execute(new Void[0]).get();
            } else {
                str = new V4PostNetworkTask(this.mRequestHeader, post, jSONObject2).doDirectly();
            }
            z = PostResponse.parseResponseSuccess(str);
            String str2 = LOG_TAG;
            GosLog.i(str2, "postJson(), successful: " + z);
            return z;
        } catch (Exception e) {
            GosLog.w(LOG_TAG, (Throwable) e);
        }
    }

    static class V4Result<T> {
        private T body;
        int code;

        V4Result() {
        }

        V4Result(T t, int i) {
            this.body = t;
            this.code = i;
        }

        public T getBody() {
            return this.body;
        }

        public void setBody(T t) {
            this.body = t;
        }

        public int getCode() {
            return this.code;
        }

        public void setCode(int i) {
            this.code = i;
        }

        /* access modifiers changed from: private */
        public V4Result<T> getV4Response(RequestHeader requestHeader, Call<T> call) {
            try {
                if (!NetworkConnector.isMainLooper()) {
                    return new V4NetworkTask(requestHeader, call.clone()).doDirectly();
                }
                GosLog.d(NetworkConnector.LOG_TAG, "creating an AsyncTask because of unsuitable performing network operations on the UI thread");
                return (V4Result) new V4NetworkTask(requestHeader, call.clone()).execute(new Void[0]).get();
            } catch (ExecutionException e) {
                GosLog.w(NetworkConnector.LOG_TAG, (Throwable) e);
                return null;
            } catch (InterruptedException e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    private static class V4NetworkTask<T> extends AsyncTask<Void, Void, V4Result<T>> {
        private Call<T> call;
        private RequestHeader mRequestHeader;
        private String parameter = null;

        V4NetworkTask(RequestHeader requestHeader, Call<T> call2) {
            this.mRequestHeader = requestHeader;
            this.call = call2;
        }

        /* access modifiers changed from: protected */
        public V4Result<T> doInBackground(Void... voidArr) {
            return doDirectly();
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x00fe, code lost:
            r4 = e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0104, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0105, code lost:
            com.samsung.android.game.gos.util.GosLog.w(com.samsung.android.game.gos.network.NetworkConnector.access$300(), "doDirectly(), NullPointerException", r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0110, code lost:
            r4 = e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0111, code lost:
            r6 = null;
            r10 = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x011a, code lost:
            r12 = "doDirectly(), Failed: Network is not connected. Allow background data usage : " + com.samsung.android.game.gos.network.NetworkConnector.access$700();
            com.samsung.android.game.gos.util.GosLog.w(com.samsung.android.game.gos.network.NetworkConnector.access$300(), r12);
            com.samsung.android.game.gos.network.NetworkConnector.access$600(com.samsung.android.game.gos.ipm.BuildConfig.VERSION_NAME, com.samsung.android.game.gos.ipm.BuildConfig.VERSION_NAME, com.samsung.android.game.gos.ipm.BuildConfig.VERSION_NAME, -1, r11, r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0141, code lost:
            r3 = "doDirectly(), Cannot get correct response Allow background data usage : " + com.samsung.android.game.gos.network.NetworkConnector.access$700();
            com.samsung.android.game.gos.util.GosLog.w(com.samsung.android.game.gos.network.NetworkConnector.access$300(), r3);
            com.samsung.android.game.gos.util.GosLog.w(com.samsung.android.game.gos.network.NetworkConnector.access$300(), (java.lang.Throwable) r11);
            com.samsung.android.game.gos.network.NetworkConnector.access$600("xTraceId:" + r10 + ", UserAgent:" + r14.mRequestHeader.getUserAgent(), r6, r14.parameter, -1, r11, r3);
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0104 A[ExcHandler: NullPointerException (r0v5 'e' java.lang.NullPointerException A[CUSTOM_DECLARE]), Splitter:B:1:0x0005] */
        /* JADX WARNING: Removed duplicated region for block: B:39:0x011a  */
        /* JADX WARNING: Removed duplicated region for block: B:40:0x0141  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.samsung.android.game.gos.network.NetworkConnector.V4Result<T> doDirectly() {
            /*
                r14 = this;
                java.lang.String r0 = ", UserAgent:"
                java.lang.String r1 = "xTraceId:"
                r2 = 0
                retrofit2.Call<T> r3 = r14.call     // Catch:{ IOException -> 0x0110, NullPointerException -> 0x0104 }
                okhttp3.Request r3 = r3.request()     // Catch:{ IOException -> 0x0110, NullPointerException -> 0x0104 }
                okhttp3.HttpUrl r3 = r3.url()     // Catch:{ IOException -> 0x0110, NullPointerException -> 0x0104 }
                java.lang.String r3 = r3.toString()     // Catch:{ IOException -> 0x0110, NullPointerException -> 0x0104 }
                retrofit2.Call<T> r4 = r14.call     // Catch:{ IOException -> 0x0100, NullPointerException -> 0x0104 }
                okhttp3.Request r4 = r4.request()     // Catch:{ IOException -> 0x0100, NullPointerException -> 0x0104 }
                java.lang.String r5 = "X-Samsung-Trace-Id"
                java.lang.String r10 = r4.header(r5)     // Catch:{ IOException -> 0x0100, NullPointerException -> 0x0104 }
                retrofit2.Call<T> r4 = r14.call     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                retrofit2.Response r11 = r4.execute()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                boolean r4 = r11.isSuccessful()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r12 = ", URL: "
                if (r4 == 0) goto L_0x0071
                java.lang.Object r4 = r11.body()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r5 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r6.<init>()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r7 = "doDirectly(), Response isSuccessful, responseCode: "
                r6.append(r7)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                int r7 = r11.code()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r6.append(r7)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r6.append(r12)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r7 = com.samsung.android.game.gos.network.NetworkConnector.replaceUrl(r3)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r6.append(r7)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r7 = ", response: "
                r6.append(r7)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                if (r4 == 0) goto L_0x005c
                java.lang.String r7 = r4.toString()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                goto L_0x005d
            L_0x005c:
                r7 = r2
            L_0x005d:
                r6.append(r7)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                com.samsung.android.game.gos.util.GosLog.d(r5, r6)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                com.samsung.android.game.gos.network.NetworkConnector$V4Result r5 = new com.samsung.android.game.gos.network.NetworkConnector$V4Result     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                int r6 = r11.code()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r5.<init>(r4, r6)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                return r5
            L_0x0071:
                okhttp3.ResponseBody r4 = r11.errorBody()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                if (r4 == 0) goto L_0x0081
                okhttp3.ResponseBody r4 = r11.errorBody()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r4 = r4.string()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r13 = r4
                goto L_0x0082
            L_0x0081:
                r13 = r2
            L_0x0082:
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r4.<init>()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r4.append(r1)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r4.append(r10)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r4.append(r0)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                com.samsung.android.game.gos.network.RequestHeader r5 = r14.mRequestHeader     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r5 = r5.getUserAgent()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r4.append(r5)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r6 = r14.parameter     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                int r7 = r11.code()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r8 = 0
                r5 = r3
                r9 = r13
                com.samsung.android.game.gos.network.NetworkConnector.reportException(r4, r5, r6, r7, r8, r9)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r4 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r5.<init>()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r6 = "doDirectly(), Response else, responseCode: "
                r5.append(r6)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                int r6 = r11.code()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r5.append(r6)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r5.append(r12)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r6 = com.samsung.android.game.gos.network.NetworkConnector.replaceUrl(r3)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r5.append(r6)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r6 = ", error response: "
                r5.append(r6)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r5.append(r13)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                java.lang.String r5 = r5.toString()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                com.samsung.android.game.gos.util.GosLog.i(r4, r5)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                int r4 = r11.code()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r5 = 500(0x1f4, float:7.0E-43)
                if (r4 < r5) goto L_0x00f4
                int r4 = r11.code()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r5 = 600(0x258, float:8.41E-43)
                if (r4 >= r5) goto L_0x00f4
                android.app.Application r4 = com.samsung.android.game.gos.context.AppContext.get()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                if (r4 == 0) goto L_0x00f4
                com.samsung.android.game.gos.network.NetworkTaskCallbackHolder r4 = com.samsung.android.game.gos.network.NetworkTaskCallbackHolder.getInstance()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r4.callOnFail()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
            L_0x00f4:
                com.samsung.android.game.gos.network.NetworkConnector$V4Result r4 = new com.samsung.android.game.gos.network.NetworkConnector$V4Result     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                int r5 = r11.code()     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                r4.<init>(r2, r5)     // Catch:{ IOException -> 0x00fe, NullPointerException -> 0x0104 }
                return r4
            L_0x00fe:
                r4 = move-exception
                goto L_0x0102
            L_0x0100:
                r4 = move-exception
                r10 = r2
            L_0x0102:
                r6 = r3
                goto L_0x0113
            L_0x0104:
                r0 = move-exception
                java.lang.String r1 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG
                java.lang.String r3 = "doDirectly(), NullPointerException"
                com.samsung.android.game.gos.util.GosLog.w(r1, r3, r0)
                goto L_0x0187
            L_0x0110:
                r4 = move-exception
                r6 = r2
                r10 = r6
            L_0x0113:
                r11 = r4
                boolean r3 = com.samsung.android.game.gos.util.NetworkUtil.isNetworkConnected()
                if (r3 != 0) goto L_0x0141
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "doDirectly(), Failed: Network is not connected. Allow background data usage : "
                r0.append(r1)
                java.lang.Boolean r1 = com.samsung.android.game.gos.network.NetworkConnector.isAllowedBGMobileDataUsage()
                r0.append(r1)
                java.lang.String r12 = r0.toString()
                java.lang.String r0 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG
                com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r0, (java.lang.String) r12)
                r10 = -1
                java.lang.String r7 = ""
                java.lang.String r8 = ""
                java.lang.String r9 = ""
                com.samsung.android.game.gos.network.NetworkConnector.reportException(r7, r8, r9, r10, r11, r12)
                goto L_0x0187
            L_0x0141:
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = "doDirectly(), Cannot get correct response Allow background data usage : "
                r3.append(r4)
                java.lang.Boolean r4 = com.samsung.android.game.gos.network.NetworkConnector.isAllowedBGMobileDataUsage()
                r3.append(r4)
                java.lang.String r3 = r3.toString()
                java.lang.String r4 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG
                com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r4, (java.lang.String) r3)
                java.lang.String r4 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG
                com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r4, (java.lang.Throwable) r11)
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                r4.append(r1)
                r4.append(r10)
                r4.append(r0)
                com.samsung.android.game.gos.network.RequestHeader r0 = r14.mRequestHeader
                java.lang.String r0 = r0.getUserAgent()
                r4.append(r0)
                java.lang.String r5 = r4.toString()
                java.lang.String r7 = r14.parameter
                r8 = -1
                r9 = r11
                r10 = r3
                com.samsung.android.game.gos.network.NetworkConnector.reportException(r5, r6, r7, r8, r9, r10)
            L_0x0187:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.network.NetworkConnector.V4NetworkTask.doDirectly():com.samsung.android.game.gos.network.NetworkConnector$V4Result");
        }
    }

    /* access modifiers changed from: private */
    public static Boolean isAllowedBGMobileDataUsage() {
        Object systemService = AppContext.get().getSystemService(NETWORK_POLICY_SERVICE);
        try {
            boolean z = false;
            if ((((Integer) systemService.getClass().getMethod("getUidPolicy", new Class[]{Integer.TYPE}).invoke(systemService, new Object[]{Integer.valueOf(AppContext.get().getPackageManager().getApplicationInfo("com.samsung.android.game.gos", 0).uid)})).intValue() & 1) == 0) {
                z = true;
            }
            return Boolean.valueOf(z);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return null;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    private static class V4PostNetworkTask extends AsyncTask<Void, Void, String> {
        private Call<JsonObject> call;
        private RequestHeader mRequestHeader;
        private String parameter;

        V4PostNetworkTask(RequestHeader requestHeader, Call<JsonObject> call2, String str) {
            this.mRequestHeader = requestHeader;
            this.call = call2;
            this.parameter = str;
        }

        /* access modifiers changed from: protected */
        public final String doInBackground(Void... voidArr) {
            return doDirectly();
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x00f3, code lost:
            r4 = e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x00f9, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x00fa, code lost:
            com.samsung.android.game.gos.util.GosLog.w(com.samsung.android.game.gos.network.NetworkConnector.access$300(), "doDirectly(), NullPointerException", r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0104, code lost:
            r4 = e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0105, code lost:
            r6 = null;
            r10 = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x010e, code lost:
            com.samsung.android.game.gos.util.GosLog.w(com.samsung.android.game.gos.network.NetworkConnector.access$300(), "doDirectly(), Failed: Network is not connected.");
            com.samsung.android.game.gos.network.NetworkConnector.access$600(com.samsung.android.game.gos.ipm.BuildConfig.VERSION_NAME, com.samsung.android.game.gos.ipm.BuildConfig.VERSION_NAME, com.samsung.android.game.gos.ipm.BuildConfig.VERSION_NAME, -1, r11, "doDirectly(), Failed: Network is not connected.");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0124, code lost:
            com.samsung.android.game.gos.util.GosLog.w(com.samsung.android.game.gos.network.NetworkConnector.access$300(), "doDirectly(), Cannot get correct response");
            com.samsung.android.game.gos.util.GosLog.w(com.samsung.android.game.gos.network.NetworkConnector.access$300(), (java.lang.Throwable) r11);
            com.samsung.android.game.gos.network.NetworkConnector.access$600("xTraceId:" + r10 + ", UserAgent:" + r15.mRequestHeader.getUserAgent(), r6, r15.parameter, -1, r11, "doDirectly(), Cannot get correct response");
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x00f9 A[ExcHandler: NullPointerException (r0v4 'e' java.lang.NullPointerException A[CUSTOM_DECLARE]), Splitter:B:1:0x0005] */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x010e  */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0124  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String doDirectly() {
            /*
                r15 = this;
                java.lang.String r0 = ", UserAgent:"
                java.lang.String r1 = "xTraceId:"
                r2 = 0
                retrofit2.Call<com.google.gson.JsonObject> r3 = r15.call     // Catch:{ IOException -> 0x0104, NullPointerException -> 0x00f9 }
                okhttp3.Request r3 = r3.request()     // Catch:{ IOException -> 0x0104, NullPointerException -> 0x00f9 }
                okhttp3.HttpUrl r3 = r3.url()     // Catch:{ IOException -> 0x0104, NullPointerException -> 0x00f9 }
                java.lang.String r3 = r3.toString()     // Catch:{ IOException -> 0x0104, NullPointerException -> 0x00f9 }
                retrofit2.Call<com.google.gson.JsonObject> r4 = r15.call     // Catch:{ IOException -> 0x00f5, NullPointerException -> 0x00f9 }
                okhttp3.Request r4 = r4.request()     // Catch:{ IOException -> 0x00f5, NullPointerException -> 0x00f9 }
                java.lang.String r5 = "X-Samsung-Trace-Id"
                java.lang.String r10 = r4.header(r5)     // Catch:{ IOException -> 0x00f5, NullPointerException -> 0x00f9 }
                retrofit2.Call<com.google.gson.JsonObject> r4 = r15.call     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                retrofit2.Response r11 = r4.execute()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r4 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r5.<init>()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r6 = "doDirectly(), Response, response.headers(): "
                r5.append(r6)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                okhttp3.Headers r6 = r11.headers()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r5.append(r6)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r5 = r5.toString()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                com.samsung.android.game.gos.util.GosLog.d(r4, r5)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                boolean r4 = r11.isSuccessful()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r12 = ", URL: "
                java.lang.String r13 = "doDirectly(), Response, responseCode: "
                if (r4 == 0) goto L_0x008e
                java.lang.Object r4 = r11.body()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                if (r4 == 0) goto L_0x0060
                java.lang.Object r4 = r11.body()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                com.google.gson.JsonObject r4 = (com.google.gson.JsonObject) r4     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                goto L_0x0061
            L_0x0060:
                r4 = r2
            L_0x0061:
                java.lang.String r5 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r6.<init>()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r6.append(r13)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                int r7 = r11.code()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r6.append(r7)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r6.append(r12)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r7 = com.samsung.android.game.gos.network.NetworkConnector.replaceUrl(r3)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r6.append(r7)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r7 = ", response: "
                r6.append(r7)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r6.append(r4)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                com.samsung.android.game.gos.util.GosLog.d(r5, r6)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                return r4
            L_0x008e:
                okhttp3.ResponseBody r4 = r11.errorBody()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                if (r4 == 0) goto L_0x009e
                okhttp3.ResponseBody r4 = r11.errorBody()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r4 = r4.string()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r14 = r4
                goto L_0x009f
            L_0x009e:
                r14 = r2
            L_0x009f:
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r4.<init>()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r4.append(r1)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r4.append(r10)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r4.append(r0)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                com.samsung.android.game.gos.network.RequestHeader r5 = r15.mRequestHeader     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r5 = r5.getUserAgent()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r4.append(r5)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r6 = r15.parameter     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                int r7 = r11.code()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r8 = 0
                r5 = r3
                r9 = r14
                com.samsung.android.game.gos.network.NetworkConnector.reportException(r4, r5, r6, r7, r8, r9)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r4 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r5.<init>()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r5.append(r13)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                int r6 = r11.code()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r5.append(r6)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r5.append(r12)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r6 = com.samsung.android.game.gos.network.NetworkConnector.replaceUrl(r3)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r5.append(r6)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r6 = ", error response: "
                r5.append(r6)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                r5.append(r14)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                java.lang.String r5 = r5.toString()     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                com.samsung.android.game.gos.util.GosLog.i(r4, r5)     // Catch:{ IOException -> 0x00f3, NullPointerException -> 0x00f9 }
                return r2
            L_0x00f3:
                r4 = move-exception
                goto L_0x00f7
            L_0x00f5:
                r4 = move-exception
                r10 = r2
            L_0x00f7:
                r6 = r3
                goto L_0x0107
            L_0x00f9:
                r0 = move-exception
                java.lang.String r1 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG
                java.lang.String r3 = "doDirectly(), NullPointerException"
                com.samsung.android.game.gos.util.GosLog.w(r1, r3, r0)
                goto L_0x0158
            L_0x0104:
                r4 = move-exception
                r6 = r2
                r10 = r6
            L_0x0107:
                r11 = r4
                boolean r3 = com.samsung.android.game.gos.util.NetworkUtil.isNetworkConnected()
                if (r3 != 0) goto L_0x0124
                java.lang.String r0 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG
                java.lang.String r1 = "doDirectly(), Failed: Network is not connected."
                com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r0, (java.lang.String) r1)
                r10 = -1
                java.lang.String r7 = ""
                java.lang.String r8 = ""
                java.lang.String r9 = ""
                java.lang.String r12 = "doDirectly(), Failed: Network is not connected."
                com.samsung.android.game.gos.network.NetworkConnector.reportException(r7, r8, r9, r10, r11, r12)
                goto L_0x0158
            L_0x0124:
                java.lang.String r3 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG
                java.lang.String r4 = "doDirectly(), Cannot get correct response"
                com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r3, (java.lang.String) r4)
                java.lang.String r3 = com.samsung.android.game.gos.network.NetworkConnector.LOG_TAG
                com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r3, (java.lang.Throwable) r11)
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                r3.append(r1)
                r3.append(r10)
                r3.append(r0)
                com.samsung.android.game.gos.network.RequestHeader r0 = r15.mRequestHeader
                java.lang.String r0 = r0.getUserAgent()
                r3.append(r0)
                java.lang.String r5 = r3.toString()
                java.lang.String r7 = r15.parameter
                r8 = -1
                java.lang.String r10 = "doDirectly(), Cannot get correct response"
                r9 = r11
                com.samsung.android.game.gos.network.NetworkConnector.reportException(r5, r6, r7, r8, r9, r10)
            L_0x0158:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.network.NetworkConnector.V4PostNetworkTask.doDirectly():java.lang.String");
        }
    }

    /* access modifiers changed from: private */
    public static void reportException(String str, String str2, String str3, int i, Exception exc, String str4) {
        LocalLogDbHelper.getInstance().addLocalLog(LOG_TAG, ReportData.createServerConnectionLogJsonMsg(str, str2, str3, i, exc, str4));
    }

    /* access modifiers changed from: private */
    public static boolean isMainLooper() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private boolean isCategoryResponseContentsNull(CategoryResponse categoryResponse) {
        if (categoryResponse.getPkgName() == null || categoryResponse.getPkgName().length() < 1 || categoryResponse.getPkgType() == null || categoryResponse.getPkgType().length() < 1) {
            return true;
        }
        return false;
    }

    private float getGmsVersion() {
        return DbHelper.getInstance().getGlobalDao().getGmsVersion();
    }

    private long getGosVersion() {
        return PackageUtil.getPackageVersionCode(AppContext.get(), "com.samsung.android.game.gos");
    }

    private String getDeviceName() {
        return DbHelper.getInstance().getGlobalDao().getDeviceName();
    }

    private int getOsVersion() {
        return Build.VERSION.SDK_INT;
    }

    /* access modifiers changed from: private */
    public static String replaceUrl(String str) {
        if (str == null) {
            return null;
        }
        if (str.contains(GameModePostService.POST_DEV_URL)) {
            return str.replace(sBaseUrlForPost, "DEV_POST/");
        }
        if (str.contains(GameModePostService.POST_STG_URL)) {
            return str.replace(sBaseUrlForPost, "STG_POST/");
        }
        if (str.contains(GameModePostService.POST_PRD_URL)) {
            return str.replace(sBaseUrlForPost, "PRD_POST/");
        }
        if (str.contains(GameModeService.DEV_URL)) {
            return str.replace(sBaseUrl, "DEV/");
        }
        if (str.contains(GameModeService.STG_URL)) {
            return str.replace(sBaseUrl, "STG/");
        }
        return str.contains(GameModeService.PRD_URL) ? str.replace(sBaseUrl, "PRD/") : str;
    }
}
