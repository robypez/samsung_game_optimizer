package com.samsung.android.game.gos.data.dbhelper;

import com.samsung.android.game.gos.data.dao.ReportDao;
import com.samsung.android.game.gos.data.database.ReportDatabase;
import com.samsung.android.game.gos.data.model.Report;
import com.samsung.android.game.gos.data.type.ReportData;
import com.samsung.android.game.gos.util.CharacterUtil;
import com.samsung.android.game.gos.util.GosLog;
import com.samsung.android.game.gos.value.AppVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportDbHelper {
    private static final String LOG_TAG = ReportDbHelper.class.getSimpleName();
    private static final long MAX_BYTE_SIZE = 104857600;
    private ReportDao mReportDAO;

    public static ReportDbHelper getInstance() {
        if (AppVariable.isUnitTest()) {
            return new ReportDbHelper();
        }
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final ReportDbHelper INSTANCE = new ReportDbHelper();

        private SingletonHolder() {
        }
    }

    private ReportDbHelper() {
        GosLog.d(LOG_TAG, "constructor");
        this.mReportDAO = ReportDatabase.getReportDatabase().reportDao();
    }

    public List<ReportData> getCombinationReportData(String str) {
        GosLog.d(LOG_TAG, "getCombinationReportData()");
        ArrayList arrayList = new ArrayList();
        for (Long longValue : this.mReportDAO.getIdListByTag(str)) {
            Report byId = this.mReportDAO.getById(longValue.longValue());
            arrayList.add(new ReportData(byId.getId(), byId.getTag(), byId.getMsg()));
        }
        return arrayList;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v0, resolved type: com.samsung.android.game.gos.data.type.IntegratedReportData} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v1, resolved type: com.samsung.android.game.gos.data.type.ReportData} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v2, resolved type: com.samsung.android.game.gos.data.type.IntegratedReportData} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: com.samsung.android.game.gos.data.type.IntegratedReportData} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.samsung.android.game.gos.data.type.ReportData> getReportDataBySize(int r12, int r13) {
        /*
            r11 = this;
            java.lang.String r0 = LOG_TAG
            java.lang.String r1 = "getReportDataBySize()"
            com.samsung.android.game.gos.util.GosLog.d(r0, r1)
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            com.samsung.android.game.gos.data.dao.ReportDao r1 = r11.mReportDAO     // Catch:{ Exception -> 0x00aa }
            java.util.List r1 = r1.getAllId()     // Catch:{ Exception -> 0x00aa }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ Exception -> 0x00aa }
            r2 = 0
            r3 = r2
        L_0x0018:
            boolean r4 = r1.hasNext()     // Catch:{ Exception -> 0x00aa }
            if (r4 == 0) goto L_0x007f
            java.lang.Object r4 = r1.next()     // Catch:{ Exception -> 0x00aa }
            java.lang.Long r4 = (java.lang.Long) r4     // Catch:{ Exception -> 0x00aa }
            com.samsung.android.game.gos.data.dao.ReportDao r5 = r11.mReportDAO     // Catch:{ Exception -> 0x00aa }
            long r6 = r4.longValue()     // Catch:{ Exception -> 0x00aa }
            com.samsung.android.game.gos.data.model.Report r4 = r5.getById(r6)     // Catch:{ Exception -> 0x00aa }
            java.lang.String r5 = "user_usage"
            java.lang.String r6 = r4.getTag()     // Catch:{ Exception -> 0x00aa }
            boolean r5 = r5.equals(r6)     // Catch:{ Exception -> 0x00aa }
            if (r5 == 0) goto L_0x005d
            java.lang.String r5 = r4.getRinglogMsg()     // Catch:{ Exception -> 0x00aa }
            if (r5 != 0) goto L_0x0046
            java.lang.String r5 = r4.getGppRepAggregation()     // Catch:{ Exception -> 0x00aa }
            if (r5 == 0) goto L_0x005d
        L_0x0046:
            com.samsung.android.game.gos.data.type.IntegratedReportData r10 = new com.samsung.android.game.gos.data.type.IntegratedReportData     // Catch:{ Exception -> 0x00aa }
            long r5 = r4.getId()     // Catch:{ Exception -> 0x00aa }
            java.lang.String r7 = r4.getMsg()     // Catch:{ Exception -> 0x00aa }
            java.lang.String r8 = r4.getRinglogMsg()     // Catch:{ Exception -> 0x00aa }
            java.lang.String r9 = r4.getGppRepAggregation()     // Catch:{ Exception -> 0x00aa }
            r4 = r10
            r4.<init>(r5, r7, r8, r9)     // Catch:{ Exception -> 0x00aa }
            goto L_0x006e
        L_0x005d:
            com.samsung.android.game.gos.data.type.ReportData r10 = new com.samsung.android.game.gos.data.type.ReportData     // Catch:{ Exception -> 0x00aa }
            long r5 = r4.getId()     // Catch:{ Exception -> 0x00aa }
            java.lang.String r7 = r4.getTag()     // Catch:{ Exception -> 0x00aa }
            java.lang.String r4 = r4.getMsg()     // Catch:{ Exception -> 0x00aa }
            r10.<init>(r5, r7, r4)     // Catch:{ Exception -> 0x00aa }
        L_0x006e:
            int r4 = r10.getSize()     // Catch:{ Exception -> 0x00aa }
            int r4 = r4 + r2
            if (r4 > r12) goto L_0x007f
            if (r3 <= r13) goto L_0x0078
            goto L_0x007f
        L_0x0078:
            r0.add(r10)     // Catch:{ Exception -> 0x00aa }
            int r3 = r3 + 1
            r2 = r4
            goto L_0x0018
        L_0x007f:
            java.lang.String r13 = LOG_TAG     // Catch:{ Exception -> 0x00aa }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00aa }
            r1.<init>()     // Catch:{ Exception -> 0x00aa }
            java.lang.String r3 = "getReportDataBySize() listCount: "
            r1.append(r3)     // Catch:{ Exception -> 0x00aa }
            int r3 = r0.size()     // Catch:{ Exception -> 0x00aa }
            r1.append(r3)     // Catch:{ Exception -> 0x00aa }
            java.lang.String r3 = " currentSize : "
            r1.append(r3)     // Catch:{ Exception -> 0x00aa }
            r1.append(r2)     // Catch:{ Exception -> 0x00aa }
            java.lang.String r2 = " maxSize :"
            r1.append(r2)     // Catch:{ Exception -> 0x00aa }
            r1.append(r12)     // Catch:{ Exception -> 0x00aa }
            java.lang.String r12 = r1.toString()     // Catch:{ Exception -> 0x00aa }
            com.samsung.android.game.gos.util.GosLog.i(r13, r12)     // Catch:{ Exception -> 0x00aa }
            goto L_0x00b0
        L_0x00aa:
            r12 = move-exception
            java.lang.String r13 = LOG_TAG
            com.samsung.android.game.gos.util.GosLog.w((java.lang.String) r13, (java.lang.Throwable) r12)
        L_0x00b0:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.data.dbhelper.ReportDbHelper.getReportDataBySize(int, int):java.util.List");
    }

    public boolean addOrUpdateReportDataList(List<Report> list) {
        try {
            this.mReportDAO.insertOrUpdate(list);
            return true;
        } catch (Exception e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return false;
        }
    }

    public boolean addOrUpdateReportData(String str, long j, String str2) {
        return addOrUpdateReportData(str, j, str2, (String) null, (String) null, (String) null);
    }

    public boolean addOrUpdateGameSessionGppRinglogData(long j, String str) {
        return addOrUpdateReportData(ReportData.TAG_USER_USAGE, j, (String) null, str, (String) null, (String) null);
    }

    public boolean addOrUpdateGameSessionGppRepData(long j, String str, String str2) {
        return addOrUpdateReportData(ReportData.TAG_USER_USAGE, j, (String) null, (String) null, str, str2);
    }

    private boolean addOrUpdateReportData(String str, long j, String str2, String str3, String str4, String str5) {
        GosLog.d(LOG_TAG, String.format(Locale.US, "addOrUpdateReportData(), tag: %s, id: %d, %nmsg: %s, %nringlogMsg: %s", new Object[]{str, Long.valueOf(j), str2, str3}));
        if (j < 0) {
            String str6 = LOG_TAG;
            GosLog.i(str6, "addOrUpdateReportData(), skip because of wrong id. id: " + j);
            return false;
        }
        try {
            Report byId = this.mReportDAO.getById(j);
            if (byId == null) {
                byId = new Report(j);
            }
            byId.setTag(str);
            if (str2 != null) {
                byId.setMsg(str2);
            }
            if (str3 != null) {
                byId.setRinglogMsg(str3);
            }
            if (str4 != null) {
                byId.setGppRepAggregation(str4);
            }
            if (str5 != null) {
                byId.setGppRepDataSchemeVersion(str5);
            }
            this.mReportDAO.insertOrUpdate(byId);
            updateReportSize(byId);
            cleanUpReportsOverMaxSize();
            return true;
        } catch (Exception e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return false;
        }
    }

    public boolean updateReportSizeAll() {
        try {
            List<Long> idListBySize = this.mReportDAO.getIdListBySize(0);
            String str = LOG_TAG;
            GosLog.d(str, "updateReportSizeAll(), zeroSizeIdList count: " + idListBySize.size());
            for (Long longValue : idListBySize) {
                updateReportSize(this.mReportDAO.getById(longValue.longValue()));
            }
            cleanUpReportsOverMaxSize();
            String str2 = LOG_TAG;
            GosLog.d(str2, "updateReportSizeAll(), finished zeroSizeIdList=" + idListBySize.size());
            return true;
        } catch (Exception e) {
            GosLog.w(LOG_TAG, (Throwable) e);
            return false;
        }
    }

    private void updateReportSize(Report report) {
        if (report != null) {
            int byteSize = report.getByteSize();
            report.setByteSize(CharacterUtil.getByteSize(report.getTag()) + CharacterUtil.getByteSize(report.getMsg()) + CharacterUtil.getByteSize(report.getRinglogMsg()) + CharacterUtil.getByteSize(report.getGppRepAggregation()) + CharacterUtil.getByteSize(report.getGppRepDataSchemeVersion()));
            this.mReportDAO.insertOrUpdate(report);
            int byteSize2 = report.getByteSize();
            GosLog.d(LOG_TAG, String.format(Locale.US, "updateReportSize(), sizeBefore: %d, sizeAfter: %d, diff: %d", new Object[]{Integer.valueOf(byteSize), Integer.valueOf(byteSize2), Integer.valueOf(byteSize2 - byteSize)}));
        }
    }

    public boolean removeReportData(long j) {
        String str = LOG_TAG;
        GosLog.d(str, "removeReportData(), id: " + j);
        this.mReportDAO.deleteById(j);
        return this.mReportDAO.getById(j) == null;
    }

    public boolean removeAll() {
        GosLog.d(LOG_TAG, "removeAll()");
        this.mReportDAO.deleteAll();
        return this.mReportDAO.getAllId().size() == 0;
    }

    private void cleanUpReportsOverMaxSize() {
        List<Report.IdAndSize> allIdAndSize_byReversedOrder = this.mReportDAO.getAllIdAndSize_byReversedOrder();
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        if (allIdAndSize_byReversedOrder != null) {
            long j = 0;
            for (Report.IdAndSize next : allIdAndSize_byReversedOrder) {
                if (j <= MAX_BYTE_SIZE) {
                    j += (long) next.size;
                }
                if (j > MAX_BYTE_SIZE) {
                    arrayList.add(Long.valueOf(next.id));
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(next.id);
                }
            }
            String str = LOG_TAG;
            GosLog.i(str, "cleanUpReportsOverMaxSize(), accumulatedSize (before cleaning): " + j);
            if (arrayList.size() > 0) {
                GosLog.i(LOG_TAG, String.format(Locale.US, "cleanUpReportsOverMaxSize(), reports count to be deleted: %d, id list: %s", new Object[]{Integer.valueOf(arrayList.size()), sb}));
            }
            this.mReportDAO.deleteByIdList(arrayList);
        }
    }
}
