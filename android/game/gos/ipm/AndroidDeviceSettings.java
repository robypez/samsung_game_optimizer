package com.samsung.android.game.gos.ipm;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Pair;
import android.util.Xml;
import com.samsung.android.game.ManagerInterface;
import com.samsung.android.game.gos.value.RinglogConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AndroidDeviceSettings {
    private static final String LOG_TAG = "AndroidDeviceSettings";
    private final Map<Integer, Map<Long, Float>> mCpuEff = new LinkedHashMap();
    private final Map<Integer, Long> mCpuFreqs = new LinkedHashMap();
    private final Map<Long, Float> mGpuEff = new LinkedHashMap();
    private final Map<Integer, Long> mGpuFreqs = new LinkedHashMap();
    private boolean mIpmScenario = false;
    private boolean mIpmScenarioDynamicChecked = false;
    private final Resources mResources;
    private final Ssrm mSsrm;

    private interface ParserCallback {
        void callback(String str);
    }

    public AndroidDeviceSettings(Resources resources, Ssrm ssrm) {
        this.mResources = resources;
        this.mSsrm = ssrm;
        parseCPUGPUPowerEfficiency();
        InputStream configFile = ssrm.getConfigFile();
        if (configFile != null) {
            parseLimits(configFile);
            checkDynamicScenarioSsrm();
            if (!this.mIpmScenario) {
                parseScenario(configFile);
                return;
            }
            return;
        }
        Log.e(LOG_TAG, "Unable to get SSRM config file");
    }

    private void checkDynamicScenarioSsrm() {
        try {
            String version = this.mSsrm.getVersion();
            if (!version.isEmpty()) {
                if (Double.parseDouble(version) >= 3.999d) {
                    this.mIpmScenario = true;
                    Log.d(LOG_TAG, "Detected new SIOP version >= 4.0, ignoring scenario");
                }
                this.mIpmScenarioDynamicChecked = true;
                return;
            }
            Log.d(LOG_TAG, "SSRM version not available yet.");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception on dynamic scenario " + e);
        }
    }

    private void parseLimits(InputStream inputStream) {
        try {
            Pair<Long, Long> parseLimitsSsrmFile = parseLimitsSsrmFile(inputStream);
            long longValue = ((Long) parseLimitsSsrmFile.first).longValue();
            long longValue2 = ((Long) parseLimitsSsrmFile.second).longValue();
            long j = 1000;
            long j2 = longValue < RinglogConstants.SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS ? 1000000 : longValue < 10000000 ? 1000 : 1;
            if (longValue2 < RinglogConstants.SYSTEM_STATUS_MINIMUM_SESSION_LENGTH_MS) {
                j = 1000000;
            } else if (longValue2 >= 10000000) {
                j = 1;
            }
            for (Map.Entry next : this.mGpuFreqs.entrySet()) {
                next.setValue(Long.valueOf(((Long) next.getValue()).longValue() * j2));
            }
            for (Map.Entry next2 : this.mCpuFreqs.entrySet()) {
                next2.setValue(Long.valueOf(((Long) next2.getValue()).longValue() * j));
            }
            for (Map.Entry next3 : this.mGpuFreqs.entrySet()) {
                if (((Long) next3.getValue()).longValue() == 0) {
                    for (Map.Entry next4 : this.mGpuFreqs.entrySet()) {
                        if (((Integer) next4.getKey()).intValue() < ((Integer) next3.getKey()).intValue() && ((Long) next4.getValue()).longValue() > ((Long) next3.getValue()).longValue()) {
                            next3.setValue(next4.getValue());
                        }
                    }
                }
            }
            for (Map.Entry next5 : this.mCpuFreqs.entrySet()) {
                if (((Long) next5.getValue()).longValue() == 0) {
                    for (Map.Entry next6 : this.mCpuFreqs.entrySet()) {
                        if (((Integer) next6.getKey()).intValue() < ((Integer) next5.getKey()).intValue() && ((Long) next6.getValue()).longValue() > ((Long) next5.getValue()).longValue()) {
                            next5.setValue(next6.getValue());
                        }
                    }
                }
            }
            Log.d(LOG_TAG, "Detected GPU limits " + Arrays.asList(new Map[]{this.mGpuFreqs}));
            Log.d(LOG_TAG, "Detected CPU limits " + Arrays.asList(new Map[]{this.mCpuFreqs}));
        } catch (Exception e) {
            Log.e(LOG_TAG, "Unable to parse Ssrm Limits " + e);
        }
    }

    private Pair<Long, Long> parseLimitsSsrmFile(InputStream inputStream) throws XmlPullParserException, IOException {
        String attributeValue;
        XmlPullParser newPullParser = Xml.newPullParser();
        newPullParser.setInput(inputStream, (String) null);
        long j = 0;
        long j2 = 0;
        while (newPullParser.next() != 1) {
            if (newPullParser.getEventType() == 2) {
                String name = newPullParser.getName();
                String attributeValue2 = newPullParser.getAttributeValue((String) null, ManagerInterface.KeyName.REQUEST_NAME);
                if (name != null && attributeValue2 != null && name.equals("scenario") && attributeValue2.contains("GameThermal")) {
                    long j3 = 0;
                    while (true) {
                        if (newPullParser.next() == 3 && newPullParser.getName().equals("scenario")) {
                            break;
                        } else if (newPullParser.getEventType() == 2 && (attributeValue = newPullParser.getAttributeValue((String) null, "min")) != null) {
                            j3 = Long.parseLong(attributeValue);
                        }
                    }
                    Matcher matcher = Pattern.compile("GameThermal(C|G)puControl([-\\d]+)").matcher(attributeValue2);
                    if (matcher.find()) {
                        int parseInt = Integer.parseInt(matcher.group(2));
                        if (matcher.group(1).contains("C")) {
                            this.mCpuFreqs.put(Integer.valueOf(parseInt), Long.valueOf(j3));
                            if (j2 < j3) {
                                j2 = j3;
                            }
                        } else {
                            this.mGpuFreqs.put(Integer.valueOf(parseInt), Long.valueOf(j3));
                            if (j < j3) {
                                j = j3;
                            }
                        }
                    }
                }
            }
        }
        return new Pair<>(Long.valueOf(j), Long.valueOf(j2));
    }

    private void parseFeatureXml(String str, ParserCallback parserCallback) {
        Resources resources = this.mResources;
        XmlResourceParser xml = resources.getXml(resources.getIdentifier("power_profile", "xml", "android"));
        if (xml != null) {
            while (xml.next() != 1) {
                try {
                    if (xml.getEventType() == 2) {
                        String name = xml.getName();
                        String attributeValue = xml.getAttributeValue((String) null, ManagerInterface.KeyName.REQUEST_NAME);
                        if (name != null && attributeValue != null && name.equals("array") && attributeValue.contains(str)) {
                            while (true) {
                                if (xml.next() == 3 && xml.getName().equals("array")) {
                                    return;
                                }
                                if (xml.getEventType() == 2) {
                                    xml.next();
                                    parserCallback.callback(xml.getText());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Unable to parse Power Profile " + e);
                    return;
                }
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:34|35|46) */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        r14.mGpuEff.clear();
        r14.mCpuEff.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:34:0x01da */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseCPUGPUPowerEfficiency() {
        /*
            r14 = this;
            java.lang.String r0 = "android"
            java.lang.String r1 = "xml"
            java.lang.String r2 = "power_profile"
            java.lang.String r3 = "AndroidDeviceSettings"
            java.util.LinkedHashMap r4 = new java.util.LinkedHashMap     // Catch:{ Exception -> 0x01e5 }
            r4.<init>()     // Catch:{ Exception -> 0x01e5 }
            java.util.LinkedHashMap r5 = new java.util.LinkedHashMap     // Catch:{ Exception -> 0x01e5 }
            r5.<init>()     // Catch:{ Exception -> 0x01e5 }
            java.util.LinkedHashMap r6 = new java.util.LinkedHashMap     // Catch:{ Exception -> 0x01e5 }
            r6.<init>()     // Catch:{ Exception -> 0x01e5 }
            java.util.LinkedHashMap r7 = new java.util.LinkedHashMap     // Catch:{ Exception -> 0x01e5 }
            r7.<init>()     // Catch:{ Exception -> 0x01e5 }
            java.util.LinkedHashMap r8 = new java.util.LinkedHashMap     // Catch:{ Exception -> 0x01e5 }
            r8.<init>()     // Catch:{ Exception -> 0x01e5 }
            java.lang.String r9 = "gpu.speeds"
            com.samsung.android.game.gos.ipm.AndroidDeviceSettings$1 r10 = new com.samsung.android.game.gos.ipm.AndroidDeviceSettings$1     // Catch:{ Exception -> 0x01e5 }
            r10.<init>(r6)     // Catch:{ Exception -> 0x01e5 }
            r14.parseFeatureXml(r9, r10)     // Catch:{ Exception -> 0x01e5 }
            java.lang.String r9 = "gpu.active"
            com.samsung.android.game.gos.ipm.AndroidDeviceSettings$2 r10 = new com.samsung.android.game.gos.ipm.AndroidDeviceSettings$2     // Catch:{ Exception -> 0x01e5 }
            r10.<init>(r7)     // Catch:{ Exception -> 0x01e5 }
            r14.parseFeatureXml(r9, r10)     // Catch:{ Exception -> 0x01e5 }
            java.lang.String r9 = "cpu.clusters.cores"
            com.samsung.android.game.gos.ipm.AndroidDeviceSettings$3 r10 = new com.samsung.android.game.gos.ipm.AndroidDeviceSettings$3     // Catch:{ Exception -> 0x01e5 }
            r10.<init>(r8)     // Catch:{ Exception -> 0x01e5 }
            r14.parseFeatureXml(r9, r10)     // Catch:{ Exception -> 0x01e5 }
            int r9 = r8.size()     // Catch:{ Exception -> 0x01e5 }
            r10 = -1
            if (r9 != 0) goto L_0x0055
            int r9 = r8.size()     // Catch:{ Exception -> 0x01e5 }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)     // Catch:{ Exception -> 0x01e5 }
            java.lang.Integer r11 = java.lang.Integer.valueOf(r10)     // Catch:{ Exception -> 0x01e5 }
            r8.put(r9, r11)     // Catch:{ Exception -> 0x01e5 }
        L_0x0055:
            java.util.Set r8 = r8.entrySet()     // Catch:{ Exception -> 0x01e5 }
            java.util.Iterator r8 = r8.iterator()     // Catch:{ Exception -> 0x01e5 }
        L_0x005d:
            boolean r9 = r8.hasNext()     // Catch:{ Exception -> 0x01e5 }
            if (r9 == 0) goto L_0x00dd
            java.lang.Object r9 = r8.next()     // Catch:{ Exception -> 0x01e5 }
            java.util.Map$Entry r9 = (java.util.Map.Entry) r9     // Catch:{ Exception -> 0x01e5 }
            android.content.res.Resources r11 = r14.mResources     // Catch:{ Exception -> 0x01e5 }
            android.content.res.Resources r12 = r14.mResources     // Catch:{ Exception -> 0x01e5 }
            int r12 = r12.getIdentifier(r2, r1, r0)     // Catch:{ Exception -> 0x01e5 }
            android.content.res.XmlResourceParser r11 = r11.getXml(r12)     // Catch:{ Exception -> 0x01e5 }
            if (r11 != 0) goto L_0x0078
            goto L_0x005d
        L_0x0078:
            java.lang.Object r12 = r9.getKey()     // Catch:{ Exception -> 0x01e5 }
            java.lang.Object r12 = r4.get(r12)     // Catch:{ Exception -> 0x01e5 }
            if (r12 != 0) goto L_0x008e
            java.lang.Object r12 = r9.getKey()     // Catch:{ Exception -> 0x01e5 }
            java.util.LinkedHashMap r13 = new java.util.LinkedHashMap     // Catch:{ Exception -> 0x01e5 }
            r13.<init>()     // Catch:{ Exception -> 0x01e5 }
            r4.put(r12, r13)     // Catch:{ Exception -> 0x01e5 }
        L_0x008e:
            java.lang.Object r12 = r9.getKey()     // Catch:{ Exception -> 0x01e5 }
            java.lang.Object r12 = r5.get(r12)     // Catch:{ Exception -> 0x01e5 }
            if (r12 != 0) goto L_0x00a4
            java.lang.Object r12 = r9.getKey()     // Catch:{ Exception -> 0x01e5 }
            java.util.LinkedHashMap r13 = new java.util.LinkedHashMap     // Catch:{ Exception -> 0x01e5 }
            r13.<init>()     // Catch:{ Exception -> 0x01e5 }
            r5.put(r12, r13)     // Catch:{ Exception -> 0x01e5 }
        L_0x00a4:
            java.lang.String r12 = ""
            java.lang.Object r13 = r9.getValue()     // Catch:{ Exception -> 0x01e5 }
            java.lang.Integer r13 = (java.lang.Integer) r13     // Catch:{ Exception -> 0x01e5 }
            int r13 = r13.intValue()     // Catch:{ Exception -> 0x01e5 }
            if (r13 == r10) goto L_0x00ca
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e5 }
            r13.<init>()     // Catch:{ Exception -> 0x01e5 }
            r13.append(r12)     // Catch:{ Exception -> 0x01e5 }
            java.lang.String r12 = ".cluster"
            r13.append(r12)     // Catch:{ Exception -> 0x01e5 }
            java.lang.Object r12 = r9.getKey()     // Catch:{ Exception -> 0x01e5 }
            r13.append(r12)     // Catch:{ Exception -> 0x01e5 }
            java.lang.String r12 = r13.toString()     // Catch:{ Exception -> 0x01e5 }
        L_0x00ca:
            r14.parseCPUFreqTable(r4, r11, r12, r9)     // Catch:{ Exception -> 0x01e5 }
            android.content.res.Resources r11 = r14.mResources     // Catch:{ Exception -> 0x01e5 }
            android.content.res.Resources r13 = r14.mResources     // Catch:{ Exception -> 0x01e5 }
            int r13 = r13.getIdentifier(r2, r1, r0)     // Catch:{ Exception -> 0x01e5 }
            android.content.res.XmlResourceParser r11 = r11.getXml(r13)     // Catch:{ Exception -> 0x01e5 }
            r14.parseCPUExtraPowerConsumptionPerFreq(r5, r11, r12, r9)     // Catch:{ Exception -> 0x01e5 }
            goto L_0x005d
        L_0x00dd:
            java.util.Set r0 = r6.entrySet()     // Catch:{ Exception -> 0x01da }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x01da }
        L_0x00e5:
            boolean r1 = r0.hasNext()     // Catch:{ Exception -> 0x01da }
            if (r1 == 0) goto L_0x0119
            java.lang.Object r1 = r0.next()     // Catch:{ Exception -> 0x01da }
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch:{ Exception -> 0x01da }
            java.util.Map<java.lang.Long, java.lang.Float> r2 = r14.mGpuEff     // Catch:{ Exception -> 0x01da }
            java.lang.Object r6 = r1.getValue()     // Catch:{ Exception -> 0x01da }
            java.lang.Object r8 = r1.getValue()     // Catch:{ Exception -> 0x01da }
            java.lang.Long r8 = (java.lang.Long) r8     // Catch:{ Exception -> 0x01da }
            long r8 = r8.longValue()     // Catch:{ Exception -> 0x01da }
            float r8 = (float) r8     // Catch:{ Exception -> 0x01da }
            java.lang.Object r1 = r1.getKey()     // Catch:{ Exception -> 0x01da }
            java.lang.Object r1 = r7.get(r1)     // Catch:{ Exception -> 0x01da }
            java.lang.Float r1 = (java.lang.Float) r1     // Catch:{ Exception -> 0x01da }
            float r1 = r1.floatValue()     // Catch:{ Exception -> 0x01da }
            float r8 = r8 / r1
            java.lang.Float r1 = java.lang.Float.valueOf(r8)     // Catch:{ Exception -> 0x01da }
            r2.put(r6, r1)     // Catch:{ Exception -> 0x01da }
            goto L_0x00e5
        L_0x0119:
            java.util.Set r0 = r4.entrySet()     // Catch:{ Exception -> 0x01da }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x01da }
        L_0x0121:
            boolean r1 = r0.hasNext()     // Catch:{ Exception -> 0x01da }
            if (r1 == 0) goto L_0x019b
            java.lang.Object r1 = r0.next()     // Catch:{ Exception -> 0x01da }
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch:{ Exception -> 0x01da }
            java.lang.Object r2 = r1.getKey()     // Catch:{ Exception -> 0x01da }
            java.lang.Integer r2 = (java.lang.Integer) r2     // Catch:{ Exception -> 0x01da }
            int r2 = r2.intValue()     // Catch:{ Exception -> 0x01da }
            java.util.Map<java.lang.Integer, java.util.Map<java.lang.Long, java.lang.Float>> r4 = r14.mCpuEff     // Catch:{ Exception -> 0x01da }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x01da }
            java.util.LinkedHashMap r7 = new java.util.LinkedHashMap     // Catch:{ Exception -> 0x01da }
            r7.<init>()     // Catch:{ Exception -> 0x01da }
            r4.put(r6, r7)     // Catch:{ Exception -> 0x01da }
            java.lang.Object r1 = r1.getValue()     // Catch:{ Exception -> 0x01da }
            java.util.Map r1 = (java.util.Map) r1     // Catch:{ Exception -> 0x01da }
            java.util.Set r1 = r1.entrySet()     // Catch:{ Exception -> 0x01da }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ Exception -> 0x01da }
        L_0x0153:
            boolean r4 = r1.hasNext()     // Catch:{ Exception -> 0x01da }
            if (r4 == 0) goto L_0x0121
            java.lang.Object r4 = r1.next()     // Catch:{ Exception -> 0x01da }
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4     // Catch:{ Exception -> 0x01da }
            java.util.Map<java.lang.Integer, java.util.Map<java.lang.Long, java.lang.Float>> r6 = r14.mCpuEff     // Catch:{ Exception -> 0x01da }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x01da }
            java.lang.Object r6 = r6.get(r7)     // Catch:{ Exception -> 0x01da }
            java.util.Map r6 = (java.util.Map) r6     // Catch:{ Exception -> 0x01da }
            java.lang.Object r7 = r4.getValue()     // Catch:{ Exception -> 0x01da }
            java.lang.Object r8 = r4.getValue()     // Catch:{ Exception -> 0x01da }
            java.lang.Long r8 = (java.lang.Long) r8     // Catch:{ Exception -> 0x01da }
            long r8 = r8.longValue()     // Catch:{ Exception -> 0x01da }
            float r8 = (float) r8     // Catch:{ Exception -> 0x01da }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x01da }
            java.lang.Object r9 = r5.get(r9)     // Catch:{ Exception -> 0x01da }
            java.util.Map r9 = (java.util.Map) r9     // Catch:{ Exception -> 0x01da }
            java.lang.Object r4 = r4.getKey()     // Catch:{ Exception -> 0x01da }
            java.lang.Object r4 = r9.get(r4)     // Catch:{ Exception -> 0x01da }
            java.lang.Float r4 = (java.lang.Float) r4     // Catch:{ Exception -> 0x01da }
            float r4 = r4.floatValue()     // Catch:{ Exception -> 0x01da }
            float r8 = r8 / r4
            java.lang.Float r4 = java.lang.Float.valueOf(r8)     // Catch:{ Exception -> 0x01da }
            r6.put(r7, r4)     // Catch:{ Exception -> 0x01da }
            goto L_0x0153
        L_0x019b:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01da }
            r0.<init>()     // Catch:{ Exception -> 0x01da }
            java.lang.String r1 = "Detected CpuEfficiency "
            r0.append(r1)     // Catch:{ Exception -> 0x01da }
            r1 = 1
            java.util.Map[] r2 = new java.util.Map[r1]     // Catch:{ Exception -> 0x01da }
            java.util.Map<java.lang.Integer, java.util.Map<java.lang.Long, java.lang.Float>> r4 = r14.mCpuEff     // Catch:{ Exception -> 0x01da }
            r5 = 0
            r2[r5] = r4     // Catch:{ Exception -> 0x01da }
            java.util.List r2 = java.util.Arrays.asList(r2)     // Catch:{ Exception -> 0x01da }
            r0.append(r2)     // Catch:{ Exception -> 0x01da }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x01da }
            com.samsung.android.game.gos.ipm.Log.d(r3, r0)     // Catch:{ Exception -> 0x01da }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01da }
            r0.<init>()     // Catch:{ Exception -> 0x01da }
            java.lang.String r2 = "Detected GpuEfficiency "
            r0.append(r2)     // Catch:{ Exception -> 0x01da }
            java.util.Map[] r1 = new java.util.Map[r1]     // Catch:{ Exception -> 0x01da }
            java.util.Map<java.lang.Long, java.lang.Float> r2 = r14.mGpuEff     // Catch:{ Exception -> 0x01da }
            r1[r5] = r2     // Catch:{ Exception -> 0x01da }
            java.util.List r1 = java.util.Arrays.asList(r1)     // Catch:{ Exception -> 0x01da }
            r0.append(r1)     // Catch:{ Exception -> 0x01da }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x01da }
            com.samsung.android.game.gos.ipm.Log.d(r3, r0)     // Catch:{ Exception -> 0x01da }
            goto L_0x01fa
        L_0x01da:
            java.util.Map<java.lang.Long, java.lang.Float> r0 = r14.mGpuEff     // Catch:{ Exception -> 0x01e5 }
            r0.clear()     // Catch:{ Exception -> 0x01e5 }
            java.util.Map<java.lang.Integer, java.util.Map<java.lang.Long, java.lang.Float>> r0 = r14.mCpuEff     // Catch:{ Exception -> 0x01e5 }
            r0.clear()     // Catch:{ Exception -> 0x01e5 }
            goto L_0x01fa
        L_0x01e5:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unable to parse Power Profile "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            com.samsung.android.game.gos.ipm.Log.e(r3, r0)
        L_0x01fa:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.game.gos.ipm.AndroidDeviceSettings.parseCPUGPUPowerEfficiency():void");
    }

    private void parseCPUFreqTable(Map<Integer, Map<Integer, Long>> map, XmlResourceParser xmlResourceParser, String str, Map.Entry<Integer, Integer> entry) throws XmlPullParserException, IOException {
        String name;
        String attributeValue;
        String str2 = "cpu.speeds" + str;
        String str3 = "cpu.core_speeds" + str;
        while (xmlResourceParser.next() != 1) {
            if (xmlResourceParser.getEventType() == 2 && (name = xmlResourceParser.getName()) != null && name.equals("array") && (attributeValue = xmlResourceParser.getAttributeValue((String) null, ManagerInterface.KeyName.REQUEST_NAME)) != null) {
                if (attributeValue.contains(str2) || attributeValue.contains(str3)) {
                    while (true) {
                        if (xmlResourceParser.next() == 3 && xmlResourceParser.getName().equals("array")) {
                            return;
                        }
                        if (xmlResourceParser.getEventType() == 2) {
                            xmlResourceParser.next();
                            map.get(entry.getKey()).put(Integer.valueOf(map.get(entry.getKey()).size()), Long.valueOf(Long.parseLong(xmlResourceParser.getText())));
                        }
                    }
                }
            }
        }
    }

    private void parseCPUExtraPowerConsumptionPerFreq(Map<Integer, Map<Integer, Float>> map, XmlResourceParser xmlResourceParser, String str, Map.Entry<Integer, Integer> entry) throws IOException, XmlPullParserException {
        String name;
        String attributeValue;
        String str2 = "cpu.active" + str;
        String str3 = "cpu.core_power" + str;
        if (xmlResourceParser != null) {
            while (xmlResourceParser.next() != 1) {
                if (xmlResourceParser.getEventType() == 2 && (name = xmlResourceParser.getName()) != null && name.equals("array") && (attributeValue = xmlResourceParser.getAttributeValue((String) null, ManagerInterface.KeyName.REQUEST_NAME)) != null) {
                    if (attributeValue.contains(str2) || attributeValue.contains(str3)) {
                        while (true) {
                            if (xmlResourceParser.next() == 3 && xmlResourceParser.getName().equals("array")) {
                                return;
                            }
                            if (xmlResourceParser.getEventType() == 2) {
                                xmlResourceParser.next();
                                map.get(entry.getKey()).put(Integer.valueOf(map.get(entry.getKey()).size()), Float.valueOf(Float.parseFloat(xmlResourceParser.getText())));
                            }
                        }
                    }
                }
            }
        }
    }

    private void parseScenario(InputStream inputStream) {
        try {
            XmlPullParser newPullParser = Xml.newPullParser();
            newPullParser.setInput(inputStream, (String) null);
            while (newPullParser.next() != 1) {
                if (newPullParser.getEventType() == 2) {
                    String name = newPullParser.getName();
                    String attributeValue = newPullParser.getAttributeValue((String) null, ManagerInterface.KeyName.REQUEST_NAME);
                    if (name != null && attributeValue != null && name.equals("scenario") && attributeValue.equals("IPM+")) {
                        this.mIpmScenario = true;
                        return;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Unable to parse Ssrm Scenario " + e);
        }
    }

    public boolean getIpmScenario() {
        if (!this.mIpmScenarioDynamicChecked) {
            checkDynamicScenarioSsrm();
        }
        return this.mIpmScenario;
    }

    public boolean isIpmScenarioDynamicChecked() {
        return this.mIpmScenarioDynamicChecked;
    }

    public Map<Long, Float> getGpuEff() {
        return this.mGpuEff;
    }

    public Map<Long, Float> getCpuEff(int i) {
        if (this.mCpuEff.containsKey(Integer.valueOf(i))) {
            return this.mCpuEff.get(Integer.valueOf(i));
        }
        return null;
    }

    public int getClusterSize() {
        return this.mCpuEff.size();
    }
}
