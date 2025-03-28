package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ContratServiceMockTests {

    @InjectMocks
    ContratServiceImpl contratService;

    ContratRepository contratRepository = Mockito.mock(ContratRepository.class);
    EtudiantRepository etudiantRepository = Mockito.mock(EtudiantRepository.class);


    Contrat contrat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contrat = new Contrat();
        contrat.setIdContrat(1);
        contrat.setDateDebutContrat(new Date());
        contrat.setDateFinContrat(new Date());
    }

    @Test
    void retrieveContrat() {
        when(contratRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(contrat));
        Contrat retrievedContrat = contratService.retrieveContrat(1);
        Assertions.assertNotNull(retrievedContrat);
    }

    @Test
    void retrieveAllContrats() {
        List<Contrat> contratList = Collections.singletonList(contrat);
        when(contratRepository.findAll()).thenReturn(contratList);
        List<Contrat> allContrats = contratService.retrieveAllContrats();
        assertEquals(1, allContrats.size());
    }

    @Test
    void updateContrat() {
        when(contratRepository.save(contrat)).thenReturn(contrat);
        Contrat updatedContrat = contratService.updateContrat(contrat);
        Assertions.assertNotNull(updatedContrat);
    }

    @Test
    void addContrat() {
        when(contratRepository.save(contrat)).thenReturn(contrat);
        Contrat addedContrat = contratService.addContrat(contrat);
        Assertions.assertNotNull(addedContrat);
    }

    @Test
    void deleteContrat() {
        Integer contratId = 1;

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        Mockito.doNothing().when(contratRepository).delete(contrat);

        contratService.removeContrat(contratId);
    }





    @Test
    void nbContratsValides() {
        Date startDate = new Date();
        Date endDate = new Date();
        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(5);
        int count = contratService.nbContratsValides(startDate, endDate);
        assertEquals(5, count);
    }



    @Test
    void affectContratToEtudiant() {
        // Étudiant avec un contrat actif
        Etudiant etudiantAvecContratActif = new Etudiant();
        etudiantAvecContratActif.setNomE("nom1");
        etudiantAvecContratActif.setPrenomE("prenom1");
        Contrat contratActif = new Contrat();
        contratActif.setArchive(false);
        etudiantAvecContratActif.setContrats(Collections.singleton(contratActif));

        // Étudiant sans contrat actif
        Etudiant etudiantSansContratActif = new Etudiant();
        etudiantSansContratActif.setNomE("nom2");
        etudiantSansContratActif.setPrenomE("prenom2");
        etudiantSansContratActif.setContrats(new HashSet<>());

        when(etudiantRepository.findByNomEAndPrenomE("nom1", "prenom1")).thenReturn(etudiantAvecContratActif);
        when(etudiantRepository.findByNomEAndPrenomE("nom2", "prenom2")).thenReturn(etudiantSansContratActif);
        when(contratRepository.findByIdContrat(Mockito.anyInt())).thenReturn(contrat);
        when(contratRepository.save(Mockito.any(Contrat.class))).thenReturn(contrat);

        // Test avec étudiant ayant un contrat actif
        Contrat contratAvecActif = contratService.affectContratToEtudiant(1, "nom1", "prenom1");
        Assertions.assertNotNull(contratAvecActif);

        // Test avec étudiant sans contrat actif
        Contrat contratSansActif = contratService.affectContratToEtudiant(1, "nom2", "prenom2");
        Assertions.assertNotNull(contratSansActif);
    }



    @Test
    public void testRetrieveAndUpdateStatusContrat() {
        Contrat contrat = new Contrat();
        contrat.setDateDebutContrat(new Date());
        contrat.setDateFinContrat(new Date());
        contrat.setArchive(false);
        List<Contrat> contrats = Collections.singletonList(contrat);
        when(contratRepository.findAll()).thenReturn(contrats);
        contratService.retrieveAndUpdateStatusContrat();
        Assertions.assertTrue(contrat.getArchive());



    }





    @Test
    void getChiffreAffaireEntreDeuxDates() {
        MockitoAnnotations.initMocks(this);

        // Date de début et de fin pour le test
        Date startDate = new Date();
        Date endDate = new Date();

        // Contrats de test
        Contrat contrat1 = new Contrat();
        contrat1.setSpecialite(Specialite.CLOUD);
        contrat1.setDateDebutContrat(startDate);
        contrat1.setDateFinContrat(endDate);
        Contrat contrat2 = new Contrat();
        contrat2.setSpecialite(Specialite.IA);
        contrat2.setDateDebutContrat(startDate);
        contrat2.setDateFinContrat(endDate);
        Contrat contrat3 = new Contrat();
        contrat3.setSpecialite(Specialite.RESEAUX);
        contrat3.setDateDebutContrat(startDate);
        contrat3.setDateFinContrat(endDate);
        Contrat contrat4 = new Contrat();
        contrat4.setSpecialite(Specialite.SECURITE);
        contrat4.setDateDebutContrat(startDate);
        contrat4.setDateFinContrat(endDate);



        List<Contrat> contrats = Arrays.asList(contrat1, contrat2, contrat3, contrat4);

        // Configuration du comportement du mock
        Mockito.when(contratRepository.findAll()).thenReturn(contrats);





        // Exécution de la méthode à tester
        float chiffreAffaire = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        assertEquals(0, chiffreAffaire);
    }



}