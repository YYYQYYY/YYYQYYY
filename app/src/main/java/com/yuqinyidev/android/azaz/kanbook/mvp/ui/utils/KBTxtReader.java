package com.yuqinyidev.android.azaz.kanbook.mvp.ui.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.yuqinyidev.android.azaz.kanbook.KBCR;
import com.yuqinyidev.android.azaz.kanbook.KBConstants;
import com.yuqinyidev.android.azaz.kanbook.mvp.model.entity.TxtLine;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class KBTxtReader {

	/** Used to display data to the screen components */
	private TextView mTextView = null;

	private final int BUFFER_SIZE = 100 * 1024;// 100k

	/** To read the files */
	private String mFileName = null;

	/** The wide-screen and high */
	private int mScreenWidth, mScreenHeigth;

	private int mViewWidth, mViewHeigth;

	/** A screen can store data on the number of line */
	private int mLinesOfOneScreen;

	private KBReadFileRandom mReadFileRandom;

	/** To read the length of the document */
	private long mFileLength;

	/** At present, the starting line */
	private int mCurrentLine = 0;

	/** The first line in the current view of migration */
	private int mCurrentOffset = 0;

	/** Save for the line and offset a collection of objects */
	private List<TxtLine> mMyLines = new ArrayList<TxtLine>();

	/** The documents show that the data used to buffer */
	private byte[] mDisplayBuffer;

	/** For the preservation of the current screen data */
	private byte[] mScreenData = new byte[0];

	/** is the document is the last page ? */
	private boolean mEndOfDoc = false;

	/** is the document is the front page ? */
	private boolean mBeforeOfDoc = true;

	/** The file encoding */
	private String mEncoding = "GB2312";

	/** Percentage */
	private int mPercent = 0;
	private int mStartOffset = 0;
	private int mEndOffset = 0;
	private int mDataStartLocation = 0;
	private int mDataEndLocation = 0;

	public KBTxtReader(TextView textView, Context c, String fileName,
                       int screenWidth, int screenHeigth) {
		this.mFileName = fileName;
		this.mTextView = textView;
		this.mScreenWidth = screenWidth;
		this.mScreenHeigth = screenHeigth;

		/** Start initialization */
		init();
	}

	public void analysisDisplayBuffer() {
		if (null == mDisplayBuffer) {
			return;
		}
		mMyLines.clear();
		int length = 0;
		int offset = 0;
		int width = 0;
		int beforeLineLength = 0;// The length of before current line
		for (offset = 0; offset < mDisplayBuffer.length;) {
			int b = mDisplayBuffer[offset] & 0xff;
			if (b == 13) {// Use the blank instead of \r
				mDisplayBuffer[offset] = ' ';
			}
			if (b == 10) {// \n
				length++;
				offset++;
				beforeLineLength++;
				/** Scroll down */
				mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
						beforeLineLength));
				length = 0;
				continue;
			}
			if (b > 0x7f) {// Chinese
				if (width + KBCR.ChineseFontWidth > mViewWidth) {// If the line
					// length
					// more than view width
					mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
							beforeLineLength));
					length = 0;
					width = 0;
					continue;
				} else {
					offset += 2;
					length += 2;
					beforeLineLength += 2;
					width += KBCR.ChineseFontWidth;
				}

			} else {// Ascii
				int aw = KBCR.upperAsciiWidth;

				if (!(b >= 65 && b <= 90)) {
					aw = KBCR.lowerAsciiWidth;
				}
				if (width + aw > mViewWidth) {

					mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
							beforeLineLength));
					length = 0;
					width = 0;
					continue;
				} else {
					offset += 1;
					length += 1;
					beforeLineLength += 1;
					width += aw;
				}
			}
		}
		// Add the last line
		mMyLines.add(new TxtLine(mCurrentOffset + offset, length,
				beforeLineLength));
		mCurrentLine = 0;
		System.gc();
	}

	public void close() {
		mReadFileRandom.close();
	}

	public void displayPreToScreen(int n) {
		String tag = "displayPreToScreen";
		int tempCurrentLine = mCurrentLine;
		int futureLine = tempCurrentLine - n;
		Log.d(tag, "futureLine : " + futureLine);
		if (futureLine < 0) {
			futureLine = 0;
		}

		Log.d(tag, "mCurrentLine:" + mCurrentLine);
		Log.d(tag, "futureLine:" + futureLine);
		Log.d(tag, "mBeforeOfDoc:" + mBeforeOfDoc);
		Log.d(tag, "mEndOfDoc:" + mEndOfDoc);
		Log.d(tag, "mCurrentLine:" + mCurrentLine);

		if (futureLine == 0 && !mBeforeOfDoc) {
			readPreBuffer();
			analysisDisplayBuffer();
			Log.d(tag, "futureLine ==0 && !mBeforeOfDoc");
			int lastLine = mLinesOfOneScreen - 1;
			if (lastLine > mMyLines.size()) {
				Log.d(tag, "lastLine  : " + lastLine);
				Log.d(tag, "mMyLines.size():" + mMyLines.size());
				mStartOffset = 0;
				mDataStartLocation = mMyLines.get(0).getBeforeLineLength();
				mEndOffset = mMyLines.get(mMyLines.size() - 1).getOffset();
				mDataEndLocation = mMyLines.get(mMyLines.size() - 1)
						.getBeforeLineLength();
			} else {
				mCurrentLine = mMyLines.size() - mLinesOfOneScreen;
				if (mCurrentLine < 0) {
					Log.d(tag, "set the mCurrentLine is 0 ....");
					mCurrentLine = 0;
				}
				mStartOffset = mMyLines.get(mCurrentLine).getOffset();
				mDataStartLocation = mMyLines.get(mCurrentLine)
						.getBeforeLineLength();
				mEndOffset = mMyLines.get(mMyLines.size() - 1).getOffset();
				mDataEndLocation = mMyLines.get(mMyLines.size() - 1)
						.getBeforeLineLength();
			}

			Log.d(tag, "mCurrentLine : " + mCurrentLine);
			Log.d(tag, "mDataStartLocation �� " + mDataStartLocation);
			Log.d(tag, "mDataEndLocation : " + mDataEndLocation);
			setData(mDataStartLocation, mDataEndLocation);

			return;
		}

		if (futureLine == 0 && mBeforeOfDoc) {
			mCurrentLine = 0;
			mStartOffset = 0;
			mDataStartLocation = 0;
			int lastLine = mLinesOfOneScreen - 1;
			if (lastLine > mMyLines.size()) {
				mEndOffset = mMyLines.get(mMyLines.size() - 1).getOffset();
				mDataEndLocation = mMyLines.get(mMyLines.size() - 1)
						.getBeforeLineLength();
			} else {
				mEndOffset = mMyLines.get(lastLine).getOffset();
				mDataEndLocation = mMyLines.get(lastLine).getBeforeLineLength();
			}

			Log.d(tag, "futureLine ==0 && mBeforeOfDoc");

			Log.d(tag, "mDataStartLocation : " + mDataStartLocation);
			Log.d(tag, "mDataEndLocation : " + mDataEndLocation);
			setData(mDataStartLocation, mDataEndLocation);
			return;
		}

		if (futureLine > 0) {
			int lastLine = futureLine + mLinesOfOneScreen;
			if (lastLine >= mMyLines.size()) {
				lastLine = mMyLines.size() - 1;
			}
			mCurrentLine = futureLine;
			mStartOffset = mMyLines.get(futureLine).getOffset();
			mDataStartLocation = mMyLines.get(futureLine).getBeforeLineLength();
			mEndOffset = mMyLines.get(lastLine).getOffset();
			mDataEndLocation = mMyLines.get(lastLine).getBeforeLineLength();
			Log.d(tag, "futureLine > 0");

			Log.d(tag, "mDataStartLocation �� " + mDataStartLocation);
			Log.d(tag, "mDataEndLocation : " + mDataEndLocation);
			setData(mDataStartLocation, mDataEndLocation);
		}
	}

	public void displayNextToScreen(int n) {
		String tag = "displayNextToScreen";
		int tempCurrentLine = mCurrentLine;
		int lastLineIndex = mMyLines.size() - 1;
		int futureLine = tempCurrentLine + n;

		if (futureLine + mLinesOfOneScreen > lastLineIndex) {
			if (!mEndOfDoc) {
				Log.d(tag, "read new buffer when skip...");
				readNextBuffer();
				analysisDisplayBuffer();
				mCurrentLine = n;
				lastLineIndex = mMyLines.size() - 1;
				mStartOffset = mMyLines.get(mCurrentLine - 1).getOffset();
				mDataStartLocation = mMyLines.get(mCurrentLine - 1)
						.getBeforeLineLength();
				if (lastLineIndex + 1 < mLinesOfOneScreen) {
					mEndOffset = mMyLines.get(lastLineIndex).getOffset();
					mDataEndLocation = mMyLines.get(lastLineIndex)
							.getBeforeLineLength();
				} else {
					int i = mCurrentLine + mLinesOfOneScreen;
					if (i >= mMyLines.size()) {
						i = mMyLines.size() - 1;
					}
					mEndOffset = mMyLines.get(i).getOffset();
					mDataEndLocation = mMyLines.get(i).getBeforeLineLength();
				}
				Log
						.d(tag,
								"futureLine+mLinesOfOneScreen > lastLineIndex !mEndOfDoc ");
				Log.d(tag, "mDataStartLocation is :" + mDataStartLocation);
				Log.d(tag, "mDataEndLocation is :" + mDataEndLocation);

				setData(mDataStartLocation, mDataEndLocation);
				return;
			}
			if (mEndOfDoc) {
				if (lastLineIndex <= mLinesOfOneScreen) {
					if (mCurrentLine == 0) {
						mStartOffset = mMyLines.get(mCurrentLine).getOffset();
					} else {
						mStartOffset = mMyLines.get(mCurrentLine).getOffset();
						mDataStartLocation = mMyLines.get(mCurrentLine)
								.getBeforeLineLength();
					}
					mEndOffset = mMyLines.get(lastLineIndex).getOffset();
					mDataEndLocation = mMyLines.get(lastLineIndex)
							.getBeforeLineLength();
					Log.d(tag, "lastLineIndex<=mLinesOfOneScreen mEndOfDoc ");
					Log.d(tag, "mDataStartLocation is :" + mDataStartLocation);
					Log.d(tag, "mDataEndLocation is :" + mDataEndLocation);

					setData(mDataStartLocation, mDataEndLocation);
					return;
				} else {
					mStartOffset = mMyLines.get(mCurrentLine).getOffset();
					mDataStartLocation = mMyLines.get(mCurrentLine)
							.getBeforeLineLength();

					mEndOffset = mMyLines.get(lastLineIndex).getOffset();
					mDataEndLocation = mMyLines.get(lastLineIndex)
							.getBeforeLineLength();

					mCurrentLine = lastLineIndex - mLinesOfOneScreen;
					Log.d(tag,
							"  !(lastLineIndex<=mLinesOfOneScreen) mEndOfDoc ");
					Log.d(tag, "mDataStartLocation is :" + mDataStartLocation);
					Log.d(tag, "mDataEndLocation is :" + mDataEndLocation);

					setData(mDataStartLocation, mDataEndLocation);
					return;
				}
			}
		}

		if (futureLine + mLinesOfOneScreen <= lastLineIndex) {
			mCurrentLine = futureLine;
			if (mCurrentLine == 0) {
				mStartOffset = mMyLines.get(mCurrentLine).getOffset();
				mDataStartLocation = mMyLines.get(mCurrentLine)
						.getBeforeLineLength();
			} else {
				mStartOffset = mMyLines.get(mCurrentLine - 1).getOffset();
				mDataStartLocation = mMyLines.get(mCurrentLine - 1)
						.getBeforeLineLength();
			}
			mEndOffset = mMyLines.get(futureLine + mLinesOfOneScreen)
					.getOffset();
			mDataEndLocation = mMyLines.get(futureLine + mLinesOfOneScreen)
					.getBeforeLineLength();

			Log.d(tag, "futureLine+mLinesOfOneScreen <= lastLineIndex ");
			Log.d(tag, "mDataStartLocation is :" + mDataStartLocation);
			Log.d(tag, "mDataEndLocation is :" + mDataEndLocation);

			setData(mDataStartLocation, mDataEndLocation);
			return;
		}
	}

	public int getCurrentLineOffset() {
		return mStartOffset;
	}

	public String getCurrentLineString() {
		int length = mScreenData.length;
		String s = KBConstants.BOOKMARK;
		if (length < 10) {
			try {
				s = new String(mScreenData, this.mEncoding);
			} catch (UnsupportedEncodingException e) {
				return s;
			}
		} else {
			byte[] b = new byte[10];
			System.arraycopy(mScreenData, 0, b, 0, b.length);
			try {
				s = new String(b, this.mEncoding);
			} catch (UnsupportedEncodingException e) {
				return s;
			}
		}
		System.gc();
		return s;
	}

	public long getFileLength() {
		return mFileLength;
	}

	public List<TxtLine> getList() {
		return mMyLines;
	}

	public int getMLinesOfOneScreen() {
		return this.mLinesOfOneScreen;
	}

	public int getOffsetWithPercent(int _percent) {
		int endOffset = 0;
		if (mFileLength != 0) {
			mPercent = _percent;
			endOffset = (int) ((double) _percent * (double) mFileLength / 1000);
		}
		return endOffset;
	}

	public int getPercentWithOffset(int _offset) {
		int percent = 0;
		if (mFileLength != 0) {
			percent = (int) (((double) _offset / (double) mFileLength) * 1000);
			mPercent = percent;
		}
		return percent;
	}

	/** Percent */
	public int getPercent() {
		return mPercent;
	}

	public void setPercent(int _percent) {
		mPercent = _percent;
	}

	public boolean isEnd() {
		return mEndOfDoc;
	}

	public void readBufferByOffset(int offset) {
		String tag = "readBufferByOffset";

		Log.d(tag, "read the data by offset");
		Log.d(tag, "offset is :" + offset);
		mStartOffset = offset;
		mMyLines.clear();
		TxtLine t = new TxtLine(offset, 0, 0);

		mCurrentLine = 0;
		mMyLines.add(t);
		readNextBuffer();
		analysisDisplayBuffer();
		displayNextToScreen(0);
	}

	/**
	 * Read the next buffer
	 */
	private void readNextBuffer() {
		byte[] b = new byte[BUFFER_SIZE];

		mCurrentOffset = (int) mStartOffset;
		mReadFileRandom.openNewStream();
		mReadFileRandom.fastSkip(mStartOffset);

		int actualLength = mReadFileRandom.readBytes(b);

		if (mStartOffset == 0) {
			mBeforeOfDoc = true;
		} else {
			mBeforeOfDoc = false;
		}
		if (actualLength < BUFFER_SIZE) {
			mEndOfDoc = true;
		} else {
			mEndOfDoc = false;
		}

		if (actualLength == -1 && mScreenData.length == 0) {
			mTextView.setText(KBConstants.NODATAINFILE);
			return;
		}

		if (mEndOfDoc) {
			mDisplayBuffer = new byte[actualLength];
			System.arraycopy(b, 0, mDisplayBuffer, 0, actualLength);
			b = null;
			System.gc();
			return;
		}

		int readDataLength = actualLength;
		int nlocation = 0;
		while (readDataLength > 0) {
			if ((b[readDataLength - 1] & 0xff) == 10) {
				nlocation = readDataLength;
				break;
			}
			readDataLength--;
		}

		if (nlocation == 0) {
			System.exit(1);
		}

		int mDisplayBufferLength = nlocation;
		mDisplayBuffer = new byte[mDisplayBufferLength];

		System.arraycopy(b, 0, mDisplayBuffer, 0, mDisplayBufferLength);
		b = null;
		System.gc();
	}

	private void readPreBuffer() {

		int x = mCurrentLine + this.mLinesOfOneScreen;
		int offsetOfLastLineInScreen = 0;
		int sizeLines = mMyLines.size();
		if (x > sizeLines) {
			offsetOfLastLineInScreen = mMyLines.get(sizeLines - 1).getOffset();
		} else {
			offsetOfLastLineInScreen = mMyLines.get(x).getOffset();
		}

		if (offsetOfLastLineInScreen <= BUFFER_SIZE) {
			mBeforeOfDoc = true;
			if (offsetOfLastLineInScreen == mFileLength) {
				mEndOfDoc = true;
			}
			byte[] b = new byte[offsetOfLastLineInScreen];
			mReadFileRandom.openNewStream();
			int readDataLength = mReadFileRandom.readBytes(b);

			mDisplayBuffer = new byte[readDataLength];
			System.arraycopy(b, 0, mDisplayBuffer, 0, readDataLength);
			mCurrentOffset = 0;
			b = null;
			System.gc();
			return;
		}
		int skipLength = offsetOfLastLineInScreen - BUFFER_SIZE;
		mReadFileRandom.openNewStream();
		mReadFileRandom.locate(skipLength);
		mCurrentOffset = skipLength;
		byte[] b = new byte[BUFFER_SIZE];
		int readLength = mReadFileRandom.readBytes(b);
		mBeforeOfDoc = false;
		if (readLength < BUFFER_SIZE) {
			mEndOfDoc = true;
		}

		int nlocation = 0;
		while (nlocation < readLength) {
			if ((b[nlocation] & 0xff) == 10) {
				break;
			}
			nlocation++;
		}
		if (nlocation == readLength) {
			System.exit(1);
		}

		mDisplayBuffer = new byte[readLength];
		System.arraycopy(b, 0, mDisplayBuffer, 0, readLength);
		b = null;
		System.gc();

	}

	private void init() {
		this.mViewHeigth = mScreenHeigth;
		this.mViewWidth = mScreenWidth;
		this.mReadFileRandom = new KBReadFileRandom(this.mFileName);
		this.mFileLength = mReadFileRandom.getFileLength();

		byte[] encodings = new byte[400];
		mReadFileRandom.readBytes(encodings);
		mReadFileRandom.close();
		KBBytesEncodingDetect be = new KBBytesEncodingDetect();
		this.mEncoding = KBBytesEncodingDetect.nicename[be
				.detectEncoding(encodings)];

		if (this.mFileLength == 0) {
			mTextView.setText(KBConstants.NODATAINFILE);
			return;
		}

		/** Initialization screen shows a number of rows of data */
		this.mLinesOfOneScreen = mViewHeigth / (KBCR.fontHeight + KBCR.lineSpace);

		readNextBuffer();
		analysisDisplayBuffer();
		displayNextToScreen(0);
	}

	private void setData(int start, int end) {
		String tag = "setData";
		Log.d(tag, "start index is :" + start);
		Log.d(tag, "end index is :" + end);
		mScreenData = null;
		mScreenData = new byte[end - start];
		mCurrentOffset = mStartOffset;
		mPercent = (int) (((double) mStartOffset / (double) mFileLength) * 1000);
		if (isEnd()) {
			mPercent = 1000;
		}
		Log.d("showPercent", "setData mPercent: " + mPercent);

		System.arraycopy(mDisplayBuffer, start, mScreenData, 0,
				mScreenData.length);
		try {
			// Log.d("setData:", new String(mScreenData, this.mEncoding));
			mTextView.setText(new String(mScreenData, this.mEncoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
