package com.hp.xo.resourcepool.model;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hp.xo.resourcepool.request.BaseRequest.FieldType;


@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface Parameter {
    String name() default "";

    String description() default "";

    boolean required() default false;

    FieldType type() default FieldType.OBJECT;

    FieldType collectionType() default FieldType.OBJECT;

    Class<?>[] entityType() default Object.class;

    boolean expose() default true;

    boolean includeInApiDoc() default true;

    int length() default 255;

    String since() default "";

    String retrieveMethod() default "getById";
}
