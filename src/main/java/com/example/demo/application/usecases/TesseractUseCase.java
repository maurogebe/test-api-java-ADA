package com.example.demo.application.usecases;

import com.example.demo.application.dtos.MedicamentFromFileWithOCRDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import lombok.Data;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Service
public class TesseractUseCase {

    private Tesseract tesseract;
    private MedicamentUseCase medicamentUseCase;

    @Autowired
    public TesseractUseCase(Tesseract tesseract, MedicamentUseCase medicamentUseCase) {
        this.tesseract = tesseract;
        this.medicamentUseCase = medicamentUseCase;
    }

    public List<MedicamentFromFileWithOCRDTO> recognizedText(InputStream inputStream) throws Exception {

        System.out.println("Primer");
        BufferedImage image = ImageIO.read(inputStream);
        System.out.println("Segundo");
        BufferedImage preprocessedImage = preprocessImage(image);
        System.out.println("Tercero");
        BufferedImage adjustImage = adjustContrast(preprocessedImage);
        System.out.println("Cuarto");
        BufferedImage binarizedImage = binarizeImage(adjustImage);
        try {

            System.out.println("Quinto");
            String res = tesseract.doOCR(binarizedImage);
            System.out.println("Sexto");
            return parseTextToMedications(res);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public List<MedicamentFromFileWithOCRDTO>  extractTextFromPdf(File pdfFile) throws IOException, TesseractException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            System.out.println("Primer");
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            System.out.println("Segundo");
            StringBuilder extractedText = new StringBuilder();
            System.out.println("Tercero");

            System.out.println("Cuarto");
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                File tempImageFile = File.createTempFile("pdf_page_" + page, ".png");
                ImageIO.write(image, "png", tempImageFile);

                System.out.println("Quinto");

                String result = tesseract.doOCR(tempImageFile);
                System.out.println("Sexto");
                extractedText.append(result);
                System.out.println("Septimo");
                tempImageFile.delete();
            }

            System.out.println("Octavo");
            String res = extractedText.toString();
            System.out.println("Noveno");
            return parseTextToMedications(res);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ApiRequestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private BufferedImage preprocessImage(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return grayImage;
    }

    private BufferedImage adjustContrast(BufferedImage image) {
        RescaleOp op = new RescaleOp(1.5f, 0, null); // 1.5f es el factor de contraste, ajusta según sea necesario
        return op.filter(image, null);
    }

    private BufferedImage binarizeImage(BufferedImage image) {
        BufferedImage binarizedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics g = binarizedImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return binarizedImage;
    }

    private List<MedicamentFromFileWithOCRDTO> parseTextToMedications(String text) {
        List<MedicamentFromFileWithOCRDTO> medicaments = new ArrayList<>();

        Pattern treatmentPattern = Pattern.compile("TRATAMIENTO\\s*(.*?)\\s*Otras indicaciones", Pattern.DOTALL);
        Matcher matcher = treatmentPattern.matcher(text);

        if (matcher.find()) {
            String treatmentSection = matcher.group(1).trim();

            String[] lines = treatmentSection.split("\\n");
            for (int i = 0; i < lines.length; i += 2) {
                if (i + 1 < lines.length) {
                    String name = lines[i].trim();
                    String secondLine = lines[i + 1].trim();

                    String[] parts = secondLine.split("\\.\\s*");
                    String dose = parts.length > 0 ? parts[0].trim() : "";
                    String form = parts.length > 1 ? parts[1].trim() : "";
                    String instructionsPart1 = parts.length > 2 ? parts[2].trim() : "";
                    String instructionsPart2 = parts.length > 3 ? parts[3].trim() : "";
                    String instructions = instructionsPart1 + ". " + instructionsPart2;

                    medicaments.add(new MedicamentFromFileWithOCRDTO(name, extractFirstNumber(dose), form, instructions));
                }
            }
        }

        return medicaments;
    }

    private String extractPatientInfo(String text) {
        return extractPattern(text, "Nombre:\\s*(.+)");
    }

    private String extractPattern(String text, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private Integer extractFirstNumber(String text) {
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            String numberStr = matcher.group();
            try {
                return Integer.parseInt(numberStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }
}

