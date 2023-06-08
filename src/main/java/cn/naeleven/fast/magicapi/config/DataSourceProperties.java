package cn.naeleven.fast.magicapi.config;

import lombok.Data;

/**
 * @author dutengfei
 * @date 2023/05/15
 */
@Data
public class DataSourceProperties {

    private String name;

    private String url;

    private String username;

    private String password;

}