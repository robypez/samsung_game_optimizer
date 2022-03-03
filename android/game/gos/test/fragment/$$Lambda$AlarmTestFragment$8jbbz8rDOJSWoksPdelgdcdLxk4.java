package com.samsung.android.game.gos.test.fragment;

import android.view.View;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Global;

/* renamed from: com.samsung.android.game.gos.test.fragment.-$$Lambda$AlarmTestFragment$8jbbz8rDOJSWoksPdelgdcdLxk4  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$AlarmTestFragment$8jbbz8rDOJSWoksPdelgdcdLxk4 implements View.OnClickListener {
    public static final /* synthetic */ $$Lambda$AlarmTestFragment$8jbbz8rDOJSWoksPdelgdcdLxk4 INSTANCE = new $$Lambda$AlarmTestFragment$8jbbz8rDOJSWoksPdelgdcdLxk4();

    private /* synthetic */ $$Lambda$AlarmTestFragment$8jbbz8rDOJSWoksPdelgdcdLxk4() {
    }

    public final void onClick(View view) {
        DbHelper.getInstance().getGlobalDao().setFullyUpdateTime(new Global.IdAndFullyUpdateTime(0));
    }
}
