package com.samsung.android.game.gos.test.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.DataUpdater;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.network.NetworkConnector;
import com.samsung.android.game.gos.selibrary.SeActivityManager;
import com.samsung.android.game.gos.test.util.TestDataSetter;
import com.samsung.android.game.gos.value.Constants;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;

public class TestDataSetter {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = TestDataSetter.class.getSimpleName();
    private Context mContext;
    private DataSettingFeedBack mDataSettingFeedBack;

    public enum DataRangeToBeSet {
        GLOBAL,
        GLOBAL_AND_SINGLE_PKG,
        GLOBAL_AND_ALL_PACKAGES,
        SINGLE_PKG
    }

    public interface DataSettingFeedBack {
        void onRestore(DataRangeToBeSet dataRangeToBeSet, String str);

        void onRestoreFailed(String str) {
        }
    }

    public TestDataSetter(Context context, DataSettingFeedBack dataSettingFeedBack) {
        this.mContext = context;
        this.mDataSettingFeedBack = dataSettingFeedBack;
    }

    public void showDialogToRestore(DataRangeToBeSet dataRangeToBeSet, String str) {
        if (this.mContext instanceof Activity) {
            String str2 = null;
            int i = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet[dataRangeToBeSet.ordinal()];
            if (i == 1 || i == 2 || i == 3) {
                str2 = "All apps will be forced to stop. Is it OK?";
            } else if (i == 4) {
                str2 = "The target app will be forced to stop. Is it OK?";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
            builder.setTitle("Needs to stop the app");
            builder.setMessage(str2);
            builder.setPositiveButton(17039379, new DialogInterface.OnClickListener(dataRangeToBeSet, str) {
                public final /* synthetic */ TestDataSetter.DataRangeToBeSet f$1;
                public final /* synthetic */ String f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void onClick(DialogInterface dialogInterface, int i) {
                    TestDataSetter.this.lambda$showDialogToRestore$0$TestDataSetter(this.f$1, this.f$2, dialogInterface, i);
                }
            }).setNegativeButton(17039369, $$Lambda$TestDataSetter$VKsKyo3rDgEcAK4X0NQfA_oCo.INSTANCE);
            AlertDialog create = builder.create();
            if (create != null) {
                create.show();
            }
        }
    }

    /* renamed from: com.samsung.android.game.gos.test.util.TestDataSetter$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.samsung.android.game.gos.test.util.TestDataSetter$DataRangeToBeSet[] r0 = com.samsung.android.game.gos.test.util.TestDataSetter.DataRangeToBeSet.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet = r0
                com.samsung.android.game.gos.test.util.TestDataSetter$DataRangeToBeSet r1 = com.samsung.android.game.gos.test.util.TestDataSetter.DataRangeToBeSet.GLOBAL     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet     // Catch:{ NoSuchFieldError -> 0x001d }
                com.samsung.android.game.gos.test.util.TestDataSetter$DataRangeToBeSet r1 = com.samsung.android.game.gos.test.util.TestDataSetter.DataRangeToBeSet.GLOBAL_AND_SINGLE_PKG     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.samsung.android.game.gos.test.util.TestDataSetter$DataRangeToBeSet r1 = com.samsung.android.game.gos.test.util.TestDataSetter.DataRangeToBeSet.GLOBAL_AND_ALL_PACKAGES     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.samsung.android.game.gos.test.util.TestDataSetter$DataRangeToBeSet r1 = com.samsung.android.game.gos.test.util.TestDataSetter.DataRangeToBeSet.SINGLE_PKG     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.util.TestDataSetter.AnonymousClass1.<clinit>():void");
        }
    }

    public /* synthetic */ void lambda$showDialogToRestore$0$TestDataSetter(DataRangeToBeSet dataRangeToBeSet, String str, DialogInterface dialogInterface, int i) {
        restoreData(dataRangeToBeSet, str);
    }

    public static void applyGlobalData() {
        if (ApplyAllGamesHandler.getInstance().getState().equals(Thread.State.NEW)) {
            ApplyAllGamesHandler.getInstance().start();
        }
        ApplyAllGamesHandler.getInstance().sendApplyGlobalData();
    }

    public void restoreData(DataRangeToBeSet dataRangeToBeSet, String str) {
        new AsyncTaskToRestore(this.mContext instanceof Activity ? new ProgressDialog(this.mContext) : null, this.mDataSettingFeedBack, dataRangeToBeSet, str, (AnonymousClass1) null).execute(new Void[0]);
    }

    private static class AsyncTaskToRestore extends AsyncTask<Void, Void, Void> {
        private DataRangeToBeSet mDataRangeToBeSet;
        private DataSettingFeedBack mDataSettingFeedBack;
        private ProgressDialog mDialog;
        private boolean mIsGlobalUpdateSuccessful;
        private boolean mIsPkgUpdateSuccessful;
        private String mLogHead;
        private String mPkgName;
        private List<String> mTargetPkgNameList;

        /* synthetic */ AsyncTaskToRestore(ProgressDialog progressDialog, DataSettingFeedBack dataSettingFeedBack, DataRangeToBeSet dataRangeToBeSet, String str, AnonymousClass1 r5) {
            this(progressDialog, dataSettingFeedBack, dataRangeToBeSet, str);
        }

        private AsyncTaskToRestore(ProgressDialog progressDialog, DataSettingFeedBack dataSettingFeedBack, DataRangeToBeSet dataRangeToBeSet, String str) {
            this.mLogHead = AsyncTaskToRestore.class.getSimpleName() + ", ";
            this.mIsGlobalUpdateSuccessful = false;
            this.mIsPkgUpdateSuccessful = false;
            this.mTargetPkgNameList = new ArrayList();
            this.mDialog = progressDialog;
            this.mDataSettingFeedBack = dataSettingFeedBack;
            this.mDataRangeToBeSet = dataRangeToBeSet;
            this.mPkgName = str;
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:5:0x0051, code lost:
            if (r0 != 4) goto L_0x006d;
         */
        /* JADX WARNING: Removed duplicated region for block: B:15:0x00ab  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x00dd  */
        /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onPreExecute() {
            /*
                r6 = this;
                super.onPreExecute()
                java.lang.String r0 = com.samsung.android.game.gos.test.util.TestDataSetter.LOG_TAG
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = r6.mLogHead
                r1.append(r2)
                java.lang.String r2 = "onPreExecute()"
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.samsung.android.game.gos.test.util.GosTestLog.d(r0, r1)
                java.lang.String r0 = com.samsung.android.game.gos.test.util.TestDataSetter.LOG_TAG
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = r6.mLogHead
                r1.append(r2)
                java.lang.String r2 = "dataRangeToBeSet : "
                r1.append(r2)
                com.samsung.android.game.gos.test.util.TestDataSetter$DataRangeToBeSet r2 = r6.mDataRangeToBeSet
                java.lang.String r2 = r2.name()
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.samsung.android.game.gos.test.util.GosTestLog.d(r0, r1)
                int[] r0 = com.samsung.android.game.gos.test.util.TestDataSetter.AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet
                com.samsung.android.game.gos.test.util.TestDataSetter$DataRangeToBeSet r1 = r6.mDataRangeToBeSet
                int r1 = r1.ordinal()
                r0 = r0[r1]
                r1 = 2
                if (r0 == r1) goto L_0x0066
                r1 = 3
                if (r0 == r1) goto L_0x0054
                r1 = 4
                if (r0 == r1) goto L_0x0066
                goto L_0x006d
            L_0x0054:
                java.util.List<java.lang.String> r0 = r6.mTargetPkgNameList
                com.samsung.android.game.gos.data.dbhelper.DbHelper r1 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
                com.samsung.android.game.gos.data.dao.PackageDao r1 = r1.getPackageDao()
                java.util.List r1 = r1.getAllPkgNameList()
                r0.addAll(r1)
                goto L_0x006d
            L_0x0066:
                java.util.List<java.lang.String> r0 = r6.mTargetPkgNameList
                java.lang.String r1 = r6.mPkgName
                r0.add(r1)
            L_0x006d:
                java.util.List<java.lang.String> r0 = r6.mTargetPkgNameList
                if (r0 == 0) goto L_0x00d9
                int r0 = r0.size()
                if (r0 <= 0) goto L_0x00d9
                java.lang.String r0 = com.samsung.android.game.gos.test.util.TestDataSetter.LOG_TAG
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = r6.mLogHead
                r1.append(r2)
                java.lang.String r2 = "mTargetPkgNameList: "
                r1.append(r2)
                java.util.List<java.lang.String> r2 = r6.mTargetPkgNameList
                java.lang.String r2 = r2.toString()
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.samsung.android.game.gos.test.util.GosTestLog.d(r0, r1)
                java.util.ArrayList r0 = new java.util.ArrayList
                r0.<init>()
                java.util.List<java.lang.String> r1 = r6.mTargetPkgNameList
                java.util.Iterator r1 = r1.iterator()
            L_0x00a5:
                boolean r2 = r1.hasNext()
                if (r2 == 0) goto L_0x00ce
                java.lang.Object r2 = r1.next()
                java.lang.String r2 = (java.lang.String) r2
                com.samsung.android.game.gos.data.dbhelper.DbHelper r3 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
                com.samsung.android.game.gos.data.dao.PackageDao r3 = r3.getPackageDao()
                com.samsung.android.game.gos.data.model.Package r2 = r3.getPackage(r2)
                if (r2 == 0) goto L_0x00a5
                int[] r3 = r2.getEachModeTargetShortSideArray()
                r4 = 1
                r5 = -1
                r3[r4] = r5
                r2.setEachModeTargetShortSide((int[]) r3)
                r0.add(r2)
                goto L_0x00a5
            L_0x00ce:
                com.samsung.android.game.gos.data.dbhelper.DbHelper r1 = com.samsung.android.game.gos.data.dbhelper.DbHelper.getInstance()
                com.samsung.android.game.gos.data.dao.PackageDao r1 = r1.getPackageDao()
                r1.insertOrUpdate((java.util.List<com.samsung.android.game.gos.data.model.Package>) r0)
            L_0x00d9:
                android.app.ProgressDialog r0 = r6.mDialog
                if (r0 == 0) goto L_0x00ed
                r1 = 0
                r0.setProgressStyle(r1)
                android.app.ProgressDialog r0 = r6.mDialog
                java.lang.String r1 = "Please wait."
                r0.setMessage(r1)
                android.app.ProgressDialog r0 = r6.mDialog
                r0.show()
            L_0x00ed:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.util.TestDataSetter.AsyncTaskToRestore.onPreExecute():void");
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            String access$100 = TestDataSetter.LOG_TAG;
            GosTestLog.d(access$100, this.mLogHead + "doInBackground()");
            int i = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet[this.mDataRangeToBeSet.ordinal()];
            if (i == 1 || i == 2) {
                this.mIsGlobalUpdateSuccessful = DataUpdater.updateGlobalSettingsFromServer(new NetworkConnector(AppContext.get()), Constants.CallTrigger.TEST);
            } else if (i == 4 && !DbHelper.getInstance().getGlobalDao().isRegisteredDevice()) {
                this.mIsGlobalUpdateSuccessful = DataUpdater.updateGlobalSettingsFromServer(new NetworkConnector(AppContext.get()), Constants.CallTrigger.TEST);
            }
            int i2 = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet[this.mDataRangeToBeSet.ordinal()];
            if (i2 != 2) {
                if (i2 == 3) {
                    boolean updateGlobalAndPkgData = DataUpdater.updateGlobalAndPkgData(AppContext.get(), DataUpdater.PkgUpdateType.ALL, true, new NetworkConnector(AppContext.get()), Constants.CallTrigger.TEST);
                    this.mIsPkgUpdateSuccessful = updateGlobalAndPkgData;
                    this.mIsGlobalUpdateSuccessful = updateGlobalAndPkgData;
                    return null;
                } else if (i2 != 4) {
                    return null;
                }
            }
            this.mIsPkgUpdateSuccessful = DataUpdater.updatePackageList(AppContext.get(), new NetworkConnector(AppContext.get()), this.mTargetPkgNameList, Constants.CallTrigger.TEST);
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
            String str;
            super.onPostExecute(voidR);
            GosTestLog.d(TestDataSetter.LOG_TAG, this.mLogHead + "onPostExecute()");
            if (!DbHelper.getInstance().getGlobalDao().isRegisteredDevice()) {
                str = "This device is not supported by the server. Please inform the developers. The device name : " + DbHelper.getInstance().getGlobalDao().getDeviceName();
                this.mDataSettingFeedBack.onRestoreFailed(str);
            } else if (this.mIsGlobalUpdateSuccessful || this.mIsPkgUpdateSuccessful) {
                int i = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet[this.mDataRangeToBeSet.ordinal()];
                if (i == 1 || i == 2 || i == 3) {
                    GlobalDbHelper.getInstance().setAllEnabledFlagByUserAsDefault();
                }
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.GAME));
                arrayList.addAll(DbHelper.getInstance().getPackageDao().getPkgNameListByCategory(Constants.CategoryCode.MANAGED_APP));
                int i2 = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet[this.mDataRangeToBeSet.ordinal()];
                if (i2 == 1 || i2 == 2 || i2 == 3) {
                    for (String applySingleGame : arrayList) {
                        GmsGlobalPackageDataSetter.getInstance().applySingleGame(applySingleGame);
                    }
                } else if (i2 == 4) {
                    GmsGlobalPackageDataSetter.getInstance().applySingleGame(this.mPkgName);
                }
                int i3 = AnonymousClass1.$SwitchMap$com$samsung$android$game$gos$test$util$TestDataSetter$DataRangeToBeSet[this.mDataRangeToBeSet.ordinal()];
                if (i3 == 1 || i3 == 2 || i3 == 3) {
                    SeActivityManager.getInstance().forceStopPackages(arrayList);
                } else if (i3 == 4) {
                    SeActivityManager.getInstance().forceStopPackages(this.mTargetPkgNameList);
                }
                this.mDataSettingFeedBack.onRestore(this.mDataRangeToBeSet, this.mPkgName);
                StringBuilder sb = new StringBuilder();
                sb.append("Restored");
                boolean z = this.mIsGlobalUpdateSuccessful;
                String str2 = BuildConfig.VERSION_NAME;
                sb.append(z ? ", Global data" : str2);
                if (this.mIsPkgUpdateSuccessful) {
                    str2 = ", Pkg data";
                }
                sb.append(str2);
                str = sb.toString();
            } else {
                str = "Failed. Please check your network.";
                this.mDataSettingFeedBack.onRestoreFailed(str);
            }
            Toast.makeText(AppContext.get(), str, 1).show();
            ProgressDialog progressDialog = this.mDialog;
            if (progressDialog != null && progressDialog.isShowing()) {
                this.mDialog.dismiss();
            }
        }
    }
}
