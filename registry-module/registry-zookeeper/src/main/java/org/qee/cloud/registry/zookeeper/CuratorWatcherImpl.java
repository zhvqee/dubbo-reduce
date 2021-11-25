package org.qee.cloud.registry.zookeeper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.qee.cloud.common.model.URL;

import java.util.ArrayList;
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

    private URL consumerUrl;

    public CuratorWatcherImpl(String parentPath, URL consumerUrl, List<URL> providerUrls, CuratorFramework curatorFramework) {
        this.parentPath = parentPath;
        this.providerUrls = providerUrls;
        this.curatorFramework = curatorFramework;
        this.consumerUrl = consumerUrl;
        List<String> childPaths = null;
        try {
            childPaths = curatorFramework.getChildren().usingWatcher(this).forPath(parentPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.providerUrls.addAll(toUrlsWithoutEmpty(consumerUrl, childPaths));
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
        if (event.getType() == Watcher.Event.EventType.None) {
            return;
        }
        List<String> childPaths = curatorFramework.getChildren().usingWatcher(this).forPath(parentPath);
        providerUrls.addAll(toUrlsWithoutEmpty(null, childPaths));
    }

    private List<URL> toUrlsWithEmpty(URL consumer, String path, List<String> providers) {
        List<URL> urls = toUrlsWithoutEmpty(consumer, providers);

        return urls;
    }

    private List<URL> toUrlsWithoutEmpty(URL consumer, List<String> providers) {
        List<URL> urls = new ArrayList<>();
        String groupVersion = parentPath.substring(parentPath.indexOf(":") + 1);
        String[] gv = groupVersion.split(":");
        if (CollectionUtils.isNotEmpty(providers)) {
            for (String provider : providers) {
                String[] split = provider.split(":");
                URL provioderUrl = URL.builder().protocol(consumer.getProtocol())
                        .path(consumer.getPath())
                        .host(split[0])
                        .port(Integer.parseInt(split[1]))
                        .build();
                provioderUrl.addParameter("service.group", gv[0]);
                provioderUrl.addParameter("service.version", gv[1]);
                urls.add(provioderUrl);
            }
        }
        return urls;
    }
}
