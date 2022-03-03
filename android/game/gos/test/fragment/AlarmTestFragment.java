package com.samsung.android.game.gos.test.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.context.AppContext;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.network.NetworkConnector;
import com.samsung.android.game.gos.service.AlarmController;
import com.samsung.android.game.gos.service.MainIntentService;
import com.samsung.android.game.gos.test.util.GosTestLog;
import com.samsung.android.game.gos.value.Constants;
import java.util.Objects;

public class AlarmTestFragment extends BaseFragment {
    private static final String LOG_TAG = "AlarmTestFragment";
    private TextView txt_normal_update;
    private TextView txt_reset_time;
    private TextView txt_retry_update;

    public int getNavItemId() {
        return R.id.nav_alarmTest;
    }

    private static String testUpdateAlarm(Context context, Constants.AlarmIntentType alarmIntentType, boolean z) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        if (alarmManager == null) {
            GosTestLog.w(LOG_TAG, "testUpdateAlarm() - mAlarmManager is null!");
            return BuildConfig.VERSION_NAME;
        }
        Intent intent = new Intent(context, MainIntentService.class);
        PendingIntent pendingIntent = null;
        int i = 134217728;
        if (alarmIntentType.equals(Constants.AlarmIntentType.UPDATE_CHECK)) {
            intent.putExtra("type", Constants.IntentType.ON_ALARM.val());
            if (!z) {
                i = 536870912;
            }
            pendingIntent = PendingIntent.getService(context, 1000, intent, i);
        } else if (alarmIntentType.equals(Constants.AlarmIntentType.ON_FAIL)) {
            intent.putExtra("type", Constants.IntentType.ON_SERVER_CONNECTION_FAIL_ALARM.val());
            if (!z) {
                i = 536870912;
            }
            pendingIntent = PendingIntent.getService(context, 1001, intent, i);
        }
        if (pendingIntent == null) {
            return "null";
        }
        if (!z) {
            return pendingIntent.toString();
        }
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        return BuildConfig.VERSION_NAME;
    }

    private static void testWiFiConnected(Context context) {
        AsyncTask.execute(new Runnable(context) {
            public final /* synthetic */ Context f$0;

            {
                this.f$0 = r1;
            }

            public final void run() {
                AlarmController.onUpdateAlarm((Context) Objects.requireNonNull(this.f$0), new NetworkConnector(AppContext.get()), Constants.IntentType.WIFI_CONNECTED);
            }
        });
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        GosTestLog.d(LOG_TAG, "onCreateView()");
        View inflate = layoutInflater.inflate(R.layout.fragment_update_alarm_test, viewGroup, false);
        init(inflate);
        return inflate;
    }

    private void init(View view) {
        GosTestLog.d(LOG_TAG, "init()");
        ((Button) view.findViewById(R.id.normal_update_btn)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AlarmTestFragment.this.lambda$init$1$AlarmTestFragment(view);
            }
        });
        ((Button) view.findViewById(R.id.normal_delete_btn)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AlarmTestFragment.this.lambda$init$2$AlarmTestFragment(view);
            }
        });
        this.txt_normal_update = (TextView) view.findViewById(R.id.normal_update_txt);
        ((Button) view.findViewById(R.id.wifi_update_btn)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AlarmTestFragment.this.lambda$init$3$AlarmTestFragment(view);
            }
        });
        ((Button) view.findViewById(R.id.retry_update_btn)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AlarmTestFragment.this.lambda$init$4$AlarmTestFragment(view);
            }
        });
        ((Button) view.findViewById(R.id.retry_delete_btn)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AlarmTestFragment.this.lambda$init$5$AlarmTestFragment(view);
            }
        });
        this.txt_retry_update = (TextView) view.findViewById(R.id.retry_update_txt);
        this.txt_reset_time = (TextView) view.findViewById(R.id.txt_reset_time);
        ((Button) view.findViewById(R.id.reset_normal_btn)).setOnClickListener($$Lambda$AlarmTestFragment$KqZU1nC8lF3r6VoJz0urhU1G1go.INSTANCE);
        ((Button) view.findViewById(R.id.reset_full_btn)).setOnClickListener($$Lambda$AlarmTestFragment$8jbbz8rDOJSWoksPdelgdcdLxk4.INSTANCE);
        ((Button) view.findViewById(R.id.refresh_btn)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AlarmTestFragment.this.lambda$init$8$AlarmTestFragment(view);
            }
        });
        refresh();
    }

    public /* synthetic */ void lambda$init$1$AlarmTestFragment(View view) {
        AlarmController.setUpdateAlarm((Context) Objects.requireNonNull(getContext()), Constants.AlarmIntentType.UPDATE_CHECK);
    }

    public /* synthetic */ void lambda$init$2$AlarmTestFragment(View view) {
        testUpdateAlarm((Context) Objects.requireNonNull(getContext()), Constants.AlarmIntentType.UPDATE_CHECK, true);
    }

    public /* synthetic */ void lambda$init$3$AlarmTestFragment(View view) {
        testWiFiConnected((Context) Objects.requireNonNull(getContext()));
    }

    public /* synthetic */ void lambda$init$4$AlarmTestFragment(View view) {
        AlarmController.setUpdateAlarm((Context) Objects.requireNonNull(getContext()), Constants.AlarmIntentType.ON_FAIL);
    }

    public /* synthetic */ void lambda$init$5$AlarmTestFragment(View view) {
        testUpdateAlarm((Context) Objects.requireNonNull(getContext()), Constants.AlarmIntentType.ON_FAIL, true);
    }

    public /* synthetic */ void lambda$init$8$AlarmTestFragment(View view) {
        refresh();
    }

    private void refresh() {
        this.txt_normal_update.setText(testUpdateAlarm((Context) Objects.requireNonNull(getContext()), Constants.AlarmIntentType.UPDATE_CHECK, false));
        this.txt_retry_update.setText(testUpdateAlarm((Context) Objects.requireNonNull(getContext()), Constants.AlarmIntentType.ON_FAIL, false));
        long currentTimeMillis = System.currentTimeMillis();
        this.txt_reset_time.setText("currentTime: " + currentTimeMillis + "\nlastUpdate: " + (currentTimeMillis - DbHelper.getInstance().getGlobalDao().getUpdateTime()) + "\nlastFullyUpdate: " + (currentTimeMillis - DbHelper.getInstance().getGlobalDao().getFullyUpdateTime()));
    }
}
