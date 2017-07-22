package com.yuqinyidev.android.azaz.kanbook.mvp.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.azaz.kanbook.KBConstants;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.KBDBAdapter;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.entity.KBBook;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.entity.KBIconText;
import com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils.KBFileAdapter;
import com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils.KBFileInfo;
import com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils.KBUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KBBookShelfActivity extends Activity {

    private static final int DIALOG_ID_ITEM_LONG_CLICK_FILE = 10;
    private static final int DIALOG_ID_ITEM_LONG_CLICK_FOLDER = 11;

    private static final int CHOICE_ITEM_OPEN = 0;
    private static final int CHOICE_ITEM_RENAME = 1;
    private static final int CHOICE_ITEM_DEL_FILE = 2;

    public static final int FILE_RESULT_CODE = 3;

    private String mFilePath;
    private KBDBAdapter mKBDBAdapter;

    private ListView lsvBookShelf;
    //    private LinearLayout llayUpOneLevel;
//    private ImageView imgUpOneLevel;
//    private TextView txvUpOneLevel;
    private TextView txvUpOneLevelBottom;
    private ProgressDialog progressDialog;

    private List<KBIconText> mDirectoryList = new ArrayList<KBIconText>();
    private IconTextListAdapter iconTextListAdapter;
//    private File mCurrentDirectory = new File(KBConstants.BOOK_SHELF_ROOT_PATH);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.kb_bookshelf);

        mKBDBAdapter = new KBDBAdapter(this);

        lsvBookShelf = (ListView) findViewById(R.id.lsvBookShelf);
//        llayUpOneLevel = (LinearLayout) findViewById(R.id.llayUpOneLevel);
//        imgUpOneLevel = (ImageView) findViewById(R.id.imgUpOneLevel);
//        txvUpOneLevel = (TextView) findViewById(R.id.txvUpOneLevel);
        txvUpOneLevelBottom = (TextView) findViewById(R.id.txvUpOneLevelBottom);

        progressDialog = ProgressDialog.show(KBBookShelfActivity.this, "正在搜索TXT文件...", "请稍候...", true, false);

        String findTargetPaths = "/sdcard/BaiduNetdisk|/sdcard/Download";
//        if (KBUtility.getExternalSdCardPath() != null) {
//            phonePicsPath = KBUtility.getExternalSdCardPath();
//        } else {
//            phonePicsPath = getFilesDir().getAbsolutePath();
//        }

        KBFileAdapter.getFileList(findTargetPaths, "*.txt", new KBFileAdapter.OnFileListCallback() {
            @Override
            public void SearchFileListInfo(ArrayList<KBFileInfo> list) {
//                mCurrentDirectory = new File(phonePicsPath);
                browseAllTxtFiles(list);

                lsvBookShelf.setSelection(0);
                lsvBookShelf.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView<?> _parent, View _view,
                                                   int _position, long _id) {
                        mFilePath = mDirectoryList.get(_position).getFilePath();
                        int itemType = mDirectoryList.get(_position).getType();
                        switch (itemType) {
                            case KBConstants.ITEM_TYPE_UP_ONE_LEVEL:
                                break;
                            case KBConstants.ITEM_TYPE_FILE:
                                showDialog(DIALOG_ID_ITEM_LONG_CLICK_FILE);
                                break;
                            case KBConstants.ITEM_TYPE_FOLDER:
                                showDialog(DIALOG_ID_ITEM_LONG_CLICK_FOLDER);
                                break;
                        }
                        return true;
                    }
                });

                lsvBookShelf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        int selectionRowID = (int) id;

                        File file = new File(mDirectoryList.get(selectionRowID).getFilePath());
                        if (file.isFile()) {
                            mFilePath = file.getAbsolutePath();
                            openFile();
                        } else if (file.isDirectory()) {
                            browseToWhere(file);
                        }
                    }
                });

//                llayUpOneLevel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        upOneLevel();
//                    }
//                });

                progressDialog.dismiss();

                if (mDirectoryList.size() == 0) {
                    Toast toast = Toast.makeText(KBBookShelfActivity.this,
                            KBConstants.WRONG_NO_BOOK, Toast.LENGTH_LONG);
                    toast.setGravity(0, 0, 0);
                    toast.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.kb_menu_bookshelf, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_bookshelf_refresh:
//                browseToWhere(mCurrentDirectory);
                return true;
            case R.id.menu_bookshelf_addfolder:
                Intent intent = new Intent(KBBookShelfActivity.this, KBMyFileManager.class);
                startActivityForResult(intent, FILE_RESULT_CODE);
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode,
                                    Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        if (_resultCode == RESULT_OK) {
            setProgressBarIndeterminateVisibility(false);

            switch (_requestCode) {
                case CHOICE_ITEM_RENAME:
                    String filePath = mFilePath + _data.getStringExtra(KBConstants.VIRUAL_DIALOG_RESULT);
                    File srcFile = new File(mFilePath);
                    File destFile = new File(filePath);
                    srcFile.renameTo(destFile);
                    if (srcFile.isFile()) {
                        mKBDBAdapter.updateBook(mFilePath, filePath, KBUtility
                                .getBookName(filePath));
                    }
                    break;
                case FILE_RESULT_CODE:
                    Bundle bundle = null;
                    if (_data != null && (bundle = _data.getExtras()) != null) {
                        String fp = bundle.getString(KBConstants.ADD_FORDER_PATH);
                    }
                    break;
            }

            setProgressBarIndeterminateVisibility(true);
//            browseToWhere(mCurrentDirectory);
        }
    }

    protected Dialog onCreateDialog(int _id) {
        switch (_id) {
            case DIALOG_ID_ITEM_LONG_CLICK_FILE:
                AlertDialog.Builder builder = new AlertDialog.Builder(KBBookShelfActivity.this);
                builder.setTitle("请选择操作：" + KBUtility.getBookName(mFilePath));
                builder.setSingleChoiceItems(
                        getResources().getStringArray(R.array.menu_bookshelf_file),
                        0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onListItemLongClick4File(which);
                                dialog.cancel();
                            }
                        });
                return builder.create();
            case DIALOG_ID_ITEM_LONG_CLICK_FOLDER:
                return new Builder(KBBookShelfActivity.this).setSingleChoiceItems(
                        getResources()
                                .getStringArray(R.array.menu_bookshelf_folder), 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onListItemLongClick4Folder(which);
                                dialog.cancel();
                            }
                        }).create();
            case CHOICE_ITEM_DEL_FILE:
                Dialog dialog = KBUtility.buildDialog(KBBookShelfActivity.this,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setProgressBarIndeterminateVisibility(false);
                                deleteFile();
                                dialog.cancel();
                                setProgressBarIndeterminateVisibility(true);
//                                browseToWhere(mCurrentDirectory);
                            }
                        }, KBConstants.SURE_DELETE, KBConstants.DELETE_FILE,
                        getString(R.string.sure), getString(R.string.cancel));
                return dialog;
            default:
                return null;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        String tag = "onDestroy";
        Log.d(tag, "destroy the bookshelf activity...");
        if (mKBDBAdapter != null) {
            mKBDBAdapter.close();
            mKBDBAdapter = null;
        }
    }

    private void browseAllTxtFiles(ArrayList<KBFileInfo> list) {
        try {
            mDirectoryList.clear();

            Drawable currentIcon = null;
            File currentFile;
            List<KBIconText> fileList = new ArrayList<KBIconText>();

            for (KBFileInfo currentKBFileInfo : list) {
                currentFile = new File(currentKBFileInfo.getFilePath());
                if (currentFile == null) {
                    continue;
                }

                currentIcon = getResources().getDrawable(R.drawable.text32);

                fileList.add(new KBIconText(currentKBFileInfo, currentIcon, KBConstants.ITEM_TYPE_FILE));
            }
            Collections.sort(fileList);
            mDirectoryList.addAll(fileList);

//            llayUpOneLevel.setVisibility(View.GONE);
            txvUpOneLevelBottom.setVisibility(View.GONE);

            iconTextListAdapter = new IconTextListAdapter(this);
            iconTextListAdapter.setListItems(mDirectoryList);
            lsvBookShelf.setAdapter(iconTextListAdapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void browseToBookShelf() {
        File bookShelf = new File(KBConstants.BOOK_SHELF_ROOT_PATH);
        if (!bookShelf.exists()) {
            bookShelf.mkdirs();
        }
        browseToWhere(bookShelf);
    }

    private void browseToWhere(final File aDirectory) {
        setTitle(aDirectory.getAbsolutePath() + " - "
                + getString(R.string.app_name));

        if (aDirectory.isDirectory()) {
//            mCurrentDirectory = aDirectory;
            fill(aDirectory.listFiles());
        }
    }

    private boolean checkEnds(String checkItsEnd, String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.endsWith(aEnd))
                return true;
        }
        return false;
    }

    private void deleteFile() {
        File file = new File(mFilePath);
        file.delete();
        mKBDBAdapter.deleteBook(mFilePath);
    }

    private void fill(File[] files) {
        mDirectoryList.clear();

        Drawable currentIcon = null;
        List<KBIconText> fileList = new ArrayList<KBIconText>();

        for (File currentFile : files) {
            if (currentFile == null) {
                continue;
            }

            int currentPathStringLenght = 0;//mCurrentDirectory.getAbsolutePath().length();

            if (currentFile.isDirectory()) {
                currentIcon = getResources().getDrawable(R.drawable.folder32);

                mDirectoryList.add(new KBIconText(new KBFileInfo(currentFile.getAbsolutePath().substring(currentPathStringLenght),
                        currentFile.getAbsolutePath(), currentFile.lastModified()), currentIcon,
                        KBConstants.ITEM_TYPE_FOLDER));
            } else {
                String fileName = currentFile.getName();
                if (checkEnds(fileName, getResources().getStringArray(
                        R.array.textEnds))) {
                    currentIcon = getResources().getDrawable(R.drawable.text32);

                    fileList.add(new KBIconText(new KBFileInfo(currentFile.getAbsolutePath().substring(currentPathStringLenght),
                            currentFile.getAbsolutePath(), currentFile.lastModified()), currentIcon,
                            KBConstants.ITEM_TYPE_FILE));
                }
            }
        }
        Collections.sort(mDirectoryList);
        Collections.sort(fileList);
        mDirectoryList.addAll(fileList);

//        if (!mCurrentDirectory.getAbsolutePath().equals(
//                KBConstants.BOOK_SHELF_ROOT_PATH)) {
//            llayUpOneLevel.setVisibility(View.VISIBLE);
//            txvUpOneLevelBottom.setVisibility(View.VISIBLE);
//            imgUpOneLevel.setImageDrawable(getResources().getDrawable(
//                    R.drawable.uponelevel));
//            txvUpOneLevel.setText(KBConstants.UP_ONE_LEVEL);
//        } else {
//            llayUpOneLevel.setVisibility(View.GONE);
//            txvUpOneLevelBottom.setVisibility(View.GONE);
//        }

        iconTextListAdapter = new IconTextListAdapter(this);
        iconTextListAdapter.setListItems(mDirectoryList);
        lsvBookShelf.setAdapter(iconTextListAdapter);
    }

    private void onListItemLongClick4File(int _which) {
        switch (_which) {
            case CHOICE_ITEM_OPEN:
                openFile();
                break;
            case CHOICE_ITEM_RENAME:
                Intent intent = new Intent(KBBookShelfActivity.this,
                        KBVirualDialogActivity.class);
                intent.putExtra(KBConstants.VIRUAL_DIALOG_START,
                        KBConstants.ACTIVITY_START_KEY_RENAME_FILE);
                startActivityForResult(intent, CHOICE_ITEM_RENAME);
                break;
            case CHOICE_ITEM_DEL_FILE:
                showDialog(CHOICE_ITEM_DEL_FILE);
        }
    }

    private void onListItemLongClick4Folder(int _which) {
        Intent intent;
        switch (_which) {
            case CHOICE_ITEM_OPEN:
                browseToWhere(new File(mFilePath));
                break;
            case CHOICE_ITEM_RENAME:
                intent = new Intent(KBBookShelfActivity.this,
                        KBVirualDialogActivity.class);
                intent.putExtra(KBConstants.VIRUAL_DIALOG_START,
                        KBConstants.ACTIVITY_START_KEY_RENAME_FOLDER);
                startActivityForResult(intent, CHOICE_ITEM_RENAME);
        }
    }

    private void openFile() {
        setProgressBarIndeterminateVisibility(false);
        KBBook book = new KBBook();
        book.setBookName(KBUtility.getBookName(mFilePath));
        book.setBookPath(mFilePath);
        Intent intent = new Intent(this, KBReadTxtActivity.class);
        intent.putExtra(KBConstants.ACTIVITY_START_KEY,
                KBConstants.ACTIVITY_START_KEY_BOOKSHELF);
        intent.putExtra(KBConstants.ACTIVITY_START_KEY_FILE_PATH, mFilePath);
        intent.putExtra(KBConstants.ACTIVITY_START_KEY_BOOK_ID, mKBDBAdapter
                .saveBook(book));
        startActivity(intent);
        finish();
        setProgressBarIndeterminateVisibility(true);
    }

    private void upOneLevel() {
//        if (!mCurrentDirectory.getAbsolutePath().equals(
//                KBConstants.BOOK_SHELF_ROOT_PATH))
//            browseToWhere(mCurrentDirectory.getParentFile());
    }

    private class IconTextListAdapter extends BaseAdapter {

        private Context mContext;
        private List<KBIconText> mItems = new ArrayList<KBIconText>();
        private TextView txvIcon;

        public IconTextListAdapter(Context _context) {
            mContext = _context;
        }

        public void setListItems(List<KBIconText> _lstIconText) {
            mItems = _lstIconText;
        }

        public int getCount() {
            return mItems.size();
        }

        public Object getItem(int _position) {
            return mItems.get(_position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int _position, View _convertView, ViewGroup _parent) {
            View view;
            if (_convertView == null) {
                view = View.inflate(mContext, R.layout.kb_icon_text, null);
            } else {
                view = _convertView;
            }

            txvIcon = null;
            txvIcon = (TextView) view.findViewById(R.id.txvIcon);

            if (!KBConstants.UP_ONE_LEVEL.equals(mItems.get(_position).getText())) {
                txvIcon.setTextSize(KBConstants.DEFAULT_FONT_SIZE);
            }
            txvIcon.setText(mItems.get(_position).getText());
            ((ImageView) view.findViewById(R.id.imgIcon))
                    .setImageDrawable(mItems.get(_position).getIcon());

            return view;
        }

    }

}
