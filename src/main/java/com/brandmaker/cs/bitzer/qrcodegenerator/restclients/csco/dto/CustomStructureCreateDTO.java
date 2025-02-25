package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomStructureCreateDTO implements Serializable {
    private String name;
    private Map<String, String> label;
    private String type;
    private Integer parentId;
    private Integer defaultObjectId;
    private String affiliateType;
    private String previewImageAttributeId;
    private List<CustomStructureAttributeDTO> attributes;
    private List<String> modules;
}
