package com.brandmaker.cs.bitzer.qrcodegenerator.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class PublishUpdateDTO {
    int qrId;
    String toPublish;
}
