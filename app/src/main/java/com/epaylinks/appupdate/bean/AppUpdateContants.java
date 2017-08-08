package com.epaylinks.appupdate.bean;

/**
 * Created by Deken on 2017/5/23.
 */

public class AppUpdateContants {
    public static final int TYPE_NOUPDATE=1; // 没有更新
    public static final int TYPE_SELECT = 2;// 可选更新
    public static final int TYPE_MUST = 3;// 必须更新

    // FIXME: 2017/5/23  未考虑国际化
    public static final String UPGRADE_TO_LATEST_VERSION = "程序版本过低,马上升级?";// 必须更新,
    public static final String COMFIRM = "确定";// 必须更新,


}
