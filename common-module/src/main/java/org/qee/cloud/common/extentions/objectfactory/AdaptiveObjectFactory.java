package org.qee.cloud.common.extentions.objectfactory;

import org.qee.cloud.common.extentions.ExtensionLoader;

import java.util.List;

public class AdaptiveObjectFactory implements ObjectFactory {
    private List<String> allLoadExtensionName;

    public AdaptiveObjectFactory() {
        allLoadExtensionName = ExtensionLoader.getExtensionLoader(ObjectFactory.class).getGeneralName();
    }

    @Override
    public Object getObject(String name, Class<?> clzz) {
        for (String extensionName : allLoadExtensionName) {
            ObjectFactory objectFactory = ExtensionLoader.getExtensionLoader(ObjectFactory.class).getExtension(extensionName);
            Object object = objectFactory.getObject(name, clzz);
            if (object != null) {
                return object;
            }
        }
        return null;
    }
}
