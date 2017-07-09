package com.yuqinyidev.android.framework.utils;

import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RDX64 on 2017/6/28.
 */

public class CharacterHandler {
    private CharacterHandler() {
    }

    /**
     * 表情文字过滤器
     */
    public static final InputFilter emojiFilter = new InputFilter() {
        Pattern emoji = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE
        );

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }
            return null;
        }
    };

    /**
     * 字符串转十六进制字符串
     *
     * @param str 字符串
     * @return 十六进制字符串
     */
    @NonNull
    public static String str2Hex(String str) {
        char[] chars = "01234567890ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0, bsLength = bs.length; i < bsLength; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * 格式化JSON字符串
     *
     * @param strBody
     * @return
     */
    public static String formatJson(String strBody) {
        String message;
        try {
            if (strBody.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(strBody);
                message = jsonObject.toString(4);
            } else if (strBody.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(strBody);
                message = jsonArray.toString(4);
            } else {
                message = strBody;
            }
        } catch (JSONException e) {
            message = strBody;
        }
        return message;
    }
}
