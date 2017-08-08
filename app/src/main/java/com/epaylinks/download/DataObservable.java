package com.epaylinks.download;

import java.util.Observable;

/**
 * Created by Deken on 2017/5/23.
 */

public class DataObservable extends Observable{
    private static DataObservable mDataObservable;

    private DataObservable(){}

    public static DataObservable getInstance(){
        synchronized (DataObservable.class){
            if(mDataObservable==null){
                synchronized (DownloadManager.class){
                    if(mDataObservable==null){
                        mDataObservable=new DataObservable();
                    }
                }
            }
        }
        return mDataObservable;
    }

    public void notifyDataChanged(DownloadEntity mDownLoadEntity) {
        setChanged();
        notifyObservers(mDownLoadEntity);
    }


}
