package com.avaneesh.vimo_backend.auth.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CookieService {
    private final String refreshTokenCookieName;
    private final boolean cookieHttpOnly;
    private final boolean cookieSecure;
    private final String cookieDomain;
    private final String cookieSameSite;

    public CookieService(
            @Value("${custom.jwt.refresh_token_cookie_name}") String refreshTokenCookieName,
            @Value("${custom.jwt.cookie_http_only}") boolean cookieHttpOnly,
            @Value("${custom.jwt.cookie_secure}") boolean cookieSecure,
            @Value("${custom.jwt.cookie_same_site}") String cookieSameSite,
            @Value("${custom.jwt.cookie_domain}") String cookieDomain
    ) {
        this.refreshTokenCookieName = refreshTokenCookieName;
        this.cookieHttpOnly = cookieHttpOnly;
        this.cookieSecure = cookieSecure;
        this.cookieSameSite = cookieSameSite;
        this.cookieDomain = cookieDomain;
    }

    public void attachCookie(HttpServletResponse response, String value, int maxAge) {
        var responseCookieBuilder = ResponseCookie.from(refreshTokenCookieName, value)
                .httpOnly(cookieHttpOnly)
                .secure(cookieSecure)
                .path("/")
                .maxAge(maxAge)
                .sameSite(cookieSameSite);

        if (cookieDomain != null && !cookieDomain.isBlank()) {
            responseCookieBuilder.domain(cookieDomain);
        }

        ResponseCookie cookie = responseCookieBuilder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clearCookie(HttpServletResponse response) {
        attachCookie(response, "", 0);
    }

    public void addNoStoreHeaders(HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, proxy-revalidate");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0);
    }
}
