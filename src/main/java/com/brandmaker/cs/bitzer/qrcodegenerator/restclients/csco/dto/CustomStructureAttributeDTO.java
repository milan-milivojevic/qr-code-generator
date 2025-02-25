package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomStructureAttributeDTO implements Serializable {
    private String name;
    private String label;
    private String comment;
    private Integer order;
    private Integer number;
    private String type;
    private Map<String, String> props;
}
