package tn.esprit.spring.kaddem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tn.esprit.spring.kaddem.entities.Departement;

 class DepartementJUnitTests {

    private Departement departement;

    @BeforeEach
     void setUp() {
        departement = new Departement("Informatique");
    }

    @Test
     void testGetIdDepart() {
        assertNotNull(departement.getIdDepart());
    }

    @Test
     void testGetNomDepart() {
        assertEquals("Informatique", departement.getNomDepart());
    }

    @Test
     void testSetNomDepart() {
        departement.setNomDepart("Mathematiques");
        assertEquals("Mathematiques", departement.getNomDepart());
    }
}