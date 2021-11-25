package org.qee.cloud.spring.processors;

import org.qee.cloud.common.exceptions.ExportException;
import org.qee.cloud.common.utils.Throws;
import org.qee.cloud.test.annotation.CloudService;
import org.qee.cloud.spring.config.CloudServiceConfigBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 为了实现 CloudService 像 @Service 的注册，我们实现
 */
public class CloudServiceBeanPostProcessor implements BeanDefinitionRegistryPostProcessor, BeanClassLoaderAware {

    private ClassPathBeanDefinitionScanner scanner;

    private ClassLoader classLoader;

    private CloudServiceBeanNameGenerator cloudServiceBeanNameGenerator = new CloudServiceBeanNameGenerator();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private String[] scanPackages;

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    //这里注册 BeanDefinition 所以需要扫描
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.setBeanNameGenerator(cloudServiceBeanNameGenerator);

        scanner.addIncludeFilter(new AnnotationTypeFilter(CloudService.class, false, false));
        for (String scanPackage : scanPackages) {
            scanner.scan(scanPackages); //这里把 CloudService 上的Impl 注册成Bean
            Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(scanPackage);
            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class<?> classType = ClassUtils.resolveClassName(beanDefinition.getBeanClassName(), classLoader);
                if (classType.getAnnotation(CloudService.class) == null) {
                    continue;
                }
                AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(classType.getAnnotation(CloudService.class), false, true);

                //这里要有个机制去 注册远程CloudServiceConfigBean
                AbstractBeanDefinition abstractBeanDefinition = buildCloudServiceBeanDefinition(classType, beanDefinition, annotationAttributes, registry);
                String beanName = RemoteObjectUtils.getRemoteObjectInSpring(annotationAttributes, getInterfaceClass(classType, annotationAttributes));

                if (!registry.containsBeanDefinition(beanName)) {
                    registry.registerBeanDefinition(beanName, abstractBeanDefinition);
                }
            }
        }
    }

    /**
     * 实现类
     *
     * @param classType
     * @param annotationAttributes
     * @return
     */
    private Class<?> getInterfaceClass(Class<?> classType, AnnotationAttributes annotationAttributes) {

        Class<?> interfaceClass = (Class<?>) annotationAttributes.get("interfaceClass");
        if (interfaceClass == null || interfaceClass == void.class || interfaceClass == Void.class) {
            Class<?>[] interfaces = classType.getInterfaces();
            if (interfaces != null && interfaces.length > 0) { //实现类有多个接口，实际上也不知道真是远程引用接口是哪个了，其实需要指定，这个取第一个咯
                interfaceClass = interfaces[0];
            }
        }
        if (interfaceClass == null) {
            Throws.throwException(ExportException.class, "实现类：" + classType.getName() + "没有实现的接口");
        }

        return interfaceClass;
    }

    private AbstractBeanDefinition buildCloudServiceBeanDefinition(Class<?> classTypeImpl, BeanDefinition implBeanDefinition, AnnotationAttributes annotationAttributes, BeanDefinitionRegistry registry) {
        String refName = cloudServiceBeanNameGenerator.generateBeanName(implBeanDefinition, registry);
        String beanName = RemoteObjectUtils.getRemoteObjectInSpring(annotationAttributes, getInterfaceClass(classTypeImpl, annotationAttributes));

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(CloudServiceConfigBean.class);
        beanDefinitionBuilder.addPropertyReference("ref", refName);
        beanDefinitionBuilder.addPropertyValue("interfaceClass", getInterfaceClass(classTypeImpl, annotationAttributes));
        beanDefinitionBuilder.addPropertyValue("version", annotationAttributes.get("version"));
        beanDefinitionBuilder.addPropertyValue("group", annotationAttributes.get("group"));
        beanDefinitionBuilder.addPropertyValue("id", beanName);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        return beanDefinition;
    }


    static class CloudServiceBeanNameGenerator extends AnnotationBeanNameGenerator {

        private Class<? extends Annotation> cloudServiceAnnotation = CloudService.class;

        @Override
        protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
            AnnotationMetadata annotationMetadata = annotatedDef.getMetadata();
            AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(cloudServiceAnnotation.getName(), false));
            if (annotationAttributes == null) {
                return null;
            }
            return (String) annotationAttributes.get("id");
        }
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


}
