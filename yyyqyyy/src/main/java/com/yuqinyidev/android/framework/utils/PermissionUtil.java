package com.yuqinyidev.android.framework.utils;

import android.Manifest;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import timber.log.Timber;

/**
 * Created by RDX64 on 2017/6/29.
 */

public class PermissionUtil {
    public static final String TAG = "PermissionUtil";

    private PermissionUtil() {
    }

    public interface RequestPermission {
        void onRequestPermissionSuccess();

        void onRequestPermissionFailure();
    }

    public static void requestPermission(final RequestPermission requestPermission,
                                         RxPermissions rxPermissions, RxErrorHandler errorHandler, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return;
        }
        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }

        if (needRequest.size() == 0) {
            requestPermission.onRequestPermissionSuccess();
        } else {
            rxPermissions
                    .request(needRequest.toArray(new String[needRequest.size()]))
                    .subscribe(new ErrorHandleSubscriber<Boolean>(errorHandler) {
                        @Override
                        public void onNext(@NonNull Boolean granted) {
                            if (granted) {
                                Timber.tag(TAG).d("Request permissions success");
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                Timber.tag(TAG).d("Request permissions failure");
                                requestPermission.onRequestPermissionFailure();
                            }
                        }
                    });
        }
    }

    public static void launchCamera(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    public static void externalStorage(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void sendSms(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.SEND_SMS);
    }

    public static void callPhone(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.CALL_PHONE);
    }

    public static void readPhoneState(RequestPermission requestPermission, RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler, Manifest.permission.READ_PHONE_STATE);
    }
}
