package org.qee.cloud.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.qee.cloud.common.model.URL;

import java.util.List;

/**
 * @ProjectName: qee-cloud
 * @Package: org.qee.cloud.registry.zookeeper
 * @ClassName: CuratorWatcherImpl
 * @Description:
 * @Date: 2021/11/22 5:30 下午
 * @Version: 1.0
 */
public class CuratorWatcherImpl implements CuratorWatcher {

    private String parentPath;

    private List<URL> providerUrls;

    private CuratorFramework curatorFramework;

    public CuratorWatcherImpl(String parentPath, List<URL> providerUrls, CuratorFramework curatorFramework) {
        this.parentPath = parentPath;
        this.providerUrls = providerUrls;
        this.curatorFramework = curatorFramework;
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
        if (event.getType() == Watcher.Event.EventType.None) {
            return;
        }
        curatorFramework.getChildren().usingWatcher(this).forPath(parentPath);
        List<String> childPathList = curatorFramework.getChildren().forPath(parentPath);
        providerUrls.addAll(ss);
    }
}
