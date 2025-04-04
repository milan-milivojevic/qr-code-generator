package com.brandmaker.cs.bitzer.qrcodegenerator.controllers;

import com.brandmaker.cs.bitzer.qrcodegenerator.config.AppProperties;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.CsCoRest;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.CustomObjectDTO;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.CustomStructureCustomObjectsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller
public class ViewController {

    private final AppProperties appProperties;
    private CsCoRest csCoRest;

    public ViewController(AppProperties appProperties, CsCoRest csCoRest ) {
        this.appProperties = appProperties;
        this.csCoRest = csCoRest;
    }

    @GetMapping("/")
    public String getIndex(Model model) {

        String customStructureId = appProperties.getCustomStructureId();
        String serverUrl = appProperties.getWebApiRoot();
        CustomStructureCustomObjectsDTO customObject = csCoRest.getCustomObjectsByCustomStructureId(Integer.parseInt(customStructureId));

        model.addAttribute("qrCodes", customObject.getData());
        model.addAttribute("serverUrl", serverUrl);
        model.addAttribute("appVersion", appProperties.getAppVersion());

        log.info("appVersion: " + appProperties.getAppVersion());

        return "index";
    }

    @GetMapping("/{id:[0-9]+}")
    public String redirectToExternalUrl(@PathVariable int id) {
        // Retrieve the object by ID
        CustomObjectDTO customObjectDTO = csCoRest.getCustomObjectById(id);
        if (customObjectDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Custom object not found");
        }

        // Extract "tracking_url" attribute
        String trackingUrl = customObjectDTO
                .getAttributeValue("tracking_url")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tracking URL not found"
                ));

        // Check if the qr code is published
        String isPublished = customObjectDTO
                .getAttributeValue("published")
                .orElse("false");

        if (isPublished.equals("false")) {
            return "redirect:" + appProperties.getBitzerUrl();
        }

        // If the value doesn't start with "http://" or "https://", prepend it
        if (!trackingUrl.startsWith("http://") && !trackingUrl.startsWith("https://")) {
            trackingUrl = "https://" + trackingUrl;
        }

//        // Add GA4 tracking params
//        String utmMedium = customObjectDTO.getAttributeValue("campaign_medium").orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medium URL param not found")
//        );
//        String utmCampaign = customObjectDTO.getAttributeValue("campaign_name").orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign URL param not found")
//        );
//        trackingUrl = trackingUrl + "?utm_source=" + id + "&utm_medium=" + utmMedium + "&utm_campaign=" + utmCampaign;

        // Return "redirect:{url}" so Spring will issue a 302 redirect
        return "redirect:" + trackingUrl;
    }

    @GetMapping("/error")
    public String getError(Model model) {
        return "error";
    }


}
