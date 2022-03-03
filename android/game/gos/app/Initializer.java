package com.samsung.android.game.gos.app;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.os.AsyncTask;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.FeatureSetManager;
import com.samsung.android.game.gos.data.DataManager;
import com.samsung.android.game.gos.data.dao.GlobalDao;
import com.samsung.android.game.gos.data.database.CategoryInfoDatabase;
import com.samsung.android.game.gos.data.database.GosDatabase;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.dbhelper.GlobalDbHelper;
import com.samsung.android.game.gos.data.dbhelper.PackageDbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.endpoint.GosSystemService;
import com.samsung.android.game.gos.feature.dss.DssFeature;
import com.samsung.android.game.gos.feature.externalsdk.ExternalSdkCore;
import com.samsung.android.game.gos.gamemanager.GmsGlobalPackageDataSetter;
import com.samsung.android.game.gos.network.NetworkTaskCallbackHolder;
import com.samsung.android.game.gos.selibrary.SeGameManager;
import com.samsung.android.game.gos.service.AlarmController;
import com.samsung.android.game.gos.service.MainIntentService;
import com.samsung.android.game.gos.test.fragment.TestActivity;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.Constants;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Initializer {
    private static final String LOG_TAG = "Initializer";

    private Initializer() {
    }

    public static void initGos(Context context) {
        try {
            DbHelper.getInstance().init(GosDatabase.build(context));
            DataManager instance = DataManager.getInstance();
            instance.registerModeChangedListener(GmsGlobalPackageDataSetter.class.getSimpleName(), GmsGlobalPackageDataSetter.getInstance());
            GosLog.v(LOG_TAG, "temp log: " + instance.toString());
            NetworkTaskCallbackHolder.getInstance().setCallback(new AlarmController());
            ExternalSdkCore.putThisToIpmCoreAsCallback();
            GosSystemService.setTestActivityClass(TestActivity.class);
            if (DbHelper.getInstance().getGlobalDao().isInitialized()) {
                GosLog.i(LOG_TAG, "DB is already initialized");
            } else {
                GosLog.i(LOG_TAG, "DB is not initialized yet");
                initDatabase();
            }
            if (!DbHelper.getInstance().getGlobalDao().isDataReady()) {
                Intent intent = new Intent(context, MainIntentService.class);
                intent.putExtra("type", Constants.IntentType.MAKE_DATA_READY.val());
                context.startService(intent);
            }
            Intent intent2 = new Intent(context, MainIntentService.class);
            intent2.putExtra("type", Constants.IntentType.INIT_GOS.val());
            context.startService(intent2);
            PackageDbHelper.getInstance().replaceTunableNonGameApps();
            CategoryInfoDatabase.buildCategoryInfoDatabase();
        } catch (SQLiteCantOpenDatabaseException unused) {
            GosLog.e(LOG_TAG, "initGOS database open fail.");
        } catch (IllegalStateException unused2) {
            GosLog.e(LOG_TAG, "startService failed.");
        }
        GosLog.i(LOG_TAG, "finished initGos()");
    }

    static void initDatabase() {
        GosLog.d(LOG_TAG, "initDatabase");
        float version = SeGameManager.getInstance().getVersion();
        GlobalDao globalDao = DbHelper.getInstance().getGlobalDao();
        globalDao.setGmsVersion(new Global.IdAndGmsVersion(version));
        for (Map.Entry next : FeatureSetManager.getAvailableFeatureFlagMap(AppContext.get()).entrySet()) {
            GlobalDbHelper.getInstance().setAvailable((String) next.getKey(), ((Boolean) next.getValue()).booleanValue());
        }
        globalDao.setSosPolicyKeyCsv(new Global.IdAndSosPolicyKeyCsv(SeGameManager.getInstance().getSosPolicyKeysCsv()));
        DssFeature.getInstance().onDbInitialized(AppContext.get());
        globalDao.setInitialized(new Global.IdAndInitialized(true));
        DataManager.getInstance().setModes(globalDao.getResolutionMode(), globalDao.getDfsMode(), GlobalDbHelper.getInstance().isUsingCustomValue(Constants.V4FeatureFlag.RESOLUTION), GlobalDbHelper.getInstance().isUsingCustomValue(Constants.V4FeatureFlag.DFS), false);
    }

    public static void initGosAsync(Context context) {
        try {
            new GosDBInit().execute(new Context[]{context}).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

    private static class GosDBInit extends AsyncTask<Context, Void, Void> {
        private GosDBInit() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Context... contextArr) {
            Initializer.initGos(contextArr[0]);
            return null;
        }
    }
}
