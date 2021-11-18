package com.individual.common.extentions;

import com.individual.common.exceptions.ResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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


    public static Properties loadExtension(URL url) throws IOException {
        if (url == null) {
            throw new ResourceException("资源url不存在");
        }
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
