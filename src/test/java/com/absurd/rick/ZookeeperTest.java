package com.absurd.rick;


import com.absurd.rick.zookeeper.ZooKeeperOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by wangwenwei on 17/6/27.
 */
@Slf4j
public class ZookeeperTest {
    @Test
    public void initZookeeper() throws IOException, KeeperException, InterruptedException {
        ZooKeeperOperator zk = new ZooKeeperOperator();
        zk.setAddress("localhost:2181");
        zk.setSessionTimeout(1000);
        zk.setConnectionTimeout(2000);
        zk.connect();
        zk.createEphemeral("/lock",new byte[0]);
        Stat stat = zk.exists("/lock",true);
        log.info(""+stat.toString());
        zk.close();

    }
}
