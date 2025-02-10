package com.brandmaker.cs.bitzer.qrcodegenerator.restclients.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Integer id;
    private String firstName;
    private String fullName;
    private String lastName;
    private String email;
    private String login;
}
