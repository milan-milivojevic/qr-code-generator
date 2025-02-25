package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectAttributeValueDTO implements Serializable {

    private Integer number;
    private String value;
    private String attributeName;
//    private String label;
    private String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomObjectAttributeValueDTO that = (CustomObjectAttributeValueDTO) o;
        return number == that.number && Objects.equals(value, that.value) && Objects.equals(attributeName, that.attributeName) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, value, attributeName, type);
    }
}
