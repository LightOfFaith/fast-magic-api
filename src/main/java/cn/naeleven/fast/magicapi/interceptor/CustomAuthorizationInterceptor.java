package cn.naeleven.fast.magicapi.interceptor;

import cn.naeleven.fast.magicapi.jwt.CustomJwtManager;
import lombok.extern.slf4j.Slf4j;
import org.ssssssss.magicapi.core.context.MagicUser;
import org.ssssssss.magicapi.core.exception.MagicLoginException;
import org.ssssssss.magicapi.core.interceptor.AuthorizationInterceptor;

import java.util.Objects;

/**
 * magic-api 登录鉴权/操作鉴权
 *
 * @author dutengfei
 * @date 2023/02/14
 */
@Slf4j
public class CustomAuthorizationInterceptor implements AuthorizationInterceptor {

    private final boolean requireLogin;
    private String validToken;
    private MagicUser configMagicUser;

    private CustomJwtManager customJwtManager;

    public CustomAuthorizationInterceptor(String username, String password, CustomJwtManager customJwtManager) {
        if (this.requireLogin = username != null && password != null) {
            this.validToken = customJwtManager.generateToken(username);
            this.configMagicUser = new MagicUser(username, username, this.validToken);
            this.customJwtManager = customJwtManager;
        }
    }

    /**
     * 配置是否需要登录
     *
     * @return
     */
    @Override
    public boolean requireLogin() {
        return requireLogin;
    }

    @Override
    public MagicUser getUserByToken(String token) throws MagicLoginException {
        if (requireLogin && Objects.equals(validToken, token)) {
            return configMagicUser;
        }
        throw new MagicLoginException("token无效");
    }

    @Override
    public MagicUser login(String username, String password) throws MagicLoginException {
        if (requireLogin && customJwtManager.validateToken(validToken)) {
            return configMagicUser;
        }
        throw new MagicLoginException("用户名或密码不正确");
    }

    @Override
    public void refreshToken(MagicUser user) {
        String newToken = customJwtManager.generateToken(user.getToken());
        user.setToken(newToken);
        user.setTimeout(30 * 60);
    }

}