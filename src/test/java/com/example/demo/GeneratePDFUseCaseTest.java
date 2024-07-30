package com.example.demo;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.usecases.GeneratePDFUseCase;
import com.lowagie.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GeneratePDFUseCaseTest {

//    @Mock
//    private TemplateEngine templateEngine;
//
//    @InjectMocks
//    private GeneratePDFUseCase generatePDFUseCase;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreatePdf_Success() throws DocumentException {
//        // Arrange
//        String templateName = "template";
//        ModelMap model = new ModelMap();
//        model.addAttribute("key", "value");
//        String html = "<html><body>Test PDF</body></html>";
//
//        when(templateEngine.process(anyString(), any(Context.class))).thenReturn(html);
//
//        ITextRenderer renderer = mock(ITextRenderer.class);
//        doNothing().when(renderer).setDocumentFromString(anyString());
//        doNothing().when(renderer).layout();
//        doNothing().when(renderer).createPDF(any(ByteArrayOutputStream.class));
//
//        // Use reflection or modify the class to inject a mock ITextRenderer if needed
//        // This is a pseudo-code example, adjust according to your setup
//        // generatePDFUseCase.setITextRenderer(renderer);
//
//        // Act
//        byte[] pdfBytes = generatePDFUseCase.createPdf(templateName, model);
//
//        // Assert
//        assertNotNull(pdfBytes);
//        assertTrue(pdfBytes.length > 0);
//        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
//        // Verify interactions with ITextRenderer if possible
//    }
//
//    @Test
//    public void testCreatePdf_DocumentException() {
//        // Arrange
//        String templateName = "template";
//        ModelMap model = new ModelMap();
//        model.addAttribute("key", "value");
//        String html = "<html><body>Test PDF</body></html>";
//
//        when(templateEngine.process(anyString(), any(Context.class))).thenReturn(html);
//
//        ITextRenderer renderer = mock(ITextRenderer.class);
//        doThrow(DocumentException.class).when(renderer).createPDF(any(ByteArrayOutputStream.class));
//
//        // Inject the mock into the GeneratePDFUseCase instance using reflection
//        generatePDFUseCase.setITextRenderer(renderer);
//
//        // Act & Assert
//        ApiRequestException exception = assertThrows(ApiRequestException.class, () ->
//                generatePDFUseCase.createPdf(templateName, model)
//        );
//        assertEquals("Error while creating PDF", exception.getMessage());
//        assertEquals(HttpStatus.BAD_GATEWAY, exception.getHttpStatus());
//    }

}