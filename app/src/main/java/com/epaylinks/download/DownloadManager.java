package com.epaylinks.download;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Deken on 2017/5/23.
 */

public class DownloadManager {

    private final Context mContext;
    private static DownloadManager mDownManager;

    private DownloadManager(Context context){
        this.mContext=context;
    }

    public static DownloadManager getInstance(Context context){
        synchronized (DownloadManager.class){
            if(mDownManager==null){
                synchronized (DownloadManager.class){
                    if(mDownManager==null){
                        mDownManager=new DownloadManager(context);
                    }
                }
            }
        }
        return mDownManager;
    }

    public void start(DownloadEntity entity){
        Intent startIntent=new Intent(mContext,DownloadService.class);
        startIntent.putExtra(DownloadConstants.KEY_DOWNLOAD_ORDER,DownloadConstants.ORDER_DOWNLOAD_START);
        startIntent.putExtra(DownloadConstants.KEY_DOWNLOAD_ENTITY, entity);
        LogUtils.i("deken","action ");
        mContext.startService(startIntent);
    }

    public void stop(DownloadEntity entity){

    }

    public void resume(DownloadEntity entity){

    }

    public void cancel(DownloadEntity entity){

    }

    public void registDataWatcher(DataWatcher dataWatcher){
        DataObservable.getInstance().addObserver(dataWatcher);
    }

    public void unRegistDataWatcher(DataWatcher dataWatcher){
        DataObservable.getInstance().deleteObserver(dataWatcher);
    }







}
