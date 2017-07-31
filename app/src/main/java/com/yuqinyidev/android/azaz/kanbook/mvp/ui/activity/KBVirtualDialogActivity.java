package com.yuqinyidev.android.azaz.kanbook.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.kanbook.KBConstants;
import com.yuqinyidev.android.framework.utils.UiUtils;

import java.text.DecimalFormat;

public class KBVirtualDialogActivity extends AppCompatActivity {

    private String choice;
    private SeekBar skbSkip;
    private EditText edtInput;
    private ToggleButton toggleButton;
    private TextView txvSkip;
    private String percent;
    private float mBrightness;
    private boolean mUsingSystemBrightness;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        UiUtils.fullScreen(KBVirtualDialogActivity.this);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            setContentView(R.layout.kb_virtual_dialog);

            edtInput = (EditText) findViewById(R.id.edtInput);
            toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
            txvSkip = (TextView) findViewById(R.id.txvSkip);
            skbSkip = (SeekBar) findViewById(R.id.skbSkip);

            choice = intent.getStringExtra(KBConstants.VIRUAL_DIALOG_START);
            if (KBConstants.ACTIVITY_START_KEY_SKIP.equals(choice)) {
                setTitle(KBConstants.DIALOG_TITLE_PERCENT);
                edtInput.setVisibility(View.GONE);
                toggleButton.setVisibility(View.GONE);
                skbSkip.setVisibility(View.VISIBLE);
                txvSkip.setVisibility(View.VISIBLE);
                int p = intent.getIntExtra(KBConstants.VIRUAL_DIALOG_PERCENT, 0);
                DecimalFormat df = new DecimalFormat("#0.0");
                txvSkip.setText(df.format(p * 0.1) + "%");
                skbSkip.setMax(1000);
                skbSkip.setProgress(p);
                skbSkip
                        .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onProgressChanged(SeekBar seekBar,
                                                          int progress, boolean fromTouch) {
                                DecimalFormat df = new DecimalFormat("#0.0");
                                percent = df.format(progress * 0.1);
                                txvSkip.setText(percent + "%");
                            }
                        });
            } else if (KBConstants.ACTIVITY_START_KEY_BRIGHTNESS.equals(choice)) {
                setTitle(KBConstants.DIALOG_TITLE_BRIGHTNESS);
                edtInput.setVisibility(View.GONE);
                toggleButton.setVisibility(View.VISIBLE);
                skbSkip.setVisibility(View.VISIBLE);
                txvSkip.setVisibility(View.VISIBLE);

                toggleButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (toggleButton.isChecked()) {
                            mUsingSystemBrightness = true;
                        } else {
                            mUsingSystemBrightness = false;
                        }
                    }
                });

                mUsingSystemBrightness = intent.getBooleanExtra(KBConstants.VIRUAL_DIALOG_USING_SYSTEM_BRIGHTNESS, false);
                toggleButton.setChecked(mUsingSystemBrightness);

                float p = intent.getFloatExtra(KBConstants.VIRUAL_DIALOG_BRIGHTNESS, 0.5F);
                skbSkip.setMax(255);
                skbSkip.setProgress((int) (p * 255));
                skbSkip
                        .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                int tmpInt = seekBar.getProgress();
                                if (tmpInt < 5) {
                                    tmpInt = 5;
                                }
                                mBrightness = (float) tmpInt / 255;
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                            }
                        });
            } else {
                skbSkip.setVisibility(View.GONE);
                toggleButton.setVisibility(View.GONE);
                txvSkip.setVisibility(View.GONE);
                edtInput.setVisibility(View.VISIBLE);
                if (KBConstants.ACTIVITY_START_KEY_RENAME_FOLDER.equals(choice)) {
                    setTitle(KBConstants.DIALOG_TITLE_INPUT_FOLDER_NAME);
                } else if (KBConstants.ACTIVITY_START_KEY_RENAME_FILE.equals(choice)) {
                    setTitle(KBConstants.DIALOG_TITLE_INPUT_FILE_NAME);
                }
            }
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }

        ((Button) findViewById(R.id.btnSure))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuilder result = new StringBuilder();

                        if (KBConstants.ACTIVITY_START_KEY_SKIP.equals(choice)) {
                            result.append(percent);
                        } else if (KBConstants.ACTIVITY_START_KEY_BRIGHTNESS.equals(choice)) {
                            result.append(mBrightness);
                            result.append("|");
                            result.append(mUsingSystemBrightness);
                        } else {
                            String input = edtInput.getText().toString();
                            if (input.length() == 0) {
                                setResult(RESULT_CANCELED);
                                finish();
                            }

                            result.append("|");
                            result.append(input);
                            if (KBConstants.ACTIVITY_START_KEY_RENAME_FILE.equals(choice)) {
                                result.append(KBConstants.FILE_END_TXT);
                            }
                        }
                        Intent intent = new Intent();
                        intent.putExtra(KBConstants.VIRUAL_DIALOG_RESULT, result.toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

        ((Button) findViewById(R.id.btnCancel))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
    }
}
