package com.example.demo.application.usecases;

import lombok.Data;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
@Data
@Service
public class TesseractUseCase {

    @Autowired
    private Tesseract tesseract;

    public String recognizedText(InputStream inputStream) throws IOException {

        BufferedImage image = ImageIO.read(inputStream);
        try {

            return tesseract.doOCR(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failder";
    }
    public String extractTextFromPdf(File pdfFile) throws IOException, TesseractException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            StringBuilder extractedText = new StringBuilder();

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                File tempImageFile = File.createTempFile("pdf_page_" + page, ".png");
                ImageIO.write(image, "png", tempImageFile);

                String result = tesseract.doOCR(tempImageFile);
                extractedText.append(result);
                tempImageFile.delete(); // Eliminar archivo temporal después de usar
            }

            return extractedText.toString();
        }
    }

}

