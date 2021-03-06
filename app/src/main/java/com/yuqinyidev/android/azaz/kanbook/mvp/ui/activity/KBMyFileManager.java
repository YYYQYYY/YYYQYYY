package com.yuqinyidev.android.azaz.kanbook.mvp.ui.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.kanbook.KBConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KBMyFileManager extends ListActivity {
    private List<String> items = null;
    private List<String> paths = null;
    private String rootPath = "/";
    private String curPath = "/";
    private TextView mPath;

    private final static String TAG = "bb";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.kb_fileselect);
        mPath = (TextView) findViewById(R.id.mPath);
        Button buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent data = new Intent(KBMyFileManager.this, KBBookShelfActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(KBConstants.ADD_FORDER_PATH, curPath);
                data.putExtras(bundle);
                setResult(2, data);
                finish();
            }
        });
        Button buttonCancle = (Button) findViewById(R.id.buttonCancle);
        buttonCancle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        getFileDir(rootPath);
    }

    private void getFileDir(String filePath) {
        mPath.setText(filePath);
        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        File f = new File(filePath);
        File[] files = f.listFiles();

        if (!filePath.equals(rootPath)) {
            items.add("b1");
            paths.add(rootPath);
            items.add("b2");
            paths.add(f.getParent());
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            items.add(file.getName());
            paths.add(file.getPath());
        }

        setListAdapter(new KBMyAdapter(this, items, paths));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(paths.get(position));
        if (file.isDirectory()) {
            curPath = paths.get(position);
            getFileDir(paths.get(position));
        }
    }
}