package com.jecber.dubbo.serialize.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;

import java.io.IOException;


public class ProtostuffUtils {

    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer) {
        return ProtobufIOUtil.toByteArray(message, schema, buffer);
    }

    public static <T> void mergeFrom(byte[] in, T message, Schema<T> schema) throws IOException {
        ProtobufIOUtil.mergeFrom(in, message, schema);
    }
}
