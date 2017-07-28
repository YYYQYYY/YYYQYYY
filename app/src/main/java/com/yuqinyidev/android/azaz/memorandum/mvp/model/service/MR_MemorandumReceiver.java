package com.yuqinyidev.android.azaz.memorandum.mvp.model.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.MR_DBAdapter;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.entity.MR_memory;
import com.yuqinyidev.android.azaz.memorandum.mvp.ui.activity.MR_MemorandumNotification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MR_MemorandumReceiver extends BroadcastReceiver {

    // 建立eventList数组来存放多个待提醒事件
    private static List<MR_memory> eventList;

    static {
        eventList = new ArrayList<MR_memory>();
    }

    // 从memory类中获取信息
    private MR_memory memo;
    int var;
    String time;
    String date;
    String smonth;
    String sday;
    String shour;
    String sminute;

    public void onReceive(Context context, Intent intent) {

        // 获得当前日时
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // 将日期，时间转换成字符串
        if (month < 10)
            smonth = "0" + String.valueOf(month);
        else
            smonth = String.valueOf(month);
        if (day < 10)
            sday = "0" + String.valueOf(day);
        else
            sday = String.valueOf(day);
        if (hour < 10)
            shour = "0" + String.valueOf(hour);
        else
            shour = String.valueOf(hour);
        if (minute < 10)
            sminute = "0" + String.valueOf(minute);
        else
            sminute = String.valueOf(minute);

        date = String.valueOf(year) + smonth + sday;
        time = shour + sminute + "00";

        if (intent != null && intent.getExtras() != null) {
            memo = (MR_memory) intent.getExtras()
                    .getSerializable("key_send_event");
        }

		/* 事件处理 */
        // 检索当前是否有待提醒事件
        if (eventList == null || eventList.size() == 0) {

            MR_memory m = new MR_memory();

            // 日期格式转换并赋给m对象
            m.memo_date = date;

            // 时间格式转换并赋给m对象
            m.memo_time = time;

            // 获得资源
            MR_DBAdapter db = new MR_DBAdapter();
            db.open(context.getResources().openRawResource(R.raw.memorandum));

            // 获得待提醒事件列表
            MR_memory[] mary = db.queryEventData(m);

            if (mary != null) {
                for (int j = 0; j < mary.length; j++) {

                    // 将数据库中已有数据赋给eventList
                    eventList.add(mary[j]);
                }
            }
            // 关闭数据库
            db.close();

            // 将第一个建立的事件信息放入事件提醒列表中
            if (memo != null && memo.memo_type == 1) {
                eventList.add(memo);
            }

            // 没有事件则返回
            else {
                return;
            }
        } else {

			/* 检索当前是否有事件要被触发 */
            for (var = 0; var < eventList.size(); var++) {

                if (memo != null && memo.pcd != 0) {

                    // 获取主键与事件提醒列表中有相同时执行待提醒事件更新
                    if (memo.pcd == eventList.get(var).pcd) {

                        // 更新事件
                        eventList.set(var, memo);
                    }

                    // 获取主键与事件提醒列表没有相同时添加一个新待提醒事件
                    else if (var == eventList.size() - 1 && memo.memo_type == 1) {

                        eventList.add(memo);
                    }
                }
            }
            for (var = 0; var < eventList.size(); var++) {
                if (eventList.get(var).memo_date.toString().equals(date)
                        && eventList.get(var).memo_time.equals(time)) {

                    Intent i = new Intent(context, MR_MemorandumNotification.class);

					/* 事件数据封装 */
                    Bundle bundleRet = new Bundle();

                    // 事件id
                    bundleRet.putInt(MR_MemorandumNotification.NOTIFICATION_PCD,
                            eventList.get(var).pcd);

                    // 事件标题
                    bundleRet.putString(
                            MR_MemorandumNotification.NOTIFICATION_TITLE,
                            eventList.get(var).memo_title);

                    // 事件内容
                    bundleRet.putString(
                            MR_MemorandumNotification.NOTIFICATION_CONTENT,
                            eventList.get(var).memo_content);

                    // 事件時間
                    bundleRet.putString(
                            MR_MemorandumNotification.NOTIFICATION_DATE, eventList
                                    .get(var).memo_date);

                    i.putExtras(bundleRet);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // 开始提醒进程
                    context.startActivity(i);
                }
            }
        }
    }
}
