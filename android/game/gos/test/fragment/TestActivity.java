package com.samsung.android.game.gos.test.fragment;

import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.samsung.android.game.gos.R;
import com.samsung.android.game.gos.data.TestDataManager;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Global;
import com.samsung.android.game.gos.ipm.BuildConfig;
import com.samsung.android.game.gos.test.util.GosTestLog;

public class TestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = "TestActivity";
    private TextView mLogcatTextView;
    private boolean mShowLogcat;
    MenuItem mShowLogcatMenuItem;

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x00d8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCreate(android.os.Bundle r10) {
        /*
            r9 = this;
            boolean r0 = com.samsung.android.game.gos.data.TestDataManager.isTestMode()
            java.lang.String r1 = "TestActivity"
            if (r0 != 0) goto L_0x0010
            java.lang.String r0 = "It is not the test mode."
            com.samsung.android.game.gos.test.util.GosTestLog.d(r1, r0)
            r9.finish()
        L_0x0010:
            super.onCreate(r10)
            r10 = 2131492892(0x7f0c001c, float:1.8609249E38)
            r9.setContentView((int) r10)
            r10 = 2131296664(0x7f090198, float:1.8211251E38)
            android.view.View r10 = r9.findViewById(r10)
            r5 = r10
            androidx.appcompat.widget.Toolbar r5 = (androidx.appcompat.widget.Toolbar) r5
            r9.setSupportActionBar(r5)
            r10 = 2131296645(0x7f090185, float:1.8211213E38)
            android.view.View r10 = r9.findViewById(r10)
            android.widget.TextView r10 = (android.widget.TextView) r10
            r0 = 2131296644(0x7f090184, float:1.821121E38)
            android.view.View r0 = r9.findViewById(r0)
            android.widget.TextView r0 = (android.widget.TextView) r0
            r9.mLogcatTextView = r0
            java.lang.Thread r0 = new java.lang.Thread
            com.samsung.android.game.gos.test.fragment.-$$Lambda$TestActivity$weg503JvQe9IthGVzxtsDhSWfTM r2 = new com.samsung.android.game.gos.test.fragment.-$$Lambda$TestActivity$weg503JvQe9IthGVzxtsDhSWfTM
            r2.<init>()
            r0.<init>(r2)
            r0.start()
            android.widget.TextView r0 = r9.mLogcatTextView
            boolean r2 = r9.mShowLogcat
            r8 = 0
            if (r2 == 0) goto L_0x0050
            r2 = r8
            goto L_0x0052
        L_0x0050:
            r2 = 8
        L_0x0052:
            r0.setVisibility(r2)
            com.samsung.android.game.gos.test.fragment.-$$Lambda$TestActivity$LakL_AOmOlVFa7uvqfCRJhwziS4 r0 = new com.samsung.android.game.gos.test.fragment.-$$Lambda$TestActivity$LakL_AOmOlVFa7uvqfCRJhwziS4
            r0.<init>()
            r10.setOnClickListener(r0)
            r10 = 2131296374(0x7f090076, float:1.8210663E38)
            android.view.View r10 = r9.findViewById(r10)
            androidx.drawerlayout.widget.DrawerLayout r10 = (androidx.drawerlayout.widget.DrawerLayout) r10
            androidx.appcompat.app.ActionBarDrawerToggle r0 = new androidx.appcompat.app.ActionBarDrawerToggle
            r6 = 2131689661(0x7f0f00bd, float:1.9008344E38)
            r7 = 2131689660(0x7f0f00bc, float:1.9008342E38)
            r2 = r0
            r3 = r9
            r4 = r10
            r2.<init>(r3, r4, r5, r6, r7)
            r10.setDrawerListener(r0)
            r0.syncState()
            r10 = 2131296481(0x7f0900e1, float:1.821088E38)
            android.view.View r10 = r9.findViewById(r10)
            com.google.android.material.navigation.NavigationView r10 = (com.google.android.material.navigation.NavigationView) r10
            r10.setNavigationItemSelectedListener(r9)
            android.view.View r10 = r10.getHeaderView(r8)
            r0 = 2131296633(0x7f090179, float:1.8211188E38)
            android.view.View r10 = r10.findViewById(r0)
            android.widget.TextView r10 = (android.widget.TextView) r10
            android.content.pm.PackageManager r0 = r9.getPackageManager()
            if (r0 == 0) goto L_0x00a8
            java.lang.String r2 = "com.samsung.android.game.gos"
            r3 = 128(0x80, float:1.794E-43)
            android.content.pm.PackageInfo r0 = r0.getPackageInfo(r2, r3)     // Catch:{ NameNotFoundException -> 0x00a4 }
            java.lang.String r0 = r0.versionName     // Catch:{ NameNotFoundException -> 0x00a4 }
            goto L_0x00aa
        L_0x00a4:
            r0 = move-exception
            com.samsung.android.game.gos.test.util.GosTestLog.w((java.lang.String) r1, (java.lang.Throwable) r0)
        L_0x00a8:
            java.lang.String r0 = "3.5.01.18"
        L_0x00aa:
            java.util.Locale r1 = java.util.Locale.US
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 2131689513(0x7f0f0029, float:1.9008043E38)
            java.lang.String r3 = r9.getString(r3)
            r2[r8] = r3
            r3 = 1
            r2[r3] = r0
            java.lang.String r0 = "%s%nv.%s"
            java.lang.String r0 = java.lang.String.format(r1, r0, r2)
            r10.setText(r0)
            androidx.fragment.app.FragmentManager r10 = r9.getSupportFragmentManager()
            com.samsung.android.game.gos.test.fragment.DeviceInfoFragment r0 = new com.samsung.android.game.gos.test.fragment.DeviceInfoFragment
            r0.<init>()
            java.lang.String r1 = r0.getTag()
            androidx.fragment.app.Fragment r2 = r10.findFragmentByTag(r1)
            if (r2 != 0) goto L_0x00d8
            goto L_0x00d9
        L_0x00d8:
            r0 = r2
        L_0x00d9:
            androidx.fragment.app.FragmentTransaction r10 = r10.beginTransaction()
            r2 = 2131296361(0x7f090069, float:1.8210637E38)
            androidx.fragment.app.FragmentTransaction r10 = r10.replace(r2, r0, r1)
            r0 = 0
            androidx.fragment.app.FragmentTransaction r10 = r10.addToBackStack(r0)
            r10.commit()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.fragment.TestActivity.onCreate(android.os.Bundle):void");
    }

    public /* synthetic */ void lambda$onCreate$0$TestActivity() {
        this.mShowLogcat = DbHelper.getInstance().getGlobalDao().isShowLogcat();
    }

    public /* synthetic */ void lambda$onCreate$1$TestActivity(View view) {
        toggleLogcatVisibility();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            drawerLayout.closeDrawer((int) GravityCompat.START);
        } else if (this.mLogcatTextView.getVisibility() == 0) {
            toggleLogcatVisibility();
        } else {
            super.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        TestDataManager.setTestMode(false);
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem findItem = menu.findItem(R.id.action_showLogcat);
        if (findItem == null) {
            return true;
        }
        this.mShowLogcatMenuItem = findItem;
        findItem.setChecked(this.mShowLogcat);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        TextView textView;
        int itemId = menuItem.getItemId();
        if (itemId == 2131296312) {
            toggleLogcatVisibility();
        } else if (itemId == 2131296302 && (textView = this.mLogcatTextView) != null) {
            textView.setText(BuildConfig.VERSION_NAME);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0076, code lost:
        r4 = r5.getTag();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onNavigationItemSelected(android.view.MenuItem r8) {
        /*
            r7 = this;
            int r8 = r8.getItemId()
            androidx.fragment.app.FragmentManager r0 = r7.getSupportFragmentManager()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            com.samsung.android.game.gos.test.fragment.DeviceInfoFragment r2 = new com.samsung.android.game.gos.test.fragment.DeviceInfoFragment
            r2.<init>()
            r1.add(r2)
            com.samsung.android.game.gos.test.fragment.GlobalDataFragment r2 = new com.samsung.android.game.gos.test.fragment.GlobalDataFragment
            r2.<init>()
            r1.add(r2)
            com.samsung.android.game.gos.test.fragment.FeatureTestFragment r2 = new com.samsung.android.game.gos.test.fragment.FeatureTestFragment
            r2.<init>()
            r1.add(r2)
            com.samsung.android.game.gos.test.fragment.FeatureJsonTestFragment r2 = new com.samsung.android.game.gos.test.fragment.FeatureJsonTestFragment
            r2.<init>()
            r1.add(r2)
            com.samsung.android.game.gos.test.fragment.IpmFragment r2 = new com.samsung.android.game.gos.test.fragment.IpmFragment
            r2.<init>()
            r1.add(r2)
            com.samsung.android.game.gos.test.fragment.GfiFragment r2 = new com.samsung.android.game.gos.test.fragment.GfiFragment
            r2.<init>()
            r1.add(r2)
            com.samsung.android.game.gos.test.fragment.ServerApiTestFragment r2 = new com.samsung.android.game.gos.test.fragment.ServerApiTestFragment
            r2.<init>()
            r1.add(r2)
            com.samsung.android.game.gos.test.fragment.SiopModeFragment r2 = new com.samsung.android.game.gos.test.fragment.SiopModeFragment
            r2.<init>()
            r1.add(r2)
            com.samsung.android.game.gos.test.fragment.ResumeBoostFragment r2 = new com.samsung.android.game.gos.test.fragment.ResumeBoostFragment
            r2.<init>()
            r1.add(r2)
            com.samsung.android.game.gos.test.fragment.AlarmTestFragment r2 = new com.samsung.android.game.gos.test.fragment.AlarmTestFragment
            r2.<init>()
            r1.add(r2)
            java.util.Iterator r1 = r1.iterator()
            r2 = 0
            r3 = r2
            r4 = r3
        L_0x0064:
            boolean r5 = r1.hasNext()
            if (r5 == 0) goto L_0x0082
            java.lang.Object r5 = r1.next()
            com.samsung.android.game.gos.test.fragment.BaseFragment r5 = (com.samsung.android.game.gos.test.fragment.BaseFragment) r5
            int r6 = r5.getNavItemId()
            if (r8 != r6) goto L_0x0064
            java.lang.String r4 = r5.getTag()
            androidx.fragment.app.Fragment r3 = r0.findFragmentByTag(r4)
            if (r3 != 0) goto L_0x0064
            r3 = r5
            goto L_0x0064
        L_0x0082:
            if (r3 == 0) goto L_0x0096
            androidx.fragment.app.FragmentTransaction r8 = r0.beginTransaction()
            r0 = 2131296361(0x7f090069, float:1.8210637E38)
            androidx.fragment.app.FragmentTransaction r8 = r8.replace(r0, r3, r4)
            androidx.fragment.app.FragmentTransaction r8 = r8.addToBackStack(r2)
            r8.commit()
        L_0x0096:
            r8 = 2131296374(0x7f090076, float:1.8210663E38)
            android.view.View r8 = r7.findViewById(r8)
            androidx.drawerlayout.widget.DrawerLayout r8 = (androidx.drawerlayout.widget.DrawerLayout) r8
            r0 = 8388611(0x800003, float:1.1754948E-38)
            r8.closeDrawer((int) r0)
            r8 = 1
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.test.fragment.TestActivity.onNavigationItemSelected(android.view.MenuItem):boolean");
    }

    public void showResult(String str) {
        int lineBottom;
        GosTestLog.d(LOG_TAG, str);
        TextView textView = this.mLogcatTextView;
        if (textView != null) {
            textView.append("\n" + str);
            Layout layout = this.mLogcatTextView.getLayout();
            if (layout != null && (lineBottom = (layout.getLineBottom(this.mLogcatTextView.getLineCount() - 1) - this.mLogcatTextView.getScrollY()) - this.mLogcatTextView.getHeight()) > 0) {
                this.mLogcatTextView.scrollBy(0, lineBottom);
            }
        }
    }

    private void toggleLogcatVisibility() {
        MenuItem menuItem = this.mShowLogcatMenuItem;
        if (menuItem != null && this.mLogcatTextView != null) {
            boolean z = true;
            int i = 0;
            if (menuItem.isChecked()) {
                z = false;
                i = 8;
            }
            this.mLogcatTextView.setVisibility(i);
            this.mShowLogcatMenuItem.setChecked(z);
            this.mShowLogcat = z;
            DbHelper.getInstance().getGlobalDao().setShowLogcat(new Global.IdAndShowLogcat(z));
        }
    }
}
