package com.epaylinks.download;

import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Deken on 2017/5/23.
 */

public class DownloadTask extends Thread{
    private String downPathFile = "/download/";
    private  DownloadEntity mDownLoadEntity;
    private Timer timer = new Timer();


    public DownloadTask(DownloadEntity entity) {
        this.mDownLoadEntity=entity;
    }
    private float temp_count=0;

    private void calSpeed(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int speed= (int) ((mDownLoadEntity.getCurrentLen()-temp_count)/1024);
                mDownLoadEntity.setSpeedInt(speed);
                temp_count=mDownLoadEntity.getCurrentLen();
            }
        }, 0, 1000);
    }

    public void startLoad() {
        calSpeed();
        mDownLoadEntity.setTotalLen(10*1024);
        mDownLoadEntity.setCurrentLen(0);
        mDownLoadEntity.setmDownState(DownloadEntity.DownState.STARTED);
        for (int i = 0; i < mDownLoadEntity.getTotalLen()/1024; i++) {
            SystemClock.sleep(1000*1);
            mDownLoadEntity.setCurrentLen((i+1)*1024);
            mDownLoadEntity.setmDownState(DownloadEntity.DownState.DOWNING);
            notifyChange(mDownLoadEntity);
        }
        mDownLoadEntity.setmDownState(DownloadEntity.DownState.FINISH);
        notifyChange(mDownLoadEntity);
    }

    public void startDownLoad(){
        InputStream in=null;
        FileOutputStream out=null;
        HttpURLConnection con =null;
        try {
            String dir = Environment.getExternalStorageDirectory().getPath() + downPathFile;
            String  filepath = dir + mDownLoadEntity.getDownedFileName();
            LogUtils.i("deken","保存路径:" + filepath);

            File dirf = new File(dir);
            if (!dirf.exists()) {
                dirf.mkdirs();
            }
            File isfile = new File(filepath);
            if (isfile.exists()) {
                isfile.delete();
            }
            URL  myurl = new URL(mDownLoadEntity.getUrl());
            LogUtils.i("deken","down url:" + mDownLoadEntity.getUrl());
            con = (HttpURLConnection) myurl.openConnection();
            con.setConnectTimeout(10 * 1000);
            con.setReadTimeout(10 * 1000);
            con.setDoInput(true);
            int fileSize = con.getContentLength();

            //1，开始下载
            mDownLoadEntity.setTotalLen(fileSize);
            mDownLoadEntity.setmDownState(DownloadEntity.DownState.STARTED);
            calSpeed();

            File fileOut = new File(filepath);
            out = new FileOutputStream(fileOut);
            byte[] buffer = new byte[1024];
            in = con.getInputStream();
            int readNum;
            int currentCount=0;
            //2，下载中
            while ((readNum = in.read(buffer)) !=-1) {
                out.write(buffer, 0, readNum);
                currentCount = currentCount + readNum;
                mDownLoadEntity.setCurrentLen(currentCount);
                mDownLoadEntity.setmDownState(DownloadEntity.DownState.DOWNING);
                notifyChange(mDownLoadEntity);
            }
            //3，下载完成
            mDownLoadEntity.setDownedFilePath(filepath);
            mDownLoadEntity.setmDownState(DownloadEntity.DownState.FINISH);
            notifyChange(mDownLoadEntity);

        } catch (Exception e) {
            e.printStackTrace();
            timer.cancel();
            mDownLoadEntity.setmDownState(DownloadEntity.DownState.ERR);
            notifyChange(mDownLoadEntity);
            LogUtils.e("deken","down err" + e.getMessage());
        } finally {
            timer.cancel(); // 取消计时器
            try {
                if (in != null){
                    in.close();
                }
                if (out != null){
                    out.close();
                }
                if(con!=null){
                    con.disconnect();
                }
            } catch (IOException e) {
                LogUtils.e("deken","io err" + e.getMessage());
            }
        }

    }

    private void notifyChange(DownloadEntity mDownLoadEntity) {
        DataObservable.getInstance().notifyDataChanged(mDownLoadEntity);
    }

    @Override
    public void run() {
        super.run();
        startDownLoad();
    }
}
