package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.IEquipeService;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Intégration avec JUnit 5 et Spring
@ExtendWith(SpringExtension.class)
// Charge le contexte Spring pour les tests
@SpringBootTest
// Définit l'ordre d'exécution des tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EquipeServiceJUnitTests {

    @Autowired
    private IEquipeService equipeService;

    @Autowired
    private EquipeRepository equipeRepository;

    @BeforeAll
    static void initAll() {
        System.out.println("Initialisation globale avant tous les tests");
    }

    @BeforeEach
    void setUp() {
        equipeRepository.deleteAll();
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
    void testRetrieveAllEquipesEmpty() {
        // Act : Récupérer toutes les équipes quand la base est vide
        List<Equipe> result = equipeService.retrieveAllEquipes();

        // Assert : Vérifier que la liste est vide
        assertNotNull(result, "La liste ne doit pas être null");
        assertTrue(result.isEmpty(), "La liste doit être vide s'il n'y a aucune équipe");
    }

    @Test
    @Order(2)
    void testAddEquipe() {
        // Arrange : Créer une équipe
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Equipe A");
        equipe.setNiveau(Niveau.JUNIOR);

        // Act : Ajouter l'équipe
        Equipe addedEquipe = equipeService.addEquipe(equipe);

        // Assert : Vérifier que l'équipe a été ajoutée correctement
        assertNotNull(addedEquipe.getIdEquipe(), "L'ID de l'équipe ajoutée ne doit pas être null");
        assertEquals("Equipe A", addedEquipe.getNomEquipe(), "Le nom de l'équipe doit être Equipe A");
        assertEquals(Niveau.JUNIOR, addedEquipe.getNiveau(), "Le niveau doit être JUNIOR");
        assertTrue(equipeRepository.findById(addedEquipe.getIdEquipe()).isPresent(),
                "L'équipe doit être présente dans la base");
    }

    @Test
    @Order(3)
    void testRetrieveAllEquipesWithData() {
        // Arrange : Ajouter deux équipes
        Equipe equipe1 = new Equipe();
        equipe1.setNomEquipe("Equipe A");
        equipe1.setNiveau(Niveau.JUNIOR);
        Equipe equipe2 = new Equipe();
        equipe2.setNomEquipe("Equipe B");
        equipe2.setNiveau(Niveau.SENIOR);
        equipeRepository.saveAll(Arrays.asList(equipe1, equipe2));

        // Act : Récupérer toutes les équipes
        List<Equipe> result = equipeService.retrieveAllEquipes();

        // Assert : Vérifier que la liste contient les deux équipes
        assertEquals(2, result.size(), "La liste doit contenir 2 équipes");
        assertTrue(result.stream().anyMatch(e -> e.getNomEquipe().equals("Equipe A")),
                "Doit contenir une équipe Equipe A");
        assertTrue(result.stream().anyMatch(e -> e.getNomEquipe().equals("Equipe B")),
                "Doit contenir une équipe Equipe B");
    }

    @Test
    @Order(4)
    void testUpdateEquipe() {
        // Arrange : Ajouter une équipe
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Equipe A");
        equipe.setNiveau(Niveau.JUNIOR);
        Equipe savedEquipe = equipeRepository.save(equipe);

        // Modifier l'équipe
        savedEquipe.setNomEquipe("Equipe A Modifiée");
        savedEquipe.setNiveau(Niveau.SENIOR);

        // Act : Mettre à jour l'équipe
        Equipe updatedEquipe = equipeService.updateEquipe(savedEquipe);

        // Assert : Vérifier que la mise à jour a été effectuée
        assertEquals("Equipe A Modifiée", updatedEquipe.getNomEquipe(),
                "Le nom doit être mis à jour à Equipe A Modifiée");
        assertEquals(Niveau.SENIOR, updatedEquipe.getNiveau(),
                "Le niveau doit être mis à jour à SENIOR");
        assertEquals(savedEquipe.getIdEquipe(), updatedEquipe.getIdEquipe(),
                "L'ID doit rester le même");
        Equipe fromDb = equipeRepository.findById(updatedEquipe.getIdEquipe()).orElse(null);
        assertNotNull(fromDb, "L'équipe doit exister dans la base");
        assertEquals("Equipe A Modifiée", fromDb.getNomEquipe(),
                "La mise à jour du nom doit être persistante dans la base");
        assertEquals(Niveau.SENIOR, fromDb.getNiveau(),
                "La mise à jour du niveau doit être persistante dans la base");
    }

    @Test
    @Order(5)
    void testRetrieveEquipe() {
        // Arrange : Ajouter une équipe
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Equipe Test");
        equipe.setNiveau(Niveau.EXPERT);
        Equipe savedEquipe = equipeRepository.save(equipe);

        // Act : Récupérer l'équipe par ID
        Equipe retrievedEquipe = equipeService.retrieveEquipe(savedEquipe.getIdEquipe());

        // Assert : Vérifier que l'équipe est bien récupérée
        assertNotNull(retrievedEquipe, "L'équipe doit être récupérée");
        assertEquals(savedEquipe.getIdEquipe(), retrievedEquipe.getIdEquipe(),
                "L'ID doit correspondre");
        assertEquals("Equipe Test", retrievedEquipe.getNomEquipe(),
                "Le nom doit être Equipe Test");
        assertEquals(Niveau.EXPERT, retrievedEquipe.getNiveau(),
                "Le niveau doit être EXPERT");
        assertNotEquals(Niveau.JUNIOR, retrievedEquipe.getNiveau(),
                "Le niveau ne doit pas être JUNIOR");
    }

    @Test
    @Order(6)
    void testDeleteEquipe() {
        // Arrange : Ajouter une équipe
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Equipe C");
        equipe.setNiveau(Niveau.SENIOR);
        Equipe savedEquipe = equipeRepository.save(equipe);

        // Act : Supprimer l'équipe
        equipeService.deleteEquipe(savedEquipe.getIdEquipe());

        // Assert : Vérifier que l'équipe a été supprimée
        assertFalse(equipeRepository.findById(savedEquipe.getIdEquipe()).isPresent(),
                "L'équipe doit être supprimée de la base");
    }

    @Test
    @Order(7)
    void testRetrieveEquipeNotFound() {
        // Act & Assert : Vérifier qu'une exception est levée si l'équipe n'existe pas
        assertThrows(Exception.class, () -> {
            equipeService.retrieveEquipe(999); // ID inexistant
        }, "Une exception doit être levée si l'équipe n'est pas trouvée");
    }

    @Test
    @Order(8)
    void testEvoluerEquipesJuniorToSenior() {
        // Arrange : Créer une équipe JUNIOR avec 3 étudiants ayant des contrats actifs > 1 an
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Equipe Junior");
        equipe.setNiveau(Niveau.JUNIOR);

        Etudiant etudiant1 = new Etudiant();
        etudiant1.setIdEtudiant(1);
        Contrat contrat1 = new Contrat();
        contrat1.setArchive(false);
        contrat1.setDateFinContrat(new Date(System.currentTimeMillis() - 2L * 365 * 24 * 60 * 60 * 1000)); // 2 ans avant
        etudiant1.setContrats(new HashSet<>(Arrays.asList(contrat1)));

        Etudiant etudiant2 = new Etudiant();
        etudiant2.setIdEtudiant(2);
        Contrat contrat2 = new Contrat();
        contrat2.setArchive(false);
        contrat2.setDateFinContrat(new Date(System.currentTimeMillis() - 2L * 365 * 24 * 60 * 60 * 1000));
        etudiant2.setContrats(new HashSet<>(Arrays.asList(contrat2)));

        Etudiant etudiant3 = new Etudiant();
        etudiant3.setIdEtudiant(3);
        Contrat contrat3 = new Contrat();
        contrat3.setArchive(false);
        contrat3.setDateFinContrat(new Date(System.currentTimeMillis() - 2L * 365 * 24 * 60 * 60 * 1000));
        etudiant3.setContrats(new HashSet<>(Arrays.asList(contrat3)));

        equipe.setEtudiants(new HashSet<>(Arrays.asList(etudiant1, etudiant2, etudiant3)));
        equipeRepository.save(equipe);

        // Act : Faire évoluer les équipes
        equipeService.evoluerEquipes();

        // Assert : Vérifier que le niveau est passé à SENIOR
        Equipe updatedEquipe = equipeRepository.findById(equipe.getIdEquipe()).orElse(null);
        assertNotNull(updatedEquipe, "L'équipe doit exister dans la base");
        assertEquals(Niveau.SENIOR, updatedEquipe.getNiveau(),
                "Le niveau doit être mis à jour à SENIOR");
    }

    @Test
    @Order(9)
    void testEvoluerEquipesSeniorToExpert() {
        // Arrange : Créer une équipe SENIOR avec 3 étudiants ayant des contrats actifs > 1 an
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Equipe Senior");
        equipe.setNiveau(Niveau.SENIOR);

        Etudiant etudiant1 = new Etudiant();
        etudiant1.setIdEtudiant(1);
        Contrat contrat1 = new Contrat();
        contrat1.setArchive(false);
        contrat1.setDateFinContrat(new Date(System.currentTimeMillis() - 2L * 365 * 24 * 60 * 60 * 1000));
        etudiant1.setContrats(new HashSet<>(Arrays.asList(contrat1)));

        Etudiant etudiant2 = new Etudiant();
        etudiant2.setIdEtudiant(2);
        Contrat contrat2 = new Contrat();
        contrat2.setArchive(false);
        contrat2.setDateFinContrat(new Date(System.currentTimeMillis() - 2L * 365 * 24 * 60 * 60 * 1000));
        etudiant2.setContrats(new HashSet<>(Arrays.asList(contrat2)));

        Etudiant etudiant3 = new Etudiant();
        etudiant3.setIdEtudiant(3);
        Contrat contrat3 = new Contrat();
        contrat3.setArchive(false);
        contrat3.setDateFinContrat(new Date(System.currentTimeMillis() - 2L * 365 * 24 * 60 * 60 * 1000));
        etudiant3.setContrats(new HashSet<>(Arrays.asList(contrat3)));

        equipe.setEtudiants(new HashSet<>(Arrays.asList(etudiant1, etudiant2, etudiant3)));
        equipeRepository.save(equipe);

        // Act : Faire évoluer les équipes
        equipeService.evoluerEquipes();

        // Assert : Vérifier que le niveau est passé à EXPERT
        Equipe updatedEquipe = equipeRepository.findById(equipe.getIdEquipe()).orElse(null);
        assertNotNull(updatedEquipe, "L'équipe doit exister dans la base");
        assertEquals(Niveau.EXPERT, updatedEquipe.getNiveau(),
                "Le niveau doit être mis à jour à EXPERT");
    }

    @Test
    @Order(10)
    void testEvoluerEquipesNoEvolution() {
        // Arrange : Créer une équipe JUNIOR avec des contrats récents (moins d'1 an)
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Equipe Junior");
        equipe.setNiveau(Niveau.JUNIOR);

        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        Contrat contrat = new Contrat();
        contrat.setArchive(false);
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() - 100 * 24 * 60 * 60 * 1000)); // 100 jours avant
        etudiant.setContrats(new HashSet<>(Arrays.asList(contrat)));

        equipe.setEtudiants(new HashSet<>(Arrays.asList(etudiant)));
        equipeRepository.save(equipe);

        // Act : Faire évoluer les équipes
        equipeService.evoluerEquipes();

        // Assert : Vérifier que le niveau reste JUNIOR
        Equipe updatedEquipe = equipeRepository.findById(equipe.getIdEquipe()).orElse(null);
        assertNotNull(updatedEquipe, "L'équipe doit exister dans la base");
        assertEquals(Niveau.JUNIOR, updatedEquipe.getNiveau(),
                "Le niveau doit rester JUNIOR car les conditions ne sont pas remplies");
    }
}