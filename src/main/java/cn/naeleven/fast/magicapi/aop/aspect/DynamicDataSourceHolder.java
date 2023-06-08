package cn.naeleven.fast.magicapi.aop.aspect;

import org.springframework.core.NamedThreadLocal;

/**
 * @author dutengfei
 * @date 2022/05/30
 */
public class DynamicDataSourceHolder {

    /**
     * 使用ThreadLocal记录当前线程的数据源key
     */
    private static final ThreadLocal<String> HOLDER = new NamedThreadLocal<>("current data source holder");

    public static String current() {
        return HOLDER.get();
    }

    public static void put(String key) {
        HOLDER.set(key);
    }

    public static void reset() {
        HOLDER.remove();
    }

}