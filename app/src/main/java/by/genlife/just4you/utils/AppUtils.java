package by.genlife.just4you.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class AppUtils {

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
