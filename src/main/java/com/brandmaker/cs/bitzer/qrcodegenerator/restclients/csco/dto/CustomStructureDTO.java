package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomStructureDTO {
    private int id;
    private Integer parentId;
    private String name;
    private Map<String, String> label;
    private String type;
    private Integer defaultObjectId;
    private Object attributes;
    private String affiliateType;
    private String previewImageAttributeId;
    private String createDate;
    private String updateDate;
}
