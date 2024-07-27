package com.example.demo.application.mappers;

import com.example.demo.application.dtos.SaleWithMedicamentDTO;
import com.example.demo.domain.entities.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    SaleMapper INSTANCE = Mappers.getMapper( SaleMapper.class );

    SaleWithMedicamentDTO saleToSaleWithMedicamentDTO(Sale sale);

    Sale saleWithMedicamentDTOToSale(SaleWithMedicamentDTO sale);

    List<SaleWithMedicamentDTO> saleListTosaleWithMedicamentDTOList(List<Sale> sale);

    List<Sale> saleWithMedicamentDTOListToSaleList(List<SaleWithMedicamentDTO> saleDTO);

}
