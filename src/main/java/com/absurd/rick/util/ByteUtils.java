package com.absurd.rick.util;

import java.nio.ByteBuffer;

/**
 * Created by wangwenwei on 17/6/30.
 */
public class ByteUtils {
    public static Long byteToLong(byte[] bytes){
        if (bytes == null || bytes.length == 0) return null;
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static byte[] LongTobyte(Long value){
        if (value == null) return null;
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, value);
        return buffer.array();
    }
}
