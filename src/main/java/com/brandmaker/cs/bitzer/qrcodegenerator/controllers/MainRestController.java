package com.brandmaker.cs.bitzer.qrcodegenerator.controllers;

import com.brandmaker.cs.bitzer.qrcodegenerator.config.AppProperties;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.CsCoRest;
import com.brandmaker.cs.bitzer.qrcodegenerator.services.MainService;
import com.brandmaker.cs.bitzer.qrcodegenerator.services.dto.CreatePayloadDTO;
import com.brandmaker.cs.bitzer.qrcodegenerator.services.dto.DownloadDTO;
import com.brandmaker.cs.bitzer.qrcodegenerator.services.dto.PublishUpdateDTO;
import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@Slf4j
@RestController
public class MainRestController {

    private final CsCoRest csCoRestService;
    private AppProperties appProperties;


    public MainRestController(CsCoRest csCoRestService, AppProperties appProperties) {
        this.csCoRestService = csCoRestService;
        this.appProperties = appProperties;
    }

    @PostMapping("/create-qr-code")
    public ResponseEntity<String> doCreateQrCode(@RequestBody CreatePayloadDTO payload) {
        String message;
        HttpStatus status;

        int createdId = MainService.createQrCode(payload, csCoRestService, appProperties);

        if ( createdId != 0) {
            message = "Qr code created";
            status = HttpStatus.CREATED;
        } else {
            message = "Qr code creation failed";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(String.valueOf(createdId), status);
    }

    @PostMapping("/publish-update")
    public ResponseEntity<String> doPublishUpdate(@RequestBody PublishUpdateDTO payload) {
        String message;
        HttpStatus status;

        if (MainService.publishUpdate(payload, csCoRestService, appProperties)) {
            message = "Qr code publish update successful";
            status = HttpStatus.OK;
        } else {
            message = "Qr code publish update failed";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(message, status);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> doDeleteQrCode(@PathVariable Integer id) {
        String message;
        HttpStatus status;

        if (MainService.deleteQrCode(id, csCoRestService)) {
            message = "Qr code delete successful";
            status = HttpStatus.OK;
        } else {
            message = "Qr code delete failed";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(message, status);
    }

    @PutMapping("/edit-qr-code/{id}")
    public ResponseEntity<String> doEditQrCode(@PathVariable Integer id, @RequestBody CreatePayloadDTO payload) {
        String message;
        HttpStatus status;

        if (MainService.updateQrCode(id, payload, csCoRestService, appProperties)) {
            message = "Qr code edit successful";
            status = HttpStatus.OK;
        } else {
            message = "Qr code edit failed";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(message, status);
    }

    @GetMapping("/download-qr-code/{id}")
    public void downloadQrCode(@PathVariable Integer id,
                               @RequestParam String qrFormat,
                               @RequestParam String qrColor,
                               @RequestParam String qrModel,
                               HttpServletResponse response) throws IOException, WriterException, TranscoderException {

        DownloadDTO payload = new DownloadDTO();
        payload.setQrFormat(qrFormat);
        payload.setQrColor(qrColor);
        payload.setQrModel(qrModel);

        boolean success = MainService.downloadQrCode(id, payload, response, csCoRestService);

        if (!success) {
            response.reset();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "QR code download failed");
        }
    }


}
