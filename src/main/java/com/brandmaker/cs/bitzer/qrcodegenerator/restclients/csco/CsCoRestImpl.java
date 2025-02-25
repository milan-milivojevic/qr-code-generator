package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco;


import com.brandmaker.cs.bitzer.qrcodegenerator.config.AppProperties;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.auth.AuthFilter;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.auth.SignLoginAuthorizer;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;

@Service
@Slf4j
public class CsCoRestImpl implements CsCoRest {

    private final AppProperties appProperties;
    private ResteasyClient client;
    private CsCoRest csco;

    public CsCoRestImpl(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @PostConstruct
    public void init() {
        reinitClient();
    }

    /**
     * A dedicated method for (re)initializing the REST client and proxy.
     * Can call this again if CSCO becomes null or if token expires.
     */
    private synchronized void reinitClient() {
        // Optionally close the old client if one exists
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                log.warn("Failed to close old ResteasyClient", e);
            }
        }

        try {
            log.info("Initializing CSCO client with webApiRoot={}", appProperties.getWebApiRoot());

            SignLoginAuthorizer authorizer = new SignLoginAuthorizer(
                    appProperties.getWebApiUsername(),
                    appProperties.getWebApiPassword(),
                    appProperties.getWebApiRoot()
            );

            client = new ResteasyClientBuilder()
                    .connectionPoolSize(50)
                    .build();
            client.register(new AuthFilter(authorizer::getBearerToken));
            csco = client.target(appProperties.getWebApiRoot()).proxy(CsCoRest.class);

            log.info("CSCO client initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize CSCO client: ", e);
            // If considered a fatal error, then rethrow
            // throw new RuntimeException("Cannot initialize CSCO client", e);

            // Or set CSCO to null so it's obvious that the bean is not ready
            csco = null;
        }
    }

    /**
     * @return AllCustomObjectsDTO
     */
    @Override
    public AllCustomObjectsDTO getAllCustomObjects() {
        if (csco == null) {
            reinitClient();
            if (csco == null) {
                log.error("CSCO client is not initialized; cannot get all custom objects");
                return null;
            }
        }

        try {
            return csco.getAllCustomObjects();
        } catch (Exception e) {
            log.error("ERR - getAllCustomObjects() : ", e);
            return null;
        }
    }

    @Override
    public CustomObjectDTO getCustomObjectById(int id) {
        if (csco == null) {
            reinitClient();
            if (csco == null) {
                log.error("CSCO client is not initialized; cannot get all custom objects");
                return null;
            }
        }

        try {
            return csco.getCustomObjectById(id);
        } catch (Exception e) {
            log.error("ERR - getCustomObjectById() : ", e);
            return null;
        }
    }

    @Override
    public CustomObjectDTO getCustomObjectByName(String name) {
        if (csco == null) {
            reinitClient();
            if (csco == null) {
                log.error("CSCO client is not initialized; cannot get all custom objects");
                return null;
            }
        }

        try {
            return csco.getCustomObjectByName(name);
        } catch (Exception e) {
            log.error("ERR - getCustomObjectByName() : ", e);
            return null;
        }
    }

    @Override
    public CustomStructureCustomObjectsDTO getCustomObjectsByCustomStructureId(int id) {
        if (csco == null) {
            reinitClient();
            if (csco == null) {
                log.error("CSCO client is not initialized; cannot get all custom objects");
                return null;
            }
        }

        try {
            return csco.getCustomObjectsByCustomStructureId(id);
        } catch (Exception e) {
            log.error("ERR - getCustomObjectByCustomStructureId() : ", e);
            return null;
        }
    }

    @Override
    public CustomObjectDTO createCustomObject(CustomObjectCreateDTO customObject) {
        if (csco == null) {
            reinitClient();
            if (csco == null) {
                log.error("CSCO client is not initialized; cannot get all custom objects");
                return null;
            }
        }

        try {
            return csco.createCustomObject(customObject);
        } catch (Exception e) {
            log.error("ERR - createCustomObject() : ", e);
            return null;
        }
    }

    @Override
    public Response updateCustomObject(int id, CustomObjectCreateDTO customObject) {
        if (csco == null) {
            reinitClient();
            if (csco == null) {
                log.error("CSCO client is not initialized; cannot get all custom objects");
                return null;
            }
        }

        try {
            return csco.updateCustomObject(id, customObject);
        } catch (Exception e) {
            log.error("ERR - updateCustomObject() : ", e);
            return null;
        }
    }

    @Override
    public Response deleteCustomObject(int id) {
        if (csco == null) {
            reinitClient();
            if (csco == null) {
                log.error("CSCO client is not initialized; cannot get all custom objects");
                return null;
            }
        }

        try {
            return csco.deleteCustomObject(id);
        } catch (Exception e) {
            log.error("ERR - deleteCustomObject() : ", e);
            return null;
        }
    }

    @Override
    public CustomStructureDTO getCustomStructureById(int id) {
        if (csco == null) {
            reinitClient();
            if (csco == null) {
                log.error("CSCO client is not initialized; cannot get all custom objects");
                return null;
            }
        }

        try {
            return csco.getCustomStructureById(id);
        } catch (Exception e) {
            log.error("ERR - getCustomStructureById() : ", e);
            return null;
        }
    }

    @Override
    public Response updateCustomStructure(int id, CustomStructureCreateDTO customStructure) {
        if (csco == null) {
            reinitClient();
            if (csco == null) {
                log.error("CSCO client is not initialized; cannot get all custom objects");
                return null;
            }
        }

        try {
            return csco.updateCustomStructure(id, customStructure);
        } catch (Exception e) {
            log.error("ERR - updateCustomStructure() : ", e);
            return null;
        }
    }

    @Override
    public Response addAttributeToCustomStructure(int id, CustomStructureAttributeDTO customStructureAttribute) {
        if (csco == null) {
            reinitClient();
            if (csco == null) {
                log.error("CSCO client is not initialized; cannot get all custom objects");
                return null;
            }
        }

        try {
            return csco.addAttributeToCustomStructure(id, customStructureAttribute);
        } catch (Exception e) {
            log.error("ERR - addAttributeToCustomStructure() : ", e);
            return null;
        }
    }
}
