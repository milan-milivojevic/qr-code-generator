package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllCustomObjectsDTO {
    int totalCount;
    List<CustomObjectDTO> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomObjectDTO {
        private int id;
        private Integer parentId;
        private String name;
        private Map<String, String> label; // Handles the dynamic keys like "DE" and "EN"
        private String state;
        private Integer affiliate;
        private int lastEditedUserId;
        private Object attributeValues;
        private CustomStructureDTO customStructure;
        private String createDate;
        private String updateDate;

    }
}
