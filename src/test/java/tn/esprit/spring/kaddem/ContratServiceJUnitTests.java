package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.services.IContratService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Intégration avec JUnit 5 et Spring
@ExtendWith(SpringExtension.class)
// Charge le contexte Spring pour les tests
@SpringBootTest
// Définit l'ordre d'exécution des tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContratServiceJUnitTests {

    @Autowired
    private IContratService userService;

    @Autowired
    private ContratRepository contratRepository;

    @BeforeAll
    static void initAll() {
        System.out.println("Initialisation globale avant tous les tests");
    }

    @BeforeEach
    void setUp() {
        contratRepository.deleteAll();
        System.out.println("Nettoyage de la base avant chaque test");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Nettoyage après chaque test");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finalisation après tous les tests");
    }

    @Test
    @Order(1)
    void testChiffreAffaireNoContracts() {
        Date startDate = new Date(2023 - 1900, 0, 1);
        Date endDate = new Date(2023 - 1900, 1, 1);
        float result = userService.getChiffreAffaireEntreDeuxDates(startDate, endDate);
        assertEquals(0.0F, result, 0.001F, "Le chiffre d'affaires doit être 0 si aucun contrat n'existe");
    }

    @Test
    @Order(2)
    void testChiffreAffaireOneMonthIA() {
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);
        contratRepository.save(contrat);
        Date startDate = new Date(2023 - 1900, 0, 1);
        Date endDate = new Date(2023 - 1900, 1, 1);
        float result = userService.getChiffreAffaireEntreDeuxDates(startDate, endDate);
        float expected = (31.0F / 30.0F) * 300.0F;
        assertEquals(expected, result, 0.001F, "Le chiffre d'affaires pour IA sur 1 mois est incorrect");
    }

    @Test
    @Order(3)
    void testChiffreAffaireOneMonthCloud() {
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.CLOUD);
        contratRepository.save(contrat);
        Date startDate = new Date(2023 - 1900, 0, 1);
        Date endDate = new Date(2023 - 1900, 1, 1);
        float result = userService.getChiffreAffaireEntreDeuxDates(startDate, endDate);
        float expected = (31.0F / 30.0F) * 400.0F;
        assertEquals(expected, result, 0.001F, "Le chiffre d'affaires pour CLOUD sur 1 mois est incorrect");
    }

    @Test
    @Order(4)
    void testChiffreAffaireMultipleContracts() {
        Contrat contratIA = new Contrat();
        contratIA.setSpecialite(Specialite.IA);
        Contrat contratCloud = new Contrat();
        contratCloud.setSpecialite(Specialite.CLOUD);
        List<Contrat> contrats = Arrays.asList(contratIA, contratCloud);
        contratRepository.saveAll(contrats);
        Date startDate = new Date(2023 - 1900, 0, 1);
        Date endDate = new Date(2023 - 1900, 1, 1);
        float result = userService.getChiffreAffaireEntreDeuxDates(startDate, endDate);
        float months = 31.0F / 30.0F;
        float expected = (months * 300.0F) + (months * 400.0F);
        assertEquals(expected, result, 0.001F, "Le chiffre d'affaires pour plusieurs contrats est incorrect");
    }

    @RepeatedTest(3) // Remplace @Test pour exécuter ce test 3 fois
    @Order(5)
    void testChiffreAffaireZeroDays() {
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);
        contratRepository.save(contrat);
        Date startDate = new Date(2023 - 1900, 0, 1);
        Date endDate = new Date(2023 - 1900, 0, 1);
        float result = userService.getChiffreAffaireEntreDeuxDates(startDate, endDate);
        assertEquals(0.0F, result, 0.001F, "Le chiffre d'affaires doit être 0 pour une différence de 0 jour");
    }

    @Test
    @Order(6)
    void testRetrieveAllContrats() {
        Contrat contrat1 = new Contrat();
        contrat1.setSpecialite(Specialite.IA);
        Contrat contrat2 = new Contrat();
        contrat2.setSpecialite(Specialite.CLOUD);
        contratRepository.saveAll(Arrays.asList(contrat1, contrat2));
        List<Contrat> result = userService.retrieveAllContrats();
        assertEquals(2, result.size(), "La liste doit contenir 2 contrats");
        assertTrue(result.stream().anyMatch(c -> c.getSpecialite() == Specialite.IA), "Doit contenir un contrat IA");
        assertTrue(result.stream().anyMatch(c -> c.getSpecialite() == Specialite.CLOUD), "Doit contenir un contrat CLOUD");
    }

    @Test
    @Order(7)
    void testAddContrat() {
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.RESEAUX);
        Contrat addedContrat = userService.addContrat(contrat);
        assertNotNull(addedContrat.getIdContrat(), "L'ID du contrat ajouté ne doit pas être null");
        assertEquals(Specialite.RESEAUX, addedContrat.getSpecialite(), "La spécialité doit être RESEAUX");
        assertTrue(contratRepository.findById(addedContrat.getIdContrat()).isPresent(), "Le contrat doit être présent dans la base");
    }

    @Test
    @Order(8)
    void testUpdateContrat() {
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);
        Contrat savedContrat = contratRepository.save(contrat);
        savedContrat.setSpecialite(Specialite.CLOUD);
        Contrat updatedContrat = userService.updateContrat(savedContrat);
        assertEquals(Specialite.CLOUD, updatedContrat.getSpecialite(), "La spécialité doit être mise à jour à CLOUD");
        assertEquals(savedContrat.getIdContrat(), updatedContrat.getIdContrat(), "L'ID doit rester le même");
        Contrat fromDb = contratRepository.findById(updatedContrat.getIdContrat()).orElse(null);
        assertNotNull(fromDb, "Le contrat doit exister dans la base");
        assertEquals(Specialite.CLOUD, fromDb.getSpecialite(), "La mise à jour doit être persistante");
    }

    @Test
    @Order(9)
    void testRetrieveContrat() {
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);
        Contrat savedContrat = contratRepository.save(contrat);
        Contrat retrievedContrat = userService.retrieveContrat(savedContrat.getIdContrat());
        assertNotNull(retrievedContrat, "Le contrat doit être récupéré");
        assertEquals(savedContrat.getIdContrat(), retrievedContrat.getIdContrat(), "L'ID doit correspondre");
        assertEquals(Specialite.IA, retrievedContrat.getSpecialite(), "La spécialité doit être IA");
        assertNotEquals(Specialite.CLOUD, retrievedContrat.getSpecialite(), "La spécialité ne doit pas être CLOUD");
    }

    @Test
    @Order(10)
    void testRemoveContrat() {
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.CLOUD);
        Contrat savedContrat = contratRepository.save(contrat);
        userService.removeContrat(savedContrat.getIdContrat());
        assertFalse(contratRepository.findById(savedContrat.getIdContrat()).isPresent(), "Le contrat doit être supprimé de la base");
    }
}