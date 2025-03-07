package com.brandmaker.cs.bitzer.qrcodegenerator.services;

import com.brandmaker.cs.bitzer.qrcodegenerator.config.AppProperties;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.CsCoRest;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.CustomObjectAttributeValueDTO;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.CustomObjectCreateDTO;
import com.brandmaker.cs.bitzer.qrcodegenerator.restclients.csco.dto.CustomObjectDTO;
import com.brandmaker.cs.bitzer.qrcodegenerator.services.dto.CreatePayloadDTO;
import com.brandmaker.cs.bitzer.qrcodegenerator.services.dto.DownloadDTO;
import com.brandmaker.cs.bitzer.qrcodegenerator.services.dto.PublishUpdateDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;
import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
public class MainService {

    public static boolean createQrCode(CreatePayloadDTO payload, CsCoRest csCoRest, AppProperties appProperties) {
        boolean result = true;

        String customStructureId = appProperties.getCustomStructureId();
        String serverUrl = appProperties.getWebApiRoot();

        // Generate CO name based on campaign name and timestamp
        String formattedCname = payload.getCampaignName().replace(" ", "_");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String finalCustomObjectName = formattedCname + "_" + timestamp;

        CustomObjectCreateDTO createDTO = new CustomObjectCreateDTO();
        createDTO.setName(finalCustomObjectName);
        createDTO.setLabel(Map.of("default", payload.getCampaignName()));
        createDTO.setState("EDIT_AND_ADD");
        createDTO.setCustomStructureId(Integer.valueOf(customStructureId));
        createDTO.setAttributeValues(List.of(
                new CustomObjectAttributeValueDTO(1, "url_code_placeholder", "url_code", "TEXT"),
                new CustomObjectAttributeValueDTO(2, payload.getCampaignName(), "campaign_name", "TEXT"),
                new CustomObjectAttributeValueDTO(3, payload.getCampaignMedium(), "campaign_medium", "TEXT"),
                new CustomObjectAttributeValueDTO(4, payload.getLanguage(), "language", "TEXT"),
                new CustomObjectAttributeValueDTO(5, serverUrl + "/qr-codes/", "url_encoded", "TEXT"),
                new CustomObjectAttributeValueDTO(6, payload.getTrackingUrl(), "tracking_url", "TEXT"),
                new CustomObjectAttributeValueDTO(7, payload.getAdditionalInfo(), "additional_information", "TEXT"),
                new CustomObjectAttributeValueDTO(8, "new", "campaign_subject", "TEXT")
        ));

        CustomObjectDTO customObject = csCoRest.createCustomObject(createDTO);

        if (customObject != null) {
            log.info("Custom object created: {}", customObject.getId());
            // We need to update created Object, so we could use its id as url code
            createDTO.setAttributeValues(List.of(
                    new CustomObjectAttributeValueDTO(1, customObject.getId().toString(), "url_code", "TEXT"),
                    new CustomObjectAttributeValueDTO(2, payload.getCampaignName(), "campaign_name", "TEXT"),
                    new CustomObjectAttributeValueDTO(3, payload.getCampaignMedium(), "campaign_medium", "TEXT"),
                    new CustomObjectAttributeValueDTO(4, payload.getLanguage(), "language", "TEXT"),
                    new CustomObjectAttributeValueDTO(5, serverUrl + "/qr-codes/" + customObject.getId(), "url_encoded", "TEXT"),
                    new CustomObjectAttributeValueDTO(6, payload.getTrackingUrl(), "tracking_url", "TEXT"),
                    new CustomObjectAttributeValueDTO(7, payload.getAdditionalInfo(), "additional_information", "TEXT"),
                    new CustomObjectAttributeValueDTO(8, "new", "campaign_subject", "TEXT"),
                    new CustomObjectAttributeValueDTO(9, "false", "published", "TEXT")

            ));
            try (Response response = csCoRest.updateCustomObject(customObject.getId(), createDTO)) {
                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    log.info("Custom object updated: {}", customObject.getId());
                } else {
                    log.info("Custom object not updated: {}", customObject.getId());
                }
            }
        } else {
            log.info("Custom object not created");
            result = false;
        }

        return result;
    }

    public static boolean publishUpdate(PublishUpdateDTO payload, CsCoRest csCoRest, AppProperties appProperties) {
        log.info("Publish update: {}", payload.toString());
        boolean result = false;

        String customStructureId = appProperties.getCustomStructureId();
        CustomObjectDTO customObjectDTO = csCoRest.getCustomObjectById(payload.getQrId());

        if (customObjectDTO != null) {
            List<CustomObjectAttributeValueDTO> attributeValues = customObjectDTO.getAttributeValues();

            for (CustomObjectAttributeValueDTO attr : attributeValues) {
                if ("published".equals(attr.getAttributeName())) {
                    attr.setValue(payload.getToPublish());
                    break;
                }
            }

            CustomObjectCreateDTO createDTO = new CustomObjectCreateDTO();
            createDTO.setParentId(customObjectDTO.getParentId());
            createDTO.setLabel(customObjectDTO.getLabel());
            createDTO.setName(customObjectDTO.getName());
            createDTO.setState("EDIT_AND_ADD");
            createDTO.setCustomStructureId(Integer.valueOf(customStructureId));
            createDTO.setAttributeValues(attributeValues);

            try (Response response = csCoRest.updateCustomObject(payload.getQrId(), createDTO)) {

                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    log.info("Custom object published attribute updated: {}", payload.getQrId());
                    result = true;
                } else {
                    log.info("Custom object published attribute not updated: {}", payload.getQrId());
                }
            }
        } else {
            log.info("Custom object not found {}", payload.getQrId());
        }

        return result;
    }

    public static boolean deleteQrCode(Integer id, CsCoRest csCoRest) {
        boolean result = false;

        System.out.println("DELETE PAYLOAD: " + id);
        try(Response response = csCoRest.deleteCustomObject(id)) {
            System.out.println("delete status: " + response.getStatus());
            if (response.getStatus() == 204) {
                log.info("Custom object deleted: {}", id);
                result = true;
            } else {
                log.info("Custom object not deleted: {}", id);
            }
        }

        return result;
    }

    public static boolean updateQrCode(Integer id, CreatePayloadDTO payload, CsCoRest csCoRest, AppProperties appProperties) {
        boolean result = false;

        String customStructureId = appProperties.getCustomStructureId();
        CustomObjectDTO customObjectDTO = csCoRest.getCustomObjectById(id);

        if (customObjectDTO != null) {
            List<CustomObjectAttributeValueDTO> attributeValues = customObjectDTO.getAttributeValues();

            attributeValues.forEach(attr -> {
                switch (attr.getAttributeName()) {
                    case "campaign_name" -> {
                        if (payload.getCampaignName() != null && !payload.getCampaignName().isEmpty()) {
                            attr.setValue(payload.getCampaignName());
                        }
                    }
                    case "campaign_medium" -> {
                        if (payload.getCampaignMedium() != null && !payload.getCampaignMedium().isEmpty()) {
                            attr.setValue(payload.getCampaignMedium());
                        }
                    }
                    case "language" -> {
                        if (payload.getLanguage() != null && !payload.getLanguage().isEmpty()) {
                            attr.setValue(payload.getLanguage());
                        }
                    }
                    case "tracking_url" -> {
                        if (payload.getTrackingUrl() != null && !payload.getTrackingUrl().isEmpty()) {
                            attr.setValue(payload.getTrackingUrl());
                        }
                    }
                    case "additional_information" -> {
                        if (payload.getAdditionalInfo() != null && !payload.getAdditionalInfo().isEmpty()) {
                            attr.setValue(payload.getAdditionalInfo());
                        }
                    }
                }
            });

            CustomObjectCreateDTO createDTO = new CustomObjectCreateDTO();
            createDTO.setParentId(customObjectDTO.getParentId());
            createDTO.setLabel(customObjectDTO.getLabel());
            createDTO.setName(customObjectDTO.getName());
            createDTO.setState("EDIT_AND_ADD");
            createDTO.setCustomStructureId(Integer.valueOf(customStructureId));
            createDTO.setAttributeValues(attributeValues);

            try (Response response = csCoRest.updateCustomObject(id, createDTO)) {

                if (response.getStatus() == Response.Status.OK.getStatusCode()) {

                    log.info("Custom object updated: {}", id);
                    result = true;
                } else {
                    log.info("Custom object not updated: {}", id);
                }
            }
        } else {
            log.info("Custom object not found {}", id);
        }

        return result;
    }

    public static boolean downloadQrCode(Integer id, DownloadDTO payload, HttpServletResponse response, CsCoRest csCoRest) throws IOException {
        try {
            CustomObjectDTO customObjectDTO = csCoRest.getCustomObjectById(id);

            if (customObjectDTO == null) {
                log.info("Custom object not found {}", id);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Custom object not found");
                return false;
            }

            int size = 300; // QR code size

            Color fgColor = switch (payload.getQrColor().toLowerCase()) {
                case "green" -> Color.decode("#3aaa35");
                case "white" -> Color.WHITE;
                default -> Color.BLACK;
            };

            Color bgColor = new Color(0, 0, 0, 0);  // ARGB = fully transparent

//            Color fgColor = switch (payload.getQrColor().toLowerCase()) {
//                case "white" -> Color.BLACK;
//                default -> Color.WHITE;
//            };

            // Generate QR Code
            String qrText = fetchQrTextFromCO(customObjectDTO);
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrText, BarcodeFormat.QR_CODE, size, size);

            try (OutputStream out = response.getOutputStream()) {
                // Determine format and write response
                if ("png".equalsIgnoreCase(payload.getQrFormat())) {
                    response.setContentType("image/png");
                    response.setHeader("Content-Disposition", "attachment; filename=qrcode.png");
                    ImageIO.write(toBufferedImage(bitMatrix, bgColor, fgColor), "png", out);
                } else if ("eps".equalsIgnoreCase(payload.getQrFormat())) {
                    response.setContentType("application/postscript");
                    response.setHeader("Content-Disposition", "attachment; filename=qrcode.eps");
                    generateEps(bitMatrix, out, fgColor, payload.getQrModel());
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            response.reset();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "QR code generation failed");
            return false;
        }
    }

    // Convert QR code BitMatrix to BufferedImage
    private static BufferedImage toBufferedImage(BitMatrix bitMatrix, Color bgColor, Color fgColor) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? fgColor.getRGB() : bgColor.getRGB());
            }
        }
        return image;
    }


    private static void generateEps(BitMatrix bitMatrix, OutputStream out, Color fgColor, String model) throws IOException {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        EPSDocumentGraphics2D g2d = new EPSDocumentGraphics2D(false);
        g2d.setupDocument(out, width, height);

        // Manually initialize the graphic context if it isn’t already set.
        if (g2d.getGraphicContext() == null) {
            g2d.setGraphicContext(new GraphicContext());
        }

        try {
            // --- Coordinate Transformation ---
            // Flip the y-axis so that (0,0) becomes the top-left.
            g2d.translate(0, height);
            g2d.scale(1, -1);

            // --- Fill the Background ---
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);

            // --- Set Foreground Color ---
            g2d.setColor(fgColor);

            // --- Draw the QR Code ---
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y)) {
                        g2d.fillRect(x, y, 1, 1);
                    }
                }
            }

            // Finalize the EPS document.
            g2d.finish();
        } finally {
            g2d.dispose();
        }
    }

//    private static void generateEps(BitMatrix bitMatrix, OutputStream out, Color bgColor, String model) throws IOException {
//        int width = bitMatrix.getWidth();
//        int height = bitMatrix.getHeight();
//
//        EPSDocumentGraphics2D g2d = new EPSDocumentGraphics2D(false);
//        g2d.setupDocument(out, width, height); // Initialize EPS document
//
//        try {
//            // Set background color
//            g2d.setBackground(bgColor);
//            g2d.clearRect(0, 0, width, height);
//
//            // Set color model
//            if ("cmyk".equalsIgnoreCase(model)) {
//                g2d.setColor(new Color(0, 0, 0, 1)); // Simulating CMYK black
//            } else {
//                g2d.setColor(Color.BLACK); // Default RGB
//            }
//
//            // Draw QR code
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    if (bitMatrix.get(x, y)) {
//                        g2d.fillRect(x, y, 1, 1);
//                    }
//                }
//            }
//
//            g2d.finish(); // Properly close EPS
//        } finally {
//            g2d.dispose(); // Manually close resources
//        }
//    }

    private static String fetchQrTextFromCO(CustomObjectDTO customObjectDTO) {
        String qrText = "";
        for (CustomObjectAttributeValueDTO attr : customObjectDTO.getAttributeValues()) {
            if ("url_encoded".equals(attr.getAttributeName())) {
                qrText = attr.getValue();
                break;
            }
        }
        return qrText;
    }
}
