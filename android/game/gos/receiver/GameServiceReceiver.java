package com.samsung.android.game.gos.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.controller.EventPublisher;
import com.samsung.android.game.gos.selibrary.SeUserHandleManager;
import com.samsung.android.game.gos.service.GameIntentService;
import com.samsung.android.game.gos.service.MainIntentService;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.util.PlatformUtil;
import com.samsung.android.game.gos.util.SecureFolderUtil;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.Constants;

public class GameServiceReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "GameServiceReceiver";

    public void onReceive(Context context, Intent intent) {
        new Task(goAsync(), intent).execute(new Void[0]);
    }

    private static class Task extends AsyncTask<Void, Void, Void> {
        private final Intent intent;
        private final BroadcastReceiver.PendingResult pendingResult;
        private final String taskObjectInfo;

        private Task(BroadcastReceiver.PendingResult pendingResult2, Intent intent2) {
            this.pendingResult = pendingResult2;
            this.intent = intent2;
            this.taskObjectInfo = toString();
            GosLog.v(GameServiceReceiver.LOG_TAG, "Task.Task() ends. taskObjectInfo=" + this.taskObjectInfo);
            GosLog.v(GameServiceReceiver.LOG_TAG, "Task.Task() ends. pendingResult=" + pendingResult2);
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            GosLog.v(GameServiceReceiver.LOG_TAG, "Task.doInBackground() starts. taskObjectInfo=" + this.taskObjectInfo);
            Application application = AppContext.get();
            Intent intent2 = this.intent;
            if (intent2 == null) {
                return null;
            }
            String action = intent2.getAction();
            GosLog.i(GameServiceReceiver.LOG_TAG, "Task.doInBackground(), action : " + action);
            if (action == null) {
                return null;
            }
            Intent intent3 = new Intent(application, MainIntentService.class);
            char c = 65535;
            switch (action.hashCode()) {
                case -1670536538:
                    if (action.equals("android.app.action.ENTER_KNOX_DESKTOP_MODE")) {
                        c = 7;
                        break;
                    }
                    break;
                case -1662080879:
                    if (action.equals("com.sec.android.app.secsetupwizard.SETUPWIZARD_COMPLETE")) {
                        c = 0;
                        break;
                    }
                    break;
                case 2034148:
                    if (action.equals("android.app.action.EXIT_KNOX_DESKTOP_MODE")) {
                        c = 8;
                        break;
                    }
                    break;
                case 172491798:
                    if (action.equals("android.intent.action.PACKAGE_CHANGED")) {
                        c = 5;
                        break;
                    }
                    break;
                case 393315116:
                    if (action.equals("com.sec.android.app.setupwizard.SETUPWIZARD_COMPLETE")) {
                        c = 1;
                        break;
                    }
                    break;
                case 724784510:
                    if (action.equals("com.samsung.intent.action.MEDIA_SERVER_REBOOTED")) {
                        c = 6;
                        break;
                    }
                    break;
                case 798292259:
                    if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                        c = 2;
                        break;
                    }
                    break;
                case 902476158:
                    if (action.equals("com.samsung.android.intent.action.PACKAGE_INSTALL_STARTED")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1737074039:
                    if (action.equals("android.intent.action.MY_PACKAGE_REPLACED")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                    intent3.putExtra("type", Constants.IntentType.BOOT_COMPLETED.val());
                    try {
                        application.startService(intent3);
                        break;
                    } catch (IllegalStateException unused) {
                        GosLog.e(GameServiceReceiver.LOG_TAG, "Task.doInBackground(), startService failed.");
                        break;
                    }
                case 3:
                    intent3.putExtra("type", Constants.IntentType.MY_PACKAGE_REPLACED.val());
                    try {
                        application.startService(intent3);
                        break;
                    } catch (IllegalStateException unused2) {
                        GosLog.e(GameServiceReceiver.LOG_TAG, "Task.doInBackground(), startService failed.");
                        break;
                    }
                case 4:
                    onPackageInstallStarted(this.intent, application);
                    break;
                case 5:
                    onPackageChanged(this.intent, application);
                    break;
                case 6:
                    Intent intent4 = new Intent(application, GameIntentService.class);
                    intent4.putExtra("type", 9);
                    try {
                        application.startService(intent4);
                        break;
                    } catch (IllegalStateException unused3) {
                        GosLog.e(GameServiceReceiver.LOG_TAG, "Task.doInBackground(), startService failed.");
                        break;
                    }
                case 7:
                    knoxDesktopModeChanged("ENTER", intent3, application);
                    break;
                case 8:
                    knoxDesktopModeChanged("EXIT", intent3, application);
                    break;
                default:
                    GosLog.e(GameServiceReceiver.LOG_TAG, "Task.doInBackground(), unexpected action. " + action);
                    break;
            }
            GosLog.v(GameServiceReceiver.LOG_TAG, "Task.doInBackground() ends. taskObjectInfo=" + this.taskObjectInfo);
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            this.pendingResult.finish();
            GosLog.v(GameServiceReceiver.LOG_TAG, "Task.onPostExecute() ends. taskObjectInfo=" + this.taskObjectInfo);
            GosLog.v(GameServiceReceiver.LOG_TAG, "Task.onPostExecute() ends. pendingResult=" + this.pendingResult);
        }

        private void onPackageInstallStarted(Intent intent2, Context context) {
            int intExtra = intent2.getIntExtra("userID", -1);
            if (intExtra == -1) {
                GosLog.e(GameServiceReceiver.LOG_TAG, "UserID : " + intExtra + " not supported.");
            } else if (SecureFolderUtil.isSupportSfGMS() || intExtra == SeUserHandleManager.getInstance().getMyUserId()) {
                String stringExtra = intent2.getStringExtra("packageName");
                if (stringExtra == null) {
                    GosLog.e(GameServiceReceiver.LOG_TAG, "packageName is null");
                    return;
                }
                GosLog.i(GameServiceReceiver.LOG_TAG, " PkgInstallStarted for " + stringExtra + " UserID : " + intExtra);
                Intent intent3 = new Intent(context, GameIntentService.class);
                intent3.putExtra("type", 15);
                intent3.putExtra("changeType", Constants.PackageIntentType.INSTALL_STARTED.val());
                intent3.putExtra("packageName", stringExtra);
                intent3.putExtra(EventPublisher.EXTRA_KEY_USER_ID, intExtra);
                try {
                    context.startService(intent3);
                } catch (IllegalStateException unused) {
                    GosLog.e(GameServiceReceiver.LOG_TAG, "startService failed.");
                }
            } else {
                GosLog.e(GameServiceReceiver.LOG_TAG, "UserID : " + intExtra + " secure folder does not supported.");
            }
        }

        private void onPackageChanged(Intent intent2, Context context) {
            String str;
            if (AppVariable.isUnitTest()) {
                str = intent2.getStringExtra("packageName");
            } else {
                str = intent2.getData() != null ? intent2.getData().getSchemeSpecificPart() : null;
            }
            GosLog.d(GameServiceReceiver.LOG_TAG, "onPackageChanged for " + str);
            int changeType = getChangeType(context.getPackageManager(), str);
            if (changeType >= 0) {
                GosLog.i(GameServiceReceiver.LOG_TAG, " Package changeType : " + Constants.PackageIntentType.valueOf(changeType) + ", packageName : " + str);
                Intent intent3 = new Intent(context, GameIntentService.class);
                intent3.putExtra("type", 15);
                intent3.putExtra("changeType", changeType);
                intent3.putExtra("packageName", str);
                try {
                    context.startService(intent3);
                } catch (IllegalStateException unused) {
                    GosLog.e(GameServiceReceiver.LOG_TAG, "startService failed.");
                }
            }
        }

        private int getChangeType(PackageManager packageManager, String str) {
            if (packageManager == null) {
                return -1;
            }
            try {
                if (packageManager.getPackageInfo(str, 128).applicationInfo.enabled) {
                    return Constants.PackageIntentType.ENABLED.val();
                }
                return Constants.PackageIntentType.DISABLED.val();
            } catch (PackageManager.NameNotFoundException unused) {
                GosLog.e(GameServiceReceiver.LOG_TAG, str + " is not exist");
                return -1;
            }
        }

        private void knoxDesktopModeChanged(String str, Intent intent2, Context context) {
            GosLog.i(GameServiceReceiver.LOG_TAG, str + "_KNOX_DESKTOP_MODE. sep: " + PlatformUtil.getSemPlatformVersion());
            intent2.putExtra("type", Constants.IntentType.DESKTOP_MODE_CHANGED.val());
            try {
                context.startService(intent2);
            } catch (IllegalStateException unused) {
                GosLog.e(GameServiceReceiver.LOG_TAG, "startService failed.");
            }
        }
    }
}
