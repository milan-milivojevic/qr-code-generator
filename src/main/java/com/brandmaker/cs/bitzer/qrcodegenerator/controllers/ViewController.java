package com.brandmaker.cs.bitzer.qrcodegenerator.controllers;

import com.brandmaker.cs.bitzer.qrcodegenerator.config.AppProperties;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.CsCoRest;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.CustomStructureCustomObjectsDTO;
import com.brandmaker.cs.bitzer.qrcodegenerator.services.dto.QrCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
        CustomStructureCustomObjectsDTO customObject = csCoRest.getCustomObjectsByCustomStructureId(Integer.parseInt(customStructureId));

        model.addAttribute("qrCodes", customObject.getData());
        model.addAttribute("appVersion", appProperties.getAppVersion());

        log.info("appVersion: " + appProperties.getAppVersion());

        return "index";
    }

    @GetMapping("/error")
    public String getError(Model model) {
        return "error";
    }

    @GetMapping("/test")
    public String getTest(Model model) {

        List<QrCode> qrCodes = List.of(
                new QrCode("kaeltemittelschieber", "print", "new", "0000267", "Android", "Deutsch",
                        "https://qr.bitzer.de/0000267",
                        "https://play.google.com/store/apps/details?id=com.bitzerus.refrigerant.ruler&utm_source=0000267&utm_medium=print&utm_campaign=Android"),
                new QrCode("kaeltemittelschieber", "print", "new", "0000266", "iOS", "Deutsch",
                        "https://qr.bitzer.de/0000266",
                        "https://apps.apple.com/us/app/bitzer-refrigerant-ruler/id373683792?utm_source=0000266&utm_medium=print&utm_campaign=iOS"),
                new QrCode("refrigerantruler", "print", "new", "0000265", "landingpage_en", "Englisch",
                        "https://qr.bitzer.de/0000265", "")
        );

        model.addAttribute("qrCodes", qrCodes);

        return "test";
    }

    @GetMapping("/proto")
    public String getPrototype(Model model) {

        String customStructureId = appProperties.getCustomStructureId();
        CustomStructureCustomObjectsDTO customObject = csCoRest.getCustomObjectsByCustomStructureId(Integer.parseInt(customStructureId));

        model.addAttribute("qrCodes", customObject.getData());

        return "test"; // prototype
    }


}
