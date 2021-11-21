package org.qee.cloud.registry.zookeeper;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.registry.api.NotifyListener;
import org.qee.cloud.registry.api.RegistryCenter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ZookeeperRegistryCenter implements RegistryCenter {

    public String ROOT = "/cloud/zookeeper/";

    private final ConcurrentHashMap<String, Object> persistentExistNodePath = new ConcurrentHashMap<>();

    private final Object NULL_OBJECT = new Object();

    private CuratorFramework curatorFramework;

    public ZookeeperRegistryCenter(URL url) {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(url.getHost() + ":" + url.getPort())
                .retryPolicy(new RetryUntilElapsed(5000, 200))
                .sessionTimeoutMs(200000)
                .build();
        curatorFramework.start();
    }


    @Override
    public void register(URL url) {

    }

    @Override
    public void unregister(URL url) {

    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {

    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {

    }

    // /cloud/zookeeper/abc
    public void recursiveCreateNode(String path, boolean ephemeral) throws Exception {
        if (!ephemeral) {
            if (persistentExistNodePath.keySet().contains(path)) {
                return;
            }
            if (curatorFramework.checkExists().forPath(path) != null) {
                persistentExistNodePath.put(path, NULL_OBJECT);
                return;
            }
        }
        String parentPath = path.substring(0, path.lastIndexOf("/"));
        if (StringUtils.isNoneBlank(parentPath)) {
            recursiveCreateNode(parentPath, false);
        }
        CreateMode createMode = CreateMode.EPHEMERAL;
        if (!ephemeral) {
            createMode = CreateMode.PERSISTENT;
        }
        if (curatorFramework.checkExists().forPath(path) == null) {
            curatorFramework.create().withMode(createMode).forPath(path);
        }
        if (!ephemeral) {
            persistentExistNodePath.put(path, NULL_OBJECT);
        }


    }

    public void callback(String path, Consumer<WatchedEvent> function) throws Exception {
        curatorFramework.getData().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                function.accept(watchedEvent);
            }
        }).forPath(path);
    }


    public void watchChild(String parentPath, Consumer<WatchedEvent> function) throws Exception {
        curatorFramework.getChildren().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent watchedEvent) throws Exception {
                function.accept(watchedEvent);
            }
        }).forPath(parentPath);
    }

    public void setData(String path, String value) throws Exception {
        curatorFramework.setData().forPath(path, value.getBytes());
    }

    public String getData(String path) throws Exception {
        return new String(curatorFramework.getData().forPath(path));
    }

    public void close() {
        curatorFramework.close();
    }


    public static void main(String[] args) throws Exception {
        URL url = URL.valueOf("zookeeper://127.0.0.1:2181");
        ZookeeperRegistryCenter zookeeperRegistryCenter = new ZookeeperRegistryCenter(url);

        zookeeperRegistryCenter.recursiveCreateNode("/cloud/zookeeper/org.qee.service.DemoService:*:*/127.0.0.1:20881", true);

        String data = zookeeperRegistryCenter.getData("/cloud/zookeeper/org.qee.service.DemoService:*:*/127.0.0.1:20881");
        System.out.println(data);

    }

}
