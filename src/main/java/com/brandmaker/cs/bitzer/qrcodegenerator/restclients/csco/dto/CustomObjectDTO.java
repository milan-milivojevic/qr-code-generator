package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof CustomObjectDto that)) return false;
//        return id == that.id && parentId == that.parentId && lastEditedUserId == that.lastEditedUserId && Objects.equals(name, that.name) && Objects.equals(label, that.label) && state == that.state && Objects.equals(affiliate, that.affiliate) && Objects.equals(attributeValues, that.attributeValues) && Objects.equals(customStructure, that.customStructure) && Objects.equals(createDate, that.createDate) && Objects.equals(updateDate, that.updateDate);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, parentId, name, label, state, affiliate, lastEditedUserId, attributeValues, customStructure, createDate, updateDate);
//    }
}
