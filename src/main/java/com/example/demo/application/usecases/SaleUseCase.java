package com.example.demo.application.usecases;

import com.example.demo.domain.entities.Medicament;
import com.example.demo.domain.entities.MedicamentSold;
import com.example.demo.domain.entities.Sale;
import com.example.demo.domain.repositories.ISaleRepository;
import com.example.demo.domain.repositories.MedicamentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Service
public class SaleUseCase {

    private final ObjectMapper objectMapper;
    private ISaleRepository isaleRepository;
    private MedicamentRepository medicamentRepository;

    @Autowired
    public SaleUseCase(ISaleRepository isaleRepository, MedicamentRepository medicamentRepository, ObjectMapper objectMapper) {
        this.isaleRepository = isaleRepository;
        this.medicamentRepository = medicamentRepository;
        this.objectMapper = objectMapper;
    }

    public Sale createSale(Sale sale){
        List<Long> ids = sale.getMedicamentsSold().stream()
            .map(MedicamentSold::getMedicament)
            .map(Medicament::getId)
            .toList();
        List<Medicament> medicaments = medicamentRepository.findAllById(ids);

        Map<Long, Medicament> medicamentMap = medicaments.stream()
            .collect(Collectors.toMap(Medicament::getId, medicament -> medicament));

        sale.getMedicamentsSold().forEach(medicamentSold -> {
            Medicament medicament = medicamentMap.get(medicamentSold.getMedicament().getId());
            if (medicament != null) {
                medicamentSold.setMedicament(medicament);
            }
        });

        sale.setTotal(calculateTotalCost(sale.getMedicamentsSold()));
        this.isaleRepository.save(sale);
        return (sale);
    }

    public List<Sale> getAllSales(){
       return isaleRepository.findAll();
    }

    public void deleteByIdSale(Long id) {
        getSaleById(id);
        isaleRepository.findById(id);
        isaleRepository.deleteById(id);
    }

    public Sale getSaleById(Long id) {
        Sale sale = isaleRepository.findById(id).get();
        return (sale);
    }

    public void updateSale (Long id, Sale sale) {
             Sale saleUpdate = isaleRepository.findById(id).get();
             saleUpdate.setId(sale.getId());
             saleUpdate.setSaleDate(sale.getSaleDate());
             saleUpdate.setPatient(sale.getPatient());
             saleUpdate.setMedicamentsSold(sale.getMedicamentsSold());
             saleUpdate.setTotal(calculateTotalCost(saleUpdate.getMedicamentsSold()));
             isaleRepository.save(saleUpdate);

    }

    // Método para calcular el costo total de los medicamentos vendidos
    private double calculateTotalCost(List<MedicamentSold> medicamentsSold) {
        double totalCost = 0.0;
        for (MedicamentSold medicamentSold : medicamentsSold) {
            Medicament medicament = medicamentSold.getMedicament();
            totalCost += medicament.getCost() * medicamentSold.getQuantity();
        }
        return totalCost;
    }

}
