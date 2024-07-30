package com.example.demo;

import com.example.demo.application.dtos.AllergyDTO;
import com.example.demo.application.dtos.PatientDTO;
import com.example.demo.application.exeptions.ApiRequestException;
import com.example.demo.application.usecases.PatientUseCase;
import com.example.demo.domain.entities.Allergy;
import com.example.demo.domain.entities.Patient;
import com.example.demo.domain.repositories.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PatientUseCaseTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientUseCase patientUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePatient() {
        // Arrange
        Set<AllergyDTO> allergiesDTO = new HashSet<>();
        AllergyDTO allergyDTO = new AllergyDTO();
        allergyDTO.setName("Polen");
        allergiesDTO.add(allergyDTO);

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setEmail("john.doe@example.com");
        patientDTO.setHealthInsuranceNumber("123456");
        patientDTO.setBirthDate(LocalDate.of(2000, 1, 1));
        patientDTO.setAllergies(allergiesDTO);

        Set<Allergy> allergies = new HashSet<>();
        Allergy allergy = new Allergy();
        allergy.setName("Polen");
        allergies.add(allergy);

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setEmail("john.doe@example.com");
        patient.setHealthInsuranceNumber("123456");
        patient.setBirthDate(LocalDate.of(2000, 1, 1));
        patient.setAllergies(allergies);

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Act
        PatientDTO result = patientUseCase.createPatient(patientDTO);

        // Assert
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("123456", result.getHealthInsuranceNumber());
        assertEquals(LocalDate.of(2000, 1, 1), result.getBirthDate());
        assertEquals(1, result.getAllergies().size()); // Aquí ocurrió el error
        assertEquals("Polen", result.getAllergies().iterator().next().getName());
    }

    @Test
    public void testGetPatientByID(){
        // Arrange
        Set<Allergy> allergies = new HashSet<>();
        Allergy allergy = new Allergy();
        allergy.setName("Polen");
        allergies.add(allergy);

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setEmail("john.doe@example.com");
        patient.setHealthInsuranceNumber("123456");
        patient.setBirthDate(LocalDate.of(2000, 1, 1));
        patient.setAllergies(allergies);

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        // Act
        PatientDTO result = patientUseCase.getPatientById(1L);

        // Assert
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("123456", result.getHealthInsuranceNumber());
        assertEquals(LocalDate.of(2000, 1, 1), result.getBirthDate());
        assertEquals(1, result.getAllergies().size());
        assertEquals("Polen", result.getAllergies().iterator().next().getName());
    }

    @Test
    public void testGetPatientByIdNotFound() {
        // Arrange
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            patientUseCase.getPatientById(1L);
        });

        assertEquals("No se encontró paciente con ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testGetAllPatients() {
        // Arrange
        Set<Allergy> allergies1 = new HashSet<>();
        Allergy allergy1 = new Allergy();
        allergy1.setName("Polen");
        allergies1.add(allergy1);

        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("John Doe");
        patient1.setEmail("john.doe@example.com");
        patient1.setHealthInsuranceNumber("123456");
        patient1.setBirthDate(LocalDate.of(2000, 1, 1));
        patient1.setAllergies(allergies1);

        Set<Allergy> allergies2 = new HashSet<>();
        Allergy allergy2 = new Allergy();
        allergy2.setName("Gatos");
        allergies2.add(allergy2);

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Jane Doe");
        patient2.setEmail("jane.doe@example.com");
        patient2.setHealthInsuranceNumber("654321");
        patient2.setBirthDate(LocalDate.of(1995, 5, 15));
        patient2.setAllergies(allergies2);

        List<Patient> patientList = Arrays.asList(patient1, patient2);

        when(patientRepository.findAll()).thenReturn(patientList);

        // Act
        List<PatientDTO> result = patientUseCase.getPatients();

        // Assert
        assertEquals(2, result.size());

        PatientDTO resultPatient1 = result.get(0);
        assertEquals("John Doe", resultPatient1.getName());
        assertEquals("john.doe@example.com", resultPatient1.getEmail());
        assertEquals("123456", resultPatient1.getHealthInsuranceNumber());
        assertEquals(LocalDate.of(2000, 1, 1), resultPatient1.getBirthDate());
        assertEquals(1, resultPatient1.getAllergies().size());
        assertEquals("Polen", resultPatient1.getAllergies().iterator().next().getName());

        PatientDTO resultPatient2 = result.get(1);
        assertEquals("Jane Doe", resultPatient2.getName());
        assertEquals("jane.doe@example.com", resultPatient2.getEmail());
        assertEquals("654321", resultPatient2.getHealthInsuranceNumber());
        assertEquals(LocalDate.of(1995, 5, 15), resultPatient2.getBirthDate());
        assertEquals(1, resultPatient2.getAllergies().size());
        assertEquals("Gatos", resultPatient2.getAllergies().iterator().next().getName());
    }

    @Test
    public void testDeleteById_Success() {
        // Arrange
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setEmail("john.doe@example.com");
        patient.setHealthInsuranceNumber("123456");

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        doNothing().when(patientRepository).deleteById(anyLong());

        // Act
        patientUseCase.deleteById(1L);

        // Assert
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_NotFound() {
        // Arrange
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            patientUseCase.deleteById(1L);
        });

        assertEquals("No se encontró paciente con ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(0)).deleteById(1L);
    }

    @Test
    public void testUpdatePatient_Success() {
        // Arrange
        Set<AllergyDTO> allergiesUpdated = new HashSet<>();
        AllergyDTO allergyDTOUpdated = new AllergyDTO();
        allergyDTOUpdated.setName("Gatos");
        allergiesUpdated.add(allergyDTOUpdated);

        PatientDTO patientUpdatedDTO = new PatientDTO();
        patientUpdatedDTO.setName("Jane Doe");
        patientUpdatedDTO.setEmail("jane.doe@example.com");
        patientUpdatedDTO.setHealthInsuranceNumber("654321");
        patientUpdatedDTO.setBirthDate(LocalDate.of(1995, 5, 15));
        patientUpdatedDTO.setAllergies(allergiesUpdated);

        Set<Allergy> allergies = new HashSet<>();
        Allergy allergy = new Allergy();
        allergy.setName("Polen");
        allergies.add(allergy);

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setEmail("john.doe@example.com");
        patient.setHealthInsuranceNumber("123456");
        patient.setBirthDate(LocalDate.of(2000, 1, 1));
        patient.setAllergies(allergies);

        // Simular el comportamiento del método findById
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        // Simular el comportamiento del método save para devolver el paciente actualizado
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PatientDTO result = patientUseCase.updatePatient(1L, patientUpdatedDTO);

        // Assert
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
        assertEquals("654321", result.getHealthInsuranceNumber());
        assertEquals(LocalDate.of(1995, 5, 15), result.getBirthDate());
        assertEquals(1, result.getAllergies().size());
        assertEquals("Gatos", result.getAllergies().iterator().next().getName());

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void testUpdatePatient_NotFound() {
        // Arrange
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        PatientDTO patientUpdatedDTO = new PatientDTO();
        patientUpdatedDTO.setName("Jane Doe");
        patientUpdatedDTO.setEmail("jane.doe@example.com");
        patientUpdatedDTO.setHealthInsuranceNumber("654321");
        patientUpdatedDTO.setBirthDate(LocalDate.of(1995, 5, 15));
        patientUpdatedDTO.setAllergies(new HashSet<>());

        // Act & Assert
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            patientUseCase.updatePatient(1L, patientUpdatedDTO);
        });

        assertEquals("No se encontró paciente con ID: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(0)).save(any(Patient.class));
    }

}
