package com.example.demo.application.usecases;

import com.example.demo.application.dtos.MedicamentDTO;
import com.example.demo.application.dtos.MedicamentSoldWithMedicamentDTO;
import com.example.demo.application.dtos.SaleWithMedicamentDTO;
import com.example.demo.application.mappers.MedicamentMapper;
import com.example.demo.application.mappers.SaleMapper;
import com.example.demo.domain.entities.Medicament;
import com.example.demo.domain.entities.Patient;
import com.example.demo.domain.entities.Sale;
import com.example.demo.domain.exeptions.NotFoundException;
import com.example.demo.domain.repositories.ISaleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class SaleUseCase {

    private ISaleRepository iSaleRepository;
    private MedicamentUseCase medicamentUseCase;
    private MailjetEmailUseCase mailjetEmailUseCase;
    private PatientUseCase patientUseCase;

    @Autowired
    public SaleUseCase(PatientUseCase patientUseCase, MailjetEmailUseCase mailjetEmailUseCase1, ISaleRepository iSaleRepository, MedicamentUseCase medicamentUseCase) {
        this.patientUseCase = patientUseCase;
        this.mailjetEmailUseCase = mailjetEmailUseCase;
        this.iSaleRepository = iSaleRepository;
        this.medicamentUseCase = medicamentUseCase;
    }

    public SaleWithMedicamentDTO createSale(SaleWithMedicamentDTO sale){
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

        int newStock = 0;

        sale.getMedicamentsSold().forEach(medicamentSold -> {
            MedicamentDTO medicament = medicamentMap.get(medicamentSold.getMedicament().getId());
            if (medicament != null){
                newStock = medicament.getStock() - medicamentSold.getQuantity();

                if (newStock < 0){
                    throw new IllegalStateException("Stock insuficiente para el medicamento ID: " + medicament.getId());
                }
                medicament.setStock(newStock);
                medicamentUseCase.updateMedicament(medicament.getId(), medicament);
            }
        });

        sale.getMedicamentsSold().forEach(medicamentSoldWithMedicamentDTO -> {
            MedicamentDTO medicament = medicamentMap.get(medicamentSold.getMedicament().getId());
            int stock = medicament.getStock();
            if (stock <= 3) {
                List<MedicamentDTO> lowStockMedicaments = sale.getMedicamentsSold().stream()
                        .map(medicamentSoldWithMedicamentDTO -> medicamentMap.get(medicamentSoldWithMedicamentDTO.getMedicament().getId()))
                        .filter(medicament -> medicament.getStock() <= 3)
                        .collect(Collectors.toList());
                if (lowStockMedicaments.size() > 1) {
                    sendStockAlertEmail(lowStockMedicaments);
                }

                public String sendStockAlertEmail(List<MedicamentDTO> lowStockMedicaments) {
                    sendStockAlertEmail message = new sendStockAlerEmail();

                public List<String> getAllPatientEmails() {
                    List<Patient> patients = patientRepository.findAll(); // Método para obtener todos los pacientes
                    return patients.stream()
                            .map(Patient::getEmail)
                            .collect(Collectors.toList());
                }
                    mailjetEmailUseCase.sendEmail();
        }
            }     return (sale);
    }

    public List<SaleWithMedicamentDTO> getSales(){
        List<Sale> saleList = iSaleRepository.findAll();
        return SaleMapper.INSTANCE.saleListTosaleWithMedicamentDTOList(saleList);
    }

    public SaleWithMedicamentDTO getSaleById(Long id) {
        Optional<Sale> sale = iSaleRepository.findById(id);
        if(sale.isEmpty()) new NotFoundException("No se encontró la venta con ID: " + id);
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

}
