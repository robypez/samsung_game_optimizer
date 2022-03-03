package com.samsung.android.game.gos.data.dao;

import android.database.sqlite.SQLiteFullException;
import com.samsung.android.game.gos.data.model.EventSubscription;
import java.util.List;

public abstract class EventSubscriptionDao {
    /* access modifiers changed from: protected */
    public abstract void _deleteSubscriber(EventSubscription.SubscriberPkgName subscriberPkgName);

    /* access modifiers changed from: protected */
    public abstract void _insertAll(List<EventSubscription> list);

    public abstract List<EventSubscription> getSubscriberListOfEvent(String str);

    public void deleteSubscriber(EventSubscription.SubscriberPkgName subscriberPkgName) {
        try {
            _deleteSubscriber(subscriberPkgName);
        } catch (SQLiteFullException unused) {
        }
    }

    public void insertAll(List<EventSubscription> list) {
        try {
            _insertAll(list);
        } catch (SQLiteFullException unused) {
        }
    }
}
