package com.example.rxjava2operator.net;

import com.example.rxjava2operator.BaseApplication;
import com.example.rxjava2operator.BuildConfig;
import com.example.rxjava2operator.net.converter.CustomConverterFactory;
import com.example.rxjava2operator.net.converter.CustomResponseBodyConverter;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by baixiaokang on 16/3/9.
 * Update by zhongjh on 17/12/11. 这是封装了传递数据和返回数据的一些方法
 */
public class Api {

    public static final String ENDPOINT = "https://api.ipify.org/";

    public Retrofit retrofit;

    //构造方法私有
    protected Api() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File cacheFile = new File(BaseApplication.getInstance().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        OkHttpClient okHttpClient;
        if (BuildConfig.DEBUG) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)  // 读取超时最大时间
                    .connectTimeout(10, TimeUnit.SECONDS) // 连接超时最大时间
                    .addNetworkInterceptor(new StethoInterceptor())
                    .cache(cache)
                    .build();
        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)  // 读取超时最大时间
                    .connectTimeout(10, TimeUnit.SECONDS)  // 连接超时最大时间
                    .cache(cache)
                    .build();
        }

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(CustomConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ENDPOINT)
                .build();
    }

    public static Api getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final Api INSTANCE = new Api();
    }

}