package com.epaylinks.appupdate.http;

import com.epaylinks.appupdate.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Deken on 2017/5/24.
 */

public class CheckUpdateHttp implements IHttp {

    @Override
    public String httpRequest(final String path) {
        return queryStringForPost(path);
    }

    /**
     * 网络访问
     */
    public  String queryStringForPost(String path) {
        try {
            URL rul = new URL(path);
            HttpURLConnection connection= (HttpURLConnection) rul.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5*1000);
            connection.setReadTimeout(5*1000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            if(connection.getResponseCode()== HttpURLConnection.HTTP_OK){
                InputStream is=connection.getInputStream();
                ByteArrayOutputStream os=new ByteArrayOutputStream();

                byte[] temp=new byte[1024];
                int len=-1;
                while ((len=is.read(temp))!=-1){
                    os.write(temp,0,len);
                }
                String result=new String(os.toByteArray(),"UTF-8");
                LogUtils.i("deken","  result : "+result);
                return  result;
            }
        } catch (Exception e) {
            // TODO: 2017/5/24  网络错误监听
            e.printStackTrace();
            return "";
        }

        return "";
    }
}
