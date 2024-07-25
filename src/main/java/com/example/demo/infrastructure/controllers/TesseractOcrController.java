package com.example.demo.infrastructure.controllers;

import com.example.demo.application.usecases.TesseractConfigUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ocr")
public class  TesseractOcrController {

    @Autowired
    private TesseractConfigUseCase tesseractConfigUseCase;
    @PostMapping
    public String recognizeText(@RequestParam("img") MultipartFile img) throws IOException {
        return tesseractConfigUseCase.recognizeText(img.getInputStream());
    }
}
