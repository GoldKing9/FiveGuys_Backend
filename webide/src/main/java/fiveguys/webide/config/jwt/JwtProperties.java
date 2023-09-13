package fiveguys.webide.config.jwt;

public interface JwtProperties {
    long ACCESS_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
