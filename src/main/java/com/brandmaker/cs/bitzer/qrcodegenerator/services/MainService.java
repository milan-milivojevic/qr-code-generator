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
import java.util.Properties;
import net.sf.epsgraphics.ColorMode;
import net.sf.epsgraphics.EpsGraphics;

@Slf4j
public class MainService {

    public static int createQrCode(CreatePayloadDTO payload, CsCoRest csCoRest, AppProperties appProperties) {
        int result = 0;

        String customStructureId = appProperties.getCustomStructureId();
        String serverUrl = appProperties.getWebApiRoot();

        // Generate CO name based on campaign name and timestamp
        String formattedCname = payload.getCampaignName().replace(" ", "_").replace("-", "_");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String finalCustomObjectName = formattedCname + "_" + timestamp;
        finalCustomObjectName = specialCharHandling(finalCustomObjectName);
        finalCustomObjectName = normalizeUnderscores(finalCustomObjectName);
        System.out.println(finalCustomObjectName);

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
            // We need to update created Object, so we could use its id as url code and set GA4
            String qrId = customObject.getId().toString();

            // Sset tracking url GA4 params
            String trackingUrl = payload.getTrackingUrl();
            String params = "?utm_source=qr-" + qrId + "&utm_medium=" + payload.getCampaignMedium() + "&utm_campaign=" + payload.getCampaignName();
            trackingUrl = trackingUrl + params.toLowerCase();

            createDTO.setAttributeValues(List.of(
                    new CustomObjectAttributeValueDTO(1, qrId, "url_code", "TEXT"),
                    new CustomObjectAttributeValueDTO(2, payload.getCampaignName(), "campaign_name", "TEXT"),
                    new CustomObjectAttributeValueDTO(3, payload.getCampaignMedium(), "campaign_medium", "TEXT"),
                    new CustomObjectAttributeValueDTO(4, payload.getLanguage(), "language", "TEXT"),
                    new CustomObjectAttributeValueDTO(5, serverUrl + "/qr-codes/" + customObject.getId(), "url_encoded", "TEXT"),
                    new CustomObjectAttributeValueDTO(6, trackingUrl, "tracking_url", "TEXT"),
                    new CustomObjectAttributeValueDTO(7, payload.getAdditionalInfo(), "additional_information", "TEXT"),
                    new CustomObjectAttributeValueDTO(8, "new", "campaign_subject", "TEXT"),
                    new CustomObjectAttributeValueDTO(9, "false", "published", "TEXT")

            ));
            try (Response response = csCoRest.updateCustomObject(customObject.getId(), createDTO)) {
                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    log.info("Custom object updated: {}", customObject.getId());
                    result = customObject.getId();
                } else {
                    log.info("Custom object not updated: {}", customObject.getId());
                }
            }
        } else {
            log.info("Custom object not created");
        }

        return result;
    }

    public static String specialCharHandling(String input) {
        if (input == null) return null;

        return input
                .replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("Ä", "Ae")
                .replace("Ö", "Oe")
                .replace("Ü", "Ue")
                .replace("ß", "ss")
                .replace("é", "e")
                .replace("è", "e")
                .replace("ê", "e")
                .replace("á", "a")
                .replace("à", "a")
                .replace("â", "a")
                .replace("ç", "c")
                .replace("ñ", "n")
                .replace("\"", "_")
                .replace("'", "_")
                .replace("&", "and")
                .replace("/", "_")
                .replace("\\", "_")
                .replace(",", "_")
                .replace(".", "_")
                .replace(":", "_")
                .replace(";", "_")
                .replace("!", "_")
                .replace("?", "_")
                .replace("(", "_")
                .replace(")", "_")
                .replace("{", "_")
                .replace("}", "_")
                .replace("]", "_")
                .replace("[", "_")
                .replace("-", "_")
                .replace("=", "equals")
                .replace("+", "plus")
                .replace("*", "_")
                .replace("%", "percent")
                .replace("$", "dollar")
                .replace("@", "at")
                .replace("#", "hash")
                .replace("^", "_")
                .replace("~", "_")
                .replace("`", "_")
                .replace("<", "_")
                .replace(">", "_");
    }

    public static String normalizeUnderscores(String input) {
        if (input == null) return null;

        // 1. Collapse multiple underscores into one
        String collapsed = input.replaceAll("_+", "_");

        // 2. Remove leading and trailing underscores
        String cleaned = collapsed.replaceAll("^_+|_+$", "");

        return cleaned;
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
                        String originalTrackingUrl = payload.getTrackingUrl();
                        String updatedTrackingUrl = "";
                        if (originalTrackingUrl != null && !originalTrackingUrl.isEmpty()) {
                            // Remove old GA4 if they exist
                            if (originalTrackingUrl.contains("utm_source=") &&
                                originalTrackingUrl.contains("utm_medium=") &&
                                originalTrackingUrl.contains("utm_campaign=")) {

                                int firstIdx = originalTrackingUrl.indexOf('?');
                                int secondIdx = originalTrackingUrl.indexOf('?', firstIdx + 1);

                                if (secondIdx != -1) {
                                    // Cut at second '?'
                                    originalTrackingUrl = originalTrackingUrl.substring(0, secondIdx);
                                } else if (firstIdx != -1) {
                                    // Cut at first '?'
                                    originalTrackingUrl = originalTrackingUrl.substring(0, firstIdx);
                                }

//                                int idx = originalTrackingUrl.indexOf('?');
//                                if (idx != -1) {
//                                    originalTrackingUrl =  originalTrackingUrl.substring(0, idx);
//                                }
                            }

                            // create new GA4
                            String params = "?utm_source=qr-" + id + "&utm_medium=" + payload.getCampaignMedium() + "&utm_campaign=" + payload.getCampaignName();
                            updatedTrackingUrl = originalTrackingUrl + params.toLowerCase();
                            System.out.println("updated tracking url 1: " + updatedTrackingUrl);

                            attr.setValue(updatedTrackingUrl);
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

            Color fgColor;
            String fileModel = payload.getQrModel();
            if (fileModel.equals("rgb")) {
                fgColor = switch (payload.getQrColor().toLowerCase()) {
                    case "green" -> Color.decode("#3aaa35");
                    case "white" -> Color.WHITE;
                    default -> Color.BLACK;
                };
            } else {
                fgColor = switch (payload.getQrColor().toLowerCase()) {
                    case "green" -> new Color(58, 170, 53);
                    case "white" -> Color.WHITE;
                    default -> Color.BLACK;
                };
            }


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
                    generateEps(bitMatrix, out, fgColor, fileModel);
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

    private static void generateEps(
            BitMatrix bitMatrix,
            OutputStream out,
            Color fgColor,
            String colorModel // "rgb" or "cmyk"
    ) throws IOException {
        System.out.println("Generating eps for " + bitMatrix.getWidth() + " x " + bitMatrix.getHeight());
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        // Pick either CMYK or RGB
        ColorMode epsColorMode;
        if ("cmyk".equalsIgnoreCase(colorModel)) {
            epsColorMode = ColorMode.COLOR_CMYK;
        } else {
            epsColorMode = ColorMode.COLOR_RGB;
        }

        // Create EpsGraphics with the selected ColorMode
        EpsGraphics g2d = new EpsGraphics(
                "QRCode",   // EPS title
                out,        // OutputStream
                0, 0,       // lower-left corner
                width,      // bounding box width
                height,     // bounding box height
                epsColorMode
        );

        try {
            // If you want top-left (0,0):
            g2d.translate(0, height);
            g2d.scale(1, -1);

            // Set background if not cmyk and white
            if (epsColorMode == ColorMode.COLOR_CMYK && fgColor == Color.WHITE) {
                System.out.println("CMYK WHITE");
            } else {
                System.out.println("set background color");
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, width, height);
            }

            // Foreground
            if (epsColorMode == ColorMode.COLOR_CMYK) {
                System.out.println("cmyk");
                if (fgColor == Color.BLACK || fgColor == Color.WHITE) {
                    g2d.setColor(fgColor);
                } else {
                    // Hard-coded brand CMYK, e.g. 75/0/100/0
                    float c = 0.75f, m = 0f, y = 1.0f, k = 0f;
                    // Convert to approximate sRGB (since Java Color is inherently RGB)
                    float R = (1 - c) * (1 - k);
                    float G = (1 - m) * (1 - k);
                    float B = (1 - y) * (1 - k);
                    g2d.setColor(new Color(R, G, B));
                    System.out.println("Color is " + R + " " + G + " " + B);
                }
            } else {
                System.out.println("rgb");
                // Just use a hex color for "rgb"
                g2d.setColor(fgColor);
            }

            // Draw the QR code
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y)) {
                        g2d.fillRect(x, y, 1, 1);
                    }
                }
            }
        } finally {
            g2d.flush();
            g2d.close(); // finalizes the EPS
        }
    }

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
