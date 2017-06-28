package com.absurd.rick.zookeeper;

import org.apache.zookeeper.KeeperException;

/**
 * Created by wangwenwei on 17/6/27.
 */
public interface ZkSerializer {
    byte[] serialize(Object data);

    Object deserialize(byte[] bytes) throws KeeperException.MarshallingErrorException;
}
