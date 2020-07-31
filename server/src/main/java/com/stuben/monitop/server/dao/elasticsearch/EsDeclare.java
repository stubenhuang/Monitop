package com.stuben.monitop.server.dao.elasticsearch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EsDeclare {
    String index() default StringUtils.EMPTY;
}
