package com.epaylinks.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Deken on 2017/5/23.
 */

public class DownloadService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i("deken","onBind ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        LogUtils.i("deken","onStartCommand ");
        doAction(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void doAction(Intent intent) {
        int action=intent.getIntExtra(DownloadConstants.KEY_DOWNLOAD_ORDER,-1);
        DownloadEntity entity= (DownloadEntity) intent.getSerializableExtra(DownloadConstants.KEY_DOWNLOAD_ENTITY);
        LogUtils.i("deken","action "+action);
        if(action==-1||entity==null){
            LogUtils.i("deken","something err");
            return;
        }
        switch (action){
            case DownloadConstants.ORDER_DOWNLOAD_START:
                start(entity);
                break;
        }
    }

    private void start(DownloadEntity entity) {
        DownloadTask task=new DownloadTask(entity);
        task.start();
    }


}
