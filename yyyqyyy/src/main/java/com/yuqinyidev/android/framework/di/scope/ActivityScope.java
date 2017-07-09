package com.yuqinyidev.android.framework.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by RDX64 on 2017/6/28.
 */
@Scope
@Retention(RUNTIME)
public @interface ActivityScope {
}
