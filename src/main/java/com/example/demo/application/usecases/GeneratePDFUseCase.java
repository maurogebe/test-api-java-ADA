package com.example.demo.application.usecases;

import com.example.demo.application.exeptions.ApiRequestException;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class GeneratePDFUseCase {

    @Autowired
    private final TemplateEngine templateEngine;

    public GeneratePDFUseCase(TemplateEngine templateEngine) {
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
            throw new ApiRequestException("Error while creating PDF", HttpStatus.BAD_GATEWAY);
        }
        return out.toByteArray();
    }
}

