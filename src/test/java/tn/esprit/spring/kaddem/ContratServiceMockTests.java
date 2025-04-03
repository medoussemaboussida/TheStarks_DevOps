package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ContratServiceMockTests {

    @Mock
    private ContratRepository contratRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    @BeforeEach
    void setUp() {
        // Initialise les mocks avant chaque test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddContrat() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        // Act
        Contrat result = contratService.addContrat(contrat);

        // Assert
        assertNotNull(result);
        assertEquals(Specialite.IA, result.getSpecialite());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void testRetrieveAllContrats() {
        // Arrange
        Contrat contrat1 = new Contrat();
        contrat1.setSpecialite(Specialite.IA);
        Contrat contrat2 = new Contrat();
        contrat2.setSpecialite(Specialite.CLOUD);
        List<Contrat> contrats = Arrays.asList(contrat1, contrat2);
        when(contratRepository.findAll()).thenReturn(contrats);

        // Act
        List<Contrat> result = contratService.retrieveAllContrats();

        // Assert
        assertEquals(2, result.size());
        assertEquals(Specialite.IA, result.get(0).getSpecialite());
        assertEquals(Specialite.CLOUD, result.get(1).getSpecialite());
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveAllContratsEmpty() {
        // Arrange
        when(contratRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Contrat> result = contratService.retrieveAllContrats();

        // Assert
        assertEquals(0, result.size());
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void testGetChiffreAffaireEntreDeuxDatesNoContracts() {
        // Arrange
        when(contratRepository.findAll()).thenReturn(Collections.emptyList());
        Date startDate = new Date(2023 - 1900, 0, 1); // 1er janvier 2023
        Date endDate = new Date(2023 - 1900, 1, 1);   // 1er février 2023

        // Act
        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert
        assertEquals(0.0F, result, 0.001F, "Le chiffre d'affaires doit être 0 si aucun contrat n'existe");
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void testGetChiffreAffaireEntreDeuxDatesOneMonthIA() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);
        when(contratRepository.findAll()).thenReturn(Arrays.asList(contrat));
        Date startDate = new Date(2023 - 1900, 0, 1); // 1er janvier 2023
        Date endDate = new Date(2023 - 1900, 1, 1);   // 1er février 2023

        // Act
        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert
        float expected = (31.0F / 30.0F) * 300.0F; // 31 jours / 30 * tarif IA (300)
        assertEquals(expected, result, 0.001F, "Le chiffre d'affaires pour IA sur 1 mois est incorrect");
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void testGetChiffreAffaireEntreDeuxDatesMultipleContracts() {
        // Arrange
        Contrat contratIA = new Contrat();
        contratIA.setSpecialite(Specialite.IA);
        Contrat contratCloud = new Contrat();
        contratCloud.setSpecialite(Specialite.CLOUD);
        List<Contrat> contrats = Arrays.asList(contratIA, contratCloud);
        when(contratRepository.findAll()).thenReturn(contrats);
        Date startDate = new Date(2023 - 1900, 0, 1); // 1er janvier 2023
        Date endDate = new Date(2023 - 1900, 1, 1);   // 1er février 2023

        // Act
        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert
        float months = 31.0F / 30.0F;
        float expected = (months * 300.0F) + (months * 400.0F); // IA (300) + CLOUD (400)
        assertEquals(expected, result, 0.001F, "Le chiffre d'affaires pour plusieurs contrats est incorrect");
        verify(contratRepository, times(1)).findAll();
    }
}