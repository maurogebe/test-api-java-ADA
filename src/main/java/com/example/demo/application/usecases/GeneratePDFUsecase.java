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
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class GeneratePDFUsecase {

    public byte[] generatePDF(Sale sale) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


            document.add(new Paragraph("Detalle de Venta"));
            document.add(new Paragraph("Fecha de venta: " + sale.getSaleDate().format(formatter)));
            document.add(new Paragraph("Número de venta: " + sale.getId()));


            java.util.List<MedicamentSold> medicamentsSold = sale.getMedicamentsSold();
            if (medicamentsSold != null && !medicamentsSold.isEmpty()) {
                document.add(new Paragraph("Medicamentos Vendidos:"));
                com.itextpdf.layout.element.List pdfList = new com.itextpdf.layout.element.List();  // Usar la List de iText
                for (MedicamentSold medicamentSold : medicamentsSold) {
                    String medicamentDetails = String.format(
                            "ID Medicamento: %d, Cantidad: %d, Precio por Unidad: $%.2f",
                            medicamentSold.getMedicament().getId(),
                            medicamentSold.getQuantity(),
                            medicamentSold.getMedicament().getCost()
                    );
                    pdfList.add(new ListItem(medicamentDetails));
                }
                document.add(pdfList);
            }


            document.add(new Paragraph("Total: $" + sale.getTotal()));


            Patient patient = sale.getPatient();
            if (patient != null) {
                document.add(new Paragraph("Detalle del Paciente"));
                document.add(new Paragraph("ID Paciente: " + patient.getId()));
                document.add(new Paragraph("Nombre Paciente: " + patient.getName()));
                document.add(new Paragraph("Email Paciente: " + patient.getEmail()));
                document.add(new Paragraph("Número de Seguro: " + patient.getHealthInsuranceNumber()));
                document.add(new Paragraph("Fecha de Nacimiento: " + patient.getBirthDate().format(formatter)));
            }

            document.close();
            return outputStream.toByteArray();
        }
    }
}

