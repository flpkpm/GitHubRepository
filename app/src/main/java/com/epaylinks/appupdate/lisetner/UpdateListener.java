package com.epaylinks.appupdate.lisetner;

import com.epaylinks.appupdate.bean.VersionInfo;

/**
 * Created by Deken on 2017/5/23.
 */

public interface UpdateListener {
    void noUpdate();
    void selectUpdate(VersionInfo info);
    void mustUpdate(VersionInfo info);
}
