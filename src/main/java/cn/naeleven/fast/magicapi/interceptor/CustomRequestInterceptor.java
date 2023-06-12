package cn.naeleven.fast.magicapi.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.context.RequestEntity;
import org.ssssssss.magicapi.core.interceptor.RequestInterceptor;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.model.JsonBean;
import org.ssssssss.magicapi.core.model.Options;
import org.ssssssss.magicapi.core.servlet.MagicHttpServletRequest;
import org.ssssssss.magicapi.core.servlet.MagicHttpServletResponse;
import org.ssssssss.script.MagicScriptContext;

/**
 * magic-api 接口鉴权
 *
 * @author dutengfei
 * @date 2023/02/15
 */
@Slf4j
@Component
public class CustomRequestInterceptor implements RequestInterceptor {

    @Override
    public Object preHandle(ApiInfo info, MagicScriptContext context, MagicHttpServletRequest request, MagicHttpServletResponse response) throws Exception {
        log.info("请求前，{}，路径：{}", info.getName(), info.getPath());
        // 接口选项配置了需要登录
        if ("true".equals(info.getOptionValue(Options.REQUIRE_LOGIN))) {
            String token = request.getHeader("token");
            if (StringUtils.isNotBlank(token)) {
                return new JsonBean<>(401, "用户未登录");
            }
            // TODO 调用登录鉴权服务
            String username = "";
            if (StringUtils.isBlank(username)) {
                return new JsonBean<>(401, "用户未登录");
            }
        }
        return null;
    }

    @Override
    public Object postHandle(ApiInfo info, MagicScriptContext context, Object returnValue, MagicHttpServletRequest request, MagicHttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.info("请求后，{}，路径：{}，执行完毕，返回结果：{}", info.getName(), info.getPath(), returnValue);
        }
        log.info("请求后，{}，路径：{}，执行完毕", info.getName(), info.getPath());
        return null;
    }

    @Override
    public void afterCompletion(RequestEntity requestEntity, Object returnValue, Throwable throwable) {

    }

}