package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.util.function.Supplier;

public final class AuthFilter implements ClientRequestFilter {
    private static final Log LOG = LogFactory.getLog(AuthFilter.class);

    private static final long TOKEN_EXPIRE_TIME =  2 * 60 * 1000;
    private final Supplier<String> tokenBuilder;
    private String lastBearerToken;
    private long tokenGenTime = 0;

    public AuthFilter(final Supplier<String> tokenBuilder) {
        this.tokenBuilder = tokenBuilder;
        updateToken();
    }

    @Override
    public void filter(final ClientRequestContext requestContext) {
        if (isTokenExpired()) {
            LOG.info("TOKEN EXPIRED");
            updateToken();
        }
        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, lastBearerToken);
    }

    private void updateToken() {
        lastBearerToken = tokenBuilder.get();
        tokenGenTime = System.currentTimeMillis();
        LOG.info("TOKEN UPDATED.");
    }

    private boolean isTokenExpired() {
        return isTimeUp();
    }

    private boolean isTimeUp() {
        return ((System.currentTimeMillis() - tokenGenTime) >= TOKEN_EXPIRE_TIME);
    }

}
