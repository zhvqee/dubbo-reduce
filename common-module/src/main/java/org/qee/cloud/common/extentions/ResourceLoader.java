package org.qee.cloud.common.extentions;

import org.qee.cloud.common.exceptions.ResourceException;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * @ProjectName: qee-cloud
 * @Package: com.individual.common.extentions
 * @ClassName: ResourceLoader
 * @Description:
 * @Date: 2021/11/18 4:30 下午
 * @Version: 1.0
 */
public class ResourceLoader {


    public static Properties loadExtension(URL url) {
        if (url == null) {
            throw new ResourceException("资源url不存在");
        }
        Properties properties = null;
        try {
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            properties = new Properties();
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static List<Properties> loadExtension(Enumeration<URL> resources) {
        List<Properties> properties = new ArrayList<>();
        while (resources.hasMoreElements()) {
            Properties prop = loadExtension(resources.nextElement());
            properties.add(prop);
        }
        return properties;
    }
}
