package com.example.demo.application.usecases;

import lombok.Data;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
@Data
@Service
private String extractTextFromScannedDocument(PDDocument document)

        @Autowired
        Tesseract tesseract;
        throws IOException, TesseractException {

    // Extract images from file
    PDFRenderer pdfRenderer = new PDFRenderer(document);
    StringBuilder out = new StringBuilder();

    Tesseract  tesseract = new Tesseract();
    tesseract.setDatapath("src/main/resources/tessdata");
    tesseract.setLanguage("spa"); // choose your language

    for (int page = 0; page < document.getNumberOfPages(); page++)
    {
        BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

        // Create a temp image file
        File temp = File.createTempFile("tempfile_" + page, ".png");
        ImageIO.write(bim, "png", temp);

        String result = tesseract.doOCR(temp);
        out.append(result);

        // Delete temp file
        temp.delete();

    }

    return out.toString();

}
