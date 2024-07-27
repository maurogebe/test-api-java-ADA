package com.example.demo.application.usecases;

import com.example.demo.application.dtos.MedicamentDTO;
import com.example.demo.application.dtos.MedicamentSoldWithMedicamentDTO;
import com.example.demo.application.dtos.PatientDTO;
import com.example.demo.application.dtos.SaleWithMedicamentDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.SaleMapper;
import com.example.demo.domain.entities.Patient;
import com.example.demo.domain.entities.Sale;
import com.example.demo.domain.repositories.ISaleRepository;
import com.mailjet.client.errors.MailjetException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
@Service
public class SaleUseCase {

    private ISaleRepository iSaleRepository;
    private MedicamentUseCase medicamentUseCase;
    private MailjetEmailUseCase mailjetEmailUseCase;
    private PatientUseCase patientUseCase;

    @Autowired
    public SaleUseCase(PatientUseCase patientUseCase, MailjetEmailUseCase mailjetEmailUseCase, ISaleRepository iSaleRepository, MedicamentUseCase medicamentUseCase) {
        this.patientUseCase = patientUseCase;
        this.mailjetEmailUseCase = mailjetEmailUseCase;
        this.iSaleRepository = iSaleRepository;
        this.medicamentUseCase = medicamentUseCase;
    }

    public SaleWithMedicamentDTO createSale(SaleWithMedicamentDTO sale) throws MailjetException {
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

        this.iSaleRepository.save(saleSave);

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
        List<Sale> saleList = iSaleRepository.findAll();
        return SaleMapper.INSTANCE.saleListTosaleWithMedicamentDTOList(saleList);
    }

    public SaleWithMedicamentDTO getSaleById(Long id) {
        Optional<Sale> sale = iSaleRepository.findById(id);
        if(sale.isEmpty()) throw new ApiRequestException("No se encontró la venta con ID: " + id, HttpStatus.NOT_FOUND);
        return SaleMapper.INSTANCE.saleToSaleWithMedicamentDTO(sale.get());
    }

    public void deleteSaleById(Long id) {
        getSaleById(id);
        iSaleRepository.findById(id);
        iSaleRepository.deleteById(id);
    }

    public SaleWithMedicamentDTO updateSale (Long id, SaleWithMedicamentDTO saleUpdate) {
         SaleWithMedicamentDTO saleById = getSaleById(id);

         saleById.setSaleDate(saleUpdate.getSaleDate());
         saleById.setPatient(saleUpdate.getPatient());
         saleById.setMedicamentsSold(saleUpdate.getMedicamentsSold());
         saleById.setTotal(calculateTotalCost(saleUpdate.getMedicamentsSold()));

         Sale saleUpdated = iSaleRepository.save(SaleMapper.INSTANCE.saleWithMedicamentDTOToSale(saleById));

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

}
