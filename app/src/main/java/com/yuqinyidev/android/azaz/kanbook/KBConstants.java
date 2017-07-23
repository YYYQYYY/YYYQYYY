package com.yuqinyidev.android.azaz.kanbook;

import java.io.File;

public class KBConstants {

	public static void init() {
		File rootPath = new File(BOOK_SHELF_ROOT_PATH);
		if (!rootPath.exists()) {
			rootPath.mkdirs();
		}
	}

	public final static String DB_NAME = "xycgkanbook.db";

	public final static String M_LINE_NUMBER = "mLineNumber";
	public final static String M_OFFSET = "mOffset";
	public final static String M_IS_MARK = "mIsMark";
	public final static String MARK = "mark";
	public final static int DATA_LENGTH = 65535;

	public final static String GB2312 = "GB2312";
	public final static String GBK = "GBK";
	public final static String UTF8 = "UTF-8";

	public final static String FILENOTFOUND = "文件无法读取！请返回...";
	public final static String NODATAINFILE = "文件中没有数据！";

	public final static String BOOKMARK = "书签";

	public final static String UPPERASCII = "A";
	public final static String LOWERASCII = "a";
	public final static String CHINESE = "汉";

	public static final int DEFAULT_FONT_SIZE = 18;

	public static final String ROOT_PATH = "/sdcard/xycgkanbook";
	public static final String BOOK_SHELF_ROOT_PATH = ROOT_PATH + "/BOOKSHELF";
	public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

	public static final String EXIT_TITLE = "确定退出";
	public static final String EXIT_DETAIL = "确定退出程序？";
	public static final String EXIT_TOAST_DETAIL = "再按一次返回键退出程序。";

	public static final String WRONG_NO_BOOK = "您的书库中没有找到任何TXT文件！";
	public final static String UP_ONE_LEVEL = "返回上级..";
	public final static String SURE_DELETE = "确认删除";
	public final static String DELETE_FILE = "删除此文件吗？";

	public static final String INPUTBMNAME = "请输入书签的名称：";
	public static final String BOOKMARKLIST = "书签列表：";
	public static final String SAVERESULT = "保存结果";
	public static final String SAVESUCCESS = "恭喜您，保存书签成功！";
	public static final String SAVEFAIL = "很抱歉，保存书签失败，请重试！";

	public static final String ABOUT_TITLE = "晨光看书 1.0 版";
	public static final String ABOUT_DETAIL = "晨光看书由小雨晨光工作室开发";
	public static final String CHECKED_ITEM_INDEX = "checkedItemIndex";

	public static final String PREFERENCE_NAME = "qqkanbook_settings";
	public static final String PREF_KEY_FONT_SIZE = "settings_fontsize";
	public static final String PREF_KEY_FONT_COLOR = "settings_fontcolor";
	public static final String PREF_KEY_BACKGROUND = "settings_background";
	public static final String PREF_KEY_FONT_SIZE_IDX = "settings_fontsize_idx";
	public static final String PREF_KEY_FONT_COLOR_IDX = "settings_fontcolor_idx";
	public static final String PREF_KEY_BACKGROUND_IDX = "settings_background_idx";
	public static final String PREF_KEY_IS_NIGHTMODE = "settings_nightmode";
	public static final String PREF_KEY_IS_BRIGHTNESS = "settings_brightness";
	public static final String PREF_KEY_USING_SYSTEM_BRIGHTNESS = "settings_using_system_brightness";

	public static final String HISTORY_TITLE_NAME = "【书名】：";
	public static final String HISTORY_TITLE_LAST_OPEN_DT = "【日期】：";
	public static final String HISTORY_TITLE_SUMMARY = "【摘要】：";
	public static final String HISTORY_TITLE_SUMMARY_DEFAULT = "晨光看书\n纯粹为看书而作的小软件！目前只支持TXT,敬请期待更多的功能!";

	public static final String VIRUAL_DIALOG_START = "start_intent_virual_dialog";
	public static final String VIRUAL_DIALOG_PERCENT = "start_intent_virual_percent";
	public static final String VIRUAL_DIALOG_RESULT = "result_intent_virual_dialog";
	public static final String VIRUAL_DIALOG_BRIGHTNESS = "start_intent_virual_brightness";
	public static final String VIRUAL_DIALOG_USING_SYSTEM_BRIGHTNESS = "start_intent_virual_using_system_brightness";

	public static final String DIALOG_TITLE_INPUT_FOLDER_NAME = "请输入文件夹名称：";
	public static final String DIALOG_TITLE_INPUT_FILE_NAME = "请输入文件名称：";
	public static final String DIALOG_TITLE_PERCENT = "跳转百分比：";
	public static final String DIALOG_TITLE_BRIGHTNESS = "屏幕亮度：";

	public static final String DIALOG_TITLE_ERROR = "错误！";
	public static final String FILE_NOT_FOUND = "源文件不存在！";
	public static final String FILE_ALREADY_EXIST = "目标文件已存在！";
	public static final String INVALID_PERCNET_INPUT = "请输入正确的百分比！（0-100）";

	public static final int ITEM_TYPE_UP_ONE_LEVEL = 0;
	public static final int ITEM_TYPE_FILE = 1;
	public static final int ITEM_TYPE_FOLDER = 2;

	public static final String FILE_END_TXT = ".txt";

	public static final String ACTIVITY_START_KEY = "activity_start_key";

	public static final String ACTIVITY_START_KEY_MAIN = "activity_start_key_main";
	public static final String ACTIVITY_START_KEY_BOOKSHELF = "activity_start_key_bookshelf";

	public static final String ACTIVITY_START_KEY_OFFSET = "activity_start_key_offset";
	public final static String ACTIVITY_START_KEY_FILE_PATH = "activity_start_key_file_path";
	public final static String ACTIVITY_START_KEY_BOOK_ID = "activity_start_key_book_id";

	public static final String ACTIVITY_START_KEY_RENAME_FILE = "activity_start_key_rename_file";
	public static final String ACTIVITY_START_KEY_RENAME_FOLDER = "activity_start_key_rename_folder";
	public static final String ACTIVITY_START_KEY_SKIP = "activity_start_key_skip";
	public static final String ACTIVITY_START_KEY_BRIGHTNESS = "activity_start_key_brightness";

	public static final String SOFT_STATE_NO_BOOK_OPENED = "soft_state_no_book_opened";

	public static final String SAVED_STATE_OFFSET = "saved_state_offset";

	public static final String ADD_FORDER_PATH = "add_forder_path";
}