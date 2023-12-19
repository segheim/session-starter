package com.example.starter.annotation;

import com.example.starter.service.BlackListProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionProvider {

    String[] blackList() default {};

    Class<? extends BlackListProvider>[] blackListProvider() default {};

}
