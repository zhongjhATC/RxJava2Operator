package com.example.rxjava2operator.net.converter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * 自定义返回的数据源
 * 更加复杂包括是否成功的可以参考公司项目的此类
 * Created by zhongjh on 2017/6/1.
 */
public class CustomResponseBodyConverter implements Converter<ResponseBody, String> {

    private final Type type;

    public CustomResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public String convert(ResponseBody value) throws IOException {

//        String httpResult = "";
//
//        JSONObject jObject;

        return value.string();
    }

    /**
     * 截断输出日志，原因是超过4000会打印不全
     *
     * @param msg 文本
     */
    public static void e(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.e(tag, msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.e(tag, logContent);
            }
            Log.e(tag, msg);// 打印剩余日志
        }
    }

}