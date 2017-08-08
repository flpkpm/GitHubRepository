package com.epaylinks.download;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Deken on 2017/5/23.
 */

public abstract class DataWatcher implements Observer{

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof DownloadEntity){
            DownloadEntity entity= (DownloadEntity) arg;
            dataChanged(entity);
        }
    }

    public abstract void dataChanged(DownloadEntity entity);
}
