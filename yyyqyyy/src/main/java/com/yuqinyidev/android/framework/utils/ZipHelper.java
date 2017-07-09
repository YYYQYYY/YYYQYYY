package com.yuqinyidev.android.framework.utils;

import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 * Created by RDX64 on 2017/6/26.
 */

public class ZipHelper {
    private ZipHelper() {
    }

    public static String decompressToStringForZLib(byte[] byteToDecompress) {
        return decompressToStringForZLib(byteToDecompress, "UTF-8");
    }

    public static String decompressToStringForZLib(byte[] byteToDecompress, String charset) {
        String resultSting = null;
        byte[] byteToDecompressed = decompressZLib(byteToDecompress);
        try {
            resultSting = new String(byteToDecompressed, 0, byteToDecompressed.length, charset);
        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        }
        return resultSting;
    }

    public static byte[] decompressZLib(byte[] byteToDecompress) {
        byte[] resultValues = null;
        Inflater inflater = new Inflater();

        int byteToDecompressLength = byteToDecompress.length;

        inflater.setInput(byteToDecompress, 0, byteToDecompressLength);

        int bufferSizeInBytes = byteToDecompressLength;

        int numberOfBytesDecompressSoFar = 0;
        List<Byte> bytesDecompressSoFar = new ArrayList<>();

        try {
            while (!inflater.needsInput()) {
                byte[] bytesDecompressedBuffer = new byte[bufferSizeInBytes];
                int numberOfBytesDecompressThisTime = inflater.inflate(bytesDecompressedBuffer);
                numberOfBytesDecompressSoFar += numberOfBytesDecompressThisTime;

                for (int i = 0; i < numberOfBytesDecompressThisTime; i++) {
                    bytesDecompressSoFar.add(bytesDecompressedBuffer[i]);
                }
            }
            resultValues = new byte[bytesDecompressSoFar.size()];
            for (int i = 0; i < resultValues.length; i++) {
                resultValues[i] = bytesDecompressSoFar.get(i);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
        }

        inflater.end();

        return resultValues;
    }

    public static byte[] compressZLib(byte[] bytesToCompress) {
        Deflater deflater = new Deflater();
        deflater.setInput(bytesToCompress);
        deflater.finish();

        byte[] bytesCompressed = new byte[Short.MAX_VALUE];
        int numberOfBytesAfterCompression = deflater.deflate(bytesCompressed);
        byte[] resultValues = new byte[numberOfBytesAfterCompression];

        System.arraycopy(bytesCompressed, 0, resultValues, 0, numberOfBytesAfterCompression);

        return resultValues;
    }

    public static byte[] compressZLib(String stringToCompress) {
        byte[] resultValues = null;
        try {
            resultValues = compressZLib(stringToCompress.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultValues;
    }

    @Nullable
    public static byte[] compressGLib(String stringToCompress) {
        ByteArrayOutputStream os = null;
        GZIPOutputStream gos = null;
        try {
            os = new ByteArrayOutputStream(stringToCompress.length());
            gos = new GZIPOutputStream(os);
            gos.write(stringToCompress.getBytes("UTF-8"));
            byte[] compressed = os.toByteArray();
            return compressed;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(gos);
            closeQuietly(os);
        }
        return null;
    }

    public static String decompressForGZip(byte[] byteToDecompress) {
        return decompressForGZip(byteToDecompress, "UTF-8");
    }

    @Nullable
    public static String decompressForGZip(byte[] byteToDecompress, String charset) {
        final int BUFFER_SIZE = byteToDecompress.length;
        ByteArrayInputStream is = null;
        GZIPInputStream gis = null;
        try {
            is = new ByteArrayInputStream(byteToDecompress);
            gis = new GZIPInputStream(is, BUFFER_SIZE);
            StringBuilder sBuilder = new StringBuilder();
            byte[] data = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = gis.read(data)) != -1) {
                sBuilder.append(new String(data, 0, bytesRead, charset));
            }
            return sBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(gis);
            closeQuietly(is);
        }
        return null;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
