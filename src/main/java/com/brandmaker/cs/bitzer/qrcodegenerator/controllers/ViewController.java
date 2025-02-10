package com.brandmaker.cs.bitzer.qrcodegenerator.controllers;

import com.brandmaker.cs.bitzer.qrcodegenerator.config.AppProperties;
import com.brandmaker.cs.bitzer.qrcodegenerator.services.dto.QrCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

@Controller
public class ViewController {

    private final AppProperties appProperties;

    public ViewController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping("/")
    public String getIndex(Model model) {

        model.addAttribute("message", "Welcome to Bitzer QR Code Generator!");
        model.addAttribute("appVersion", appProperties.getAppVersion());

        System.out.println("appVersion: " + appProperties.getAppVersion());

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
    public String getProtorype(Model model) {

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

        return "prototype";
    }


}
