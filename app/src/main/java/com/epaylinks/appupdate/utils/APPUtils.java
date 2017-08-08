package com.epaylinks.appupdate.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.List;

/**
 * Created by Deken on 2017/5/23.
 */

public class APPUtils {
    /**
     * 安装程序
     */
    public static void installApk(String filePath, Context mContext) {
        LogUtils.i("deken"," install apk filePath: "+filePath);
//        File file = new File(filePath);
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        String type = "application/vnd.android.package-archive";
//        //file:/// exposed beyond app through Intent.getData()
//        intent.setDataAndType(Uri.fromFile(file), type);
//        mContext.startActivity(intent);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", new File(filePath));
            LogUtils.i("deken","getPackageName: "+mContext.getPackageName());
            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                mContext.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }else {
            uri = Uri.fromFile(new File(filePath));
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }
}
