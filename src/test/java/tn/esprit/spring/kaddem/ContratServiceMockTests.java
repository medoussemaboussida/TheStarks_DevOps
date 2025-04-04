package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ContratServiceMockTests {

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;

    private Departement sampleDepartement;

    @BeforeEach
    void setUp() {
        // Initialise les mocks avant chaque test
        MockitoAnnotations.openMocks(this);
        sampleDepartement = new Departement();
        sampleDepartement.setIdDepart(1); // Corrigé : setIdDepart -> setIdDepartement
        sampleDepartement.setNomDepart("Informatique");
    }

    @Test
    void testAddDepartement() {
        // Arrange
        when(departementRepository.save(any(Departement.class))).thenReturn(sampleDepartement);

        // Act
        Departement result = departementService.addDepartement(sampleDepartement);

        // Assert
        assertNotNull(result, "Le département sauvegardé ne devrait pas être null");
        assertEquals(sampleDepartement.getIdDepart(), result.getIdDepart(), "L'ID du département devrait correspondre");
        assertEquals(sampleDepartement.getNomDepart(), result.getNomDepart(), "Le nom du département devrait correspondre");
        verify(departementRepository, times(1)).save(sampleDepartement); // Corrigé : any() -> sampleDepartement pour précision
    }

    @Test
    void testRetrieveAllDepartements() {
        // Arrange
        Departement dept1 = new Departement();
        dept1.setIdDepart(1);
        dept1.setNomDepart("Informatique");
        Departement dept2 = new Departement();
        dept2.setIdDepart(2);
        dept2.setNomDepart("Mathématiques");
        List<Departement> departements = Arrays.asList(dept1, dept2);
        when(departementRepository.findAll()).thenReturn(departements);

        // Act
        List<Departement> result = departementService.retrieveAllDepartements();

        // Assert
        assertEquals(2, result.size(), "La liste devrait contenir 2 départements");
        assertEquals("Informatique", result.get(0).getNomDepart(), "Le premier département devrait être Informatique");
        assertEquals("Mathématiques", result.get(1).getNomDepart(), "Le deuxième département devrait être Mathématiques");
        verify(departementRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveAllDepartementsEmpty() {
        // Arrange
        when(departementRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Departement> result = departementService.retrieveAllDepartements();

        // Assert
        assertEquals(0, result.size(), "La liste devrait être vide s'il n'y a aucun département");
        verify(departementRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveDepartement() {
        // Arrange
        Integer departementId = 1;
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(sampleDepartement));

        // Act
        Departement result = departementService.retrieveDepartement(departementId);

        // Assert
        assertNotNull(result, "Le département récupéré ne devrait pas être null");
        assertEquals(sampleDepartement.getIdDepart(), result.getIdDepart(), "L'ID du département devrait correspondre");
        assertEquals(sampleDepartement.getNomDepart(), result.getNomDepart(), "Le nom du département devrait correspondre");
        verify(departementRepository, times(1)).findById(departementId);
    }

    @Test
    void testRetrieveDepartementNotFound() {
        // Arrange
        Integer departementId = 999;
        when(departementRepository.findById(departementId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> departementService.retrieveDepartement(departementId),
                "Une exception devrait être levée si le département n'est pas trouvé");
        verify(departementRepository, times(1)).findById(departementId);
    }

    @Test
    void testUpdateDepartement() {
        // Arrange
        when(departementRepository.save(any(Departement.class))).thenReturn(sampleDepartement);

        // Act
        Departement result = departementService.updateDepartement(sampleDepartement);

        // Assert
        assertNotNull(result, "Le département mis à jour ne devrait pas être null");
        assertEquals(sampleDepartement.getIdDepart(), result.getIdDepart(), "L'ID du département devrait correspondre");
        assertEquals(sampleDepartement.getNomDepart(), result.getNomDepart(), "Le nom du département devrait correspondre");
        verify(departementRepository, times(1)).save(sampleDepartement); // Corrigé : any() -> sampleDepartement
    }

    @Test
    void testDeleteDepartement() {
        // Arrange
        Integer departementId = 1;
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(sampleDepartement));
        doNothing().when(departementRepository).delete(sampleDepartement);

        // Act
        departementService.deleteDepartement(departementId);

        // Assert
        verify(departementRepository, times(1)).findById(departementId);
        verify(departementRepository, times(1)).delete(sampleDepartement);
    }

    @Test
    void testDeleteDepartementNotFound() {
        // Arrange
        Integer departementId = 999;
        when(departementRepository.findById(departementId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> departementService.deleteDepartement(departementId),
                "Une exception devrait être levée si le département n'est pas trouvé");
        verify(departementRepository, times(1)).findById(departementId);
        verify(departementRepository, never()).delete(any(Departement.class));
    }
}