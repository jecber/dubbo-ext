package com.jecber.dubbo.serialize.protostuff;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jecber on 2016/9/19.
 */
public class ProtostuffSerialization implements Serialization {

    static {
        System.setProperty("protostuff.runtime.collection_schema_on_repeated_fields", "true");
        System.setProperty("protostuff.runtime.morph_collection_interfaces", "true");
        System.setProperty("protostuff.runtime.morph_map_interfaces", "true");
    }


    public byte getContentTypeId() {
        return 11;
    }

    public String getContentType() {
        return "x-application/protostuff";
    }

    public ObjectOutput serialize(URL url, OutputStream outputStream) throws IOException {
        return new ProtostuffObjectOutput(outputStream);
    }

    public ObjectInput deserialize(URL url, InputStream inputStream) throws IOException {
        return new ProtostuffObjectInput(inputStream);
    }
}
