package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectDTO implements Serializable {
    private Integer id;
    private Integer parentId;
    private String name;
    private Map<String, String> label;
    private String state;
    private String affiliate;
    private Integer lastEditedUserId;
    private List<CustomObjectAttributeValueDTO> attributeValues;
    private CustomStructureDTO customStructure;
    private String createDate;
    private String updateDate;

    public Optional<String> getAttributeValue(String attributeName) {
        if (attributeValues == null) {
            return Optional.empty();
        }
        return attributeValues.stream()
                .filter(av -> attributeName.equals(av.getAttributeName()))
                .map(CustomObjectAttributeValueDTO::getValue)
                .findFirst();
    }
}
