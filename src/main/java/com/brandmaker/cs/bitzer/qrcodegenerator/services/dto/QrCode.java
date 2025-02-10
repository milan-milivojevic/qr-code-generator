package com.brandmaker.cs.bitzer.qrcodegenerator.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QrCode {
    private String name;
    private String medium;
    private String term;
    private String id;
    private String content;
    private String language;
    private String encodedUrl;
    private String trackingUrl;
}
