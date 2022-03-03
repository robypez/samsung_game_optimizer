package com.samsung.android.game.gos.test.fragment;

import android.view.View;
import com.samsung.android.game.gos.data.dbhelper.DbHelper;
import com.samsung.android.game.gos.data.model.Global;

/* renamed from: com.samsung.android.game.gos.test.fragment.-$$Lambda$AlarmTestFragment$KqZU1nC8lF3r6VoJz0urhU1G1go  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$AlarmTestFragment$KqZU1nC8lF3r6VoJz0urhU1G1go implements View.OnClickListener {
    public static final /* synthetic */ $$Lambda$AlarmTestFragment$KqZU1nC8lF3r6VoJz0urhU1G1go INSTANCE = new $$Lambda$AlarmTestFragment$KqZU1nC8lF3r6VoJz0urhU1G1go();

    private /* synthetic */ $$Lambda$AlarmTestFragment$KqZU1nC8lF3r6VoJz0urhU1G1go() {
    }

    public final void onClick(View view) {
        DbHelper.getInstance().getGlobalDao().setUpdateTime(new Global.IdAndUpdateTime(0));
    }
}
