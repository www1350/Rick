package com.absurd.rick.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wangwenwei on 17/6/27.
 */
@Slf4j
public class AbstractZookeeper implements Watcher{
    protected ZooKeeper zk;

    private String address;

    private int sessionTimeout;

    private int connectionTimeout;

    private CountDownLatch latch;//计数器

    private CountDownLatch connectedSignal=new CountDownLatch(1);

    protected ZkSerializer zkSerializer;


    public AbstractZookeeper() {
        zkSerializer = new JsonZkSerializer();
    }

    public void connect(){
        try {
            zk = new ZooKeeper(address,sessionTimeout,this);
            connectedSignal.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            if (zk!=null) {
                zk.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //建立连接用
        if(watchedEvent.getState()== Event.KeeperState.SyncConnected){
            connectedSignal.countDown();
            return;
        }
        if (this.latch != null){
            latch.countDown();
        }
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public ZkSerializer getZkSerializer() {
        return zkSerializer;
    }

    public void setZkSerializer(ZkSerializer zkSerializer) {
        this.zkSerializer = zkSerializer;
    }
}
