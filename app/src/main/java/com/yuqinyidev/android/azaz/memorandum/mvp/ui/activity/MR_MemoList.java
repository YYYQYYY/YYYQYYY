package com.yuqinyidev.android.azaz.memorandum.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.MR_DBAdapter;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.entity.MR_memory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MR_MemoList extends Activity {

    private static final int DIALOG_CREATE_MEMO = 1;
    private static final int DIALOG_UPDATE_MEMO = 2;
    private static final String BUNDLE_KEY_MEMORY = "memorandum_memory";

    private MR_DBAdapter db;

    private ListView listMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mr_memolist);

        db = new MR_DBAdapter();
        db.open(getResources().openRawResource(R.raw.memorandum));

        listMemo = (ListView) findViewById(R.id.lsvMListMemo);
        listMemo.setAdapter(new MemoListViewAdapter());

        Button btnCreate = (Button) findViewById(R.id.btnMListCreate);
        btnCreate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_CREATE_MEMO);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle bundle) {
        switch (id) {
            case DIALOG_CREATE_MEMO:
                return showCreateMemoDialog();
            case DIALOG_UPDATE_MEMO:
                return showUpdateMemoDialog((MR_memory) bundle
                        .getSerializable(BUNDLE_KEY_MEMORY));
        }
        return null;
    }

    private AlertDialog showCreateMemoDialog() {

        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.mr_newmemo, null);

        final AlertDialog createDialog = new AlertDialog(MR_MemoList.this) {
            protected void onCreate(Bundle savedInstanceState) {
                setView(view, 0, 0, 0, 0);
                super.onCreate(savedInstanceState);
            }
        };

        final Calendar cal;
        cal = Calendar.getInstance();

        final MR_memory memo = new MR_memory();

        Button memoCreate = (Button) view.findViewById(R.id.btnMemoCreate);
        final EditText memoTitle = (EditText) view
                .findViewById(R.id.edtMemoTitle);
        final Button memoDate = (Button) view.findViewById(R.id.btnMemoDate);
        final Button memoTime = (Button) view.findViewById(R.id.btnMemoTime);
        final CheckBox memoImportant = (CheckBox) view
                .findViewById(R.id.chbMemoImportant);
        final CheckBox memoComplete = (CheckBox) view
                .findViewById(R.id.chbMemoCompleted);
        memoComplete.setEnabled(false);
        final EditText memoContent = (EditText) view
                .findViewById(R.id.edtMemoContent);

        memoDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(MR_MemoList.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String yea = String.valueOf(year);
                                String month = null;
                                String day = null;
                                if (monthOfYear < 10) {
                                    month = '0' + String
                                            .valueOf(monthOfYear + 1);
                                } else {
                                    month = String.valueOf(monthOfYear + 1);
                                }
                                if (dayOfMonth < 10) {
                                    day = '0' + String.valueOf(dayOfMonth);
                                } else {
                                    day = String.valueOf(dayOfMonth);
                                }

                                memo.memo_date = yea + month + day;
                                try {
                                    memoDate.setText(new SimpleDateFormat(
                                            "yyyy-MM-dd(EEE)")
                                            .format(new SimpleDateFormat(
                                                    "yyyyMMdd").parse(
                                                    memo.memo_date).getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        memoTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                new TimePickerDialog(MR_MemoList.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                String hour = null;
                                String mimu = null;
                                if (hourOfDay < 10) {
                                    hour = '0' + String.valueOf(hourOfDay);
                                } else {
                                    hour = String.valueOf(hourOfDay);
                                }
                                if (minute < 10) {
                                    mimu = '0' + String.valueOf(minute);
                                } else {
                                    mimu = String.valueOf(minute);
                                }

                                memoTime.setText(hour + ":" + mimu);
                                memo.memo_time = hour + mimu + "00";
                            }
                        }, cal.get(Calendar.HOUR_OF_DAY), cal
                        .get(Calendar.MINUTE), true).show();
            }
        });

        memoCreate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                memo.memo_type = 2;
                memo.memo_title = memoTitle.getText().toString();
                memo.memo_content = memoContent.getText().toString();
                if (memoImportant.isChecked()) {
                    memo.important_flg = 1;
                } else {
                    memo.important_flg = 0;
                }
                db.insert(memo);

                listMemo.setAdapter(new MemoListViewAdapter());
                createDialog.cancel();
            }
        });
        createDialog.show();

        return null;
    }

    private AlertDialog showUpdateMemoDialog(final MR_memory _memo) {
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.mr_updatememo, null);

        final AlertDialog updateDialog = new AlertDialog(MR_MemoList.this) {
            protected void onCreate(Bundle savedInstanceState) {
                setView(layout, 0, 0, 0, 0);
                super.onCreate(savedInstanceState);
            }
        };

        final Calendar cal;
        cal = Calendar.getInstance();

        final MR_memory memo = new MR_memory();

        Button memoUpdate = (Button) layout.findViewById(R.id.btnMemoUpdate);
        Button memoCancle = (Button) layout.findViewById(R.id.btnMemoCancle);
        final EditText memoTitle = (EditText) layout
                .findViewById(R.id.edtMemoTitle);
        final Button memoDate = (Button) layout.findViewById(R.id.btnMemoDate);
        final Button memoTime = (Button) layout.findViewById(R.id.btnMemoTime);
        final CheckBox memoImportant = (CheckBox) layout
                .findViewById(R.id.chbMemoImportant);
        final CheckBox memoComplete = (CheckBox) layout
                .findViewById(R.id.chbMemoCompleted);
        final EditText memoContent = (EditText) layout
                .findViewById(R.id.edtMemoContent);

        if (_memo.memo_title != null) {
            memoTitle.setText(_memo.memo_title);
        }
        if (_memo.memo_date != null) {
            try {
                memoDate.setText(new SimpleDateFormat("yyyy-MM-dd(EEE)")
                        .format(new SimpleDateFormat("yyyyMMdd").parse(
                                _memo.memo_date).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            memo.memo_date = _memo.memo_date;
        }
        if (_memo.memo_time != null) {
            memoTime.setText(_memo.memo_time.substring(0, 2) + ":"
                    + _memo.memo_time.substring(2, 4));
            memo.memo_time = _memo.memo_time;
        }
        if (1 == _memo.important_flg) {
            memoImportant.setChecked(true);
        }
        if (1 == _memo.complete_flg) {
            memoComplete.setChecked(true);
        }
        if (_memo.memo_content != null) {
            memoContent.setText(_memo.memo_content);
        }

        memoDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(MR_MemoList.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String yea = String.valueOf(year);
                                String month = null;
                                String day = null;
                                if (monthOfYear < 10) {
                                    month = '0' + String
                                            .valueOf(monthOfYear + 1);
                                } else {
                                    month = String.valueOf(monthOfYear + 1);
                                }
                                if (dayOfMonth < 10) {
                                    day = '0' + String.valueOf(dayOfMonth);
                                } else {
                                    day = String.valueOf(dayOfMonth);
                                }

                                memo.memo_date = yea + month + day;
                                try {
                                    memoDate.setText(new SimpleDateFormat(
                                            "yyyy-MM-dd(EEE)")
                                            .format(new SimpleDateFormat(
                                                    "yyyyMMdd").parse(
                                                    memo.memo_date).getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        memoTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                new TimePickerDialog(MR_MemoList.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                // ?�u??
                                String hour = null;
                                String mimu = null;
                                if (hourOfDay < 10) {
                                    hour = '0' + String.valueOf(hourOfDay);
                                } else {
                                    hour = String.valueOf(hourOfDay);
                                }
                                if (minute < 10) {
                                    mimu = '0' + String.valueOf(minute);
                                } else {
                                    mimu = String.valueOf(minute);
                                }

                                memoTime.setText(hour + ":" + mimu);
                                memo.memo_time = hour + mimu + "00";
                            }
                        }, cal.get(Calendar.HOUR_OF_DAY), cal
                        .get(Calendar.MINUTE), true).show();
            }
        });

        memoUpdate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                memo.pcd = _memo.pcd;
                memo.memo_type = 2;
                memo.memo_title = memoTitle.getText().toString();
                memo.memo_content = memoContent.getText().toString();
                if (memoImportant.isChecked()) {
                    memo.important_flg = 1;
                } else {
                    memo.important_flg = 0;
                }
                if (memoComplete.isChecked()) {
                    memo.complete_flg = 1;
                } else {
                    memo.complete_flg = 0;
                }
                db.update(memo);
                listMemo.setAdapter(new MemoListViewAdapter());
                updateDialog.cancel();
            }
        });

        memoCancle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                updateDialog.cancel();
            }
        });
        updateDialog.show();

        return null;
    }

    private void showDeleteDialog(final int _deleteAllFlag, final MR_memory _memo) {
        new AlertDialog.Builder(MR_MemoList.this).setTitle(R.string.deleteConfirn)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                if (_deleteAllFlag == 1) {
                                    db.delete(_memo);
                                } else {
                                    db.deleteMemoryOneData(_memo);
                                }
                            }
                        }).setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                }).show();
    }

    private class MemoListViewAdapter extends BaseAdapter {

        View[] itemViews;
        boolean isNotNull = false;

        MR_memory memo = new MR_memory();

        public MemoListViewAdapter() {

            memo.memo_type = 2;
            MR_memory[] memos = db.queryMemoryData(memo);

            if (memos != null) {
                isNotNull = true;
                itemViews = new View[memos.length];

                for (int i = 0; i < itemViews.length; i++) {
                    itemViews[i] = makeItemView(memos[i], i);
                }
            }
        }

        public int getCount() {
            return isNotNull ? itemViews.length : 0;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        private View makeItemView(final MR_memory _memo, int _i) {
            LayoutInflater inflater = (LayoutInflater) MR_MemoList.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // �g�pView�I?��itemView�^R.layout.item??
            final View itemView = inflater.inflate(R.layout.mr_item, null);

            // ��?findViewById()��@?��R.layout.item���e?��
            ImageView image = (ImageView) itemView.findViewById(R.id.itemImage);
            TextView title = (TextView) itemView.findViewById(R.id.itemTitle);

            if (_i % 2 == 1) {
                itemView.setBackgroundColor(Color
                        .parseColor(getString(R.color.lightblue)));
            }

            title.setText(_memo.memo_title);
            title.setTextColor(Color.BLUE);

            if (_memo.complete_flg == 1) {
                image.setImageResource(R.drawable.memo_btn_disabled);
                title.setTextColor(Color.GRAY);
            }
            if (_memo.important_flg == 1) {
                title.setTextColor(Color.RED);
                itemView.setBackgroundColor(Color
                        .parseColor(getString(R.color.lightpink)));
            }

            image.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (_memo.complete_flg == 0) {

                        showAlertDialog(_memo, itemView);
                    }
                }
            });
            title.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (_memo.complete_flg == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(BUNDLE_KEY_MEMORY, _memo);
                        showDialog(DIALOG_UPDATE_MEMO, bundle);
                    }
                }
            });

            title.setOnLongClickListener(new OnLongClickListener() {

                public boolean onLongClick(View v) {
                    final String[] items = {getString(R.string.Delete),
                            getString(R.string.deleteAll)};
                    new AlertDialog.Builder(MR_MemoList.this).setTitle(
                            R.string.select_dialog).setItems(items,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    showDeleteDialog(which, _memo);
                                }
                            }).create().show();
                    return true;
                }

            });
            return itemView;
        }

        private void showAlertDialog(final MR_memory _memo, final View _itemView) {
            new AlertDialog.Builder(MR_MemoList.this).setTitle(
                    R.string.memoAlarttitle).setMessage(
                    R.string.memoAlartContent).setPositiveButton(R.string.yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            ImageView image = (ImageView) _itemView
                                    .findViewById(R.id.itemImage);
                            image
                                    .setImageResource(R.drawable.memo_btn_disabled);
                            TextView title = (TextView) _itemView
                                    .findViewById(R.id.itemTitle);
                            title.setTextColor(Color.GRAY);
                            // TextView text = (TextView)
                            // _itemView.findViewById(R.id.itemText);
                            // text.setTextColor(R.color.grey);

                            // �X�V����?�u completeFlag?1
                            MR_memory memo = new MR_memory();
                            memo.complete_flg = 1;
                            memo.pcd = _memo.pcd;
                            db.update(memo);

                            listMemo.setAdapter(new MemoListViewAdapter());
                        }
                    }).setNegativeButton(R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                if (isNotNull) {
                    view = itemViews[position];
                } else {
                    LayoutInflater inflater = (LayoutInflater) MR_MemoList.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.mr_item, null);
                }
            } else {
                view = convertView;
            }
            return view;
        }
    }
}