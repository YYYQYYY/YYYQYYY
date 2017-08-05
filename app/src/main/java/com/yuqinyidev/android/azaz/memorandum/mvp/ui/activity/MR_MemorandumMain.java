package com.yuqinyidev.android.azaz.memorandum.mvp.ui.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.MR_DBAdapter;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.entity.MR_holiday;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.entity.MR_memory;
import com.yuqinyidev.android.azaz.memorandum.mvp.model.service.MR_MemorandumReceiver;
import com.yuqinyidev.android.framework.utils.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MR_MemorandumMain extends Activity implements OnGestureListener {
    /**
     * Called when the activity is first created.
     */
    private ViewFlipper flipper;
    private GestureDetector detector;

    private ListView lsvMainEvent;
    private ListView lsvMainMemo;
    private GridView gridview;
    private GridView gridviewtitle;

    private MR_DBAdapter db;

    private Calendar calSelected = Calendar.getInstance();
    private Calendar calDatePicker = Calendar.getInstance();

    private SharedPreferences preference;

    private String strStartDate = null;
    private String strCurrentMonth = null;

    private int iMonthViewCurrentMonth = calSelected.get(Calendar.MONTH);
    private int iMonthViewCurrentYear = calSelected.get(Calendar.YEAR);

    private int mAlpha;
    private int mFirstDayOfWeek;
    private String mNation;

    HashMap<String, MR_holiday> mapholidayCN = new HashMap<String, MR_holiday>();
    HashMap<String, MR_holiday> mapholidayUS = new HashMap<String, MR_holiday>();

    private static final int REQUEST_CODE_MEMO = 0;
    private static final int REQUEST_CODE_EVENT = 1;
    private static final int REQUEST_CODE_OPTION = 2;

    private static final int PRE_VIEW_ITME = 0;
    private static final int CURRENT_VIEW_ITME = 1;
    private static final int NEXT_VIEW_ITME = 2;

    private class ListViewEventAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater inflater;

        private List<View> mlistviewEventItem = new ArrayList<View>();

        public ListViewEventAdapter(Context _context, MR_memory[] _memories) {
            this.mContext = _context;
            inflater = (LayoutInflater) mContext
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            int listCount = 2;
            MR_memory[] memories;
            if (_memories != null) {
                listCount = _memories.length;
                memories = _memories;

                SimpleDateFormat format = new SimpleDateFormat("(EEE) HH:mm ");
                Calendar calendar = Calendar.getInstance();
                for (int i = 0; i < listCount; i++) {
                    View view = inflater.inflate(R.layout.mr_listitemevent, null);
                    TextView listEvent = (TextView) view
                            .findViewById(R.id.listEvents);

                    calendar.set(Calendar.MINUTE, Integer
                            .valueOf(memories[i].memo_time.substring(2, 4)));
                    calendar.set(Calendar.HOUR_OF_DAY, Integer
                            .valueOf(memories[i].memo_time.substring(0, 2)));

                    listEvent.setText(format.format(calendar.getTime())
                            + memories[i].memo_title);
                    if (memories[i].important_flg == 1) {
                        listEvent.setTextColor(Color.RED);
                    }
                    mlistviewEventItem.add(view);
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    View view = inflater.inflate(R.layout.mr_listitemevent, null);
                    TextView listEvent = (TextView) view
                            .findViewById(R.id.listEvents);
                    listEvent.setText("");
                    mlistviewEventItem.add(view);
                }
            }
        }

        public int getCount() {
            return mlistviewEventItem.size();
        }

        public Object getItem(int _position) {
            return mlistviewEventItem.get(_position);
        }

        public long getItemId(int _position) {
            return _position;
        }

        public View getView(int _position, View _convertView, ViewGroup _parent) {
            View view = mlistviewEventItem.get(_position);
            view.getBackground().setAlpha(mAlpha);
            return view;
        }
    }

    private class ListViewMemoAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater inflater;

        private List<View> mlistviewMemoItem = new ArrayList<View>();

        public ListViewMemoAdapter(Context _context, MR_memory[] _memories) {
            this.mContext = _context;
            this.inflater = (LayoutInflater) mContext
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            int listCount = 2;
            MR_memory[] memories;
            if (_memories != null) {
                listCount = _memories.length;
                memories = _memories;
            } else {
                memories = new MR_memory[2];
                memories[0] = new MR_memory();
                memories[1] = new MR_memory();
            }
            for (int i = 0; i < listCount; i++) {
                View view = inflater.inflate(R.layout.mr_listitemmemo, null);
                TextView listMemo = (TextView) view.findViewById(R.id.listMemo);
                listMemo.setText(memories[i].memo_title);
                if (memories[i].important_flg == 1) {
                    listMemo.setTextColor(Color.RED);
                }
                mlistviewMemoItem.add(view);
            }
        }

        public int getCount() {
            return mlistviewMemoItem.size();
        }

        public Object getItem(int _position) {
            return mlistviewMemoItem.get(_position);
        }

        public long getItemId(int _position) {
            return _position;
        }

        public View getView(int _position, View _convertView, ViewGroup _parent) {
            View view = mlistviewMemoItem.get(_position);
            view.getBackground().setAlpha(mAlpha);

            return view;
        }
    }

    private class GridViewAdapter extends BaseAdapter {

        private Context mContext;
        private Integer[] mGridViewItems = {R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem, R.layout.mr_griditem,
                R.layout.mr_griditem, R.layout.mr_griditem};

        private int today;
        private int daysOfThisMonth;
        private int daysOfPreMonth;
        private String theDate;
        LayoutInflater inflater;

        private String sysDt = new SimpleDateFormat("yyyyMMdd").format(Calendar
                .getInstance().getTime());

        public GridViewAdapter(Context _context, int _s, int _thisMonth,
                               int _lastMonth, String _theDate) {
            this.mContext = _context;
            this.today = _s;
            this.daysOfThisMonth = _thisMonth;
            this.daysOfPreMonth = _lastMonth;
            this.theDate = _theDate;

            inflater = (LayoutInflater) mContext
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return mGridViewItems.length;
        }

        public Object getItem(int _position) {
            return mGridViewItems[_position];
        }

        public long getItemId(int _position) {
            return _position;
        }

        public View getView(int _position, View _convertView, ViewGroup _parent) {
            View view;
            if (_convertView == null) {
                view = inflater.inflate(R.layout.mr_griditem, null);
            } else {
                view = _convertView;
            }
            TextView txvMainDay = (TextView) view.findViewById(R.id.txvMainDay);
            TextView txvMainEventCount = (TextView) view
                    .findViewById(R.id.txvMainEventCount);
            TextView txvMainDisplay1 = (TextView) view
                    .findViewById(R.id.txvMainDisplay1);
            TextView txvMainDisplay2 = (TextView) view
                    .findViewById(R.id.txvMainDisplay2);
            TextView txvThisDate = (TextView) view
                    .findViewById(R.id.txvThisDate);
            String strYM = theDate.substring(0, 6);
            if (_position < today) {
                int dayOfMonth = daysOfPreMonth - today + 1 + _position;
                txvMainDay.setText(String.valueOf(dayOfMonth));
                txvMainEventCount.setText("");
                txvMainEventCount.setVisibility(View.INVISIBLE);
                txvMainDisplay1.setText("");
                txvMainDisplay2.setText("");
                txvThisDate.setText("" + (Integer.parseInt(strYM) - 1)
                        + paddingZero(dayOfMonth));
                view.setBackgroundResource(R.color.grid_oth);
            } else if (_position < daysOfThisMonth + today) {
                int dayOfMonth = _position - today + 1;
                txvMainDay.setText(String.valueOf(dayOfMonth));

                List<String> info = getDisplayInfo(strYM
                        .concat(paddingZero(dayOfMonth)));
                if (info.get(0) != null) {
                    txvMainEventCount.setText(info.get(0));
                    txvMainEventCount.setVisibility(View.VISIBLE);
                } else {
                    txvMainEventCount.setText("");
                    txvMainEventCount.setVisibility(View.INVISIBLE);
                }
                if (info.get(1) != null && "".equals(info.get(1))) {
                    txvMainDisplay1.setTextColor(getResources().getColor(R.color.grid_holiday));
                } else {
                    txvMainDisplay1.setTextColor(getResources().getColor(R.color.grid_event));
                }
                txvMainDisplay1.setText(info.get(1));
                txvMainDisplay2.setText(info.get(2));
                txvThisDate.setText(strYM + paddingZero(dayOfMonth));
                switch (mFirstDayOfWeek) {
                    case MR_OptionMain.FIRST_DAY_SUN:
                        if ((dayOfMonth + today) % 7 == 0) {
                            view.setBackgroundResource(R.color.grid_sta);
                        } else if ((dayOfMonth + today) % 7 == 1) {
                            view.setBackgroundResource(R.color.grid_sun);
                        } else {
                            view.setBackgroundResource(R.color.grid_mon);
                        }
                        break;
                    case MR_OptionMain.FIRST_DAY_MON:
                        if ((dayOfMonth + today) % 7 == 0) {
                            view.setBackgroundResource(R.color.grid_sun);
                        } else if ((dayOfMonth + today) % 7 == 6) {
                            view.setBackgroundResource(R.color.grid_sta);
                        } else {
                            view.setBackgroundResource(R.color.grid_mon);
                        }
                        break;
                }
                // システム日の場合
                if (sysDt.compareTo(strYM + paddingZero(dayOfMonth)) == 0) {
                    view.setBackgroundResource(R.color.grid_today);
                }
            } else {
                int dayOfMonth = _position - daysOfThisMonth - today + 1;
                txvMainDay.setText(String.valueOf(dayOfMonth));
                txvMainEventCount.setText("");
                txvMainEventCount.setVisibility(View.INVISIBLE);
                txvMainDisplay1.setText("");
                txvMainDisplay2.setText("");
                txvThisDate.setText("" + (Integer.parseInt(strYM) + 1)
                        + paddingZero(dayOfMonth));
                view.setBackgroundResource(R.color.grid_oth);
            }
            view.getBackground().setAlpha(mAlpha);

            return view;
        }
    }

    private class GridViewTitleAdapter extends BaseAdapter {

        private Context mContext;
        private View[] mGridViewItems = new View[7];

        private String[] mGridViewStrings = {getString(R.string.Mon),
                getString(R.string.Tue), getString(R.string.Wed),
                getString(R.string.Thu), getString(R.string.Fri)};
        private LayoutInflater inflater;

        public GridViewTitleAdapter(Context _context, int _s, int _thisMonth,
                                    int _lastMonth, String _theDate) {
            this.mContext = _context;

            inflater = (LayoutInflater) mContext
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            int iWeek = 0;
            for (int i = 0; i < 7; i++) {
                View view = inflater.inflate(R.layout.mr_gridtitleitem, null);
                switch (mFirstDayOfWeek) {
                    case MR_OptionMain.FIRST_DAY_SUN:
                        if (i % 7 == 6) {
                            view.setBackgroundResource(R.color.grid_sta);
                            ((TextView) view.findViewById(R.id.txvWeek))
                                    .setText(getString(R.string.Sta));
                        } else if (i % 7 == 0) {
                            view.setBackgroundResource(R.color.grid_sun);
                            ((TextView) view.findViewById(R.id.txvWeek))
                                    .setText(getString(R.string.Sun));
                        } else {
                            view.setBackgroundResource(R.color.grid_mon);
                            ((TextView) view.findViewById(R.id.txvWeek))
                                    .setText(mGridViewStrings[iWeek++]);
                        }
                        break;
                    case MR_OptionMain.FIRST_DAY_MON:
                        if (i % 7 == 6) {
                            view.setBackgroundResource(R.color.grid_sun);
                            ((TextView) view.findViewById(R.id.txvWeek))
                                    .setText(getString(R.string.Sun));
                        } else if (i % 7 == 5) {
                            view.setBackgroundResource(R.color.grid_sta);
                            ((TextView) view.findViewById(R.id.txvWeek))
                                    .setText(getString(R.string.Sta));
                        } else {
                            view.setBackgroundResource(R.color.grid_mon);
                            ((TextView) view.findViewById(R.id.txvWeek))
                                    .setText(mGridViewStrings[iWeek++]);
                        }
                        break;
                }
                mGridViewItems[i] = view;
            }
        }

        public int getCount() {
            return mGridViewItems.length;
        }

        public Object getItem(int _position) {
            return mGridViewItems[_position];
        }

        public long getItemId(int _position) {
            return _position;
        }

        public View getView(int _position, View _convertView, ViewGroup _parent) {
            View view = mGridViewItems[_position];
            view.getBackground().setAlpha(mAlpha);
            return view;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mr_main);
        StatusBarUtil.setTransparent(MR_MemorandumMain.this);

        preference = getSharedPreferences(MR_OptionMain.PROFERENCE_NAME,
                Context.MODE_PRIVATE);

        initStatus();

        gridview = (GridView) findViewById(R.id.grvMainCalendar);
        gridviewtitle = (GridView) findViewById(R.id.grvMainTitle);
        lsvMainEvent = (ListView) findViewById(R.id.lsvMainEvent);
        lsvMainMemo = (ListView) findViewById(R.id.lsvMainMemo);

        detector = new GestureDetector(this);
        flipper = (ViewFlipper) this.findViewById(R.id.viewFlipper1);

        db = new MR_DBAdapter();
        db.open(getResources().openRawResource(R.raw.memorandum));

        getHolidayInfo();

        Intent intent = getIntent();
        if (intent != null) {
            String strDate = intent
                    .getStringExtra(MR_MemorandumNotification.NOTIFICATION_EVENT_DATE);
            if (strDate != null) {
                setTodayViewItem(strDate);
                showEventListDiolog(strDate);
            } else {
                // Gridview事件监听
                strStartDate = new SimpleDateFormat("yyyyMMdd").format(Calendar
                        .getInstance().getTime());
                setTodayViewItem(strStartDate);
            }
        } else {
            // Gridview事件监听
            strStartDate = new SimpleDateFormat("yyyyMMdd").format(Calendar
                    .getInstance().getTime());
            setTodayViewItem(strStartDate);
        }

        listviewEventItem();
        listviewMemoItem();

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> _parent, View _view,
                                    int _position, long _id) {
                String clickdate = ((TextView) _view
                        .findViewById(R.id.txvThisDate)).getText().toString();
                int compareValue = strCurrentMonth.compareTo(clickdate
                        .substring(0, 6));
                if (compareValue == 0) {
                    showEventListDiolog(clickdate);
                } else if (compareValue < 0) {
                    setGridViewItem(NEXT_VIEW_ITME);
                } else {
                    setGridViewItem(PRE_VIEW_ITME);
                }
            }
        });

        gridview.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MR_MemorandumMain.this.detector.onTouchEvent(event);
                return false;
            }
        });

        lsvMainMemo.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MR_MemorandumMain.this.detector.onTouchEvent(event);
                return false;
            }
        });

        lsvMainEvent.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MR_MemorandumMain.this.detector.onTouchEvent(event);
                return false;
            }
        });

        lsvMainMemo
                .setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        showMemoListDiolog();
                    }
                });

        // 画面滑动事件监听
        flipper.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                switch (v.getId()) {
                    case 1:
                        break;
                    default:
                        break;
                }
            }
        });
        runReceiver();
    }

    /*
     * 画面状態初期化
     */
    private void initStatus() {
        mFirstDayOfWeek = preference.getInt(MR_OptionMain.OPTION_KEY_FIRSTDAY,
                MR_OptionMain.FIRST_DAY_SUN);
        mAlpha = preference.getInt(MR_OptionMain.OPTION_KEY_BG_TRANSPARENCY,
                MR_OptionMain.OPTION_VALUE_BG_TRANS_DEFAULT);
        mNation = preference.getString(MR_OptionMain.OPTION_KEY_HOLIDAY,
                getString(R.string.china));
        setBackground(preference.getBoolean(MR_OptionMain.OPTION_KEY_BG_ENABLE,
                false));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_OPTION:
                switch (resultCode) {
                    case MR_OptionMain.RESULT_BACKGROUND:
                        boolean isSetBackground = data.getBooleanExtra(
                                MR_OptionMain.OPTION_KEY_BG_ENABLE, false);
                        setBackground(isSetBackground);
                        mAlpha = data.getIntExtra(
                                MR_OptionMain.OPTION_KEY_BG_TRANSPARENCY,
                                MR_OptionMain.OPTION_VALUE_BG_TRANS_DEFAULT);
                        setAlpha();
                        break;
                    case MR_OptionMain.RESULT_HOLIDAY:
                        mNation = data.getStringExtra(MR_OptionMain.OPTION_KEY_HOLIDAY);
                        setGridViewItem(CURRENT_VIEW_ITME);
                        break;
                    case MR_OptionMain.RESULT_FIRST_DAY:
                        mFirstDayOfWeek = data.getIntExtra(
                                MR_OptionMain.OPTION_KEY_FIRSTDAY,
                                MR_OptionMain.FIRST_DAY_SUN);
                        setGridViewItem(CURRENT_VIEW_ITME);
                        break;
                }
                break;
            case REQUEST_CODE_MEMO:
                listviewMemoItem();
                break;
            case REQUEST_CODE_EVENT:
                setGridViewItem(CURRENT_VIEW_ITME);
                listviewEventItem();
                break;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void getHolidayInfo() {
        MR_holiday[] holidays = db.queryHolidayData(null);
        if (holidays == null) {
            return;
        }
        for (MR_holiday holiday : holidays) {
            if (getString(R.string.china).toLowerCase().equals(holiday.nation)) {
                mapholidayCN.put(holiday.holiday_date, holiday);
            } else {
                mapholidayUS.put(holiday.holiday_date, holiday);
            }
        }
    }

    private void runReceiver() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 1);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long startTime = c.getTimeInMillis();
        long repeatTime = 60 * 1000;
        Intent intent = new Intent(MR_MemorandumMain.this,
                MR_MemorandumReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(MR_MemorandumMain.this,
                0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, startTime, repeatTime, sender);
    }

    private void listviewEventItem() {
        lsvMainEvent = (ListView) findViewById(R.id.lsvMainEvent);
        MR_memory[] memories = getEventList();
        if (memories != null && memories.length > 4) {
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) lsvMainEvent
                    .getLayoutParams();
            linearParams.height = formatDipToPx(lsvMainEvent.getContext(), 100);
            lsvMainEvent.setLayoutParams(linearParams);
        }
        lsvMainEvent.setAdapter(new ListViewEventAdapter(MR_MemorandumMain.this,
                memories));
        return;
    }

    private MR_memory[] getEventList() {
        MR_memory memory = new MR_memory();
        Calendar calendar = Calendar.getInstance();
        memory.memo_date = new SimpleDateFormat("yyyyMMdd").format(calendar
                .getTime());
        memory.memo_type = 1;
        MR_memory[] result = db.queryMemoryData(memory);
        return result;
    }

    private void listviewMemoItem() {
        lsvMainMemo = (ListView) findViewById(R.id.lsvMainMemo);
        MR_memory[] memories = getMemoList();
        lsvMainMemo.setAdapter(new ListViewMemoAdapter(MR_MemorandumMain.this,
                memories));
        return;
    }

    private MR_memory[] getMemoList() {
        MR_memory[] result = db.queryMemoryData4MainList();
        return result;
    }

    // 取得Gridview对象
    private void gridviewItem(int S, int ThisMonth, int LastMonth,
                              String theDate) {
        strCurrentMonth = theDate.substring(0, 6);
        gridviewtitle.setAdapter(new GridViewTitleAdapter(MR_MemorandumMain.this,
                S, ThisMonth, LastMonth, theDate));
        gridview.setAdapter(new GridViewAdapter(MR_MemorandumMain.this, S,
                ThisMonth, LastMonth, theDate));
    }

    private String paddingZero(int _num) {
        return _num < 10 ? "0" + _num : String.valueOf(_num);
    }

    private void setBackground(boolean _enabledBg) {
        View view = this.findViewById(R.id.layMain);
        Drawable drawable = Drawable.createFromPath(MR_OptionMain.IMAGE_SAVE_PATH);
        if (_enabledBg && drawable != null) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackgroundResource(R.drawable.default_bg);
        }
    }

    private void setAlpha() {
        ((TextView) findViewById(R.id.txvMainTitleEvent)).getBackground()
                .setAlpha(mAlpha);
        ((TextView) findViewById(R.id.txvMainTitleMemo)).getBackground()
                .setAlpha(mAlpha);
        setGridViewItem(CURRENT_VIEW_ITME);
        listviewEventItem();
        listviewMemoItem();
    }

    // OptionsMenu创建
    public boolean onCreateOptionsMenu(Menu menu) {
        // 添加冁E��
        menu.add(0, 0, 0, "Memo");
        menu.add(0, 1, 1, "This Month");
        menu.add(0, 2, 2, "Options");
        return true;
    }

    // OptionsMenu事件监听
    public boolean onOptionsItemSelected(MenuItem item) {
        // 得到当前选中的MenuItem的ID
        int item_id = item.getItemId();
        // 新建一个Intent对象
        Intent intent = new Intent();
        switch (item_id) {
            case 0:
                showMemoListDiolog();
                break;
            case 1:
                setTodayViewItem(strStartDate);
                break;
            case 2:
                intent.setClass(MR_MemorandumMain.this, MR_OptionMain.class);
                // 启动一个新的Activity
                startActivityForResult(intent, REQUEST_CODE_OPTION);
                break;
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > 120) {
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_out));
            setGridViewItem(NEXT_VIEW_ITME);
            this.flipper.showNext();
            return true;
        } else if (e1.getX() - e2.getX() < -120) {
            this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_out));
            setGridViewItem(PRE_VIEW_ITME);
            this.flipper.showPrevious();
            return true;
        }
        return false;
    }

    public void onLongPress(MotionEvent e) {
        if (e.getRawY() > 80 && e.getRawY() < 130F) {
            calDatePicker = Calendar.getInstance();
            new DatePickerDialog(MR_MemorandumMain.this,
                    new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            calSelected.set(year, monthOfYear, dayOfMonth);
                            iMonthViewCurrentMonth = monthOfYear;
                            iMonthViewCurrentYear = year;
                            setGridViewItem(CURRENT_VIEW_ITME);
                        }
                    }, calDatePicker.get(Calendar.YEAR), calDatePicker
                    .get(Calendar.MONTH), calDatePicker
                    .get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    private List<String> getDisplayInfo(String _theDate) {
        List<String> result = new ArrayList<String>();

        // dbから
        MR_memory memory = new MR_memory();
        memory.memo_date = _theDate;
        memory.memo_type = 1;
        MR_memory[] memoAry = db.queryMemoryData(memory);

        // EventCount
        if (memoAry != null) {
            result.add(String.valueOf(memoAry.length));
        } else {
            result.add(null);
        }
        if (getString(R.string.china).equals(mNation)) {
            if (mapholidayCN.get(_theDate) != null) {
                // Display1
                result.add(((MR_holiday) mapholidayCN.get(_theDate)).holiday_name);

                // Display2
                if (memoAry != null) {
                    result.add(memoAry[0].memo_title);
                } else {
                    result.add("");
                }
            } else {
                // Display1
                if (memoAry != null) {
                    result.add(memoAry[0].memo_title);
                } else {
                    result.add("");
                }

                // Display2
                if (memoAry != null && memoAry.length > 1) {
                    result.add(memoAry[1].memo_title);
                } else {
                    result.add("");
                }
            }
        } else if (getString(R.string.america).equals(mNation)) {
            if (mapholidayUS.get(_theDate) != null) {
                // Display1
                result.add(((MR_holiday) mapholidayUS.get(_theDate)).holiday_name);

                // Display2
                if (memoAry != null) {
                    result.add(memoAry[0].memo_title);
                } else {
                    result.add("");
                }
            } else {
                // Display1
                if (memoAry != null) {
                    result.add(memoAry[0].memo_title);
                } else {
                    result.add("");
                }

                // Display2
                if (memoAry != null && memoAry.length > 1) {
                    result.add(memoAry[1].memo_title);
                } else {
                    result.add("");
                }
            }
        }
        return result;
    }

    private void setGridViewItem(int _flg) {
        if (_flg == PRE_VIEW_ITME) {
            iMonthViewCurrentMonth--;
            if (iMonthViewCurrentMonth == -1) {
                iMonthViewCurrentMonth = 11;
                iMonthViewCurrentYear--;
            }
        } else if (_flg == NEXT_VIEW_ITME) {
            iMonthViewCurrentMonth++;
            if (iMonthViewCurrentMonth == 12) {
                iMonthViewCurrentMonth = 0;
                iMonthViewCurrentYear++;
            }
        }
        calSelected.set(Calendar.YEAR, iMonthViewCurrentYear);

        calSelected.set(Calendar.MONTH, iMonthViewCurrentMonth - 1);
        int LastMonth = calSelected.getActualMaximum(Calendar.DAY_OF_MONTH);

        calSelected.set(Calendar.MONTH, iMonthViewCurrentMonth);
        int ThisMonth = calSelected.getActualMaximum(Calendar.DAY_OF_MONTH);

        calSelected.set(Calendar.DAY_OF_MONTH, 1);
        int S = calSelected.get(Calendar.DAY_OF_WEEK) - 1;
        if (mFirstDayOfWeek == MR_OptionMain.FIRST_DAY_MON) {
            S--;
        }

        ((TextView) findViewById(R.id.txvMainTitle1))
                .setText(new SimpleDateFormat("MMMM").format(calSelected
                        .getTime()));
        ((TextView) findViewById(R.id.txvMainTitle2))
                .setText(new SimpleDateFormat("yyyy").format(calSelected
                        .getTime()));

        gridviewItem(S, ThisMonth, LastMonth, new SimpleDateFormat("yyyyMMdd")
                .format(calSelected.getTime()));
    }

    private void setTodayViewItem(String _strStartDate) {
        Calendar calStarted = Calendar.getInstance();

        iMonthViewCurrentYear = Integer.valueOf(_strStartDate.substring(0, 4));
        iMonthViewCurrentMonth = Integer.valueOf(_strStartDate.substring(4, 6)) - 1;

        calStarted.set(iMonthViewCurrentYear, iMonthViewCurrentMonth, Integer
                .valueOf(_strStartDate.substring(6)));

        calStarted.set(Calendar.DAY_OF_MONTH, 1);
        int S = calStarted.get(Calendar.DAY_OF_WEEK) - 1;
        if (mFirstDayOfWeek == MR_OptionMain.FIRST_DAY_MON) {
            S--;
        }

        ((TextView) findViewById(R.id.txvMainTitle1))
                .setText(new SimpleDateFormat("MMMM").format(calStarted
                        .getTime()));
        ((TextView) findViewById(R.id.txvMainTitle2))
                .setText(new SimpleDateFormat("yyyy").format(calStarted
                        .getTime()));

        int ThisMonth = calStarted.getActualMaximum(Calendar.DAY_OF_MONTH);
        calStarted.add(Calendar.MONTH, -1);
        int LastMonth = calStarted.getActualMaximum(Calendar.DAY_OF_MONTH);

        gridviewItem(S, ThisMonth, LastMonth, _strStartDate);
    }

    private void showMemoListDiolog() {
        Intent intent = new Intent();
        intent.setClass(MR_MemorandumMain.this, MR_MemoList.class);
        startActivityForResult(intent, REQUEST_CODE_MEMO);
    }

    private void showEventListDiolog(String _dateTime) {
        Intent intent = new Intent();
        intent.putExtra(MR_EventList.EVENT_DATE_TIME, _dateTime);
        intent.setClass(MR_MemorandumMain.this, MR_EventList.class);
        startActivityForResult(intent, REQUEST_CODE_EVENT);
    }

    private static int formatDipToPx(Context context, int dip) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(
                dm);
        return (int) Math.ceil(dip * dm.density);
    }
}