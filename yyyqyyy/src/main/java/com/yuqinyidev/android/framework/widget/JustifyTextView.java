package com.yuqinyidev.android.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Created by yuqy on 2017-07-28.
 */
public class JustifyTextView extends AppCompatTextView {

    /**
     * TextView的总宽度
     */
    private int mViewWidth;
    /**
     * 行高
     */
    private int mLineY;

    public JustifyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JustifyTextView(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth();//拿到textview的实际宽度
        String text = getText().toString();
        mLineY = 0;
        mLineY += getTextSize();
        Layout layout = getLayout();
        for (int i = 0; i < layout.getLineCount(); i++) {//每行循环
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            String line = text.substring(lineStart, lineEnd);//获取到TextView每行中的内容
            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
            if (needScale(line)) {
                if (i == layout.getLineCount() - 1) {//最后一行不需要重绘
                    canvas.drawText(line, 0, mLineY, paint);
                } else {
                    drawScaleText(canvas, lineStart, line, width);
                }
            } else {
                canvas.drawText(line, 0, mLineY, paint);
            }
            mLineY += getLineHeight();//写完一行以后，高度增加一行的高度
            System.out.println("lineHeight---" + getLineHeight());
        }
    }

    /**
     * 重绘此行
     *
     * @param canvas    画布
     * @param lineStart 行头
     * @param line      该行所有的文字
     * @param lineWidth 该行每个文字的宽度的总和
     */
    private void drawScaleText(Canvas canvas, int lineStart, String line,
                               float lineWidth) {
        float x = 0;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, getPaint());// 以 (x, mLineY) 为起点，画出blanks
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());// 画出一个空格需要的宽度
            x += bw;
            line = line.substring(3);
        }
        // 比如说一共有5个字，中间隔了4个空儿，
        //	那就用整个TextView的宽度 - 这5个字的宽度，
        //然后除以4，填补到这4个空儿中
        float d = (mViewWidth - lineWidth) / (line.length() - 1);

        for (int i = 0; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

    /**
     * 判断是不是段落的第一行。
     * 一个汉字相当于一个字符，此处判断是否为第一行的依据是：
     * 字符长度大于3且前两个字符为空格
     *
     * @param lineStart
     * @param line
     * @return
     */
    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' '
                && line.charAt(1) == ' ';
    }


    /**
     * 判断需不需要缩放
     * 该行最后一个字符不是换行符的时候返回true，
     * 该行最后一个字符是换行符的时候返回false
     *
     * @param line
     * @return
     */
    private boolean needScale(String line) {
        if (line.length() == 0) {
            return false;
        } else {
            return line.charAt(line.length() - 1) != '\n';//该行最后一个字符不是换行符的时候返回true，是换行符的时候返回false
        }
    }

}
