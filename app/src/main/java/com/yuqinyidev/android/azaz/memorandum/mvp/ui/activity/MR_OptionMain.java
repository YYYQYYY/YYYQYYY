package com.yuqinyidev.android.azaz.memorandum.mvp.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.yuqinyidev.android.azaz.R;

public class MR_OptionMain extends Activity {

    private String MIME_TYPE_IMAGE_ALL = "image/*";
    private static final int ACTIVITY_GET_IMAGE = 0;
    private static final String PACKAGE_PATH = "com.totyu.hz.memorandum";
    public static final String IMAGE_SAVE_DIR_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_PATH + "/images";
    public static final String IMAGE_SAVE_PATH = IMAGE_SAVE_DIR_PATH
            + "/bg.png";

    public static final String PROFERENCE_NAME = "memorandum_options";
    public static final String OPTION_KEY_HOLIDAY = "option_key_holiday";
    public static final String OPTION_KEY_FIRSTDAY = "option_key_firstday";
    public static final String OPTION_KEY_BG_ENABLE = "option_key_bg_enable";
    public static final String OPTION_KEY_BG_TRANSPARENCY = "option_key_bg_transparency";

    private static final String OPTION_KEY_HOLIDAY_IDX = "option_key_holiday_index";
    private static final String OPTION_KEY_FIRSTDAY_IDX = "option_key_firstday_index";
    private static final String OPTION_KEY_BG_TRANSPARENCY_IDX = "option_key_bg_transparency_index";

    public static int OPTION_VALUE_BG_TRANS_DEFAULT = 255;

    private static final int DIALOG_BACKGROUND = 1;
    private static final int DIALOG_HOLIDAY = 2;
    private static final int DIALOG_ABOUT = 3;
    private static final int DIALOG_FIRSTDAY = 4;

    public static final int RESULT_BACKGROUND = 1;
    public static final int RESULT_HOLIDAY = 2;
    public static final int RESULT_FIRST_DAY = 3;

    public static final int FIRST_DAY_SUN = 1;
    public static final int FIRST_DAY_MON = 2;

    private int firstDayIdx = 0;
    private int holidayIdx = 0;

    private SharedPreferences preference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_GET_IMAGE:
                Uri uri = data == null ? Uri.EMPTY : data.getData();
                try {
                    Cursor cursor = getContentResolver().query(uri, null, null,
                            null, null);
                    if (cursor == null || !cursor.moveToFirst()) {
                        return;
                    }
                    String imageFilePath = cursor.getString(1);
                    cursor.close();

                    saveImage(imageFilePath);
                } catch (Exception e) {
                }
                break;
        }
    }

    // Dialog定義を下する
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_BACKGROUND:
                return buildBackgroundDialog(MR_OptionMain.this);
            case DIALOG_HOLIDAY:
                return buildHolidayDialog(MR_OptionMain.this);
            case DIALOG_ABOUT:
                return buildAboutDialog(MR_OptionMain.this);
            case DIALOG_FIRSTDAY:
                return buildFirstDayDialog(MR_OptionMain.this);
            default:
                return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mr_option);

        preference = getSharedPreferences(PROFERENCE_NAME, Context.MODE_PRIVATE);

        ListView lsvBackground = (ListView) findViewById(R.id.lsvBackground);
        ArrayList<HashMap<String, String>> lstBackground = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mapBackground = new HashMap<String, String>();
        mapBackground.put("listEvents", this.getString(R.string.bgd_option));
        mapBackground.put("listEvent", this.getString(R.string.bgd_dtl));
        lstBackground.add(mapBackground);
        SimpleAdapter adpBackground = new SimpleAdapter(this, lstBackground,
                R.layout.mr_optlistitembgd, new String[]{"listEvents",
                "listEvent"}, new int[]{R.id.textView1,
                R.id.textView2});
        lsvBackground.setAdapter(adpBackground);

        ListView lsvHoliday = (ListView) findViewById(R.id.lsvHoliday);
        ArrayList<HashMap<String, String>> lstHoliday = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mapHoliday = new HashMap<String, String>();
        mapHoliday.put("listEvents", this.getString(R.string.hdy_option));
        mapHoliday.put("listEvent", this.getString(R.string.hdy_dtl));
        lstHoliday.add(mapHoliday);
        SimpleAdapter adpHoliday = new SimpleAdapter(this, lstHoliday,
                R.layout.mr_optlistitemhdy, new String[]{"listEvents",
                "listEvent"}, new int[]{R.id.textView1,
                R.id.textView2});
        lsvHoliday.setAdapter(adpHoliday);

        ListView lsvFirstDay = (ListView) findViewById(R.id.lsvFirstDay);
        lsvFirstDay.setAdapter(new FirstDayAdapter(this));

        ListView lsvAbout = (ListView) findViewById(R.id.lsvAbout);
        ArrayList<HashMap<String, String>> lstAbout = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mapAbout = new HashMap<String, String>();
        mapAbout.put("listEvents", this.getString(R.string.abt_option));
        mapAbout.put("listEvent", this.getString(R.string.abt_dtl));
        lstAbout.add(mapAbout);
        SimpleAdapter adpAbout = new SimpleAdapter(this, lstAbout,
                R.layout.mr_optlistitemabt, new String[]{"listEvents",
                "listEvent"}, new int[]{R.id.textView1,
                R.id.textView2});
        lsvAbout.setAdapter(adpAbout);

        lsvBackground.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                onCreateDialog(DIALOG_BACKGROUND);
            }
        });

        lsvHoliday.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View _view,
                                    int position, long id) {
                showDialog(DIALOG_HOLIDAY);
            }
        });

        lsvFirstDay.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View _view,
                                    int position, long id) {
                showDialog(DIALOG_FIRSTDAY);
            }
        });

        lsvAbout.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                showDialog(DIALOG_ABOUT);
            }
        });
    }

    public class FirstDayAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private int[] mData = {R.layout.mr_optlistitemdtm};

        public FirstDayAdapter(Context _context) {
            mInflater = LayoutInflater.from(_context);
        }

        public int getCount() {
            return mData.length;
        }

        public Object getItem(int _position) {
            return _position;
        }

        public long getItemId(int _id) {
            return _id;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.mr_optlistitemdtm, null);
                holder.imgFirstDay = (ImageView) convertView
                        .findViewById(R.id.imgFirstDay);
                holder.textView1 = (TextView) convertView
                        .findViewById(R.id.textView1);
                holder.textView2 = (TextView) convertView
                        .findViewById(R.id.textView2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imgFirstDay.setBackgroundResource(R.drawable.download);
            holder.textView1.setText(getString(R.string.date_option));
            holder.textView2.setText(getString(R.string.date_dtl));
            return convertView;
        }

        public final class ViewHolder {
            public TextView textView1;
            public TextView textView2;
            public ImageView imgFirstDay;
        }
    }

    private Dialog buildHolidayDialog(final Context context) {
        final CharSequence[] items = {this.getString(R.string.china),
                this.getString(R.string.america)};
        Builder builder = new Builder(context);
        builder.setTitle(R.string.hdy_option);

        builder.setSingleChoiceItems(items, preference.getInt(
                OPTION_KEY_HOLIDAY_IDX, 0),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        holidayIdx = which;
                    }
                });
        builder.setPositiveButton(this.getString(R.string.hdy_aqe),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent result = new Intent();
                        result.putExtra(OPTION_KEY_HOLIDAY, items[holidayIdx]
                                .toString());
                        setResult(RESULT_HOLIDAY, result);
                        putShare(OPTION_KEY_HOLIDAY, items[holidayIdx]
                                .toString());
                        putShare(OPTION_KEY_HOLIDAY_IDX, holidayIdx);
                        finish();
                    }
                });
        builder.setNeutralButton(this.getString(R.string.hdy_cel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        return builder.create();
    }

    private Dialog buildFirstDayDialog(final Context context) {
        final CharSequence[] items = {this.getString(R.string.sun),
                this.getString(R.string.mon)};
        Builder builder = new Builder(context);
        builder.setTitle(R.string.date_title);

        builder.setSingleChoiceItems(items, preference.getInt(
                OPTION_KEY_FIRSTDAY_IDX, 0),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        firstDayIdx = which;
                    }
                });
        builder.setPositiveButton(this.getString(R.string.hdy_aqe),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent result = new Intent();
                        result.putExtra(OPTION_KEY_FIRSTDAY, getString(
                                R.string.sun).equals(
                                items[firstDayIdx].toString()) ? FIRST_DAY_SUN
                                : FIRST_DAY_MON);
                        setResult(RESULT_FIRST_DAY, result);
                        putShare(
                                OPTION_KEY_FIRSTDAY,
                                getString(R.string.sun).equals(
                                        items[firstDayIdx].toString()) ? FIRST_DAY_SUN
                                        : FIRST_DAY_MON);
                        putShare(OPTION_KEY_FIRSTDAY_IDX, firstDayIdx);
                        finish();
                    }
                });
        builder.setNeutralButton(this.getString(R.string.hdy_cel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        return builder.create();
    }

    private Dialog buildAboutDialog(Context context) {
        Builder builder = new Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View textEntryView = inflater
                .inflate(R.layout.mr_optdialoglayoutabt, null);
        builder.setTitle(this.getString(R.string.abt_option));
        builder.setView(textEntryView);
        final AlertDialog alert = builder.create();
        textEntryView.findViewById(R.id.btnClose).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.cancel();
                    }
                });
        alert.show();
        return null;

    }

    private Dialog buildBackgroundDialog(Context context) {
        Builder builder = new Builder(context);

        LayoutInflater inflater = LayoutInflater.from(this);
        View textEntryView = inflater
                .inflate(R.layout.mr_optdialoglayoutbdg, null);

        boolean isEnabledBg = preference
                .getBoolean(OPTION_KEY_BG_ENABLE, false);

        final CheckBox chbEnabled = (CheckBox) textEntryView
                .findViewById(R.id.checkBox1);
        chbEnabled.setChecked(isEnabledBg);

        final Spinner spnTransp = (Spinner) textEntryView
                .findViewById(R.id.spnTrac);
        List<String> list = new ArrayList<String>();
        list.add("none");
        list.add("25%");
        list.add("50%");
        list.add("75%");
        list.add("100%");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTransp.setAdapter(adapter);
        spnTransp.setSelection(preference.getInt(
                OPTION_KEY_BG_TRANSPARENCY_IDX, 0));

        final Button btnBackground = (Button) textEntryView
                .findViewById(R.id.button3);

        builder.setTitle(this.getString(R.string.bgd_title));
        builder.setView(textEntryView);

        builder.setPositiveButton(this.getString(R.string.bgd_upd),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int alpha = OPTION_VALUE_BG_TRANS_DEFAULT;
                        boolean enableBg = chbEnabled.isChecked();
                        String strSelectedItem = ((String) spnTransp
                                .getSelectedItem()).replace("%", "");
                        if (!"none".equals(strSelectedItem)) {
                            alpha = 255 - (255 * Integer
                                    .parseInt(strSelectedItem) / 100);
                        }
                        putShare(OPTION_KEY_BG_ENABLE, enableBg);
                        putShare(OPTION_KEY_BG_TRANSPARENCY, alpha);
                        putShare(OPTION_KEY_BG_TRANSPARENCY_IDX, spnTransp
                                .getSelectedItemPosition());

                        Intent result = new Intent();
                        result.putExtra(OPTION_KEY_BG_ENABLE, enableBg);
                        result.putExtra(OPTION_KEY_BG_TRANSPARENCY, alpha);
                        setResult(RESULT_BACKGROUND, result);

                        finish();
                    }
                }).setNeutralButton(this.getString(R.string.hdy_cel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        chbEnabled.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    btnBackground.setEnabled(true);
                } else {
                    btnBackground.setEnabled(false);
                }
            }
        });

        btnBackground.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType(MIME_TYPE_IMAGE_ALL);
                startActivityForResult(getImage, ACTIVITY_GET_IMAGE);
            }
        });

        if (isEnabledBg) {
            btnBackground.setEnabled(true);
        } else {
            btnBackground.setEnabled(false);
        }

        builder.create();
        builder.show();

        return null;
    }

    private String saveImage(String _bitSrcPath) {
        File dir = new File(IMAGE_SAVE_DIR_PATH);
        if (!dir.exists()) {
            dir.mkdir();
            try {
                String command = "chmod 751 " + dir.getAbsolutePath();
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("chmod", "chmod fail!!!!");
            }
        }

        File file = new File(IMAGE_SAVE_PATH);
        file.deleteOnExit();

        FileOutputStream fOut = null;
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(_bitSrcPath);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return IMAGE_SAVE_PATH;
    }

    private void putShare(String _key, String _value) {
        Editor edit = preference.edit();
        edit.putString(_key, _value);
        edit.commit();
    }

    private void putShare(String _key, boolean _value) {
        Editor edit = preference.edit();
        edit.putBoolean(_key, _value);
        edit.commit();
    }

    private void putShare(String _key, int _value) {
        Editor edit = preference.edit();
        edit.putInt(_key, _value);
        edit.commit();
    }
}
