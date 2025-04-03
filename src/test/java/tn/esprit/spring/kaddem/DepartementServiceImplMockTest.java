package tn.esprit.spring.kaddem;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Define test order
@ExtendWith(MockitoExtension.class) // Enable Mockito annotations
class DepartementServiceImplMockTest {

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;

    private static Integer departementId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        departementId = 1; // Set a default ID for tests
    }

    @Test
    @Order(1)
    void testAddDepartement() {
        // Create a new Departement
        Departement departement = new Departement();
        departement.setNomDepart("Informatique");

        // Mock the save behavior
        Departement savedDepartement = new Departement();
        savedDepartement.setIdDepart(departementId);
        savedDepartement.setNomDepart("Informatique");
        when(departementRepository.save(any(Departement.class))).thenReturn(savedDepartement);

        // Call the method to test
        Departement result = departementService.addDepartement(departement);

        // Verify the result
        assertNotNull(result);
        assertEquals(departementId, result.getIdDepart());
        assertEquals("Informatique", result.getNomDepart());

        // Verify the repository interaction
        verify(departementRepository, times(1)).save(departement);
    }

    @Test
    @Order(2)
    void testRetrieveAllDepartements() {
        // Mock the findAll behavior
        Departement dept1 = new Departement();
        dept1.setIdDepart(1);
        dept1.setNomDepart("Informatique");
        Departement dept2 = new Departement();
        dept2.setIdDepart(2);
        dept2.setNomDepart("Mathématiques");
        List<Departement> mockDepartements = Arrays.asList(dept1, dept2);
        when(departementRepository.findAll()).thenReturn(mockDepartements);

        // Call the method to test
        List<Departement> departements = departementService.retrieveAllDepartements();

        // Verify the result
        assertNotNull(departements);
        assertFalse(departements.isEmpty());
        assertEquals(2, departements.size());
        assertEquals("Informatique", departements.get(0).getNomDepart());

        // Verify the repository interaction
        verify(departementRepository, times(1)).findAll();
    }

    @Test
    @Order(3)
    void testRetrieveDepartement() {
        // Mock the findById behavior
        Departement departement = new Departement();
        departement.setIdDepart(departementId);
        departement.setNomDepart("Informatique");
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));

        // Call the method to test
        Departement result = departementService.retrieveDepartement(departementId);

        // Verify the result
        assertNotNull(result);
        assertEquals(departementId, result.getIdDepart());
        assertEquals("Informatique", result.getNomDepart());

        // Verify the repository interaction
        verify(departementRepository, times(1)).findById(departementId);
    }

    @Test
    @Order(4)
    void testUpdateDepartement() {
        // Create a Departement to update
        Departement departement = new Departement();
        departement.setIdDepart(departementId);
        departement.setNomDepart("Informatique");

        // Mock the save behavior for the updated departement
        Departement updatedDepartement = new Departement();
        updatedDepartement.setIdDepart(departementId);
        updatedDepartement.setNomDepart("Génie Logiciel");
        when(departementRepository.save(any(Departement.class))).thenReturn(updatedDepartement);

        // Update the departement
        departement.setNomDepart("Génie Logiciel");
        Departement result = departementService.updateDepartement(departement);

        // Verify the result
        assertNotNull(result);
        assertEquals(departementId, result.getIdDepart());
        assertEquals("Génie Logiciel", result.getNomDepart());

        // Verify the repository interaction
        verify(departementRepository, times(1)).save(departement);
    }

    @Test
    @Order(5)
    void testDeleteDepartement() {
        // Mock the findById behavior
        Departement departement = new Departement();
        departement.setIdDepart(departementId);
        departement.setNomDepart("Informatique");
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));

        // Call the method to test
        departementService.deleteDepartement(departementId);

        // Verify the repository interactions
        verify(departementRepository, times(1)).findById(departementId);
        verify(departementRepository, times(1)).delete(departement);
    }
}