package tn.esprit.spring.kaddem;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.text.SimpleDateFormat;

import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class ContratServiceImplMockTest {

    @Mock
    private ContratRepository contratRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    private static Integer contratId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contratId = 1; // Initialize contratId
    }

    @Test
    @Order(1)
    void testAddContrat() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Contrat contrat = new Contrat();
        contrat.setDateDebutContrat(dateFormat.parse("2025-04-06"));
        contrat.setDateFinContrat(dateFormat.parse("2026-12-31"));
        contrat.setSpecialite(Specialite.IA);
        contrat.setArchive(false);
        contrat.setMontantContrat(6000);

        Contrat savedContratMock = new Contrat();
        savedContratMock.setIdContrat(contratId);
        savedContratMock.setDateDebutContrat(contrat.getDateDebutContrat());
        savedContratMock.setDateFinContrat(contrat.getDateFinContrat());
        savedContratMock.setSpecialite(contrat.getSpecialite());
        savedContratMock.setArchive(contrat.getArchive());
        savedContratMock.setMontantContrat(contrat.getMontantContrat());

        when(contratRepository.save(any(Contrat.class))).thenReturn(savedContratMock);

        Contrat savedContrat = contratService.addContrat(contrat);

        assertNotNull(savedContrat);
        assertEquals(6000, savedContrat.getMontantContrat());
        assertNotNull(savedContrat.getIdContrat());
        assertEquals(contratId, savedContrat.getIdContrat());
    }

    @Test
    @Order(2)
    void testRetrieveAllContrats() {
        Contrat contrat1 = new Contrat();
        contrat1.setIdContrat(1);
        Contrat contrat2 = new Contrat();
        contrat2.setIdContrat(2);

        when(contratRepository.findAll()).thenReturn(Arrays.asList(contrat1, contrat2));

        List<Contrat> contrats = contratService.retrieveAllContrats();
        assertFalse(contrats.isEmpty());
        assertEquals(2, contrats.size());
    }

    @Test
    @Order(3)
    void testRetrieveContrat() { //test
        Contrat contrat = new Contrat();
        contrat.setIdContrat(contratId);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));

        Contrat retrievedContrat = contratService.retrieveContrat(contratId);
        assertNotNull(retrievedContrat);
        assertEquals(contratId, retrievedContrat.getIdContrat());
    }

    @Test
    @Order(4)
    void testUpdateContrat() {
        // Simulate updating a contract
        Contrat contrat = new Contrat();
        contrat.setIdContrat(contratId);
        contrat.setArchive(false);

        // Mock only the save behavior (remove unnecessary findById stubbing)
        Contrat updatedContratMock = new Contrat();
        updatedContratMock.setIdContrat(contratId);
        updatedContratMock.setArchive(true);
        when(contratRepository.save(any(Contrat.class))).thenReturn(updatedContratMock);

        contrat.setArchive(true);
        Contrat updatedContrat = contratService.updateContrat(contrat);
        assertNotNull(updatedContrat);
        assertTrue(updatedContrat.getArchive());
        assertEquals(contratId, updatedContrat.getIdContrat());
    }

    @Test
    @Order(5)
    void testRemoveContrat() {
        // Simulate removing a contract
        Contrat contrat = new Contrat();
        contrat.setIdContrat(contratId);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));

        contratService.removeContrat(contratId);

        // Verify the actual method called (delete instead of deleteById)
        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    @Order(6)
    public void testNbContratsValides() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2025-01-01");
        Date endDate = dateFormat.parse("2027-01-30");

        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(6);

        Integer nbContratsValides = contratService.nbContratsValides(startDate, endDate);
        assertEquals(6, nbContratsValides);
    }
}