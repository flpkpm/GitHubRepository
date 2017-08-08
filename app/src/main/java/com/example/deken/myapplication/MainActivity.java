package com.example.deken.myapplication;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epaylinks.appupdate.AppUpdateManager;
import com.epaylinks.appupdate.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppUpdateManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_start= (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        manager = AppUpdateManager.getInstance(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
//                manager.startCheckUpdate();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String d="<?xml version=\"1.0\" encoding=\"UTF-8\"?><CardServiceRequest xmlns=\"http://www.nrf-arts.org/IXRetail/namespace\" xmlns:IFSF=\"http://www.ifsf.org/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"\" RequestType=\"GetAllSaleList\" ApplicationSender=\"CNOOC\" WorkstationID=\"88888888\"  RequestID=\"00000012\"><POSdata><POSTimeStamp>2017-07-21T14:19:59+08:00</POSTimeStamp><OutdoorPosition>00</OutdoorPosition></POSdata></CardServiceRequest>";
                        JConnection11 connection=new JConnection11();
                        connection.connect("172.20.16.185",20005);
                        connection.write(d.getBytes());
//                        SystemClock.sleep(5000);
                        String read=connection.read();
                        LogUtils.i("deken","the result data: "+read);
                        connection.disconnect();
                    }
                }).start();
                break;
        }
        String s=new String("");
        StringBuffer sb=new StringBuffer();

        ArrayList list=new ArrayList();
        HashMap<String,String> map=new HashMap<>();
        HashSet<String> set=new HashSet<>();

        ListView listView=new ListView(this);

        mHandler.sendEmptyMessage(1);

        new Handler(Looper.getMainLooper());
        Toast.makeText(this, "heheh", Toast.LENGTH_SHORT).show();
        TextView view =new TextView(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("deken","current thread: "+Thread.currentThread().getName());
            }
        });
        view.post(new Runnable() {
            @Override
            public void run() {

            }
        });

    }


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO: 2017/7/30  调用父类  的handleMessage
        }
    };

    @Override
    protected void onDestroy() {

        super.onDestroy();
        manager.onDestory();
    }
}
