package com.brandmaker.cs.bitzer.qrcodegenerator.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class DownloadDTO {
    private String qrFormat;
    private String qrColor;
    private String qrModel;

    // Getters and Setters
    public String getQrFormat() { return qrFormat; }
    public void setQrFormat(String qrFormat) { this.qrFormat = qrFormat; }

    public String getQrColor() { return qrColor; }
    public void setQrColor(String qrColor) { this.qrColor = qrColor; }

    public String getQrModel() { return qrModel; }
    public void setQrModel(String qrModel) { this.qrModel = qrModel; }
}
