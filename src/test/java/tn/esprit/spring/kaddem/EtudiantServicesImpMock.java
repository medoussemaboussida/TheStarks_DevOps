package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EtudiantServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class EtudiantServicesImpMock {

    @Mock
    private EtudiantRepository etudiantRepository;
    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEtudiant() {
        // Create a sample Etudiant
        Etudiant sampleEtudiant = new Etudiant();
        // Set up behavior for the mock repository
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(sampleEtudiant);

        // Call the service method
        Etudiant savedEtudiant = etudiantService.addEtudiant(sampleEtudiant);

        // Verify that the save method of the repository was called
        verify(etudiantRepository, times(1)).save(any(Etudiant.class));

        // Check if the returned Etudiant matches the one saved
        assertEquals(sampleEtudiant, savedEtudiant);
    }

    @Test
    public void testRetrieveEtudiant() {
        // Create a sample Etudiant ID
        Integer etudiantId = 1;
        // Create a sample Etudiant
        Etudiant sampleEtudiant = new Etudiant();
        // Set up behavior for the mock repository
        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(sampleEtudiant));

        // Call the service method
        Etudiant retrievedEtudiant = etudiantService.retrieveEtudiant(etudiantId);

        // Verify that the find method of the repository was called
        verify(etudiantRepository, times(1)).findById(etudiantId);

        // Check if the returned Etudiant matches the one from the repository
        assertEquals(sampleEtudiant, retrievedEtudiant);
    }

    @Test
    public void testUpdateEtudiant() {
        // Create a sample Etudiant
        Etudiant sampleEtudiant = new Etudiant();
        // Set up behavior for the mock repository
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(sampleEtudiant);

        // Call the service method
        Etudiant updatedEtudiant = etudiantService.updateEtudiant(sampleEtudiant);

        // Verify that the save method of the repository was called
        verify(etudiantRepository, times(1)).save(any(Etudiant.class));

        // Check if the returned Etudiant matches the one saved
        assertEquals(sampleEtudiant, updatedEtudiant);
    }

    @Test
    public void testRemoveEtudiant() {
        // Create a sample Etudiant ID
        Integer etudiantId = 1;
        // Create a sample Etudiant
        Etudiant sampleEtudiant = new Etudiant();
        // Set up behavior for the mock repository
        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(sampleEtudiant));

        // Call the service method
        etudiantService.removeEtudiant(etudiantId);

        // Verify that the delete method of the repository was called
        verify(etudiantRepository, times(1)).delete(sampleEtudiant);
    }
}