package com.yuqinyidev.android.azaz.kanbook.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.kanbook.KBConstants;
import com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils.KBUtility;

public class KBSettingActivity extends Activity {

    private static final int DIALOG_ID_FONT_SIZE = 0;
    private static final int DIALOG_ID_FONT_COLOR = 1;
    private static final int DIALOG_ID_BACKGROUND = 2;

    private TextView viewfont;

    private static SharedPreferences mPreference;

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent(getApplicationContext(),
                KBReadTxtActivity.class);
        setResult(RESULT_OK, mIntent);
        super.onBackPressed();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kb_setting);

        mPreference = getSharedPreferences(KBConstants.PREFERENCE_NAME,
                Context.MODE_PRIVATE);

        viewfont = (TextView) findViewById(R.id.viewfont);
        viewfont.setTextSize(mPreference.getInt(KBConstants.PREF_KEY_FONT_SIZE,
                KBConstants.DEFAULT_FONT_SIZE));

        RelativeLayout imgFontSize = (RelativeLayout) findViewById(R.id.rlayFontSize);
        imgFontSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_FONT_SIZE);
            }
        });

        RelativeLayout imgFontColor = (RelativeLayout) findViewById(R.id.rlayFontColor);
        imgFontColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_FONT_COLOR);
            }
        });

        RelativeLayout imgBackground = (RelativeLayout) findViewById(R.id.rlayBackground);
        imgBackground.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_BACKGROUND);
            }
        });

        initializing();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID_FONT_SIZE:
                return showSingleChoiceDialog(KBSettingActivity.this, getResources()
                        .getStringArray(R.array.fontsize), mPreference.getInt(
                        KBConstants.PREF_KEY_FONT_SIZE_IDX, 1), DIALOG_ID_FONT_SIZE);
            case DIALOG_ID_FONT_COLOR:
                return showSingleChoiceDialog(KBSettingActivity.this, getResources()
                        .getStringArray(R.array.fontcolor), mPreference.getInt(
                        KBConstants.PREF_KEY_FONT_COLOR_IDX, 0), DIALOG_ID_FONT_COLOR);
            case DIALOG_ID_BACKGROUND:
                return showSingleChoiceDialog(KBSettingActivity.this, getResources()
                        .getStringArray(R.array.background), mPreference.getInt(
                        KBConstants.PREF_KEY_BACKGROUND_IDX, 7), DIALOG_ID_BACKGROUND);
            default:
                return null;
        }
    }

    private Dialog showSingleChoiceDialog(final Context context,
                                          final String[] _items, final int _checkedItemIdx, final int _type) {
        AlertDialog dialog = new Builder(context).setSingleChoiceItems(_items,
                _checkedItemIdx, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (_type) {
                            case DIALOG_ID_FONT_SIZE:
                                setFontSize(Integer.valueOf(_items[which]));
                                KBUtility.putShare(mPreference,
                                        KBConstants.PREF_KEY_FONT_SIZE_IDX, which);
                                break;
                            case DIALOG_ID_FONT_COLOR:
                                setFontColor(which);
                                KBUtility.putShare(mPreference,
                                        KBConstants.PREF_KEY_FONT_COLOR_IDX, which);
                                break;
                            case DIALOG_ID_BACKGROUND:
                                setBackground(which);
                                KBUtility.putShare(mPreference,
                                        KBConstants.PREF_KEY_BACKGROUND_IDX, which);
                                break;
                            default:
                                break;
                        }
                        dialog.cancel();
                    }
                }).create();
        return dialog;
    }

    private void initializing() {
        viewfont.setTextSize(mPreference.getInt(KBConstants.PREF_KEY_FONT_SIZE,
                KBConstants.DEFAULT_FONT_SIZE));
        viewfont.setTextColor(mPreference.getInt(KBConstants.PREF_KEY_FONT_COLOR,
                Color.BLACK));
        viewfont.setBackgroundResource(mPreference.getInt(
                KBConstants.PREF_KEY_BACKGROUND, R.drawable.bg_lyxg));
    }

    private void setFontSize(int _textSize) {
        viewfont.setTextSize(_textSize);
        KBUtility.putShare(mPreference, KBConstants.PREF_KEY_FONT_SIZE, _textSize);
    }

    private void setFontColor(int _selectedIndex) {
        // 黑色
        int textColor = Color.BLACK;
        switch (_selectedIndex) {
            // 白色
            case 1:
                textColor = Color.WHITE;
                break;
            // 灰色
            case 2:
                textColor = Color.DKGRAY;
                break;
            // 黄色
            case 3:
                textColor = Color.YELLOW;
                break;
            // 绿色
            case 4:
                textColor = Color.GREEN;
                break;
            // 蓝色
            case 5:
                textColor = Color.BLUE;
                break;
            // 红色
            case 6:
                textColor = Color.RED;
                break;
        }
        viewfont.setTextColor(textColor);
        KBUtility.putShare(mPreference, KBConstants.PREF_KEY_FONT_COLOR, textColor);
    }

    private void setBackground(int _selectedIndex) {
        // 蓝月星光
        int background = R.drawable.bg_lyxg;
        switch (_selectedIndex) {
            // 清新绿叶
            case 1:
                background = R.drawable.bg_qxly;
                break;
            // 黄色书皮
            case 2:
                background = R.drawable.bg_hssp;
                break;
             // 黑夜灯桥
             case 3:
             background = R.drawable.bg_hydq;
             break;
            // // 火红烛心
            // case 4:
            // background = R.drawable.bg_hzhx;
            // break;
            // // 灰色书本
            // case 5:
            // background = R.drawable.bg_hssb;
            // break;
            // // 苹果灰色
            // case 6:
            // background = R.drawable.bg_pghs;
            // break;
            // // 忆景思甜
            // case 7:
            // background = R.drawable.bg_yjst;
            // break;
        }
        viewfont.setBackgroundResource(background);
        KBUtility
                .putShare(mPreference, KBConstants.PREF_KEY_BACKGROUND,
                        background);
    }

}
