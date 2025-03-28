package tn.esprit.spring.kaddem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tn.esprit.spring.kaddem.entities.Departement;

public class DepartementJUnit {

    private Departement departement;

    @BeforeEach
    public void setUp() {
        departement = new Departement("Informatique");
    }

    @Test
    public void testGetIdDepart() {
        assertNotNull(departement.getIdDepart());
    }

    @Test
    public void testGetNomDepart() {
        assertEquals("Informatique", departement.getNomDepart());
    }

    @Test
    public void testSetNomDepart() {
        departement.setNomDepart("Mathematiques");
        assertEquals("Mathematiques", departement.getNomDepart());
    }
}