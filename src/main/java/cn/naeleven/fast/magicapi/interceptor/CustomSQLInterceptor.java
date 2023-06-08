package cn.naeleven.fast.magicapi.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.context.RequestEntity;
import org.ssssssss.magicapi.modules.db.BoundSql;
import org.ssssssss.magicapi.modules.db.inteceptor.SQLInterceptor;

/**
 * @author dutengfei
 * @date 2023/02/15
 */
@Slf4j
@Component
public class CustomSQLInterceptor implements SQLInterceptor {

    @Override
    public void preHandle(BoundSql boundSql, RequestEntity requestEntity) {
        log.info("执行的SQL：{}，执行的SQL参数：{}", boundSql.getSql(), boundSql.getParameters());
    }

}