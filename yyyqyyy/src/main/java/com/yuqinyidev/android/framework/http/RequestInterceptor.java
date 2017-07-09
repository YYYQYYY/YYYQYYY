package com.yuqinyidev.android.framework.http;

import android.support.annotation.Nullable;

import com.yuqinyidev.android.framework.utils.CharacterHandler;
import com.yuqinyidev.android.framework.utils.ZipHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import timber.log.Timber;

/**
 * Created by RDX64 on 2017/6/26.
 */
@Singleton
public class RequestInterceptor implements Interceptor {
    private GlobalHttpHandler mHandler;

    @Inject
    public RequestInterceptor(@Nullable GlobalHttpHandler handler) {
        this.mHandler = handler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        boolean hasRequestBody = request.body() != null;

        Timber.tag(getTag(request, "Request_Info")).w("Params : 「 %s 」%nConnection : 「 %s 」%nHeaders : %n「 %s 」",
                hasRequestBody ? parseParams(request.newBuilder().build().body()) : "Null",
                chain.connection(), request.headers());

        long t1 = System.nanoTime();

        Response originalResponse;
        try {
            originalResponse = chain.proceed(request);
        } catch (Exception e) {
            Timber.w("Http Error: " + e);
            throw e;
        }

        long t2 = System.nanoTime();

        String bodySize;
        if (originalResponse.body() != null && originalResponse.body().contentLength() != -1) {
            bodySize = originalResponse.body().contentLength() + "-byte";
        } else {
            bodySize = "unknown-length";
        }

        Timber.tag(getTag(request, "Response_Info")).w("Received response in [ %d-ms ] , [ %s ]%n%s",
                TimeUnit.NANOSECONDS.toMillis(t2 - t1), bodySize, originalResponse.headers());

        String bodyString = printResult(request, originalResponse);

        if (mHandler != null) {
            return mHandler.onHttpResultResponse(bodyString, chain, originalResponse);
        }

        return originalResponse;
    }

    @Nullable
    private String printResult(Request request, Response originalResponse) throws IOException {
        ResponseBody responseBody = originalResponse.body();
        String bodyString = null;
        if (isParseable(responseBody.contentType())) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            String encoding = originalResponse.headers().get("Content-Encoding");
            Buffer clone = buffer.clone();
            bodyString = parseContent(responseBody, encoding, clone);
            Timber.tag(getTag(request, "Response_Result"))
                    .w(isJson(responseBody.contentType()) ? CharacterHandler.formatJson(bodyString) : bodyString);
        } else {
            Timber.tag(getTag(request, "Response_Result")).w("This result isn't parsed");
        }
        return bodyString;
    }

    private String getTag(Request request, String tag) {
        return String.format(" [%s] 「 %s 」>>> %s", request.method(), request.url().toString(), tag);
    }

    private String parseContent(ResponseBody responseBody, String encoding, Buffer clone) {
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
            return ZipHelper.decompressForGZip(clone.readByteArray(), convertCharset(charset));
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {
            return ZipHelper.decompressToStringForZLib(clone.readByteArray(), convertCharset(charset));
        } else {
            return clone.readString(charset);
        }
    }

    public static String parseParams(RequestBody body) throws UnsupportedEncodingException {
        if (isParseable(body.contentType())) {
            try {
                Buffer requestBuffer = new Buffer();
                body.writeTo(requestBuffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                return URLDecoder.decode(requestBuffer.readString(charset), convertCharset(charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "This params isn't pased";
    }

    public static boolean isParseable(MediaType mediaType) {
        return mediaType != null &&
                (
                        isText(mediaType) || isJson(mediaType) ||
                                isForm(mediaType) || isHtml(mediaType) || isXml(mediaType)
                );
    }

    public static boolean isText(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("text");
    }

    public static boolean isJson(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("json");
    }

    public static boolean isForm(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("x-www-form-urlencoded");
    }

    public static boolean isHtml(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("html");
    }

    public static boolean isXml(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("xml");
    }

    public static String convertCharset(Charset charset) {
        String s = charset.toString();
        int i = s.indexOf("[");
        if (i == -1) {
            return s;
        }
        return s.substring(i + 1, s.length() - 1);
    }
}
