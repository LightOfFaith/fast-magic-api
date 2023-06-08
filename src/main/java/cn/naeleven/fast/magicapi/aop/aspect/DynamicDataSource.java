package cn.naeleven.fast.magicapi.aop.aspect;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author dutengfei
 * @date 2022/06/01
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // 使用DynamicDataSourceHolder保证线程安全，并且得到当前线程中的数据源key
        return DynamicDataSourceHolder.current();
    }

}