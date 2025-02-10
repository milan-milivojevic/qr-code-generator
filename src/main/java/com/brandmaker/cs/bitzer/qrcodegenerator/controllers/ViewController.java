package com.brandmaker.cs.bitzer.qrcodegenerator.controllers;

import com.brandmaker.cs.bitzer.qrcodegenerator.config.AppProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final AppProperties appProperties;

    public ViewController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        System.out.println("getIndex");

        model.addAttribute("message", "Welcome to Bitzer QR Code Generator!");
        model.addAttribute("appVersion", appProperties.getAppVersion());

        System.out.println("appVersion: " + appProperties.getAppVersion());

        return "index";
    }

    @GetMapping("/error")
    public String getError(Model model) {
        System.out.println("get error");

        return "error";
    }

    @GetMapping("/test")
    public String getTest(Model model) {
        System.out.println("getTest");

        return "index";
    }


}
