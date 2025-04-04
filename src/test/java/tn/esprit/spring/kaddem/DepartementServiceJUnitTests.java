package tn.esprit.spring.kaddem;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.services.IDepartementService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Intégration avec JUnit 5 et Spring
@ExtendWith(SpringExtension.class)
// Charge le contexte Spring pour les tests
@SpringBootTest
// Définit l'ordre d'exécution des tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartementServiceJUnitTests {

    @Autowired
    private IDepartementService departementService;

    @Autowired
    private DepartementRepository departementRepository;

    @BeforeAll
    static void initAll() {
        System.out.println("Initialisation globale avant tous les tests");
    }

    @BeforeEach
    void setUp() {
        departementRepository.deleteAll();
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
    void testRetrieveAllDepartementsEmpty() {
        // Act : Récupérer tous les départements quand la base est vide
        List<Departement> result = departementService.retrieveAllDepartements();

        // Assert : Vérifier que la liste est vide
        assertNotNull(result, "La liste ne doit pas être null");
        assertTrue(result.isEmpty(), "La liste doit être vide s'il n'y a aucun département");
    }

    @Test
    @Order(2)
    void testAddDepartement() {
        // Arrange : Créer un département
        Departement departement = new Departement();
        departement.setNomDepart("Informatique");

        // Act : Ajouter le département
        Departement addedDepartement = departementService.addDepartement(departement);

        // Assert : Vérifier que le département a été ajouté correctement
        assertNotNull(addedDepartement.getIdDepart(), "L'ID du département ajouté ne doit pas être null");
        assertEquals("Informatique", addedDepartement.getNomDepart(), "Le nom du département doit être Informatique");
        assertTrue(departementRepository.findById(addedDepartement.getIdDepart()).isPresent(),
                "Le département doit être présent dans la base");
    }

    @Test
    @Order(3)
    void testRetrieveAllDepartementsWithData() {
        // Arrange : Ajouter deux départements
        Departement dept1 = new Departement();
        dept1.setNomDepart("Informatique");
        Departement dept2 = new Departement();
        dept2.setNomDepart("Mathématiques");
        departementRepository.saveAll(Arrays.asList(dept1, dept2));

        // Act : Récupérer tous les départements
        List<Departement> result = departementService.retrieveAllDepartements();

        // Assert : Vérifier que la liste contient les deux départements
        assertEquals(2, result.size(), "La liste doit contenir 2 départements");
        assertTrue(result.stream().anyMatch(d -> d.getNomDepart().equals("Informatique")),
                "Doit contenir un département Informatique");
        assertTrue(result.stream().anyMatch(d -> d.getNomDepart().equals("Mathématiques")),
                "Doit contenir un département Mathématiques");
    }

    @Test
    @Order(4)
    void testUpdateDepartement() {
        // Arrange : Ajouter un département
        Departement departement = new Departement();
        departement.setNomDepart("Informatique");
        Departement savedDepartement = departementRepository.save(departement);

        // Modifier le département
        savedDepartement.setNomDepart("Informatique Modifiée");

        // Act : Mettre à jour le département
        Departement updatedDepartement = departementService.updateDepartement(savedDepartement);

        // Assert : Vérifier que la mise à jour a été effectuée
        assertEquals("Informatique Modifiée", updatedDepartement.getNomDepart(),
                "Le nom doit être mis à jour à Informatique Modifiée");
        assertEquals(savedDepartement.getIdDepart(), updatedDepartement.getIdDepart(),
                "L'ID doit rester le même");
        Departement fromDb = departementRepository.findById(updatedDepartement.getIdDepart()).orElse(null);
        assertNotNull(fromDb, "Le département doit exister dans la base");
        assertEquals("Informatique Modifiée", fromDb.getNomDepart(),
                "La mise à jour doit être persistante dans la base");
    }

    @Test
    @Order(5)
    void testRetrieveDepartement() {
        // Arrange : Ajouter un département
        Departement departement = new Departement();
        departement.setNomDepart("Physique");
        Departement savedDepartement = departementRepository.save(departement);

        // Act : Récupérer le département par ID
        Departement retrievedDepartement = departementService.retrieveDepartement(savedDepartement.getIdDepart());

        // Assert : Vérifier que le département est bien récupéré
        assertNotNull(retrievedDepartement, "Le département doit être récupéré");
        assertEquals(savedDepartement.getIdDepart(), retrievedDepartement.getIdDepart(),
                "L'ID doit correspondre");
        assertEquals("Physique", retrievedDepartement.getNomDepart(),
                "Le nom doit être Physique");
        assertNotEquals("Informatique", retrievedDepartement.getNomDepart(),
                "Le nom ne doit pas être Informatique");
    }

    @Test
    @Order(6)
    void testDeleteDepartement() {
        // Arrange : Ajouter un département
        Departement departement = new Departement();
        departement.setNomDepart("Chimie");
        Departement savedDepartement = departementRepository.save(departement);

        // Act : Supprimer le département
        departementService.deleteDepartement(savedDepartement.getIdDepart());

        // Assert : Vérifier que le département a été supprimé
        assertFalse(departementRepository.findById(savedDepartement.getIdDepart()).isPresent(),
                "Le département doit être supprimé de la base");
    }

    @Test
    @Order(7)
    void testRetrieveDepartementNotFound() {
        // Act & Assert : Vérifier qu'une exception est levée si le département n'existe pas
        assertThrows(Exception.class, () -> {
            departementService.retrieveDepartement(999); // ID inexistant
        }, "Une exception doit être levée si le département n'est pas trouvé");
    }
}
