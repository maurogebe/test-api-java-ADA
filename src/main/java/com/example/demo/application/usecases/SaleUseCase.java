package com.example.demo.application.usecases;

import com.example.demo.application.dtos.MedicamentDTO;
import com.example.demo.application.dtos.MedicamentSoldWithMedicamentDTO;
import com.example.demo.application.dtos.PatientDTO;
import com.example.demo.application.dtos.SaleWithMedicamentDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.SaleMapper;
import com.example.demo.domain.entities.Patient;
import com.example.demo.domain.entities.Sale;
import com.example.demo.domain.repositories.SaleRepository;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.Attachment;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
@Service
public class SaleUseCase {

    private SaleRepository saleRepository;
    private MedicamentUseCase medicamentUseCase;
    private MailjetEmailUseCase mailjetEmailUseCase;
    private PatientUseCase patientUseCase;
    private GeneratePDFUseCase generatePDFUseCase;

    @Autowired
    public SaleUseCase(PatientUseCase patientUseCase, MailjetEmailUseCase mailjetEmailUseCase, SaleRepository saleRepository, MedicamentUseCase medicamentUseCase, GeneratePDFUseCase generatePDFUseCase) {
        this.patientUseCase = patientUseCase;
        this.mailjetEmailUseCase = mailjetEmailUseCase;
        this.saleRepository = saleRepository;
        this.medicamentUseCase = medicamentUseCase;
        this.generatePDFUseCase = generatePDFUseCase;
    }

    public SaleWithMedicamentDTO createSale(SaleWithMedicamentDTO sale) throws MailjetException, IOException {
        List<Long> ids = sale.getMedicamentsSold().stream()
            .map(MedicamentSoldWithMedicamentDTO::getMedicament)
            .map(MedicamentDTO::getId)
            .toList();
        List<MedicamentDTO> medicaments = medicamentUseCase.getMedicamentsById(ids);

        Map<Long, MedicamentDTO> medicamentMap = medicaments.stream()
            .collect(Collectors.toMap(MedicamentDTO::getId, medicament -> medicament));

        sale.getMedicamentsSold().forEach(medicamentSold -> {
            MedicamentDTO medicament = medicamentMap.get(medicamentSold.getMedicament().getId());
            if (medicament != null) {
                medicamentSold.setMedicament(medicament);
            }
        });
        sale.setTotal(calculateTotalCost(sale.getMedicamentsSold()));

        Sale saleSave = SaleMapper.INSTANCE.saleWithMedicamentDTOToSale(sale);

        saleSave.getMedicamentsSold().forEach(medicamentSold -> medicamentSold.setSale(saleSave));

        Sale saleSaved = this.saleRepository.save(saleSave);

        byte[] pdf = generatePdf(saleSaved.getId());
        InputStream resource = new ByteArrayResource(pdf).getInputStream();

        String fileName = "venta-" + UUID.randomUUID() + ".pdf";

        Attachment attachment = Attachment.fromInputStream(resource, fileName, "pdf");

        String htmlBody = "<html>" +
                "<head>" +
                "  <style>" +
                "    body { font-family: Arial, sans-serif; color: #333; }" +
                "    .container { width: 80%; margin: 0 auto; padding: 20px; }" +
                "    .header { background-color: #f8f8f8; padding: 10px; text-align: center; border-bottom: 1px solid #ddd; }" +
                "    .content { margin: 20px 0; }" +
                "    .footer { background-color: #f8f8f8; padding: 10px; text-align: center; border-top: 1px solid #ddd; font-size: 0.8em; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>" +
                "      <h1>Factura de Venta</h1>" +
                "    </div>" +
                "    <div class='content'>" +
                "      <p>Estimado/a [Nombre del Cliente],</p>" +
                "      <p>Adjunto a este correo electrónico encontrará la factura correspondiente a la venta realizada en nuestra tienda.</p>" +
                "      <p>Por favor, revise el archivo adjunto para los detalles completos de la transacción.</p>" +
                "      <p>Si tiene alguna pregunta o necesita asistencia adicional, no dude en ponerse en contacto con nosotros.</p>" +
                "      <p>Gracias por su compra.</p>" +
                "    </div>" +
                "    <div class='footer'>" +
                "      <p>&copy; [Año] [Nombre de la Empresa]. Todos los derechos reservados.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";


        mailjetEmailUseCase.sendEmailWithAttachment(new ArrayList<>(Collections.singleton(saleSaved.getPatient().getEmail())), "Envio de factura de venta", htmlBody, attachment);

        AtomicInteger newStock = new AtomicInteger();

        sale.getMedicamentsSold().forEach(medicamentSold -> {
            MedicamentDTO medicament = medicamentMap.get(medicamentSold.getMedicament().getId());
            if (medicament != null){
                newStock.set(medicament.getStock() - medicamentSold.getQuantity());

                if (newStock.get() < 0){
                    throw new IllegalStateException("Stock insuficiente para el medicamento ID: " + medicament.getId());
                }
                medicament.setStock(newStock.get());
                medicamentUseCase.updateMedicament(medicament.getId(), medicament);
            }
        });

        List<MedicamentDTO> medicamentList = medicamentUseCase.getLowStockMedicaments();

        if(!medicamentList.isEmpty()) {
            List<PatientDTO> patientsDTO = patientUseCase.getPatients();
            List<String> emails = patientsDTO.stream()
                .map(PatientDTO::getEmail)
                .toList();

            String report = generateReportLowStockMedicaments(medicamentList);
            mailjetEmailUseCase.sendEmail(emails, "Reporte de medicamentos con bajo stock", report);
        }

        return sale;
    }

    public List<SaleWithMedicamentDTO> getSales(){
        List<Sale> saleList = saleRepository.findAll();
        return SaleMapper.INSTANCE.saleListTosaleWithMedicamentDTOList(saleList);
    }

    public SaleWithMedicamentDTO getSaleById(Long id) {
        Optional<Sale> sale = saleRepository.findById(id);
        if(sale.isEmpty()) throw new ApiRequestException("No se encontró la venta con ID: " + id, HttpStatus.NOT_FOUND);
        return SaleMapper.INSTANCE.saleToSaleWithMedicamentDTO(sale.get());
    }

    public void deleteSaleById(Long id) {
        getSaleById(id);
        saleRepository.findById(id);
        saleRepository.deleteById(id);
    }

    public SaleWithMedicamentDTO updateSale (Long id, SaleWithMedicamentDTO saleUpdate) {
         SaleWithMedicamentDTO saleById = getSaleById(id);

         saleById.setSaleDate(saleUpdate.getSaleDate());
         saleById.setPatient(saleUpdate.getPatient());
         saleById.setMedicamentsSold(saleUpdate.getMedicamentsSold());
         saleById.setTotal(calculateTotalCost(saleUpdate.getMedicamentsSold()));

         Sale saleUpdated = saleRepository.save(SaleMapper.INSTANCE.saleWithMedicamentDTOToSale(saleById));

         return SaleMapper.INSTANCE.saleToSaleWithMedicamentDTO(saleUpdated);
    }

    private double calculateTotalCost(List<MedicamentSoldWithMedicamentDTO> medicamentsSold) {
        double totalCost = 0.0;
        for (MedicamentSoldWithMedicamentDTO medicamentSold : medicamentsSold) {
            MedicamentDTO medicament = medicamentSold.getMedicament();
            totalCost += medicament.getCost() * medicamentSold.getQuantity();
        }
        return totalCost;
    }

    private String generateReportLowStockMedicaments(List<MedicamentDTO> medicaments) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='es'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Reporte de Medicamentos con Bajo Stock</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; }");
        html.append(".container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #dddddd; border-radius: 5px; background-color: #f9f9f9; }");
        html.append("h1 { color: #333333; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("table, th, td { border: 1px solid #dddddd; }");
        html.append("th, td { padding: 10px; text-align: left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<h1>Reporte de Medicamentos con Bajo Stock</h1>");
        html.append("<p>Estimado/a,</p>");
        html.append("<p>Adjunto encontrará el listado de medicamentos cuyo stock es igual o menor a 3 unidades</p>");
        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>ID</th>");
        html.append("<th>Nombre</th>");
        html.append("<th>Descripción</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");

        // Iteración sobre la lista de medicamentos para agregar filas a la tabla
        for (MedicamentDTO medicament : medicaments) {
            html.append("<tr>");
            html.append("<td>").append(medicament.getId()).append("</td>");
            html.append("<td>").append(medicament.getName()).append("</td>");
            html.append("<td>").append(medicament.getDescription()).append("</td>");
            html.append("</tr>");
        }

        html.append("</tbody>");
        html.append("</table>");
        html.append("<p>Saludos cordiales,</p>");
        html.append("<p>Su equipo de farmacia</p>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    public byte[] generatePdf(Long id) {
        SaleWithMedicamentDTO sale = getSaleById(id);

        ModelMap model = new ModelMap();

        model.addAttribute("sale", sale);

        return generatePDFUseCase.createPdf("saleDetails", model);
    }

}
