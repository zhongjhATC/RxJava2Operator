package com.example.rxjava2operator.net.optional;

import java.util.NoSuchElementException;

import androidx.annotation.Nullable;

/**
 * Optional, 这个是Java8新出的特性。是为了解决null安全问题出的API（核心）
 * @param <T>
 */
public class Optional <T> {

    private final T optional; // 接收到的返回结果

    public Optional(@Nullable T optional) {
        this.optional = optional;
    }

    // 判断返回结果是否为null
    public boolean isEmpty() {
        return this.optional == null;
    }

    // 获取不能为null的返回结果，如果为null，直接抛异常，经过二次封装之后，这个异常最终可以在走向RxJava的onError()
    public T get() {
        if (optional == null) {
            throw new NoSuchElementException("No value present");
        }
        return optional;
    }

    // 获取可以为null的返回结果
    public T getIncludeNull() {
        return optional;
    }
}
