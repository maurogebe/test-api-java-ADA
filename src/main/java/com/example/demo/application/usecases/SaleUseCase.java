package com.example.demo.application.usecases;

import com.example.demo.domain.entities.Medicament;
import com.example.demo.domain.entities.MedicamentSold;
import com.example.demo.domain.entities.Sale;
import com.example.demo.domain.repositories.ISaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class SaleUseCase {

    private final ObjectMapper objectMapper;
    private ISaleRepository isaleRepository;


    public SaleUseCase(ISaleRepository isaleRepository, ObjectMapper objectMapper) {
        this.isaleRepository = isaleRepository;
        this.objectMapper = objectMapper;
    }

    public Sale createSale(Sale sale){
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
