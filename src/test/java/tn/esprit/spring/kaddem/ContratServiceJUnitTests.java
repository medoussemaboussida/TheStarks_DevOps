package tn.esprit.spring.kaddem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.kaddem.services.IContratService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class ContratServiceJUnitTests {
    @Autowired
    private IContratService userService;
    @Autowired
    private ContratRepository contratRepository;

    @BeforeEach
    void setUp() {
        // Initialisation avant chaque test (peut être ajustée selon le cas)
        contratRepository.deleteAll();
    }

    @Test
    void testChiffreAffaireNoContracts() {
        // Arrange

        Date startDate = new Date(2023 - 1900, 0, 1); // 1er janvier 2023
        Date endDate = new Date(2023 - 1900, 1, 1);   // 1er février 2023

        // Act
        float result = userService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert
        assertEquals(0.0F, result, 0.001F, "Le chiffre d'affaires doit être 0 si aucun contrat n'existe");
    }

    @Test
    void testChiffreAffaireOneMonthIA() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);
        contratRepository.save(contrat);
        Date startDate = new Date(2023 - 1900, 0, 1); // 1er janvier 2023
        Date endDate = new Date(2023 - 1900, 1, 1);   // 1er février 2023

        // Act
        float result = userService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert
        float expected = (31.0F / 30.0F) * 300.0F; // 1 mois approx * 300
        assertEquals(expected, result, 0.001F, "Le chiffre d'affaires pour IA sur 1 mois est incorrect");
        contratRepository.deleteAll();
    }

    @Test
    void testChiffreAffaireOneMonthCloud() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.CLOUD);
        contratRepository.save(contrat);
        Date startDate = new Date(2023 - 1900, 0, 1); // 1er janvier 2023
        Date endDate = new Date(2023 - 1900, 1, 1);   // 1er février 2023

        // Act
        float result = userService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert
        float expected = (31.0F / 30.0F) * 400.0F; // 1 mois approx * 400
        assertEquals(expected, result, 0.001F, "Le chiffre d'affaires pour CLOUD sur 1 mois est incorrect");
        contratRepository.deleteAll();
    }

    @Test
    void testChiffreAffaireMultipleContracts() {
        // Arrange
        Contrat contratIA = new Contrat();
        contratIA.setSpecialite(Specialite.IA);
        Contrat contratCloud = new Contrat();
        contratCloud.setSpecialite(Specialite.CLOUD);
        List<Contrat> contrats = Arrays.asList(contratIA, contratCloud);
        contratRepository.saveAll(contrats);
        Date startDate = new Date(2023 - 1900, 0, 1); // 1er janvier 2023
        Date endDate = new Date(2023 - 1900, 1, 1);   // 1er février 2023

        // Act
        float result = userService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert
        float months = 31.0F / 30.0F;
        float expected = (months * 300.0F) + (months * 400.0F); // IA + CLOUD
        assertEquals(expected, result, 0.001F, "Le chiffre d'affaires pour plusieurs contrats est incorrect");
        contratRepository.deleteAll();
    }

    @Test
    void testChiffreAffaireZeroDays() {
        // Arrange
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);
        contratRepository.save(contrat);
        Date startDate = new Date(2023 - 1900, 0, 1); // 1er janvier 2023
        Date endDate = new Date(2023 - 1900, 0, 1);   // Même jour

        // Act
        float result = userService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert
        assertEquals(0.0F, result, 0.001F, "Le chiffre d'affaires doit être 0 pour une différence de 0 jour");
        contratRepository.deleteAll();
    }
}

