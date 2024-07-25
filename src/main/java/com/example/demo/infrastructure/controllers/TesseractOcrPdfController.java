package com.example.demo.infrastructure.controllers;

import net.minidev.json.JSONObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@PostMapping("pdf/extractText")
public @ResponseBody ResponseEntity<String>
        @Autowired
extractTextFromPDFFile(@RequestParam("file") MultipartFile file) {
    try {
        // Load file into PDFBox class
        PDDocument document = PDDocument.load(file.getBytes());
        PDFTextStripper stripper = new PDFTextStripper();
        String strippedText = stripper.getText(document);

        // Check text exists into the file
        if (strippedText.trim().isEmpty()){
            strippedText = extractTextFromScannedDocument(document);
        }

        JSONObject obj = new JSONObject();
        obj.put("fileName", file.getOriginalFilename());
        obj.put("text", strippedText.toString());

        return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}


