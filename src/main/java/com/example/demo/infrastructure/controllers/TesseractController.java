package com.example.demo.infrastructure.controllers;


import com.example.demo.application.usecases.TesseractUseCase;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class TesseractController {
    @Autowired
    private TesseractUseCase tesseractUseCase;
    @PostMapping("/ocr")
    public String recognizedText(@RequestParam MultipartFile img) throws IOException {
        return tesseractUseCase.recognizedText(img.getInputStream());
    }
}
