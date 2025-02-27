package com.brandmaker.cs.bitzer.qrcodegenerator.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class CreatePayloadDTO {
    String campaignName;
    String campaignMedium;
    String language;
    String trackingUrl;
    String additionalInfo;
}
