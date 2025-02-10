package com.brandmaker.cs.bitzer.qrcodegenerator;

import com.brandmaker.cs.bitzer.qrcodegenerator.config.AppProperties;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.CsCoRest;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.AllCustomObjectsDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class QrCodeGenIT {

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private CsCoRest csCoRest;

    @Test
    void test_getAllCustomObjects() {

        AllCustomObjectsDTO allCustomObjectsDTO = csCoRest.getAllCustomObjects();

        Assertions.assertNotNull(allCustomObjectsDTO);

        log.info(allCustomObjectsDTO.toString());
        log.info("Test DONE!");
    }


}
