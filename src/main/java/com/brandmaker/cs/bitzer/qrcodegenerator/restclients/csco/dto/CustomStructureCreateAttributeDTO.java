package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomStructureCreateAttributeDTO {
    private String name;
    private String label;
    private String comment;
    private String type;
    private Integer order;
}
