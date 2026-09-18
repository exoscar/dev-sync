package org.devsync.spring.common.security;

import java.time.Duration;

public interface JwtBlacklistService {
    void blacklist(String token);
    boolean isBlacklisted(String jti);
}
