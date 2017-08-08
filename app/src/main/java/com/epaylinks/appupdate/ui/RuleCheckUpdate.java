package com.epaylinks.appupdate.ui;

import com.epaylinks.appupdate.bean.AppUpdateContants;
import com.epaylinks.appupdate.bean.VersionInfo;
import com.epaylinks.appupdate.utils.LogUtils;

/**
 * Created by Deken on 2017/5/23.
 */

public class RuleCheckUpdate  implements IRuleCheckUpdate{

    @Override
    public int isUpdate(VersionInfo info) {
        return update(info);
    }

    private int update(VersionInfo vInfo){
        String serverVersion = vInfo.getVersion();
        String localVersion = vInfo.getLocalVersion();
        LogUtils.i("deken", "服务端版本号--->" + serverVersion);
        LogUtils.i("deken", "本地版本号--->" + localVersion);
        if (null != serverVersion && null != vInfo.getApkPath()) {
            if (localVersion.compareTo(serverVersion) >= 0) { // 不用更新
                return AppUpdateContants.TYPE_NOUPDATE;
            }
            // 是否强制更新 true or false
            String mustFlag = vInfo.getMustUpdate();
            if ("true".equalsIgnoreCase(mustFlag)) {
                return  AppUpdateContants.TYPE_MUST;
            } else {
                // 当前版本跟服务器版本相差超过0.2，强制更新
                String tempServer = serverVersion.replace('.', '@');
                String tempLocal = localVersion.replace('.', '@');
                String[] serverArr = tempServer.split("@");
                String[] localArr = tempLocal.split("@");
                String serverMidTag = serverArr[0] + serverArr[1];
                String localMidTag = localArr[0] + localArr[1];
                if (Integer.parseInt(serverMidTag) - Integer.parseInt(localMidTag) >= 2) {
                    return  AppUpdateContants.TYPE_MUST;
                } else {
                    return  AppUpdateContants.TYPE_SELECT;
                }
            }
        }
        return AppUpdateContants.TYPE_NOUPDATE;
    }
}
