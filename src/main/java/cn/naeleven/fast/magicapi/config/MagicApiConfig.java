package cn.naeleven.fast.magicapi.config;

import cn.naeleven.fast.magicapi.interceptor.CustomAuthorizationInterceptor;
import cn.naeleven.fast.magicapi.jwt.CustomJwtManager;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.ssssssss.magicapi.core.config.MagicAPIProperties;
import org.ssssssss.magicapi.core.config.Security;
import org.ssssssss.magicapi.core.interceptor.AuthorizationInterceptor;
import org.ssssssss.magicapi.datasource.model.MagicDynamicDataSource;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author dutengfei
 * @date 2022/12/28
 */
@Configuration
public class MagicApiConfig {

    @Bean
    public MagicDynamicDataSource magicDynamicDataSource(@Autowired @Qualifier("primaryDataSource") DataSource dataSource,
                                                         @Autowired MagicDataSourceProperties magicDataSourceProperties) {
        MagicDynamicDataSource dynamicDataSource = new MagicDynamicDataSource();
        dynamicDataSource.setDefault(dataSource);
        dynamicDataSource.put("default", dataSource);
        List<DataSourceProperties> multiDataSourceProperties = magicDataSourceProperties.getMulti();
        if (!CollectionUtils.isEmpty(multiDataSourceProperties)) {
            for (DataSourceProperties dataSourceProperties : multiDataSourceProperties) {
                MysqlDataSource mysqlDataSource = new MysqlDataSource();
                mysqlDataSource.setUrl(dataSourceProperties.getUrl());
                mysqlDataSource.setUser(dataSourceProperties.getUsername());
                mysqlDataSource.setPassword(dataSourceProperties.getPassword());
                dynamicDataSource.put(dataSourceProperties.getName(), mysqlDataSource);
            }
        }
        return dynamicDataSource;
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor(MagicAPIProperties properties, CustomJwtManager customJwtManager) {
        Security security = properties.getSecurity();
        return new CustomAuthorizationInterceptor(security.getUsername(), security.getPassword(), customJwtManager);
    }

}
