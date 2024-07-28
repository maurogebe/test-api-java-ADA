package com.example.demo.application.usecases;

import com.example.demo.application.dtos.MedicamentDTO;
import com.example.demo.application.dtos.MedicamentSoldWithMedicamentDTO;
import com.example.demo.application.dtos.SaleWithMedicamentDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.SaleMapper;
import com.example.demo.domain.entities.Sale;
import com.example.demo.domain.repositories.SaleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class SaleUseCase {

    private SaleRepository saleRepository;
    private MedicamentUseCase medicamentUseCase;

    @Autowired
    public SaleUseCase(SaleRepository saleRepository, MedicamentUseCase medicamentUseCase) {
        this.saleRepository = saleRepository;
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

        this.saleRepository.save(saleSave);

        sale.getMedicamentsSold().forEach(medicamentSold -> {
            MedicamentDTO medicament = medicamentMap.get(medicamentSold.getMedicament().getId());
            if (medicament != null){
                int newStock = medicament.getStock() - medicamentSold.getQuantity();

                if (newStock < 0){
                    throw new IllegalStateException("Stock insuficiente para el medicamento ID: " + medicament.getId());
                }
                medicament.setStock(newStock);
                medicamentUseCase.updateMedicament(medicament.getId(), medicament);
            }
        });
        return (sale);
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

}
