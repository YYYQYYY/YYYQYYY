package com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils;

import android.widget.TextView;

import com.yuqinyidev.android.azaz.kanbook.KBConstants;
import com.yuqinyidev.android.framework.utils.io.BufferedRandomAccessFile;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;

public class KBTxtStringReader {

    /**
     * Used to display data to the screen components
     */
    private TextView mTextView = null;

//    private final int BUFFER_SIZE = 100 * 1024;// 100k

    /**
     * To read the files
     */
    private String mFileName = null;

    private int mVisibleWidth, mVisibleHeight;
    private int mLineCount; // 每页可以显示的行数
    private int mFontSize = 18;        //字体大小
    private float mDensity = 1;

    private MappedByteBuffer mReadFileRandom;

    /**
     * To read the length of the document
     */
    private long mFileLength;

//    /**
//     * At present, the starting line
//     */
//    private int mCurrentLine = 0;
//
//    /**
//     * The first line in the current view of migration
//     */
//    private int mCurrentOffset = 0;
//
//    /**
//     * Save for the line and offset a collection of objects
//     */
//    private List<TxtLine> mMyLines = new ArrayList<TxtLine>();
//
//    /**
//     * The documents show that the data used to buffer
//     */
//    private byte[] mDisplayBuffer;
//
//    /**
//     * For the preservation of the current screen data
//     */
//    private byte[] mScreenData = new byte[0];

    /**
     * is the document is the last page ?
     */
    private boolean m_isLastPage = false;

    /**
     * is the document is the front page ?
     */
    private boolean m_isFirstPage = true;

    /**
     * The file encoding
     */
    private String mEncoding = "GB2312";

    /**
     * Percentage
     */
    private int mPercent = 0;
    private int m_mbBufBegin = 0;
    private int m_mbBufEnd = 0;
//    private int mDataStartLocation = 0;
//    private int mDataEndLocation = 0;

    private Vector<String> m_lines = new Vector<>();

    public int getCurrentLineOffset() {
        return m_mbBufBegin;
    }

    public String getCurrentLineString() {
        return m_lines.size() > 0 ? m_lines.get(0) : KBConstants.NODATAINFILE;
    }

    public long getFileLength() {
        return mFileLength;
    }

//    public List<TxtLine> getList() {
//        return mMyLines;
//    }

    public int getLinesOfOneScreen() {
        return this.mLineCount;
    }

    public int getOffsetWithPercent(int _percent) {
        int endOffset = 0;
        if (mFileLength != 0) {
            mPercent = _percent;
            endOffset = (int) ((double) _percent * (double) mFileLength / 1000);
        }
        return endOffset;
    }

    public int getPercentWithOffset(long _offset) {
        int percent = 0;
        if (mFileLength != 0) {
            percent = (int) (((double) _offset / (double) mFileLength) * 1000);
            mPercent = percent;
        }
        return percent;
    }

    public int getPercent() {
        return mPercent;
    }

    public void setPercent(int _percent) {
        mPercent = _percent;
    }

    public void setTextSize(int size) {
        mFontSize = size;
        mTextView.getPaint().setTextSize(mFontSize * mDensity);
        mLineCount = mVisibleHeight / mFontSize - mVisibleHeight / mFontSize / 8;
    }

    public int getTextSize() {
        return mFontSize;
    }

    public boolean isEnd() {
        return m_isLastPage;
    }

    public KBTxtStringReader(TextView textView, String fileName, int visibleWidth, int visibleHeight, float density) {
        this.mFileName = fileName;
        this.mTextView = textView;
        this.mVisibleWidth = visibleWidth;
        this.mVisibleHeight = visibleHeight;
        setTextSize(Math.round(mFontSize));

        mEncoding = getCharsetName();

        init();
    }

    public void close() {
        mReadFileRandom = null;
    }

    public void readPrePage() {
        if (m_mbBufBegin <= 0) {
            m_mbBufBegin = 0;
            m_isFirstPage = true;
            return;
        } else m_isFirstPage = false;
        m_lines.clear();
        pageUp();
        m_lines = pageDown();
        displayText();
    }

    public void readNextPage() {
        if (m_mbBufEnd >= mFileLength) {
            m_isLastPage = true;
            return;
        } else m_isLastPage = false;
        m_lines.clear();
        m_mbBufBegin = m_mbBufEnd;
        m_lines = pageDown();
        displayText();
    }

    public void read(int offset) {
        m_mbBufEnd = m_mbBufBegin = offset;
        m_lines.clear();
        m_lines = pageDown();
        displayText();
    }

    private void displayText() {
        StringBuilder stringBuilder = new StringBuilder();
        if (m_lines.size() > 0) {
            for (String strLine : m_lines) {
                stringBuilder.append(strLine);
            }
        }
        mTextView.setText(stringBuilder.toString());

        mPercent = (int) (m_mbBufBegin * 1000 / mFileLength);
    }

//    private void getCharsetName() {
//        File file = new File(mFileName);
//        if (!file.exists()) {
//            return;
//        }
//
//        byte[] encodings = new byte[512];
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(file);
//            if (fis.read(encodings) > 0) {
//                mEncoding = KBBytesEncodingDetect.nicename[new KBBytesEncodingDetect().detectEncoding(encodings)];
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fis != null) {
//                    fis.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private String getCharsetName() {
        File file = new File(mFileName);
        if (!file.exists()) {
            System.err.println("getFileIncode: file not exists!");
            return null;
        }

        byte[] buf = new byte[4096];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            // (1)
            UniversalDetector detector = new UniversalDetector(null);

            // (2)
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            // (3)
            detector.dataEnd();

            // (4)
            String encoding = detector.getDetectedCharset();
            if (encoding != null) {
                System.out.println("Detected encoding = " + encoding);
            } else {
                System.out.println("No encoding detected.");
            }

            // (5)
            detector.reset();
            fis.close();
            return encoding;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void init() {
        File file = new File(mFileName);
        mFileLength = file.length();
        if (mFileLength == 0) {
            mTextView.setText(KBConstants.NODATAINFILE);
            return;
        }

        try {
            this.mReadFileRandom = new BufferedRandomAccessFile(file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, mFileLength);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        readNextBuffer();
//        analysisDisplayBuffer();
//        displayNextToScreen(0);
    }

    private byte[] readParagraphBack(int nEnd) {  //读取上一段
        int i;
        byte b0, b1;        //分别存储一个汉字的前后两个字符

        //判别文字编码
        //utf-16是固定16位的(双字节)，分为LE 和 BE
        //比如说char 'a', ascii为
        //0x61, 那么它的utf-8, 则为 [0x61], 但utf-16是16位的, 所以为[0x00, 0x61]，LE 为Ox6100
        switch (mEncoding) {
            case "UTF-16LE"://UTF-16LE(little endian)
                i = nEnd - 2;   //一个字 2 byte
                while (i > 0) {
                    b0 = mReadFileRandom.get(i);
                    b1 = mReadFileRandom.get(i + 1);
                    if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {   //Ox0a  分段标识
                        i += 2;
                        break;
                    }
                    i--;
                }
                break;
            case "UTF-16BE":        //UTF-16BE (big endian)
                i = nEnd - 2;
                while (i > 0) {
                    b0 = mReadFileRandom.get(i);
                    b1 = mReadFileRandom.get(i + 1);
                    if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
                        i += 2;
                        break;
                    }
                    i--;
                }
                break;
            default:
                i = nEnd - 1;
                while (i > 0) {
                    b0 = mReadFileRandom.get(i);
                    if (b0 == 0x0a && i != nEnd - 1) {
                        i++;
                        break;
                    }
                    i--;
                }
                break;
        }
        if (i < 0)
            i = 0;
        int nParaSize = nEnd - i;
        int j;
        byte[] buf = new byte[nParaSize];
        for (j = 0; j < nParaSize; j++) {
            buf[j] = mReadFileRandom.get(i + j);
        }
        return buf;
    }

    private byte[] readParagraphForward(int nFromPos) {
        int i = nFromPos;
        byte b0, b1;

        // 根据编码格式判断换行，Ox0a为换行
        switch (mEncoding) {
            case "UTF-16LE":
                while (i < mFileLength - 1) {
                    b0 = mReadFileRandom.get(i++);
                    b1 = mReadFileRandom.get(i++);
                    if (b0 == 0x0a && b1 == 0x00) {
                        break;
                    }
                }
                break;
            case "UTF-16BE":
                while (i < mFileLength - 1) {
                    b0 = mReadFileRandom.get(i++);
                    b1 = mReadFileRandom.get(i++);
                    if (b0 == 0x00 && b1 == 0x0a) {
                        break;
                    }
                }
                break;
            default:
                while (i < mFileLength) {
                    b0 = mReadFileRandom.get(i++);
                    if (b0 == 0x0a) {
                        break;
                    }
                }
                break;
        }

        int nParaSize = i - nFromPos;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = mReadFileRandom.get(nFromPos + i);
        }
        return buf;
    }

    private void pageUp() { //上一页
        if (m_mbBufBegin < 0)
            m_mbBufBegin = 0;
        Vector<String> lines = new Vector<>();
        String strParagraph = "";
        while (lines.size() < mLineCount && m_mbBufBegin > 0) {
            Vector<String> paraLines = new Vector<>();
            byte[] paraBuf = readParagraphBack(m_mbBufBegin);
            m_mbBufBegin -= paraBuf.length;
            try {   //将byte 转化为汉字
                strParagraph = BCConvert.half2full(new String(paraBuf, mEncoding));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "");
            strParagraph = strParagraph.replaceAll("\n", "");

            if (strParagraph.length() == 0) {
                paraLines.add(strParagraph);
            }
            while (strParagraph.length() > 0) {
                int nSize = mTextView.getPaint().breakText(strParagraph, true, mVisibleWidth, null);
                paraLines.add(strParagraph.substring(0, nSize) + "\r\n");
                strParagraph = strParagraph.substring(nSize);
            }
            lines.addAll(0, paraLines);
        }
        while (lines.size() > mLineCount) {
            try {
                m_mbBufBegin += lines.get(0).getBytes(mEncoding).length;
                lines.remove(0);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        m_mbBufEnd = m_mbBufBegin;
    }

    private Vector<String> pageDown() {  //下一页
        String strParagraph = "";
        Vector<String> lines = new Vector<>();
        while (lines.size() < mLineCount && m_mbBufEnd < mFileLength) {
            byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
            m_mbBufEnd += paraBuf.length;
            try {
                strParagraph = BCConvert.half2full(new String(paraBuf, mEncoding));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String strReturn = "";

            //\r换行，\n新行   \r\n新段落
            if (strParagraph.contains("\r\n")) {
                strReturn = "\r\n";
                strParagraph = strParagraph.replaceAll("\r\n", "");
            } else if (strParagraph.contains("\n")) {
                strReturn = "\n";
                strParagraph = strParagraph.replaceAll("\n", "");
            }

            if (strParagraph.length() == 0) {
//                lines.add(strParagraph);
                lines.add("\r\n");
            }
            while (strParagraph.length() > 0) {
                int nSize = mTextView.getPaint().breakText(strParagraph, true, mVisibleWidth, null);
                lines.add(strParagraph.substring(0, nSize) + "\r\n");
                strParagraph = strParagraph.substring(nSize);
                if (lines.size() >= mLineCount) {
                    break;
                }
            }
            if (strParagraph.length() != 0) {
                try {
                    m_mbBufEnd -= (strParagraph + strReturn).getBytes(mEncoding).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return lines;
    }

}
