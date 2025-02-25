package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco;


import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/data-structures/rest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CsCoRest {

    // CUSTOM OBJECT
    String CO = "/custom-object";

    @GET
    @Path(CO)
    AllCustomObjectsDTO getAllCustomObjects();

    @GET
    @Path(CO + "/{id}")
    CustomObjectDTO getCustomObjectById(@PathParam("id") int id);

    @GET
    @Path(CO)
    CustomObjectDTO getCustomObjectByName(@QueryParam("name") String name);

    @GET
    @Path(CO)
    CustomStructureCustomObjectsDTO getCustomObjectsByCustomStructureId(@QueryParam("customStructureId") int id);

    @POST
    @Path(CO)
    CustomObjectDTO createCustomObject(CustomObjectCreateDTO customObject);

    @PUT
    @Path(CO + "/{id}")
    Response updateCustomObject(@PathParam("id") int id, CustomObjectCreateDTO customObject);

    @DELETE
    @Path(CO + "/{id}")
    Response deleteCustomObject(@PathParam("id") int id);


    // CUSTOM STRUCTURE
    String CS = "/custom-structure";

    @GET
    @Path(CS + "/{id}")
    CustomStructureDTO getCustomStructureById(@PathParam("id") int id);

    @PUT
    @Path(CS + "/{id}")
    Response updateCustomStructure(@PathParam("id") int id, CustomStructureCreateDTO customStructure);

    @POST
    @Path(CS + "/{id}/attributes")
    Response addAttributeToCustomStructure(@PathParam("id") int id, CustomStructureAttributeDTO customStructureAttribute);




}
