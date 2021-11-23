import org.junit.Test;
import org.qee.cloud.common.model.URL;
import org.qee.cloud.registry.zookeeper.ZookeeperRegistry;

/**
 * @ProjectName: qee-cloud
 * @Package: PACKAGE_NAME
 * @ClassName: ZookeeperRegistryCenterTest
 * @Description:
 * @Date: 2021/11/22 11:13 上午
 * @Version: 1.0
 */
public class ZookeeperRegistryCenterTest {

    @Test
    public void testRecursiveCreateNode() throws Exception {
        URL url = URL.valueOf("zookeeper://127.0.0.1:2181");
        ZookeeperRegistry zookeeperRegistryCenter = new ZookeeperRegistry(url);

        zookeeperRegistryCenter.recursiveCreateNode("/cloud/zookeeper/org.qee.service.DemoService:*:*/127.0.0.1:20881", true);

        String data = zookeeperRegistryCenter.getData("/cloud/zookeeper/org.qee.service.DemoService:*:*/127.0.0.1:20881");
        System.out.println(data);
    }
}
