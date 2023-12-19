package com.example.starter.service;

import com.example.starter.annotation.SessionProvider;
import com.example.starter.exception.SessionProviderException;
import com.example.starter.model.Session;
import com.example.starter.properties.SessionProviderProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class SessionProviderInterceptor implements MethodInterceptor {

    public static final String FIELD_NAME_LOGIN = "login";
    private final Object originalBean;
    private final SessionProviderService sessionProviderService;
    private final SessionProviderProperties sessionProviderProperties;
    private final BeanFactory beanFactory;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (method.isAnnotationPresent(SessionProvider.class)) {
            SessionProvider annotation = method.getAnnotation(SessionProvider.class);

            Set<String> blackList = getBlackList(annotation);

            Class<?>[] parameterTypes = method.getParameterTypes();
            log.info(Arrays.toString(parameterTypes));

            boolean isSessionTypePresent = Arrays.stream(parameterTypes)
                    .anyMatch(arg -> arg.isInstance(Session.class));

            if (!isSessionTypePresent) {
                throw new SessionProviderException("Session object is absent");
            }
            List<String> logins = new ArrayList<>();
            for (Object object : args) {
                Field[] fields = object.getClass().getDeclaredFields();
                List<String> argLogins = Arrays.stream(fields)
                        .filter(field -> FIELD_NAME_LOGIN.equals(field.getName()) && String.class.equals(field.getType()))
                        .map(f -> {
                            try {
                                return (String) f.get(object);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());
                if (argLogins.size() > 1) {
                    throw new SessionProviderException("Field login more then 1");
                }
                logins.add(argLogins.get(0));
            }
            checkLoginInBlackList(blackList, logins);

            List<Object> newArgs = Arrays.stream(args)
                    .map(arg -> arg instanceof Session ? sessionProviderService.getSession(logins.get(0)) : arg)
                    .collect(Collectors.toList());

            return method.invoke(originalBean, newArgs.toArray());
        }
        return method.invoke(originalBean, args);
    }

    private static void checkLoginInBlackList(Set<String> blackList, List<String> logins) {
        if (blackList.contains(logins.get(0))) {
            throw new SessionProviderException(String.format("Login %s in black list", logins.get(0)));
        }
    }

    private Set<String> getBlackList(SessionProvider annotation) {
        List<String> annotationBlackList = Arrays.asList(annotation.blackList());
        Class<? extends BlackListProvider>[] blackListProviderAnnotation = annotation.blackListProvider();
        Set<String> blackListAnnotation = Arrays.stream(blackListProviderAnnotation).map(beanFactory::getBean)
                .map(BlackListProvider::getBlackList)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Set<? extends BlackListProvider> blackListProviders = sessionProviderProperties.getBlackListProviders();
        Set<String> blackList = blackListProviders.stream().map(p -> p.getBlackList())
                .flatMap(b -> b.stream())
                .collect(Collectors.toSet());
        blackList.addAll(annotationBlackList);
        blackList.addAll(blackListAnnotation);
        return blackList;
    }
}
