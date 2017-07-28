package com.yuqinyidev.android.azaz.kanbook.mvp.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.kanbook.KBConstants;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.KBDBAdapter;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.entity.KBBook;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.entity.KBHistory;
import com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils.KBUtility;
import com.yuqinyidev.android.framework.utils.UiUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class KBMainActivity extends AppCompatActivity {

    private static final int DIALOG_ID_ABOUT = 0;
    private static final int DIALOG_ID_EXIT = 1;

    private static final int REQUEST_CODE_SETTING = 10;
    private static final int REQUEST_CODE_BOOKSHELF = 21;
    private static final int REQUEST_CODE_READTXT = 32;

    private KBDBAdapter mKBDBAdapter;

    private List<View> galleryItems;
    private HistoryListAdapter historyListAdapter;

    private Gallery glyHistory;
    private TextView txvHello;

//    private boolean isClickedExit = false;

//    @Override
//    public void onBackPressed() {
//        if (isClickedExit) {
//            isClickedExit = false;
//            System.exit(0);
//        } else {
//            Toast toast = Toast.makeText(KBMainActivity.this,
//                    KBConstants.EXIT_TOAST_DETAIL, Toast.LENGTH_LONG);
//            DisplayMetrics dm = getResources().getDisplayMetrics();
//            int mScreenHeight = dm.heightPixels;
//            toast.setGravity(0, 0, mScreenHeight / 3);
//            toast.show();
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    isClickedExit = false;
//                }
//            }, 2000);
//            isClickedExit = true;
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String tag = "onCreate";
        Log.d(tag, "create the main activity...");
        UiUtils.fullScreen(KBMainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kb_main);

        KBConstants.init();
        mKBDBAdapter = new KBDBAdapter(this);

        glyHistory = (Gallery) findViewById(R.id.glyHistory);
        galleryItems = getHistoryList();
        historyListAdapter = new HistoryListAdapter(this);
        glyHistory.setAdapter(historyListAdapter);
        glyHistory.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _parent, View _view,
                                    int _position, long _id) {
                if (!KBConstants.SOFT_STATE_NO_BOOK_OPENED.equals(_view.getTag()
                        .toString())) {
                    openHistoryFile((KBHistory) _view.getTag());
                }
            }
        });

        ImageView imgBookShelf = (ImageView) findViewById(R.id.imgBookshelf);
        imgBookShelf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(new Intent(KBMainActivity.this,
//                        KBBookShelfActivity.class), REQUEST_CODE_BOOKSHELF);
                startActivity(new Intent(KBMainActivity.this,
                        KBBookShelfActivity.class));
            }
        });

        TextView txvExit = (TextView) findViewById(R.id.txvExit);
        txvExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_EXIT);
            }
        });

        txvHello = (TextView) findViewById(R.id.txvHello);
        txvHello.setText(sayHello());

        copyFilesFromAssets("/sx.txt", "/sdcard/Download");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.kb_menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_main_setting:
                startActivityForResult(new Intent(this, KBSettingActivity.class),
                        REQUEST_CODE_SETTING);
                return true;
            case R.id.menu_main_about:
                showDialog(DIALOG_ID_ABOUT);
                return true;
            case R.id.menu_main_exit:
                showDialog(DIALOG_ID_EXIT);
                return true;
            default:
                break;
        }
        return false;
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID_ABOUT:
                return KBUtility.buildDialog(this, null, KBConstants.ABOUT_TITLE,
                        KBConstants.ABOUT_DETAIL, getString(R.string.sure));
            case DIALOG_ID_EXIT:
                return KBUtility.buildDialog(this,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        }, KBConstants.EXIT_TITLE, KBConstants.EXIT_DETAIL,
                        getString(R.string.sure), getString(R.string.cancel));
            default:
                return null;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        String tag = "onDestroy";
        Log.d(tag, "destroy the main activity...");
        if (mKBDBAdapter != null) {
            mKBDBAdapter.close();
            mKBDBAdapter = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String tag = "onResume";
        Log.d(tag, "resume the main activity...");
        historyListAdapter.removeAllItems();
        galleryItems = getHistoryList();
        historyListAdapter.notifyDataSetChanged();
        txvHello.setText(sayHello());
        glyHistory.invalidate();
        glyHistory.setSelection(0);
    }

    private List<View> getHistoryList() {
        List<View> items = new ArrayList<View>();
        galleryItems = null;

        List<KBHistory> histories = mKBDBAdapter.queryAllHistory();
        filter(histories);

        if (histories == null || histories.size() == 0) {
            View view = View.inflate(this, R.layout.kb_gallery_item, null);
            ((TextView) view.findViewById(R.id.txvBookName))
                    .setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.txvSaveTime))
                    .setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.txvSummary))
                    .setText(KBConstants.HISTORY_TITLE_SUMMARY_DEFAULT);
            view.setTag(KBConstants.SOFT_STATE_NO_BOOK_OPENED);
            items.add(view);

            return items;
        }

        // get ten book history
        int cnt = histories.size() > 10 ? 10 : histories.size();
        for (int i = 0; i < cnt; i++) {
            View view = View.inflate(this, R.layout.kb_gallery_item, null);
            ((TextView) view.findViewById(R.id.txvBookName))
                    .setText(KBConstants.HISTORY_TITLE_NAME
                            + histories.get(i).getBookName());
            ((TextView) view.findViewById(R.id.txvSaveTime))
                    .setText(KBConstants.HISTORY_TITLE_LAST_OPEN_DT
                            + histories.get(i).getSaveTime());
            ((TextView) view.findViewById(R.id.txvSummary))
                    .setText(KBConstants.HISTORY_TITLE_SUMMARY
                            + histories.get(i).getSummary());
            view.setTag(histories.get(i));
            items.add(view);
        }

        return items;
    }

    private void filter(List<KBHistory> _histories) {
        if (_histories == null || _histories.size() == 0) {
            return;
        }
        // file exists check
        for (KBHistory history : _histories) {
            try {
                KBBook book = mKBDBAdapter.queryBook(history.getBookId());
                if (book == null) {
                    mKBDBAdapter.deleteHistory(history.getHistoryId());
                    _histories.remove(history);
                } else if (!new File(book.getBookPath()).exists()) {
                    mKBDBAdapter.deleteBook(book.getBookPath());
                    mKBDBAdapter.deleteHistory(history.getHistoryId());
                    _histories.remove(history);
                }
            } catch (Exception e) {
                mKBDBAdapter.deleteHistory(history.getHistoryId());
            }
        }
    }

    private void openHistoryFile(KBHistory _history) {
        Intent intent = new Intent(this, KBReadTxtActivity.class);
        intent.putExtra(KBConstants.ACTIVITY_START_KEY,
                KBConstants.ACTIVITY_START_KEY_MAIN);
        intent.putExtra(KBConstants.ACTIVITY_START_KEY_FILE_PATH, _history
                .getBookPath());
        intent.putExtra(KBConstants.ACTIVITY_START_KEY_BOOK_ID, _history
                .getBookId());
        intent.putExtra(KBConstants.ACTIVITY_START_KEY_OFFSET, _history
                .getCurrentOffset());
        startActivityForResult(intent, REQUEST_CODE_READTXT);
    }

    private String sayHello() {
        GregorianCalendar time = new GregorianCalendar();
        int hour = time.get(Calendar.HOUR_OF_DAY);
        if (hour > 18 && hour < 23) {
            return "晚上好！";
        } else if (hour > 16) {
            return "傍晚好！";
        } else if (hour > 12) {
            return "下午好！";
        } else if (hour > 10) {
            return "中午好！";
        } else if (hour > 7) {
            return "上午好！";
        } else if (hour > 5) {
            return "早上好！";
        } else {
            return "深夜了，请注意休息！";
        }
    }

    private class HistoryListAdapter extends BaseAdapter {

        public HistoryListAdapter(Context _context) {
        }

        public void removeAllItems() {
            int cnt = galleryItems == null ? 0 : galleryItems.size();
            if (cnt == 0) {
                return;
            }
            for (int i = 0; i < cnt; i++) {
                galleryItems.remove(0);
            }
        }

        @Override
        public int getCount() {
            return galleryItems.size();
        }

        @Override
        public Object getItem(int _position) {
            return galleryItems.get(_position);
        }

        @Override
        public long getItemId(int _position) {
            return _position;
        }

        @Override
        public View getView(int _position, View _convertView, ViewGroup _parent) {
            return galleryItems.get(_position);
        }
    }

    private void copyFilesFromAssets(String oldPath, String newPath) {
        File f = new File(newPath + oldPath);
        if (f.exists()) {
            return;
        }
        try {
//            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
//            if (fileNames.length > 0) {//如果是目录
//                File file = new File(newPath);
//                file.mkdirs();//如果文件夹不存在，则递归
//                for (String fileName : fileNames) {
//                    copyFilesFromAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
//                }
//            } else {//如果是文件
            File file = new File(newPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            InputStream is = getResources().getAssets().open("sx.txt"); //getAssets().open(oldPath);
            FileOutputStream fos = new FileOutputStream(new File(newPath + oldPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
//            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
//            MainActivity.handler.sendEmptyMessage(COPY_FALSE);
        }
    }

}
