package cn.naeleven.fast.magicapi.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author dutengfei
 * @date 2022/05/30
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface CurrentDataSource {

    String value() default "";

}