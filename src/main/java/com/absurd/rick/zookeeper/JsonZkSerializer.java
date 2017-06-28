package com.absurd.rick.zookeeper;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.KeeperException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by wangwenwei on 17/6/27.
 */
public class JsonZkSerializer implements ZkSerializer{
    @Override
    public byte[] serialize(Object data) {
        String jsonStr = JSON.toJSONString(data);
        return jsonStr.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public Object deserialize(byte[] bytes) throws KeeperException.MarshallingErrorException {
        try {
            String jsonStr = new String(bytes, "utf-8");
            return JSON.parseObject(jsonStr);
        } catch (UnsupportedEncodingException e) {
            try {
                return new String(bytes, "utf-8");
            } catch (UnsupportedEncodingException e1) {
                throw new KeeperException.MarshallingErrorException();
            }
        }
    }
}
