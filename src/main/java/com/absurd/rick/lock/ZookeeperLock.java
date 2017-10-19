package com.absurd.rick.lock;

import com.absurd.rick.zookeeper.ZooKeeperOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by wangwenwei on 17/6/26.
 */
@Slf4j
public class ZookeeperLock implements Lock {
    private ZooKeeperOperator zk;

    private final String lockName;

    private final String KEY_PREIX = "_lock_";

    private final String root = "/lock";


    private String waitNode;//等待前一个锁

    private String myZnode;


    public ZookeeperLock(ZooKeeperOperator zk,String lockName) {
        this.lockName = lockName;
        this.zk = zk;
        try {
            this.zk.connect();
            Stat stat = this.zk.exists(root, false);
            if (stat == null) {
                zk.createPersistent(root, new byte[0]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void lock() {
        if (tryLock()) {
            log.info("Thread {} get the Lock {}", Thread.currentThread().getId(), myZnode);
            return;
        }
        try {
            waitForLock(waitNode, this.zk.getSessionTimeout());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    private boolean waitForLock(String lower, long waitTime) throws InterruptedException, KeeperException {
        Stat stat = this.zk.exists(root + "/" + lower, true);//同时注册监听。
        //判断比自己小一个数的节点是否存在,如果不存在则无需等待锁,同时注册监听
        if (stat != null) {
            log.info("Thead {} waiting for {} ", Thread.currentThread().getId(), root + "/" + lower);
            this.zk.setLatch(new CountDownLatch(1) );
            this.zk.getLatch().await(waitTime, TimeUnit.MILLISECONDS);//等待，这里应该一直等待其他线程释放锁
            this.zk.setLatch( null );
        }
        return true;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.lock();
    }

    @Override
    public boolean tryLock() {
        try {
            //创建临时子节点
            myZnode = zk.createEphemeralSequential(root + "/" + lockName + KEY_PREIX, new byte[0]);
            log.info(myZnode + " is created ");
            //取出所有子节点
            List<String> subNodes = zk.getChildren(root, false);
            //取出所有lockName的锁
            List<String> lockObjNodes = new ArrayList<>();
            for (String node : subNodes) {
                String _node = node.split(KEY_PREIX)[0];
                if (_node.equals(lockName)) {
                    lockObjNodes.add(node);
                }
            }
            Collections.sort(lockObjNodes);

            if (myZnode.equals(root + "/" + lockObjNodes.get(0))) {
                //如果是最小的节点,则表示取得锁
                log.info(myZnode + "==" + lockObjNodes.get(0));
                return true;
            }
            //如果不是最小的节点，找到比自己小1的节点
            String subMyZnode = myZnode.substring(myZnode.lastIndexOf("/") + 1);
            waitNode = lockObjNodes.get(Collections.binarySearch(lockObjNodes, subMyZnode) - 1);//找到前一个子节点
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        try {
            if(this.tryLock()){
                return true;
            }
            return waitForLock(waitNode,calcSeconds(time,unit));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void unlock() {
//        try {
            log.info("unlock {}",myZnode);
//            this.zk.delete(myZnode,-1);
            myZnode = null;
            this.zk.close();
//        } catch (KeeperException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("不支持当前的操作");
    }

    /**
     * 时间转换成毫秒
     * @param time
     * @param unit
     * @return
     */
    private long calcSeconds(long time, TimeUnit unit){
        if (unit == TimeUnit.DAYS) {
            return time * 24 * 60 * 60 * 1000;
        } else if (unit == TimeUnit.HOURS) {
            return time * 60 * 60 * 1000;
        } else  if (unit == TimeUnit.MINUTES) {
            return time * 60 * 1000;
        } else {
            return time * 1000;
        }
    }
}
