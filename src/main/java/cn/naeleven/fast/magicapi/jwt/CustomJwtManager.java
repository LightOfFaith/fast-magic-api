package cn.naeleven.fast.magicapi.jwt;

import cn.naeleven.fast.magicapi.util.RSAUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class CustomJwtManager implements JwtManager {

    private static final ZoneId TIMEZONE = ZoneId.of("Asia/Shanghai");

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    private final Cache<String, Long> tokenCache = CacheBuilder.newBuilder()
            .maximumSize(9999).expireAfterWrite(5, TimeUnit.MINUTES).build();

    public CustomJwtManager(@Value("${jwt.private-key-base64}") String privateKeyBase64,
                            @Value("${jwt.public-key-base64}") String publicKeyBase64) throws IOException, GeneralSecurityException {
        this.privateKey = RSAUtils.getPrivateKey(privateKeyBase64);
        this.publicKey = RSAUtils.getPublicKey(publicKeyBase64);
        log.info("Init JwtManager successfully!");
    }

    @Override
    public String generateToken(String subject) {
        LocalDateTime now = LocalDateTime.now(TIMEZONE);
        LocalDateTime expireAt = now.plusMinutes(30);
        return Jwts.builder().setSubject(subject).setExpiration(Date.from(expireAt.atZone(TIMEZONE).toInstant()))
                .signWith(SignatureAlgorithm.RS512, privateKey).compact();
    }

    /**
     * 验证JWT
     *
     * @param token jwt token
     * @return
     */
    @Override
    public boolean validateToken(String token) {
        long start = System.currentTimeMillis();
        Long tokenExpireAt = tokenCache.getIfPresent(token);
        if (tokenExpireAt != null) {
            // 如果未超时
            if (tokenExpireAt > Instant.now().getEpochSecond()) {
                return true;
            }
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody();

            Date expireAt = claims.get("exp", Date.class);
            if (expireAt != null) {
                tokenCache.put(token, expireAt.getTime());
            }
        } catch (ExpiredJwtException e) {
            log.error("Token is expire!", e);
            return false;
        } catch (Exception e) {
            log.error("Verify jwt caught exception", e);
            return false;
        } finally {
            long cost = System.currentTimeMillis() - start;
            if (cost > 10) {
                log.warn("Verify jwt cost too much, cost:{}", cost);
            }
        }
        return true;
    }

    public String refreshToken(String subject, String token) {
        Long tokenExpireAt = tokenCache.getIfPresent(token);
        if (tokenExpireAt != null) {
            if (!needRefresh(tokenExpireAt)) {
                return token;
            }
            return generateToken(subject);
        }
        // 缓存中未获取到token的过期时间戳
        Claims claims = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .getBody();

        Date expireAt = claims.get("exp", Date.class);
        if (expireAt != null) {
            tokenCache.put(token, expireAt.getTime());
        }
        tokenExpireAt = expireAt.getTime();
        if (!needRefresh(tokenExpireAt)) {
            return token;
        }
        return generateToken(subject);
    }

    private boolean needRefresh(long expiredTimeMills) {
        long refreshTimeMills = 10 * 60 * 1000;
        return System.currentTimeMillis() + refreshTimeMills > expiredTimeMills;
    }

}