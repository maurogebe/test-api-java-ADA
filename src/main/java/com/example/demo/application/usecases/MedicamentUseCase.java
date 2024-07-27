package com.example.demo.application.usecases;

import com.example.demo.application.dtos.MedicamentDTO;
import com.example.demo.application.mappers.MedicamentMapper;
import com.example.demo.domain.entities.Medicament;
import com.example.demo.domain.exeptions.NotFoundException;
import com.example.demo.domain.repositories.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentUseCase {

    private final MedicamentRepository medicamentRepository;

    @Autowired
    public MedicamentUseCase(MedicamentRepository medicamentRepository){
        this.medicamentRepository = medicamentRepository;
    }

    public MedicamentDTO createMedicament(MedicamentDTO medicament){
        Medicament medicamentSaved = this.medicamentRepository.save(MedicamentMapper.INSTANCE.medicamentDTOToMedicament(medicament));
        return MedicamentMapper.INSTANCE.medicamentToMedicamentDTO(medicamentSaved);
    }

    public List<MedicamentDTO> getMedicaments(){
        List<Medicament> medicamentList = medicamentRepository.findAll();
        return MedicamentMapper.INSTANCE.medicamentListToMedicamentDTOList(medicamentList);
    }

    public List<MedicamentDTO> getLowStockMedicaments(){
        List<Medicament> medicamentList = medicamentRepository.findByStockLessThanEqual(3);
        return MedicamentMapper.INSTANCE.medicamentListToMedicamentDTOList(medicamentList);
    }

    public List<MedicamentDTO> getMedicamentsById(List<Long> ids){
        List<Medicament> medicamentList = medicamentRepository.findAllById(ids);
        return MedicamentMapper.INSTANCE.medicamentListToMedicamentDTOList(medicamentList);
    }

    public MedicamentDTO getMedicamentById(Long id){
        Optional<Medicament> medicament = medicamentRepository.findById(id);
        if(medicament.isEmpty()) new NotFoundException("No se encontró medicamento con ID: " + id);
        return MedicamentMapper.INSTANCE.medicamentToMedicamentDTO(medicament.get());
    }

    public void deleteById(Long id){
        getMedicamentById(id);
        medicamentRepository.deleteById(id);
    }

    public MedicamentDTO updateMedicament(Long id, MedicamentDTO medicamentUpdate){
        MedicamentDTO medicamentById = getMedicamentById(id);

        medicamentById.setName(medicamentUpdate.getName());
        medicamentById.setDescription(medicamentUpdate.getDescription());
        medicamentById.setForm(medicamentUpdate.getForm());
        medicamentById.setStock(medicamentUpdate.getStock());
        medicamentById.setCost(medicamentUpdate.getCost());
        medicamentById.setPrescriptionRequired(medicamentUpdate.isPrescriptionRequired());

        Medicament medicament = medicamentRepository.save(MedicamentMapper.INSTANCE.medicamentDTOToMedicament(medicamentById));
        return MedicamentMapper.INSTANCE.medicamentToMedicamentDTO(medicament);
    }
}
