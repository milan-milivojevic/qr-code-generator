package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.auth;

import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.auth.dto.AccessTokenDTO;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public final class SignLoginAuthorizer {

    private static final String BEARER_PREFIX = "Bearer ";
    private final String serverUrl;
    private final String login;
    private final String password;

    public SignLoginAuthorizer(final String login, final String password ,final String serverUrl) {
        this.login = login;
        this.password = password;
        this.serverUrl = serverUrl;
    }

    public String getBearerToken() throws IllegalStateException {
        return BEARER_PREFIX + getAccessToken();
    }

    private String getAccessToken() throws IllegalStateException {
        ResteasyClient client = null;
        try {
            client = new ResteasyClientBuilder().connectionPoolSize(50).build();
            final ResteasyWebTarget target = (ResteasyWebTarget) client.target(serverUrl);
            AuthRestService ars = target.proxy(AuthRestService.class);
            final AccessTokenDTO accessTokenDto = ars.authUser(login, password);
            return accessTokenDto.getAccessToken();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException("Get access token problem", ex);
        }
        finally {
            client.close();
        }
    }
}
