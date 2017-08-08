package com.epaylinks.appupdate;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.epaylinks.appupdate.bean.AppUpdateContants;
import com.epaylinks.appupdate.bean.VersionInfo;
import com.epaylinks.appupdate.http.UpdateHttpManager;
import com.epaylinks.appupdate.ui.IRuleCheckUpdate;
import com.epaylinks.appupdate.ui.RuleCheckUpdate;
import com.epaylinks.appupdate.utils.APPUtils;
import com.epaylinks.appupdate.utils.LogUtils;
import com.epaylinks.download.DataWatcher;
import com.epaylinks.download.DownloadEntity;
import com.epaylinks.download.DownloadManager;


/**
 * Created by Deken on 2017/5/23.
 */

public class AppUpdateManager {
    private Context mContext;
    private VersionInfo versionInfo =null;
    private static  AppUpdateManager mAppUpdateManager ;
    private AppUpdateWatcher mWatcher=new AppUpdateWatcher() {
        @Override
        public void dataChanged(VersionInfo entity) {
            versionInfo =entity;
            versionInfo.setLocalVersion("2.8.2");
            versionInfo.setMustUpdate("false");
            if(entity!=null){
                updateApp(versionInfo);
            }
        }
    };
    private  IRuleCheckUpdate ruleCheckUpdate=new RuleCheckUpdate();
    private final DownloadManager mDownloadManager;
    private DataWatcher mDataWatcher=new DataWatcher() {
        @Override
        public void dataChanged(DownloadEntity entity) {
            LogUtils.i("deken","entity:  "+entity.getCurrentLen()+"/"+entity.getTotalLen());
            if(entity.getmDownState()== DownloadEntity.DownState.FINISH){
                //下载完成
                progressNotify.dismiss();
                progressNotify.cancleNotification();
                APPUtils.installApk(entity.getDownedFilePath(),mContext);
            }else if(entity.getmDownState()== DownloadEntity.DownState.DOWNING){
                // 通知栏以及dialog 通知进度
                progressNotify.setMax(entity.getTotalLen());
                progressNotify.setProgress(entity.getCurrentLen());
                progressNotify.setDownloadSpeed(entity.getSpeedInt());
                LogUtils.i("deken","speed: "+entity.getSpeedInt());
            }else if(entity.getmDownState()== DownloadEntity.DownState.ERR){
                progressNotify.dismiss();
                progressNotify.cancleNotification();
            }
        }
    };
    private final UpgradeProgress progressNotify;

    private  AppUpdateManager (Context context){
        this.mContext=context;
        AppUpdateObservable.getInstance().addObserver(mWatcher);
        // FIXME: 2017/5/23  需要优化
        mDownloadManager = DownloadManager.getInstance(mContext);
        mDownloadManager.registDataWatcher(mDataWatcher);
        progressNotify = new UpgradeProgress(mContext);
    }

    public static  AppUpdateManager  getInstance(Context mContext){
        synchronized ( AppUpdateManager .class){
            if(mAppUpdateManager ==null){
                synchronized ( AppUpdateManager .class){
                    if(mAppUpdateManager ==null){
                        mAppUpdateManager =new  AppUpdateManager (mContext);
                    }
                }
            }
        }
        return mAppUpdateManager ;
    }

    public void startCheckUpdate(){
        UpdateHttpManager mUpdateHttpManager=new UpdateHttpManager();
        mUpdateHttpManager.checkUpdate();
    }

    public void onDestory(){
        AppUpdateObservable.getInstance().deleteObserver(mWatcher);
        mDownloadManager.unRegistDataWatcher(mDataWatcher);
    }


    private void updateApp(VersionInfo entity) {
        switch (ruleCheckUpdate.isUpdate(entity)){
            case AppUpdateContants.TYPE_NOUPDATE:
                noUpdate();
                break;
            case AppUpdateContants.TYPE_SELECT:
                selectUpdate();
                break;
            case AppUpdateContants.TYPE_MUST:
                mustUpdate();
                break;
        }
    }

   // FIXME: 2017/5/24  监听
    public void noUpdate(){
        LogUtils.i("deken"," noUpdate");
    }
    /**
     * 需要更新的操作
     */
    private void selectUpdate(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setIcon(0x00000000);
        dialog.setTitle("发现新版本 " + versionInfo.getVersion());
        dialog.setMessage(versionInfo.getDesc());
        dialog.setPositiveButton("立即更新", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downApk();
            }
        });
        dialog.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noUpdate();
            }
        });
        dialog.create().show();
    }

    private void downApk() {
        // TODO: 2017/5/23  需要添加下载的sdk临时路径
        // TODO: 网络变动的判断
        //// TODO: 2017/5/23  断点续传 
        DownloadEntity entity=new DownloadEntity();
        entity.setUrl(versionInfo.getApkPath());
        entity.setId(1);
        String fileName="CTCloud_V" + versionInfo.getVersion() + ".apk";
        entity.setDownedFileName(fileName);
        mDownloadManager.start(entity);
        progressNotify.setTitle("正在下载...");
        progressNotify.setButton("最小化",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressNotify.createNotification();
                    }
                });
        progressNotify.setCanceledOnTouchOutside(false);  //在对话框的外面点击，不让对话框消失
        progressNotify.show();
    }


    /**
     * 需要强制更新的操作
     */
    public void mustUpdate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(AppUpdateContants.UPGRADE_TO_LATEST_VERSION)
                .setPositiveButton(AppUpdateContants.COMFIRM,
                        new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downApk();
                            }
                        });
        builder.setMessage(versionInfo.getDesc());
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }


}
