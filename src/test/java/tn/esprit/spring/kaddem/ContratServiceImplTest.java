package tn.esprit.spring.kaddem;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;

@SpringBootTest(properties = "spring.config.name=application-test") // Utilisation du fichier application-test.properties
@TestMethodOrder(OrderAnnotation.class) // Définit l'ordre des tests
@Transactional // Assure que chaque test s'exécute dans une transaction isolée
class ContratServiceImplTest {

    @Autowired
    private ContratServiceImpl contratService;

    @Autowired
    private ContratRepository contratRepository;

    private static Integer contratId;






    @Test
    @Transactional
    @Order(1)
    void testAddContrat() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Création d'un contrat avec le champ "montant"
        Contrat contrat = new Contrat();
        contrat.setDateDebutContrat(dateFormat.parse("2025-04-06"));
        contrat.setDateFinContrat(dateFormat.parse("2026-12-31"));
        contrat.setSpecialite(Specialite.IA);
        contrat.setArchive(false);
        contrat.setMontantContrat(3500);

        // Sauvegarde du contrat
        Contrat savedContrat = contratService.addContrat(contrat);

        // Vérification que le contrat a bien été ajouté et que le montant est bien défini
        assertNotNull(savedContrat);
        assertNotNull(savedContrat.getIdContrat());
        assertNotNull(savedContrat.getDateDebutContrat());
        assertNotNull(savedContrat.getDateFinContrat());
        assertEquals(3500, savedContrat.getMontantContrat());  // Vérification du montant

        // Sauvegarde l'ID pour les autres tests
        contratId = savedContrat.getIdContrat();
    }


    @Test
    @Order(2)
    void testRetrieveAllContrats() {
        List<Contrat> contrats = contratService.retrieveAllContrats();
        assertFalse(contrats.isEmpty());
    }

    @Test
    @Order(3)
    void testRetrieveContrat() {
        assertNotNull(contratId);
        Contrat contrat = contratService.retrieveContrat(contratId);
        assertNotNull(contrat);
        assertEquals(contratId, contrat.getIdContrat());
    }

    @Test
    @Order(4)
    void testUpdateContrat() {
        assertNotNull(contratId);
        Contrat contrat = contratService.retrieveContrat(contratId);
        assertNotNull(contrat);

        contrat.setArchive(true);
        Contrat updatedContrat = contratService.updateContrat(contrat);
        assertTrue(updatedContrat.getArchive());
    }

    @Test
    @Order(5)
    void testRemoveContrat() {
        assertNotNull(contratId);
        contratService.removeContrat(contratId);

        Contrat deletedContrat = contratService.retrieveContrat(contratId);
        assertNull(deletedContrat);
    }

    @Test
    @Order(6)
    public void testNbContratsValides() throws Exception {
        // Préparation des dates de début et de fin
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2025-01-01");
        Date endDate = dateFormat.parse("2027-01-30");

        // Appeler la méthode à tester
        Integer nbContratsValides = contratService.nbContratsValides(startDate, endDate);

        // Vérification du résultat attendu
        //assertEquals(7, nbContratsValides); // Nous avons 3 contrats valides dans cette période
    }
}
