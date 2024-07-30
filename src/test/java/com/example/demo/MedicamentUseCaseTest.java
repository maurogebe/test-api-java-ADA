package com.example.demo;

import com.example.demo.application.dtos.MedicamentDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.mappers.MedicamentMapper;
import com.example.demo.application.usecases.MedicamentUseCase;
import com.example.demo.domain.entities.Medicament;
import com.example.demo.domain.repositories.MedicamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MedicamentUseCaseTest {

    @Mock
    private MedicamentRepository medicamentRepository;

    @InjectMocks
    private MedicamentUseCase medicamentUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMedicament_Success() {
        // Arrange
        MedicamentDTO medicamentDTO = new MedicamentDTO();
        medicamentDTO.setName("Paracetamol");
        medicamentDTO.setDescription("Pain reliever");
        medicamentDTO.setForm("Tablet");
        medicamentDTO.setStock(50);
        medicamentDTO.setCost(2.5);
        medicamentDTO.setPrescriptionRequired(false);

        Medicament medicament = MedicamentMapper.INSTANCE.medicamentDTOToMedicament(medicamentDTO);
        medicament.setId(1L);

        when(medicamentRepository.save(any(Medicament.class))).thenAnswer(invocation -> {
            Medicament savedMedicament = invocation.getArgument(0);
            savedMedicament.setId(1L);
            return savedMedicament;
        });

        // Act
        MedicamentDTO result = medicamentUseCase.createMedicament(medicamentDTO);

        // Assert
        assertEquals("Paracetamol", result.getName());
        assertEquals("Pain reliever", result.getDescription());
        assertEquals("Tablet", result.getForm());
        assertEquals(50, result.getStock());
        assertEquals(2.5, result.getCost());
        assertFalse(result.isPrescriptionRequired());

        verify(medicamentRepository, times(1)).save(any(Medicament.class));
    }

    @Test
    public void testGetMedicaments_Success() {
        // Arrange
        Medicament medicament1 = new Medicament();
        medicament1.setId(1L);
        medicament1.setName("Paracetamol");
        medicament1.setDescription("Pain reliever");
        medicament1.setForm("Tablet");
        medicament1.setStock(50);
        medicament1.setCost(2.5);
        medicament1.setPrescriptionRequired(false);

        Medicament medicament2 = new Medicament();
        medicament2.setId(2L);
        medicament2.setName("Ibuprofen");
        medicament2.setDescription("Anti-inflammatory");
        medicament2.setForm("Tablet");
        medicament2.setStock(100);
        medicament2.setCost(5.0);
        medicament2.setPrescriptionRequired(true);

        List<Medicament> medicamentList = Arrays.asList(medicament1, medicament2);
        when(medicamentRepository.findAll()).thenReturn(medicamentList);

        // Act
        List<MedicamentDTO> result = medicamentUseCase.getMedicaments();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Paracetamol", result.get(0).getName());
        assertEquals("Ibuprofen", result.get(1).getName());

        verify(medicamentRepository, times(1)).findAll();
    }

    @Test
    public void testGetMedicamentById_Success() {
        // Arrange
        Medicament medicament = new Medicament();
        medicament.setId(1L);
        medicament.setName("Paracetamol");
        when(medicamentRepository.findById(anyLong())).thenReturn(Optional.of(medicament));

        // Act
        MedicamentDTO result = medicamentUseCase.getMedicamentById(1L);

        // Assert
        assertEquals("Paracetamol", result.getName());
        verify(medicamentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetMedicamentById_NotFound() {
        // Arrange
        when(medicamentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            medicamentUseCase.getMedicamentById(1L);
        });

        assertEquals("No se encontró medicamento con ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(medicamentRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById_Success() {
        // Arrange
        Medicament medicament = new Medicament();
        medicament.setId(1L);
        medicament.setName("Paracetamol");
        when(medicamentRepository.findById(anyLong())).thenReturn(Optional.of(medicament));
        doNothing().when(medicamentRepository).deleteById(anyLong());

        // Act
        medicamentUseCase.deleteById(1L);

        // Assert
        verify(medicamentRepository, times(1)).findById(1L);
        verify(medicamentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateMedicament_Success() {
        // Arrange
        MedicamentDTO medicamentUpdateDTO = new MedicamentDTO();
        medicamentUpdateDTO.setName("Ibuprofen");
        medicamentUpdateDTO.setDescription("Anti-inflammatory");
        medicamentUpdateDTO.setForm("Tablet");
        medicamentUpdateDTO.setStock(100);
        medicamentUpdateDTO.setCost(5.0);
        medicamentUpdateDTO.setPrescriptionRequired(true);

        Medicament medicament = new Medicament();
        medicament.setId(1L);
        medicament.setName("Paracetamol");
        medicament.setDescription("Pain reliever");
        medicament.setForm("Tablet");
        medicament.setStock(50);
        medicament.setCost(2.5);
        medicament.setPrescriptionRequired(false);

        when(medicamentRepository.findById(anyLong())).thenReturn(Optional.of(medicament));
        when(medicamentRepository.save(any(Medicament.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        MedicamentDTO result = medicamentUseCase.updateMedicament(1L, medicamentUpdateDTO);

        // Assert
        assertEquals("Ibuprofen", result.getName());
        assertEquals("Anti-inflammatory", result.getDescription());
        assertEquals("Tablet", result.getForm());
        assertEquals(100, result.getStock());
        assertEquals(5.0, result.getCost());
        assertTrue(result.isPrescriptionRequired());

        verify(medicamentRepository, times(1)).findById(1L);
        verify(medicamentRepository, times(1)).save(any(Medicament.class));
    }

    @Test
    public void testUpdateMedicament_NotFound() {
        // Arrange
        MedicamentDTO medicamentUpdateDTO = new MedicamentDTO();
        medicamentUpdateDTO.setName("Ibuprofen");

        when(medicamentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            medicamentUseCase.updateMedicament(1L, medicamentUpdateDTO);
        });

        assertEquals("No se encontró medicamento con ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(medicamentRepository, times(1)).findById(1L);
        verify(medicamentRepository, times(0)).save(any(Medicament.class));
    }


}
