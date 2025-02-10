package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.auth;


import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.auth.dto.AccessTokenDTO;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.auth.dto.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;

@Path("/rest/sso")
public interface AuthRestService {
    String LOGIN = "login";
    String PASSWORD = "password";
    String BEARER_PREFIX = "Bearer ";
    String JSESSIONIDSSO = "JSESSIONIDSSO";

    @POST
    @Path("/auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    AccessTokenDTO authUser(@FormParam(LOGIN) final String login, @FormParam(PASSWORD) final String password);

    @POST
    @Path("/auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    String authUserString(@FormParam(LOGIN) final String login, @FormParam(PASSWORD) final String password);

    @GET
    @Path("/auth/jaas/jwt")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    AccessTokenDTO getTokenByJaasSession(@CookieParam(JSESSIONIDSSO) final NewCookie jSessionIdSso);

    @GET
    @Path("/users/current")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    UserDTO getCurrentUser(@CookieParam(JSESSIONIDSSO) final NewCookie jSessionIdSso);


}
