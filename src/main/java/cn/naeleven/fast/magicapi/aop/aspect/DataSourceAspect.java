package cn.naeleven.fast.magicapi.aop.aspect;

import cn.naeleven.fast.magicapi.aop.annotation.CurrentDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author dutengfei
 * @date 2022/05/30
 */
@Slf4j
@Aspect
@Component
public class DataSourceAspect {

    @Before(value = "@annotation(dataSource)")
    public void doBefore(JoinPoint joinPoint, CurrentDataSource dataSource) {
        String value = dataSource.value();
        if (StringUtils.isNotBlank(value)) {
            DynamicDataSourceHolder.put(value);
        }
        String classname = getClassName(joinPoint);
        String methodName = getMethodName(joinPoint);
        if (log.isDebugEnabled()) {
            log.info("switch data source to [{}] in method [{}]",
                    DynamicDataSourceHolder.current(), classname + "." + methodName);
        }
    }

    @After(value = "@annotation(dataSource)")
    public void doAfter(JoinPoint joinPoint, CurrentDataSource dataSource) {
        DynamicDataSourceHolder.reset();
        String classname = getClassName(joinPoint);
        String methodName = getMethodName(joinPoint);
        if (log.isDebugEnabled()) {
            log.info("clear data source to [{}] in method [{}]",
                    DynamicDataSourceHolder.current(), classname + "." + methodName);
        }
    }

    private String getClassName(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName();
    }

    private String getMethodName(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod().getName();
    }

}