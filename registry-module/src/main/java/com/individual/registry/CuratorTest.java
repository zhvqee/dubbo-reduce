package com.individual.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class CuratorTest {

    private CuratorFramework curatorFramework;

    public void init() {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(new RetryUntilElapsed(5000, 200))
                .sessionTimeoutMs(200000)
                .build();

        curatorFramework.start();
    }

    public void createNode(String path) throws Exception {
        if (curatorFramework.checkExists().forPath(path) == null) {
            curatorFramework.create().forPath(path);
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
        test1();
    }

    private static void test1() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        CuratorTest curatorTest1 = new CuratorTest();
        CuratorTest curatorTest2 = new CuratorTest();
        curatorTest1.init();
        curatorTest1.createNode("/test");

        curatorTest1.createNode("/test/abc");
        curatorTest2.init();
        new Thread(() -> {
            while (true) {

                try {
                    curatorTest1.watchChild("/test", watchedEvent -> {
                        System.out.println(watchedEvent.toString());
                        try {
                            String data = curatorTest1.getData("/test");
                            System.out.println("watchedEvent:" + watchedEvent.toString() + ",:" + data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }).start();

        new Thread(() -> {
            long i = 0;
            while (true) {
                try {
                    curatorTest2.setData("/test/abc", "current value:" + i);
                    String data = curatorTest2.getData("/test/abc");
                    curatorTest2.close();
                    break;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }).start();
        Thread.sleep(500000);
    }
}
