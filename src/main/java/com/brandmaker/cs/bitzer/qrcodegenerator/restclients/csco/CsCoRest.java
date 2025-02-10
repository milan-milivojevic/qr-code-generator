package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco;


import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.AllCustomObjectsDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(CsCoRest.CSCO_REST_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CsCoRest {

    String CSCO_REST_PATH = "/data-structures/rest";

    @GET
    @Path("/custom-object")
    AllCustomObjectsDTO getAllCustomObjects();


}
