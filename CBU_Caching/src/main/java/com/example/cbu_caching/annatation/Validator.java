package com.example.cbu_caching.annatation;

import com.example.cbu_caching.enums.ValidateEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface Validator {
    ValidateEnum[] type();

    int intValue() default 0;
    String strValue() default "";
    String name() default "";
}
