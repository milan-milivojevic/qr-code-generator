package com.brandmaker.cs.bitzer.qrcodegenerator;

import com.brandmaker.cs.bitzer.qrcodegenerator.config.AppProperties;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.CsCoRest;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class QrCodeGenIT {

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private CsCoRest csCoRest;

    @Test
    void test_updateCustomObject() {
        String customStructureId = appProperties.getCustomStructureId();
        int objectId = 1546;

        CustomObjectCreateDTO createDTO = new CustomObjectCreateDTO();
        createDTO.setName("Test_Object");
        createDTO.setLabel(Map.of("default", "Test Label"));
        createDTO.setState("EDIT_AND_ADD");
        createDTO.setCustomStructureId(Integer.valueOf(customStructureId));
        createDTO.setAttributeValues(List.of(
                new CustomObjectAttributeValueDTO(1, "url code value2", "url_code", "TEXT"),
                new CustomObjectAttributeValueDTO(2, "campaign_name value2", "campaign_name", "TEXT"),
                new CustomObjectAttributeValueDTO(3, "campaign_medium value2", "campaign_medium", "TEXT"),
                new CustomObjectAttributeValueDTO(4, "language value2", "language", "TEXT"),
                new CustomObjectAttributeValueDTO(5, "url_encoded value2", "url_encoded", "TEXT"),
                new CustomObjectAttributeValueDTO(6, "tracking_url value2", "tracking_url", "TEXT"),
                new CustomObjectAttributeValueDTO(7, "additional_information value2", "additional_information", "TEXT"),
                new CustomObjectAttributeValueDTO(8, "campaign_subject value2", "campaign_subject", "TEXT")
        ));

        Response response = csCoRest.updateCustomObject(objectId, createDTO);

        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        log.info(response.readEntity(String.class));
        log.info("Test DONE!");
    }

    @Test
    void test_updateCustomObject_2() {
        String customStructureId = appProperties.getCustomStructureId();
        int objectId = 1553;

        CustomObjectDTO customObjectDTO = csCoRest.getCustomObjectById(objectId);

        System.out.println(customObjectDTO.getAttributeValues());

        List<CustomObjectAttributeValueDTO> attributeValues = customObjectDTO.getAttributeValues();

        for (CustomObjectAttributeValueDTO attr : attributeValues) {
            if ("published".equals(attr.getAttributeName())) {
                attr.setValue("true");
                break;
            }
        }

        CustomObjectCreateDTO createDTO = new CustomObjectCreateDTO();
        createDTO.setParentId(customObjectDTO.getParentId());
        createDTO.setLabel(customObjectDTO.getLabel());
        createDTO.setName(customObjectDTO.getName());
        createDTO.setState("EDIT_AND_ADD");
        createDTO.setCustomStructureId(Integer.valueOf(customStructureId));
        createDTO.setAttributeValues(attributeValues);

        Response response = csCoRest.updateCustomObject(objectId, createDTO);

        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        log.info(response.readEntity(String.class));
        log.info("Test DONE!");

    }

    @Test
    void test_createCustomObject() {
        String customStructureId = appProperties.getCustomStructureId();

        CustomObjectCreateDTO createDTO = new CustomObjectCreateDTO();
        createDTO.setName("Test_Object_X");
        createDTO.setLabel(Map.of("default", "Test Label"));
        createDTO.setState("EDIT_AND_ADD");
        createDTO.setCustomStructureId(Integer.valueOf(customStructureId));
        createDTO.setAttributeValues(List.of(
                new CustomObjectAttributeValueDTO(1, "url code value 4", "url_code", "TEXT"),
                new CustomObjectAttributeValueDTO(2, "campaign_name value 4", "campaign_name", "TEXT"),
                new CustomObjectAttributeValueDTO(3, "campaign_medium value 4", "campaign_medium", "TEXT"),
                new CustomObjectAttributeValueDTO(4, "language value 4", "language", "TEXT"),
                new CustomObjectAttributeValueDTO(5, "url_encoded value 4", "url_encoded", "TEXT"),
                new CustomObjectAttributeValueDTO(6, "tracking_url value 4", "tracking_url", "TEXT"),
                new CustomObjectAttributeValueDTO(7, "additional_information value 4", "additional_information", "TEXT"),
                new CustomObjectAttributeValueDTO(8, "campaign_subject value 4", "campaign_subject", "TEXT")
                ));

        CustomObjectDTO customObject = csCoRest.createCustomObject(createDTO);

//        Assertions.assertNotNull(customObject);
        log.info(customObject.toString());
        log.info("Test DONE!");
    }

    @Test
    void test_getCustomObjectById() {
        int id = 1545;

        CustomObjectDTO customObjectDTO = csCoRest.getCustomObjectById(id);

        Assertions.assertNotNull(customObjectDTO);
        log.info(customObjectDTO.toString());
        log.info("Test DONE!");
    }

    @Test
    void test_getCustomObjectsByCustomStructureId() {
        String customStructureId = appProperties.getCustomStructureId();

        CustomStructureCustomObjectsDTO customObject = csCoRest.getCustomObjectsByCustomStructureId(Integer.parseInt(customStructureId));

        Assertions.assertNotNull(customObject);
        log.info(customObject.getData().toString());
        log.info("Test DONE!");
    }

    @Test
    void test_getAllCustomObjects() {

        AllCustomObjectsDTO allCustomObjectsDTO = csCoRest.getAllCustomObjects();

        Assertions.assertNotNull(allCustomObjectsDTO);

        log.info(allCustomObjectsDTO.toString());
        log.info("Test DONE!");
    }

    @Test
    void test_getCustomStructureById() {

        String customStructureId = appProperties.getCustomStructureId();

        CustomStructureDTO customStructureDTO = csCoRest.getCustomStructureById(Integer.parseInt(customStructureId));

        log.info(customStructureDTO.toString());
        log.info("Test DONE!");

    }

}
