package cn.naeleven.fast.magicapi.jwt;

public interface JwtManager {

    String generateToken(String subject);

    boolean validateToken(String token);

}