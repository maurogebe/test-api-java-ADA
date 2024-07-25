package com.example.demo.application.usecases;

import lombok.Data;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
@Data
@Service
public class TesseractConfigUseCase {

    @Autowired
    Tesseract tesseract;

    public String recognizeText(InputStream inputStream) throws IOException {
        BufferedImage image = ImageIO.read(inputStream);
        try {
            return tesseract.doOCR(image);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "failed";
    }
}
