package com.epaylinks.appupdate;

import com.epaylinks.appupdate.bean.VersionInfo;
import com.epaylinks.download.DownloadEntity;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Deken on 2017/5/23.
 */

public abstract class AppUpdateWatcher implements Observer{

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof VersionInfo){
            VersionInfo entity= (VersionInfo) arg;
            dataChanged(entity);
        }
    }

    public abstract void dataChanged(VersionInfo entity);
}
