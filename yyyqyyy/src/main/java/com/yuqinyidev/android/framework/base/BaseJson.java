package com.yuqinyidev.android.framework.base;

import java.io.Serializable;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class BaseJson<T> implements Serializable {
    public static final String STATE_SUCCESS = "200";

    private String code;
    private String message;
    private T data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return STATE_SUCCESS.equals(code);
    }
}
