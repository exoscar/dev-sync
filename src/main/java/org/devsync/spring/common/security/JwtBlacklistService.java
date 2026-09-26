package org.devsync.spring.common.security;

public interface JwtBlacklistService {
    void blacklist(String token);

    boolean isBlacklisted(String jti);
}
