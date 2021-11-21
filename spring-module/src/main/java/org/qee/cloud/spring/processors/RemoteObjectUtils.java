package org.qee.cloud.spring.processors;

import org.springframework.core.annotation.AnnotationAttributes;

public class RemoteObjectUtils {
    public static String getRemoteObjectInSpring(AnnotationAttributes annotationAttributes, Class<?> injectClassType) {
        return injectClassType.getName() + ":" + annotationAttributes.get("group") + ":" + annotationAttributes.get("version");
    }
}
