package com.yuqinyidev.android.azaz.kanbook.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.kanbook.KBConstants;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.KBDBAdapter;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.entity.KBBookMark;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.entity.KBHistory;
import com.yuqinyidev.android.azaz.kanbook.mvp.ui.customviews.KBTextProgressBar;
import com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils.KBTxtStringReader;
import com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils.KBUtility;
import com.yuqinyidev.android.framework.utils.DateUtils;
import com.yuqinyidev.android.framework.utils.UiUtils;

import java.util.Date;
import java.util.List;

public class KBReadTxtStringActivity extends Activity {

    private static final int DIALOG_ID_ABOUT = 0;
    private static final int DIALOG_ID_SAVE_BOOK_MARK_SUCCESS = 11;
    private static final int DIALOG_ID_SAVE_BOOK_MARK_FAIL = 12;

    private static final int REQUEST_CODE_SKIP = 0;
    private static final int REQUEST_CODE_SETTING = 1;
    private static final int REQUEST_CODE_BRIGHTNESS = 2;

    private final Handler mHandlerFling = new Handler();
    private boolean isFling = false;

    private GestureDetector mDetector = new GestureDetector(new OnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isFling) {
                return false;
            }
//                    int offsetLines = mTxtReader.getLinesOfOneScreen();
            if (e.getRawY() < (mVisibleHeight * 1 / 3)) {
                mTxtReader.readPrePage();
                showPercent();
//                mScvContent.scrollTo(0, 0);
                return true;
            } else if (e.getRawY() > (mVisibleHeight * 2 / 3)) {
                mTxtReader.readNextPage();
                showPercent();
                return true;
            } else {
                openOptionsMenu();
            }
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(final MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            isFling = true;
            int distance = (int) (e1.getRawY() - e2.getRawY());
            mFlingLines = distance / mTxtReader.getTextSize() * 2;
            mHandlerFling.postDelayed(mFling, 50);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            isFling = false;
            return false;
        }
    });

    private int mFlingLines = 0;
    private int mRunLine = 0;

    private Runnable mFling = new Runnable() {
        @Override
        public void run() {
            if (mTxtReader == null) {
                return;
            }
            if (mFlingLines < 0) {
//                mTxtReader.displayPreToScreen(1);
                showPercent();
//                mScvContent.scrollTo(0, 0);
            } else if (mFlingLines > 0) {
//                mTxtReader.displayNextToScreen(1);
                showPercent();
            }
            if (mRunLine++ < Math.abs(mFlingLines)) {
                mHandlerFling.postDelayed(this, 50);
            } else {
                mHandlerFling.removeCallbacks(this);
                mRunLine = 0;
                showPercent();
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                int status = intent.getIntExtra("status", 0);
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 0);

                String statusString = "电量：" + (level * 100 / scale) + "%";

                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        statusString = "充电中..." + (level * 100 / scale) + "%";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        statusString = "100%";
                        break;
                }
                mTxvBatteryStatus.setText(statusString);
            }
        }
    };

    private SharedPreferences mPreference;
    private KBDBAdapter mKBDBAdapter;
    private KBTxtStringReader mTxtReader;

    private PopupWindow mPopupWindow;
    private LinearLayout mLayContent;
    private LinearLayout mLayFooter;
    //    private ScrollView mScvContent;
    private TextView mTxvContent;
    private KBTextProgressBar mTpbPercent;
    private TextView mTxvBatteryStatus;
    private TextView mTxvFileName;
    private DigitalClock mDcTime;

    private int mVisibleHeight;

    private int mLastPercent = 0;
    private static int mOffset = 0;

    private List<KBBookMark> mBookMarkList = null;

    private KBBookMark mBookMark = null;
    private int bmLocation = 0;
    private String mFilePath = null;
    private int mBookId = 0;

    //    private int mLinesOfScreen;
    private boolean mIsNightMode;

    private OnKeyListener mUpOrDown = new OnKeyListener() {

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (0 == mTxtReader.getFileLength()) {
                return false;
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//                mScvContent.scrollTo(0, mTxtReader.getTextSize());
//                if (null != mTxtReader) {
//                    mTxtReader.displayNextToScreen(1);
//                }
                showPercent();
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//                mScvContent.scrollTo(0, 0);
//                if (null != mTxtReader) {
//                    mTxtReader.displayPreToScreen(1);
//                }
                showPercent();
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (null != mTxtReader) {
                    mTxtReader.readPrePage();
                }
                showPercent();
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (null != mTxtReader) {
                    mTxtReader.readNextPage();
                }
                showPercent();
                return true;
            }
            return false;
        }
    };

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.ll_menu_reader_skip:
                    mPopupWindow.dismiss();
                    intent = new Intent(KBReadTxtStringActivity.this, KBVirtualDialogActivity.class);
                    intent.putExtra(KBConstants.VIRUAL_DIALOG_START, KBConstants.ACTIVITY_START_KEY_SKIP);
                    intent.putExtra(KBConstants.VIRUAL_DIALOG_PERCENT, mTxtReader.getPercent());
                    startActivityForResult(intent, REQUEST_CODE_SKIP);
                    break;
                case R.id.ll_menu_reader_brightness:
                    mPopupWindow.dismiss();
                    // 取得当前亮度
                    float normal = mPreference.getFloat(KBConstants.PREF_KEY_IS_BRIGHTNESS, 0.5F);
                    boolean usingSystemBrightness = mPreference.getBoolean(KBConstants.PREF_KEY_USING_SYSTEM_BRIGHTNESS, false);
                    intent = new Intent(KBReadTxtStringActivity.this, KBVirtualDialogActivity.class);
                    intent.putExtra(KBConstants.VIRUAL_DIALOG_START, KBConstants.ACTIVITY_START_KEY_BRIGHTNESS);
                    intent.putExtra(KBConstants.VIRUAL_DIALOG_BRIGHTNESS, normal);
                    intent.putExtra(KBConstants.VIRUAL_DIALOG_USING_SYSTEM_BRIGHTNESS, usingSystemBrightness);
                    startActivityForResult(intent, REQUEST_CODE_BRIGHTNESS);
                    break;
                case R.id.ll_menu_reader_night_mode:
                    mPopupWindow.dismiss();
                    mIsNightMode = mPreference.getBoolean(KBConstants.PREF_KEY_IS_NIGHTMODE, false);
                    setNightMode(mIsNightMode);
                    KBUtility.putShare(mPreference, KBConstants.PREF_KEY_IS_NIGHTMODE, !mIsNightMode);
                    break;
                case R.id.ll_menu_reader_save_bookmark:
                    mPopupWindow.dismiss();
                    saveBookMarkDialog();
                    break;
                case R.id.ll_menu_reader_view_bookmark:
                    mPopupWindow.dismiss();
                    bookMarkView();
                    break;
                case R.id.ll_menu_reader_setting:
                    mPopupWindow.dismiss();
                    intent = new Intent(getApplicationContext(), KBSettingActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SETTING);
                    break;
                case R.id.ll_menu_reader_backward:
                    mPopupWindow.dismiss();
                    finish();
                    break;
                case R.id.bt_cancel:
                    mPopupWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void showPopMenu() {
        View view = View.inflate(getApplicationContext(), R.layout.kb_menu_reader, null);
        LinearLayout ll_menu_reader_skip = (LinearLayout) view.findViewById(R.id.ll_menu_reader_skip);
        LinearLayout ll_menu_reader_brightness = (LinearLayout) view.findViewById(R.id.ll_menu_reader_brightness);
        LinearLayout ll_menu_reader_night_mode = (LinearLayout) view.findViewById(R.id.ll_menu_reader_night_mode);
        LinearLayout ll_menu_reader_save_bookmark = (LinearLayout) view.findViewById(R.id.ll_menu_reader_save_bookmark);
        LinearLayout ll_menu_reader_view_bookmark = (LinearLayout) view.findViewById(R.id.ll_menu_reader_view_bookmark);
        LinearLayout ll_menu_reader_setting = (LinearLayout) view.findViewById(R.id.ll_menu_reader_setting);
        LinearLayout ll_menu_reader_backward = (LinearLayout) view.findViewById(R.id.ll_menu_reader_backward);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        ll_menu_reader_skip.setOnClickListener(mOnClickListener);
        ll_menu_reader_brightness.setOnClickListener(mOnClickListener);
        ll_menu_reader_night_mode.setOnClickListener(mOnClickListener);
        ll_menu_reader_save_bookmark.setOnClickListener(mOnClickListener);
        ll_menu_reader_view_bookmark.setOnClickListener(mOnClickListener);
        ll_menu_reader_setting.setOnClickListener(mOnClickListener);
        ll_menu_reader_backward.setOnClickListener(mOnClickListener);
        bt_cancel.setOnClickListener(mOnClickListener);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.gl_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_bottom_in));

        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(this);
            mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(LayoutParams.MATCH_PARENT);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
        }

        mPopupWindow.setContentView(view);
        mPopupWindow.showAtLocation(mTxvBatteryStatus, Gravity.BOTTOM, 0, 0);
        mPopupWindow.update();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String tag = "onCreate";
        Log.d(tag, "create the read text activity...");
        UiUtils.fullScreen(KBReadTxtStringActivity.this);
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

//        List<String> permissions = new ArrayList<>();
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
//            permissions.add(Manifest.permission.WRITE_SETTINGS);
//        }
//        if (!permissions.isEmpty()) {
//            String[] p = permissions.toArray(new String[permissions.size()]);
//            ActivityCompat.requestPermissions(this, p, 1);
//        } else {
//        setScreenBrightness();
//        }

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        mBookId = intent.getIntExtra(KBConstants.ACTIVITY_START_KEY_BOOK_ID, 0);
        if (mBookId == 0) {
            return;
        }

        mFilePath = intent.getStringExtra(KBConstants.ACTIVITY_START_KEY_FILE_PATH);
        if (TextUtils.isEmpty(mFilePath)) {
            finish();
            return;
        }
        setContentView(R.layout.kb_string_reader);

        mKBDBAdapter = new KBDBAdapter(this);
        mPreference = getSharedPreferences(KBConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);

        mLayContent = (LinearLayout) findViewById(R.id.llayContent);
//        mScvContent = (ScrollView) findViewById(R.id.scvContent);
        mTxvContent = (TextView) findViewById(R.id.textContent);
        mLayFooter = (LinearLayout) findViewById(R.id.llayFooter);
        mTpbPercent = (KBTextProgressBar) findViewById(R.id.tpbPercent);
        mTxvBatteryStatus = (TextView) findViewById(R.id.mTxvBatteryStatus);
        mTxvFileName = (TextView) findViewById(R.id.txvFileName);
        mDcTime = (DigitalClock) findViewById(R.id.dcTime);

        loadData();
        if (savedInstanceState != null) {
            mOffset = savedInstanceState.getInt(KBConstants.SAVED_STATE_OFFSET);
        } else if (KBConstants.ACTIVITY_START_KEY_MAIN.equals(intent.getStringExtra(KBConstants.ACTIVITY_START_KEY))) {
            mOffset = intent.getIntExtra(KBConstants.ACTIVITY_START_KEY_OFFSET, 0);
        } else {
            mOffset = 0;
        }
        mTxtReader.read(mOffset);
        showPercent(mTxtReader.getPercentWithOffset(mOffset));
        mTxvFileName.setText(KBUtility.getBookName(mFilePath));

        setNightMode(!(mIsNightMode = mPreference.getBoolean(KBConstants.PREF_KEY_IS_NIGHTMODE, false)));
        setScreenBrightness();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0) {
//                    for (int grantResult : grantResults) {
//                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
//                            Toast.makeText(this, "你没有权限", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    }
//                    setScreenBrightness();
//                } else {
//                    Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    public void openOptionsMenu() {
        showPopMenu();
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.kb_menu_reader, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        Intent intent;
//        switch (id) {
//            case R.id.menu_reader_skip:
//                intent = new Intent(KBReadTxtStringActivity.this, KBVirtualDialogActivity.class);
//                intent.putExtra(KBConstants.VIRUAL_DIALOG_START, KBConstants.ACTIVITY_START_KEY_SKIP);
//                intent.putExtra(KBConstants.VIRUAL_DIALOG_PERCENT, mTxtReader.getPercent());
//                startActivityForResult(intent, REQUEST_CODE_SKIP);
//                return true;
//            case R.id.menu_reader_save_bookmark:
//                saveBookMarkDialog();
//                return true;
//            case R.id.menu_reader_view_bookmark:
//                bookMarkView();
//                return true;
//            case R.id.menu_reader_nightmode:
//                mIsNightMode = mPreference.getBoolean(KBConstants.PREF_KEY_IS_NIGHTMODE, false);
//                setNightMode(mIsNightMode);
//                KBUtility.putShare(mPreference, KBConstants.PREF_KEY_IS_NIGHTMODE, !mIsNightMode);
//                return true;
//            case R.id.menu_reader_setting:
//                intent = new Intent(getApplicationContext(), KBSettingActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_SETTING);
//                return true;
//            case R.id.menu_reader_brightness:
//                // 取得当前亮度
//                int normal = Settings.System.getInt(getContentResolver(),
//                        Settings.System.SCREEN_BRIGHTNESS, 10);
//                boolean usingSystemBrightness = mPreference.getBoolean(
//                        KBConstants.PREF_KEY_USING_SYSTEM_BRIGHTNESS, false);
//                intent = new Intent(KBReadTxtStringActivity.this, KBVirtualDialogActivity.class);
//                intent.putExtra(KBConstants.VIRUAL_DIALOG_START, KBConstants.ACTIVITY_START_KEY_BRIGHTNESS);
//                intent.putExtra(KBConstants.VIRUAL_DIALOG_BRIGHTNESS, normal);
//                intent.putExtra(KBConstants.VIRUAL_DIALOG_USING_SYSTEM_BRIGHTNESS, usingSystemBrightness);
//                startActivityForResult(intent, REQUEST_CODE_BRIGHTNESS);
//                return true;
//            case R.id.menu_reader_back:
//                finish();
//                return true;
//            case R.id.menu_reader_about:
//                showDialog(DIALOG_ID_ABOUT);//DialogFragment
//                return true;
//            default:
//                return true;
//        }
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mDetector.onTouchEvent(event);
    }

    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        String tag = "onActivityResult";
        Log.d(tag, "onActivityResult the read text activity...");
        if (_resultCode == RESULT_OK) {
            switch (_requestCode) {
                case REQUEST_CODE_SETTING:
                    mLayContent.setBackgroundResource(mPreference.getInt(KBConstants.PREF_KEY_BACKGROUND, R.drawable.bg_lyxg));

                    int fontColor = mPreference.getInt(KBConstants.PREF_KEY_FONT_COLOR, Color.BLACK);
                    mTxvContent.setTextColor(fontColor);

                    mTxvFileName.setTextColor(fontColor);
                    mTxvBatteryStatus.setTextColor(fontColor);
                    mDcTime.setTextColor(fontColor);
                    int textSize = mPreference.getInt(KBConstants.PREF_KEY_FONT_SIZE, KBConstants.DEFAULT_FONT_SIZE);
                    if ((int) (mTxvContent.getTextSize()) != textSize) {
                        mTxtReader.setTextSize(textSize);
//                        TextPaint tp = mTxvContent.getPaint();
//                        KBCR.fontHeight = mTxvContent.getLineHeight();
//                        mLinesOfScreen = mVisibleHeight / KBCR.fontHeight;
//                        KBCR.upperAsciiWidth = (int) tp.measureText(KBConstants.UPPERASCII);
//                        KBCR.lowerAsciiWidth = (int) tp.measureText(KBConstants.LOWERASCII);
//                        KBCR.ChineseFontWidth = (int) tp.measureText(KBConstants.CHINESE.toCharArray(), 0, 1);

//                        Log.d("onActRet CR.FontHeight:", "" + KBCR.fontHeight);
//                        Log.d("onActRet CR.AsciiWidth:", "" + KBCR.upperAsciiWidth);
//                        Log.d("onActRet CR.FontWidth:", "" + KBCR.ChineseFontWidth);
                        mTxtReader.read((mOffset = mTxtReader.getCurrentLineOffset()));
                        showPercent();
                    }
                    break;
                case REQUEST_CODE_SKIP:
                    String strPercent = _data.getStringExtra(KBConstants.VIRUAL_DIALOG_RESULT);
                    if (!TextUtils.isEmpty(strPercent)) {
                        int intPercent = (int) (Float.valueOf(strPercent) * 10);
                        mOffset = mTxtReader.getOffsetWithPercent(intPercent);
                        mTxtReader.read(mOffset);
                        showPercent(intPercent);
                    }
                    break;
                case REQUEST_CODE_BRIGHTNESS:
                    String[] rd = _data.getStringExtra(KBConstants.VIRUAL_DIALOG_RESULT).split("\\|");
                    KBUtility.putShare(mPreference, KBConstants.PREF_KEY_IS_BRIGHTNESS, Float.valueOf(rd[0]));
                    KBUtility.putShare(mPreference, KBConstants.PREF_KEY_USING_SYSTEM_BRIGHTNESS, Boolean.valueOf(rd[1]));
                    setScreenBrightness();
                    break;
            }
        }
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID_SAVE_BOOK_MARK_SUCCESS:
                return saveBookMarkSuccess();
            case DIALOG_ID_SAVE_BOOK_MARK_FAIL:
                return saveBookMarkFail();
            case DIALOG_ID_ABOUT:
                return KBUtility.buildDialog(this, null, KBConstants.ABOUT_TITLE, KBConstants.ABOUT_DETAIL, getString(R.string.sure));
            default:
                return null;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        String tag = "onDestroy";
        Log.d(tag, "destroy the read text activity...");
        if (mTxtReader != null) {
            mTxtReader.close();
            mTxtReader = null;
        }
        if (mKBDBAdapter != null) {
            mKBDBAdapter.close();
            mKBDBAdapter = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String tag = "onResume";
        Log.d(tag, "resume the read text activity...");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String tag = "onRestoreInstanceState";
        Log.d(tag, "onRestoreInstanceState the read text activity...");
        mOffset = savedInstanceState.getInt(KBConstants.SAVED_STATE_OFFSET);
        if (mTxtReader != null) {
            mTxtReader.read(mOffset);
            showPercent(mTxtReader.getPercentWithOffset(mOffset));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String tag = "onSaveInstanceState";
        Log.d(tag, "onSaveInstanceState the read text activity...");
        outState.putInt(KBConstants.SAVED_STATE_OFFSET, mTxtReader.getCurrentLineOffset());
    }

    @Override
    protected void onStop() {
        super.onStop();
        String tag = "onStop";
        Log.d(tag, "stop the read text activity...");
        if (mTxtReader != null) {
            mOffset = mTxtReader.getCurrentLineOffset();
            saveHistory();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String tag = "onPause";
        Log.d(tag, "pause the read text activity...");
        if (mTxtReader != null) {
            mOffset = mTxtReader.getCurrentLineOffset();
            saveHistory();
        }
        unregisterReceiver(mBroadcastReceiver);
    }

    private void bookMarkView() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.kb_book_mark_list);
        final Button btnDelete = (Button) dialog.findViewById(R.id.deletebm);
        final Button btnGo = (Button) dialog.findViewById(R.id.skipbm);
        final Button btnCancel = (Button) dialog.findViewById(R.id.cancelbm);
        dialog.setTitle(KBConstants.BOOKMARKLIST);
        final ListView lsvBookMark = (ListView) dialog.findViewById(R.id.bookmarklistview);
        mBookMarkList = mKBDBAdapter.queryAllBookMark(mBookId);
        final ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, mBookMarkList);
        lsvBookMark.setAdapter(listAdapter);
        lsvBookMark.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lsvBookMark.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _parent, View _view, int _position, long _id) {
                lsvBookMark.setItemChecked(_position, true);
                bmLocation = _position;
                mBookMark = mBookMarkList.get(bmLocation);
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBookMark != null) {
                    mOffset = mBookMark.getCurrentOffset();
                    mTxtReader.read(mOffset);
                    showPercent(mTxtReader.getPercentWithOffset(mOffset));
                    dialog.dismiss();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tag = "delete book mark ";
                if (mBookMark != null) {
                    Log.d(tag, "start delete book mark");
                    boolean b = mKBDBAdapter.deleteBookMark(mBookMark.getBookMarkId());
                    if (b && mBookMarkList.size() > 0) {
                        mBookMarkList.remove(bmLocation);
                        lsvBookMark.setAdapter(listAdapter);
                        mBookMark = null;
                        System.gc();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void loadData() {
        int footHeight = Math.round(mTxvFileName.getLineHeight() * getResources().getDisplayMetrics().density) + 10;
        Log.d("footHeight : ", "" + footHeight);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTxvContent.getLayoutParams();
        mVisibleHeight = dm.heightPixels - footHeight - params.topMargin - params.bottomMargin;
        int visibleWidth = (int) (dm.widthPixels - params.leftMargin - params.rightMargin - (10 * getResources().getDisplayMetrics().density));
        mTxtReader = new KBTxtStringReader(mTxvContent, mFilePath, visibleWidth, mVisibleHeight, dm.density);

        mTxtReader.setTextSize(mPreference.getInt(KBConstants.PREF_KEY_FONT_SIZE, KBConstants.DEFAULT_FONT_SIZE));
        mTxvContent.setTextColor(mPreference.getInt(KBConstants.PREF_KEY_FONT_COLOR, Color.BLACK));
        mLayContent.setBackgroundResource(mPreference.getInt(KBConstants.PREF_KEY_BACKGROUND, R.drawable.bg_lyxg));

        /* load the attribute for font */
//        TextPaint tp = mTxvContent.getPaint();
//        KBCR.fontHeight = mTxvContent.getLineHeight();
//        mLinesOfScreen = mVisibleHeight / KBCR.fontHeight;

//        /** Ascii char width */
//        KBCR.upperAsciiWidth = (int) tp.measureText(KBConstants.UPPERASCII);
//        KBCR.lowerAsciiWidth = (int) tp.measureText(KBConstants.LOWERASCII);
//        /** Chinese char width */
//        KBCR.ChineseFontWidth = (int) tp.measureText(KBConstants.CHINESE.toCharArray(), 0, 1);

//        Log.d("onCrtDig CRFontHeight:", "" + KBCR.fontHeight);
//        Log.d("onCrtDig CRAsciiWidth:", "" + KBCR.upperAsciiWidth);
//        Log.d("onCrtDig CRFontWidth:", "" + KBCR.ChineseFontWidth);

        setTitle(mFilePath + "-" + getString(R.string.app_name));
//        mScvContent.setOnKeyListener(mUpOrDown);
//        mScvContent.setOnTouchListener(new OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                KBReadTxtStringActivity.this.mDetector.onTouchEvent(event);
//                return false;
//            }
//        });
    }

    private void saveBookMarkDialog() {
        final Dialog dialog = new Dialog(KBReadTxtStringActivity.this);
        dialog.setTitle(KBConstants.INPUTBMNAME);
        dialog.setContentView(R.layout.kb_book_mark_dialog);
        final EditText et = (EditText) dialog.findViewById(R.id.bmet);
        et.setText(mTxtReader.getCurrentLineString());
        final int offset = mTxtReader.getCurrentLineOffset();
        final Button sure = (Button) dialog.findViewById(R.id.bmsure);
        final Button cancel = (Button) dialog.findViewById(R.id.bmcancel);
        sure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String bmn = et.getText().toString();
                if (bmn.length() < 1) {
                    dialog.dismiss();
                    dialog.show();
                } else {
                    if (bmn.length() > 10) {
                        bmn = bmn.substring(0, 10);
                    }
                    KBBookMark bm = new KBBookMark();
                    bm.setBookId(mBookId);
                    bm.setMarkName(bmn);
                    bm.setCurrentOffset(offset);
                    bm.setSaveTime(DateUtils.dateToString(new Date(), KBConstants.DATE_FORMAT_YYYYMMDDHHMMSS));
                    boolean operateResult = mKBDBAdapter.saveBookMark(bm);
                    if (operateResult) {
                        showDialog(DIALOG_ID_SAVE_BOOK_MARK_SUCCESS);
                    } else {
                        showDialog(DIALOG_ID_SAVE_BOOK_MARK_FAIL);
                    }
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        System.gc();
    }

    private Dialog saveBookMarkFail() {
        return new AlertDialog.Builder(this).setPositiveButton(
                getString(R.string.sure),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setTitle(KBConstants.SAVERESULT)
                .setIcon(R.drawable.fail)
                .setMessage(KBConstants.SAVEFAIL).create();
    }

    private Dialog saveBookMarkSuccess() {
        return new AlertDialog.Builder(this).setPositiveButton(
                getString(R.string.sure),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setTitle(KBConstants.SAVERESULT)
                .setIcon(R.drawable.success)
                .setMessage(KBConstants.SAVESUCCESS).create();
    }

    private void saveHistory() {
        KBHistory history = new KBHistory();
        history.setBookId(mBookId);
        history.setCurrentOffset(mTxtReader.getCurrentLineOffset());
        history.setBookName(KBUtility.getBookName(mFilePath));
        history.setBookPath(mFilePath);
        history.setSaveTime(DateUtils.dateToString(new Date(), KBConstants.DATE_FORMAT_YYYYMMDDHHMMSS));
        history.setSummary(mTxtReader.getCurrentLineString());
        mKBDBAdapter.saveHistory(history);
    }

    private void setNightMode(boolean _isNightMode) {
        if (_isNightMode) {
            mLayContent.setBackgroundResource(mPreference.getInt(KBConstants.PREF_KEY_BACKGROUND, R.drawable.bg_lyxg));

            int fontColor = mPreference.getInt(KBConstants.PREF_KEY_FONT_COLOR, Color.BLACK);
            mTxvContent.setTextColor(fontColor);

            mTxvFileName.setTextColor(fontColor);
            mTxvBatteryStatus.setTextColor(fontColor);
            mDcTime.setTextColor(fontColor);

            mTpbPercent.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_normal));
        } else {
            mLayContent.setBackgroundResource(R.drawable.bg_hydq);
            mTxvContent.setTextColor(Color.DKGRAY);
            mTxvFileName.setTextColor(Color.DKGRAY);
            mTxvBatteryStatus.setTextColor(Color.DKGRAY);
            mDcTime.setTextColor(Color.DKGRAY);

            mTpbPercent.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_night));
        }
    }

    private void setScreenBrightness() {
        boolean usingSystemBrightness = mPreference.getBoolean(KBConstants.PREF_KEY_USING_SYSTEM_BRIGHTNESS, false);
        WindowManager.LayoutParams wl = getWindow().getAttributes();
        if (!usingSystemBrightness) {
//            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            wl.screenBrightness = mPreference.getFloat(KBConstants.PREF_KEY_IS_BRIGHTNESS, (5F / 255F));
        } else {
//            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            wl.screenBrightness = -1;
        }
        getWindow().setAttributes(wl);
    }

    private void showPercent() {
        int percent = mTxtReader.getPercent();
        Log.d("showPercent", "showPercent(): " + percent + "  mLastPercent : " + mLastPercent);
        if (percent != mLastPercent) {
            mLastPercent = percent;
            mTpbPercent.setProgress(mLastPercent);
            mLayFooter.postInvalidate();
        }
    }

    private void showPercent(int _percent) {
        Log.d("showPercent", "showPercent(int _percent): " + _percent);
        mTxtReader.setPercent(_percent);
        showPercent();
    }
}
