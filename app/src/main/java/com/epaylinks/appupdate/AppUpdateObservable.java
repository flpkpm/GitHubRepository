package com.epaylinks.appupdate;

import com.epaylinks.appupdate.bean.VersionInfo;
import com.epaylinks.download.DownloadEntity;
import com.epaylinks.download.DownloadManager;

import java.util.Observable;

/**
 * Created by Deken on 2017/5/23.
 */

public class AppUpdateObservable extends Observable{

    private static AppUpdateObservable mDataObservable;

    private AppUpdateObservable(){}

    public static AppUpdateObservable getInstance(){
        synchronized (AppUpdateObservable.class){
            if(mDataObservable==null){
                synchronized (DownloadManager.class){
                    if(mDataObservable==null){
                        mDataObservable=new AppUpdateObservable();
                    }
                }
            }
        }
        return mDataObservable;
    }

    public void notifyDataChanged(VersionInfo mVersionInfo) {
        setChanged();
        notifyObservers(mVersionInfo);
    }


}
