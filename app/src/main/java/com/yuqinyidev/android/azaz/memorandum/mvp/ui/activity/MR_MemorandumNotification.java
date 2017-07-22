package com.yuqinyidev.android.azaz.memorandum.mvp.ui.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.yuqinyidev.android.azaz.R;

public class MR_MemorandumNotification extends Activity {

	public static final String NOTIFICATION_EVENT_DATE = "notification_event_date";

	public static final String NOTIFICATION_TITLE = "notification_title";
	public static final String NOTIFICATION_DATE = "notification_date";
	public static final String NOTIFICATION_CONTENT = "notification_content";
	public static final String NOTIFICATION_PCD = "notification_pcd";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 从系统中获得Notification服务，getSystemService()就是这么用，来获得系统服务的。
		NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// 从MemorandumReceiver中获取的信息
		Bundle b = getIntent().getExtras();

		// 获取标题
		String title = b.getString(NOTIFICATION_TITLE);

		// 获取内容
		String content = b.getString(NOTIFICATION_CONTENT);

		// 获取日期
		String date = b.getString(NOTIFICATION_DATE);

		// 获取id
		int id = b.getInt(NOTIFICATION_PCD);

		/**
		 * 然后是构造一个Notification，包括三个属性，图标，图标后面的文字，以及Notification时间部分显示出来的时间，
		 * 通常使用当前时间。FLAG_AUTO_CANCEL说明Notification点击一次就消失。
		 **/
		Notification notification = new Notification(R.drawable.notif, title,
				System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		/**
		 * 构造一个Intent，并且放进去一条信息。 FLAG_ACTIVITY_CLEAR_TOP，
		 * FLAG_ACTIVITY_NEW_TASK者两个FLAG表示优先寻找已经打开的应用， 如果应用没有打开那么启动它。
		 **/
		Intent intent = new Intent(MR_MemorandumNotification.this,
				MR_MemorandumMain.class);

		// 数据封装
		Bundle bundle = new Bundle();

		// 事件日期
		bundle.putString(NOTIFICATION_EVENT_DATE, date);

		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		/**
		 * 把Intent包装在PendingIntent里，this是Context，id是PendingIntent的标志，
		 * 如果id相同会被认为是一个。 FLAG_UPDATE_CURRENT是指后来的PendingIntent会更新前面的。
		 **/
		PendingIntent contentIntent = PendingIntent.getActivity(
				MR_MemorandumNotification.this, id, intent, 0);

		// 添加状态栏的详细信息，包装PendingIntent给Notification，最后发送Notification。
		notification.setLatestEventInfo(this, title, content, contentIntent);
		notiManager.notify(id, notification);
		this.finish();
	}
}
