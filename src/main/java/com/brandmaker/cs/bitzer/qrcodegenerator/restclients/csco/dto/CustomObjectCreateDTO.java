package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectCreateDTO implements Serializable {
    private String name;
    private Map<String, String> label;
    private Integer customStructureId;
    private String state;
    private Integer parentId;
    private String affiliate;
    private List<CustomObjectAttributeValueDTO> attributeValues;
}
