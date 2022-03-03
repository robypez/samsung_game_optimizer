package com.samsung.android.game.gos.selibrary;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import com.samsung.android.game.gos.value.AppVariable;
import com.samsung.android.game.gos.value.SecureSettingConstants;

public class SeSecureSetting {
    public static final String KEY_ALLOW_MORE_HEAT_HISTORY = "allow_more_heat_history";

    public int getInt(ContentResolver contentResolver, String str, int i) {
        if (!AppVariable.isUnitTest() || !SecureSettingConstants.KEY_AUTO_CONTROL.equalsIgnoreCase(str)) {
            return Settings.Secure.getInt(contentResolver, str, i);
        }
        return 3;
    }

    public float getFloat(ContentResolver contentResolver, String str, float f) {
        return Settings.Secure.getFloat(contentResolver, str, f);
    }

    public Uri getUriFor(String str) {
        return Settings.Secure.getUriFor(str);
    }

    public String getString(ContentResolver contentResolver, String str) {
        return Settings.Secure.getString(contentResolver, str);
    }

    public boolean putString(ContentResolver contentResolver, String str, String str2) {
        return Settings.Secure.putString(contentResolver, str, str2);
    }
}
