package com.absurd.rick.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import java.util.List;

/**
 * Created by wangwenwei on 17/6/27.
 */
public class ZooKeeperOperator extends AbstractZookeeper {

    public String createEphemeralSequential(final String path, final List<ACL> acl) throws RuntimeException, KeeperException, InterruptedException {
      return create(path, null, acl, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public String createEphemeralSequential(final String path, byte[] data) throws RuntimeException, KeeperException, InterruptedException {
       return create(path, data, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public String createEphemeralSequential(final String path) throws RuntimeException, KeeperException, InterruptedException {
       return create(path, null, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public String createEphemeral(final String path, final List<ACL> acl) throws RuntimeException, KeeperException, InterruptedException {
       return create(path, null, acl, CreateMode.EPHEMERAL);
    }

    public String createEphemeral(final String path, byte[] data) throws RuntimeException, KeeperException, InterruptedException {
        return create(path, data, CreateMode.EPHEMERAL);
    }

    public String createEphemeral(final String path) throws RuntimeException, KeeperException, InterruptedException {
        return create(path, null, CreateMode.EPHEMERAL);
    }

    public void createPersistent(String path) throws RuntimeException, KeeperException, InterruptedException {
        createPersistent(path, false);
    }

    public String createPersistent(String path, Object data, List<ACL> acl) throws KeeperException, InterruptedException {
        return create(path, zkSerializer.serialize(data), acl, CreateMode.PERSISTENT);
    }

    public String createPersistent(String path, byte[] data, List<ACL> acl) throws KeeperException, InterruptedException {
        return create(path, data, acl, CreateMode.PERSISTENT);
    }

    public String createPersistent(String path, Object data) throws RuntimeException, KeeperException, InterruptedException {
        return create(path, zkSerializer.serialize(data), CreateMode.PERSISTENT);
    }

    public String createPersistent(String path, byte[] data) throws RuntimeException, KeeperException, InterruptedException {
        return create(path, data, CreateMode.PERSISTENT);
    }

    public void createPersistent(String path, boolean createParents) throws RuntimeException, KeeperException, InterruptedException {
        createPersistent(path, createParents, ZooDefs.Ids.OPEN_ACL_UNSAFE);
    }

    public String createPersistentSequential(String path, Object data) throws RuntimeException, KeeperException, InterruptedException {
        return create(path, zkSerializer.serialize(data), CreateMode.PERSISTENT_SEQUENTIAL);
    }

    public String createPersistentSequential(String path, byte[] data) throws RuntimeException, KeeperException, InterruptedException {
        return create(path, data, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    public String createPersistentSequential(String path, byte[] data, List<ACL> acl) throws RuntimeException, KeeperException, InterruptedException {
        return create(path, data, acl, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    public void createPersistent(String path, boolean createParents, List<ACL> acl) throws   RuntimeException, KeeperException, InterruptedException {
        try {
            create(path, null, acl, CreateMode.PERSISTENT);
        } catch (KeeperException.NodeExistsException e) {
            if (!createParents) {
                throw e;
            }
        } catch (KeeperException.NoNodeException e) {
            if (!createParents) {
                throw e;
            }
            String parentDir = path.substring(0, path.lastIndexOf('/'));
            createPersistent(parentDir, createParents, acl);
            createPersistent(path, createParents, acl);
        }
    }

    public String create(final String path, byte[] data, final List<ACL> acl, final CreateMode mode) throws KeeperException, InterruptedException {
        if (path == null) {
            throw new NullPointerException("Missing value for path");
        }
        if (acl == null || acl.size() == 0) {
            throw new NullPointerException("Missing value for ACL");
        }
        return this.zk.create(path, data, acl, mode);
    }

    public String create(final String path, byte[] data, final CreateMode mode) throws RuntimeException, KeeperException, InterruptedException {
        return create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
    }

    public byte[] getData(final String path, boolean watch,Stat stat) throws KeeperException, InterruptedException {
        return this.zk.getData(path,watch,stat);
    }

    public Stat exists(final String path, final boolean watch) throws KeeperException, InterruptedException {
       return this.zk.exists(path,watch);
    }


    public List<String> getChildren(String parent, boolean watch) throws KeeperException, InterruptedException {
       return this.zk.getChildren(parent,watch);
    }

    public void delete(String path,int version) throws KeeperException, InterruptedException {
        this.zk.delete(path,version);
    }
}
