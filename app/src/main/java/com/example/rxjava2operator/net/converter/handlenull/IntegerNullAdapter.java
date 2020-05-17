package com.example.rxjava2operator.net.converter.handlenull;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 处理int为null的
 * Created by zhongjh on 2017/6/29.
 */
public class IntegerNullAdapter extends TypeAdapter<Integer> {
    @Override
    public Integer read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return 0;
        }
        return reader.nextInt();
    }

    @Override
    public void write(JsonWriter writer, Integer value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }
}