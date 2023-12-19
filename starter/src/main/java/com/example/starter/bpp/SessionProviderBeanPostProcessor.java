package com.example.starter.bpp;

import com.example.starter.annotation.SessionProvider;
import com.example.starter.properties.SessionProviderProperties;
import com.example.starter.service.SessionProviderInterceptor;
import com.example.starter.service.SessionProviderService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Constructor;
import java.util.*;

public class SessionProviderBeanPostProcessor implements BeanFactoryAware, BeanPostProcessor {

    private BeanFactory beanFactory;
    private final Map<String, Class<?>> beanNamesWithAnnotatedMethods = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        boolean annotationPresent = Arrays.stream(clazz.getMethods())
                .anyMatch(method -> method.isAnnotationPresent(SessionProvider.class));
        if (annotationPresent) {
            beanNamesWithAnnotatedMethods.put(beanName, clazz);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return Optional.ofNullable(beanNamesWithAnnotatedMethods.get(beanName))
                .map(clazz -> getSessionProviderProxy(bean))
                .orElse(bean);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private Object getSessionProviderProxy(Object bean) {
        SessionProviderService sessionProviderService = beanFactory.getBean(SessionProviderService.class);
        SessionProviderProperties sessionProviderProperties = beanFactory.getBean(SessionProviderProperties.class);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback(new SessionProviderInterceptor(bean, sessionProviderService, sessionProviderProperties, beanFactory));
        return isPresentDefaultConstructor(bean)
                ? enhancer.create()
                : enhancer.create(getNotDefaultConstructorArgTypes(bean), getNotDefaultConstructorArgs(bean));

    }

    private boolean isPresentDefaultConstructor(Object bean) {
        return Arrays.stream(bean.getClass().getConstructors())
                .anyMatch(constructor -> constructor.getParameterCount() == 0);
    }

    private Class<?>[] getNotDefaultConstructorArgTypes(Object object) {
        return Arrays.stream(object.getClass().getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .map(Constructor::getParameterTypes)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Object[] getNotDefaultConstructorArgs(Object object) {
        Class<?>[] constructorArgTypes = getNotDefaultConstructorArgTypes(object);
        return Arrays.stream(constructorArgTypes)
                .map(beanFactory::getBean)
                .toArray();
    }
}
