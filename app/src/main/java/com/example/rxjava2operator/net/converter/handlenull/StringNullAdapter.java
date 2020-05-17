package com.example.rxjava2operator.net.converter.handlenull;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 处理字符串为null的
 * Created by zhongjh on 2017/6/29.
 */
public class StringNullAdapter extends TypeAdapter<String> {
    @Override
    public String read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return "";
        }
        return reader.nextString();
    }

    @Override
    public void write(JsonWriter writer, String value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }
}