package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)

public class DepartementServicesImpMock {

    @Mock
    private DepartementRepository departementRepository;
    @InjectMocks
    private DepartementServiceImpl departementServiceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Order(1)
    public void testAddContrat() {
        Departement sampleDepartement = new Departement();
        when(departementRepository.save(any(Departement.class))).thenReturn(sampleDepartement);

        Departement savedDepartement = departementServiceService.addDepartement(sampleDepartement);

        verify(departementRepository, times(1)).save(any(Departement.class));

        assertEquals(sampleDepartement, savedDepartement);
    }

    @Order(2)
    public void testRetrieveContrat() {
        Integer DepartementId = 1;
        Departement sampleDepartement = new Departement();
        when(departementRepository.findById(DepartementId)).thenReturn(Optional.of(sampleDepartement));

        Departement retrievedContrat = departementServiceService.retrieveDepartement(DepartementId);

        verify(departementRepository, times(1)).findById(DepartementId);

        assertEquals(sampleDepartement, retrievedContrat);
    }

    @Test
    @Order(3)
    public void testUpdateContrat() {
        Departement sampleDepartement = new Departement();
        when(departementRepository.save(any(Departement.class))).thenReturn(sampleDepartement);
        Departement updatedDepartement = departementServiceService.updateDepartement(sampleDepartement);
        verify(departementRepository, times(1)).save(any(Departement.class));
        assertEquals(sampleDepartement, updatedDepartement);
    }

    @Test
    @Order(4)
    public void testRemoveContrat() {
        Integer updatedDepartementId = 1;
        Departement sampleContrat = new Departement();
        when(departementRepository.findById(updatedDepartementId)).thenReturn(Optional.of(sampleContrat));

        departementServiceService.deleteDepartement(updatedDepartementId);

        verify(departementRepository, times(1)).delete(sampleContrat);
    }





}