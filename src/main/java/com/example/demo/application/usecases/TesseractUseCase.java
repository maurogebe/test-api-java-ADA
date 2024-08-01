package com.example.demo.application.usecases;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Service
public class TesseractUseCase {

    private Tesseract tesseract;

    @Autowired
    public TesseractUseCase(Tesseract tesseract) {
        this.tesseract = tesseract;
    }

    public String recognizedText(InputStream inputStream) throws Exception {

        BufferedImage image = ImageIO.read(inputStream);
        BufferedImage preprocessedImage = preprocessImage(image);
        BufferedImage adjustImage = adjustContrast(preprocessedImage);
        BufferedImage binarizedImage = binarizeImage(adjustImage);
        try {

            return tesseract.doOCR(binarizedImage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public String  extractTextFromPdf(File pdfFile) throws IOException, TesseractException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            StringBuilder extractedText = new StringBuilder();

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                File tempImageFile = File.createTempFile("pdf_page_" + page, ".png");
                ImageIO.write(image, "png", tempImageFile);


                String result = tesseract.doOCR(tempImageFile);
                extractedText.append(result);
                tempImageFile.delete();
            }

            return extractedText.toString();
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

