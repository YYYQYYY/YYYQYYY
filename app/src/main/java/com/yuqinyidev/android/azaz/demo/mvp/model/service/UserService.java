package com.yuqinyidev.android.azaz.demo.mvp.model.service;

import com.yuqinyidev.android.azaz.demo.mvp.model.entity.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by RDX64 on 2017/6/29.
 */

public interface UserService {
    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    @Headers(HEADER_API_VERSION)
    @GET("/users")
    Observable<List<User>> getUser(@Query("since") int lastIdQueried, @Query("per_page") int perPage);
}
