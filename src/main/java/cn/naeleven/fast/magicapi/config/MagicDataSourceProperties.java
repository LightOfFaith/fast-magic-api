package cn.naeleven.fast.magicapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author dutengfei
 * @date 2023/05/15
 */
@Data
@ConfigurationProperties(prefix = "magic-api.datasource")
public class MagicDataSourceProperties {

    private List<DataSourceProperties> multi;

}