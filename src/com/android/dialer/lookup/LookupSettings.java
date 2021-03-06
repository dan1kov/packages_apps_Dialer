/*
 * Copyright (C) 2014 Xiao-Long Chen <chenxiaolong@cxl.epac.to>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.dialer.lookup;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;

import java.util.List;

public final class LookupSettings {
    private static final String TAG = LookupSettings.class.getSimpleName();

    /** Forward lookup providers */
    public static final String FLP_GOOGLE = "Google";
    public static final String FLP_OPENSTREETMAP = "OpenStreetMap";

    /** Reverse lookup providers */
    public static final String RLP_GOOGLE = "Google";
    public static final String RLP_OPENCNAM = "OpenCnam";
    public static final String RLP_WHITEPAGES = "WhitePages";
    public static final String RLP_WHITEPAGES_CA = "WhitePages_CA";
    public static final String RLP_YELLOWPAGES = "YellowPages";
    public static final String RLP_ZABASEARCH = "ZabaSearch";

    private LookupSettings() {
    }

    public static boolean isForwardLookupEnabled(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.ENABLE_FORWARD_LOOKUP, 1) != 0;
    }

    public static boolean isReverseLookupEnabled(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.ENABLE_REVERSE_LOOKUP, 1) != 0;
    }

    public static String getForwardLookupProvider(Context context) {
        String provider = getString(context,
                Settings.System.FORWARD_LOOKUP_PROVIDER);

        if (provider == null) {
            putString(context,
                    Settings.System.FORWARD_LOOKUP_PROVIDER, FLP_GOOGLE);

            provider = getString(context,
                    Settings.System.FORWARD_LOOKUP_PROVIDER);
        }

        return provider;
    }

    public static String getReverseLookupProvider(Context context) {
        String provider = getString(context,
                Settings.System.REVERSE_LOOKUP_PROVIDER);

        if (provider == null) {
            // If Google Play Services is not available, default to the next
            // provider in the list (OpenCnam)
            putString(context,
                    Settings.System.REVERSE_LOOKUP_PROVIDER,
                    isGmsInstalled(context) ? RLP_GOOGLE : RLP_OPENCNAM);

            provider = getString(context,
                    Settings.System.REVERSE_LOOKUP_PROVIDER);
        }

        return provider;
    }

    private static String getString(Context context, String key) {
        return Settings.System.getString(context.getContentResolver(), key);
    }

    private static void putString(Context context, String key, String value) {
        Settings.System.putString(context.getContentResolver(), key, value);
    }

    private static boolean isGmsInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            if (info.packageName.equals("com.google.android.gms")) {
                return true;
            }
        }
        return false;
    }
}
