package com.example.demo.application.usecases;

import com.example.demo.domain.entities.MedicamentSold;
import com.example.demo.domain.entities.Patient;
import com.example.demo.domain.entities.Sale;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class GeneratePDFUsecase {

    @Autowired
    private final TemplateEngine templateEngine;

    public GeneratePDFUsecase(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] createPdf(String templateName, ModelMap model) {
        // Create a Thymeleaf context and set the variables for the template
        Context context = new Context();
        context.setVariables(model);

        // Process the template and model using Thymeleaf, which generates the HTML
        String html = templateEngine.process(templateName, context);

        System.out.println(html);

        // Convert the generated HTML to PDF using Flying Saucer and return as byte[]
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(out);
        } catch (DocumentException e) {
            throw new RuntimeException("Error while creating PDF", e);
        }
        return out.toByteArray();
    }
}

