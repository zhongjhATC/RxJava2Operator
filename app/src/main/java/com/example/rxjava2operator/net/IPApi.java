package com.example.rxjava2operator.net;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * https都是可以支持的
 */
public interface IPApi {

    /**
     * 根据openId获取用户
     * @return 返回数据源
     */
    @GET("/")
    Observable<String> GetIP();

    /**
     * 这种形式可以直接传url进去
     * @param url url
     * @param message 参数
     */
    @POST
    Observable<String> GetIP2(@Url String url, @Body String message);

}
