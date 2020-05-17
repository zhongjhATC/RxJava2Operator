package com.example.rxjava2operator.net;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface IPApi {

    /**
     * 根据openId获取用户
     * @return 返回数据源
     */
    @GET("/")
    Observable<String> GetIP();

    @GET("/")
    Flowable<String> GetIP2();

}
