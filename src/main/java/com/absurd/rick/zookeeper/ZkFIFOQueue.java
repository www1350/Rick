package com.absurd.rick.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangwenwei on 17/6/27.
 */
public class ZkFIFOQueue implements BlockingQueue {
    private ZooKeeperOperator zk;

    private final String root="/queue";

    private final String queueName;

    private String queueRoot;

    public ZkFIFOQueue(ZooKeeperOperator zk, String queueName) {
        this.zk = zk;
        this.queueName = queueName;

        queueRoot = root + "-" + queueName;
        try {
            this.zk.connect();
            Stat stat = this.zk.exists(queueRoot, false);
            if (stat == null) {
                zk.createPersistent(queueRoot, new byte[0]);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }



    @Override
    public int size() {
        try {
            List<String> list = this.zk.getChildren(queueRoot, true);
            return list.size();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int drainTo(Collection c) {
        return 0;
    }

    @Override
    public int drainTo(Collection c, int maxElements) {
        return 0;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        try {
            this.zk.createPersistentSequential(queueRoot+"/element",o);
        } catch (KeeperException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        for(Object e: c){
            this.add(e);
        }
        return true;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public boolean offer(Object o) {
        return false;
    }

    @Override
    public void put(Object o) throws InterruptedException {

    }

    @Override
    public boolean offer(Object o, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Object take() throws InterruptedException {
        while(true){
            if (isEmpty()){
                this.zk.setLatch(new CountDownLatch(1) );
                this.zk.getLatch().await(this.zk.getSessionTimeout(), TimeUnit.MILLISECONDS);//等待，这里应该一直等待其他线程释放锁
                this.zk.setLatch( null );
            }else{
                return peek();
            }
        }
    }

    @Override
    public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public Object remove() {
        return null;
    }

    @Override
    public Object poll() {
        return null;
    }

    @Override
    public Object element() {
        return null;
    }

    @Override
    public Object peek() {
        Stat stat = null;
        try {
            List<String> list = this.zk.getChildren(queueRoot, true);
            String minNode = list.get(0).substring(7);
            Integer min = Integer.parseInt(minNode.trim());
            for(String n : list){
                String node = n.substring(7);
                Integer nodeIndex = Integer.parseInt(node.trim());
                if (min < nodeIndex){
                    min = nodeIndex;
                    minNode = node;
                }
            }
            byte[] data = this.zk.getData(queueRoot+"/element"+minNode ,false,stat);
            this.zk.delete(queueRoot + "/element" + minNode, 0);
            return zk.getZkSerializer().deserialize(data);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
