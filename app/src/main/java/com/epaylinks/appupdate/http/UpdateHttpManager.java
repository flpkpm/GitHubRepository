package com.epaylinks.appupdate.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.epaylinks.appupdate.AppUpdateObservable;
import com.epaylinks.appupdate.bean.VersionInfo;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by Deken on 2017/5/23.
 */

public class UpdateHttpManager {

    private  Handler mHandler;
    private String path="http://www.globalcash.hk/applicationManage/open/update.do?appid=1";
    private IHttp iHttp=new CheckUpdateHttp();

    public UpdateHttpManager() {
        mHandler = new Handler(Looper.getMainLooper()){//主线程handler
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0){
                    VersionInfo info=(VersionInfo) msg.obj;
                    AppUpdateObservable.getInstance().notifyDataChanged(info);
                }
            }
        };
    }

    public void checkUpdate() {
        checkUpdate(path);
    }

    public void setHttpRequest(IHttp http){
        this.iHttp =http;
    }

    private void checkUpdate(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result=iHttp.httpRequest(path);
                VersionInfo info=xmlToVersion(result);
                Message ms=mHandler.obtainMessage();
                ms.what=0;
                ms.obj=info;
                mHandler.sendMessage(ms);
            }
        }).start();
    }

    public VersionInfo xmlToVersion(String result) {
        VersionInfo versioninfo = new VersionInfo();
        try {
            JSONObject json = new JSONObject(result);
            String s4 = json.optString("optional");
            versioninfo.setIsoptional(s4);
            String s5 = json.optString("path");
            if (!s5.contains("http://"))
                s5 = "http://" + s5;
            versioninfo.setApkPath(s5);
            String s6 = json.optString("version");
            versioninfo.setVersion(s6);
            JSONObject s7JsonObj = json.optJSONObject("desc");
            String s7Title = s7JsonObj.optString("title") + "\n";
            JSONArray s7JsonArr = s7JsonObj.optJSONArray("items");
            int length = s7JsonArr.length()-1;
            for (int i = 0; i <= length; i++) {
                s7Title += s7JsonArr.getString(i);
                if (i != length) {
                    s7Title += "\n";
                }
            }
            versioninfo.setDesc(s7Title);
            String s8 = json.optString("must");
            // 是否强制更新
            if (TextUtils.isEmpty(s8) || !s8.equalsIgnoreCase("true")) {
                s8 = "false";
            } else {
                s8 = "true";
            }
            versioninfo.setMustUpdate(s8);

        } catch (Exception e) {

        }
        return versioninfo;
    }


}
