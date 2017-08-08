package com.epaylinks.appupdate;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.epaylinks.appupdate.utils.LogUtils;
import com.example.deken.myapplication.R;


public class UpgradeProgress extends AlertDialog {
	private Context context;
	private ProgressBar mProgress;
	private TextView mProgressNumber;
	private TextView mProgressPercent;
	private TextView mProgressSpeed;
	
	private NotificationManager notificationManager;
	private Notification notification;
	private int notificationId=1;
	
	public static final int M = 1024 * 1024;
	public static final int K = 1024;
	public static boolean updateNotificationFlag;
	private int middle = K;
	private double max;
	private double progress;
	private int speedInt=0;
	private int prev = 0;
	
	private static final NumberFormat nf = NumberFormat.getPercentInstance();
	private static final DecimalFormat df = new DecimalFormat("###.##");

	private int lastProK;
	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			double precent = progress / max;
			switch (msg.what) {
			case 0:
				if (notification==null) {
				    if (prev != (int)(precent * 100)) {
						mProgress.setProgress((int)(precent * 100));
						mProgressPercent.setText(nf.format(precent));
						mProgressNumber.setText(df.format(progress) + "/" + df.format(max) + (middle == K ? "K" : "M"));
						prev = (int)(precent * 100);
				    }
				}
				break;
			case 1:
				if (notification==null) {
					mProgressSpeed.setText(handleSpeedStr(speedInt)+"/s");
				}else {
					notification.contentView.setProgressBar(R.id.notification_progressBar, 100,(int)(precent*100), false);
					if ((int)(precent*100)!=0) {
						notification.contentView.setTextViewText(R.id.notification_percent, nf.format(precent));
					}
					notification.contentView.setTextViewText(R.id.notification_number, df.format(progress) + "/" + df.format(max) + (middle == K ? "K" : "M"));
//					notification.contentView.setTextViewText(R.id.notification_speed, speed+"K/s");
					notification.contentView.setTextViewText(R.id.notification_speed, handleSpeedStr(speedInt)+"/s");
					showNotification();
				}
				break;

			default:
				break;
			}
		}
	};

	private String handleSpeedStr(int speedInt){
		String re="";
		int k=speedInt/1024;
		if(k>0){
			re=k+"M";
		}else if(k==0){
			re=speedInt+"K";
		}
		return re;
	}


	public UpgradeProgress(Context context) {
		super(context);
		this.context=context;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.my_progressdialog, null);
		setView(view);
		super.onCreate(savedInstanceState);

		mProgress = (ProgressBar) findViewById(R.id.progressBar);
		mProgressNumber = (TextView) view.findViewById(R.id.progress_number);
		mProgressPercent = (TextView) view.findViewById(R.id.progress_percent);
		mProgressSpeed = (TextView) view.findViewById(R.id.progress_speed);
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		if(max > M) {
			middle = M;
		}else{
			middle = K;
		}
		this.max = max / middle;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress / middle;
		updateHandler.sendEmptyMessage(0);
	}

	public void setDownloadSpeed(int speed){
		speedInt=speed;
		updateHandler.sendEmptyMessage(1);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			createNotification();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@TargetApi(Build.VERSION_CODES.N)
	public void createNotification(){
		UpgradeProgress.updateNotificationFlag=true;
        notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notification=new Notification.Builder(context).setWhen(System.currentTimeMillis())
				.setSmallIcon(android.R.drawable.stat_sys_download_done)
				.setTicker("some thing is downing")
				.build();
//    	notification=new Notification(android.R.drawable.stat_sys_download_done,"ȫ��-��������",System.currentTimeMillis());

		notification.contentView = new RemoteViews(context.getPackageName(),R.layout.notification_update);
    	notification.contentView.setProgressBar(R.id.notification_progressBar, 100,0, false);

//    	Intent notificationIntent = new Intent(context,SplashActivity.class);
//    	PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,0);
//    	notification.contentIntent = contentIntent;
    	showNotification();
	}
	
    public void showNotification(){
    	notificationManager.notify(notificationId, notification);   	
    }
    
    public void cancleNotification(){
    	if (notification!=null) {
    		UpgradeProgress.updateNotificationFlag=false;
    		notificationManager.cancel(notificationId);
		}
    }

}
