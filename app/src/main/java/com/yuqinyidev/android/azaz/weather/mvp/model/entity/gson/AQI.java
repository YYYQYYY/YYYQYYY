package com.yuqinyidev.android.azaz.weather.mvp.model.entity.gson;

/**
 * Created by RDX64 on 2017/6/18.
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
