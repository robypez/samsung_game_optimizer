package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.Report;
import java.util.List;

public abstract class ReportDao {
    /* access modifiers changed from: protected */
    public abstract void _deleteAll();

    /* access modifiers changed from: protected */
    public abstract void _deleteById(long j);

    /* access modifiers changed from: protected */
    public abstract void _deleteByIdList(List<Long> list);

    /* access modifiers changed from: protected */
    public abstract long _insertOrUpdate(Report report);

    /* access modifiers changed from: protected */
    public abstract void _insertOrUpdate(List<Report> list);

    public abstract List<Long> getAllId();

    public abstract List<Report.IdAndSize> getAllIdAndSize_byReversedOrder();

    public abstract Report getById(long j);

    public abstract List<Long> getIdListBySize(int i);

    public abstract List<Long> getIdListByTag(String str);

    public long insertOrUpdate(Report report) {
        try {
            return _insertOrUpdate(report);
        } catch (SQLiteFullException unused) {
            return -1;
        }
    }

    public void insertOrUpdate(List<Report> list) {
        try {
            _insertOrUpdate(list);
        } catch (SQLiteFullException unused) {
        }
    }

    public void deleteById(long j) {
        try {
            _deleteById(j);
        } catch (SQLiteFullException unused) {
        }
    }

    public void deleteByIdList(List<Long> list) {
        try {
            _deleteByIdList(list);
        } catch (SQLiteFullException unused) {
        }
    }

    public void deleteAll() {
        try {
            _deleteAll();
        } catch (SQLiteFullException unused) {
        }
    }
}
