package tn.esprit.spring.kaddem;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class) // Define test order
@Transactional // Ensure each test runs in an isolated transaction
class DepartementServiceImplTest {

    @Autowired
    private DepartementServiceImpl departementService;

    @Autowired
    private DepartementRepository departementRepository;

    private static Integer departementId;



    @Test
    @Order(1)
    void testAddDepartement() {
        // Create a new Departement
        Departement departement = new Departement();
        departement.setNomDepart("Informatique");

        // Save the departement
        Departement savedDepartement = departementService.addDepartement(departement);

        // Verify the departement was added
        assertNotNull(savedDepartement);
        assertNotNull(savedDepartement.getIdDepart());
        assertEquals("Informatique", savedDepartement.getNomDepart());

        // Save the ID for other tests
        departementId = savedDepartement.getIdDepart();
    }

    @Test
    @Order(2)
    void testRetrieveAllDepartements() {
        // Assuming testAddDepartement ran and added one departement
        List<Departement> departements = departementService.retrieveAllDepartements();
        assertFalse(departements.isEmpty());
        //assertEquals(1, departements.size()); // Should have one from testAddDepartement
    }

    @Test
    @Order(3)
    void testRetrieveDepartement() {
        assertNotNull(departementId);
        Departement departement = departementService.retrieveDepartement(departementId);
        assertNotNull(departement);
        assertEquals(departementId, departement.getIdDepart());
        assertEquals("Informatique", departement.getNomDepart());
    }

    @Test
    @Order(4)
    void testUpdateDepartement() {
        assertNotNull(departementId);
        Departement departement = departementService.retrieveDepartement(departementId);
        assertNotNull(departement);

        // Update the departement
        departement.setNomDepart("Génie Logiciel");
        Departement updatedDepartement = departementService.updateDepartement(departement);

        // Verify the update
        assertNotNull(updatedDepartement);
        assertEquals("Génie Logiciel", updatedDepartement.getNomDepart());
        assertEquals(departementId, updatedDepartement.getIdDepart());
    }

    @Test
    @Order(5)
    void testDeleteDepartement() {
        assertNotNull(departementId);
        departementService.deleteDepartement(departementId);

        // Verify the departement was deleted
        assertThrows(Exception.class, () -> {
            departementService.retrieveDepartement(departementId);
        }, "Expected an exception when retrieving a deleted departement");
    }
}