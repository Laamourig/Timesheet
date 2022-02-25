package tn.esprit.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;
import tn.esprit.spring.services.IEntrepriseService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntrepriseServiceImplTest {
	@Autowired
	IEntrepriseService es;
	@Autowired
	EntrepriseRepository er;
	@Autowired
	DepartementRepository dr;

	@Test
	public void ajouterEntrepriseTest() {
		Entreprise ent = new Entreprise("dummy", "dummy");
		int a = es.ajouterEntreprise(ent);
		assertTrue(a > 0);
		// clear db
		er.deleteById(a);
	}

	@Test
	public void ajouterDepartementTest() {
		Departement dep = new Departement("dummy");
		int a = es.ajouterDepartement(dep);
		assertTrue(a > 0);
		// clear db
		dr.deleteById(a);
	}

	@Test
	public void affecterDepartementAEntrepriseTest() {
		Entreprise entreprise = new Entreprise("dummy", "dummy");
		int addedEntrepriseId = es.ajouterEntreprise(entreprise);
		Departement departement = new Departement("dummy");
		int addedDepId = es.ajouterDepartement(departement);
		es.affecterDepartementAEntreprise(addedDepId, addedEntrepriseId);
		Optional<Departement> depOpt = dr.findById(addedDepId);
		Departement departementEntity = null;
		if (depOpt.isPresent()) {
			departementEntity = depOpt.get();
		}
		assertEquals(departementEntity.getEntreprise().getId(), addedEntrepriseId);
		// clear db
		dr.deleteById(addedDepId);
		er.deleteById(addedEntrepriseId);
	}

	@Test
	public void getAllDepartementsNamesByEntrepriseTest() {
		Entreprise entreprise = new Entreprise("dummy", "dummy");
		int addedEntrepriseId = es.ajouterEntreprise(entreprise);
		Departement departement = new Departement("dummy");
		int addedDepId = es.ajouterDepartement(departement);
		List<String> names = es.getAllDepartementsNamesByEntreprise(addedEntrepriseId);
		assertNotNull(names);
		// clear db
		er.deleteById(addedEntrepriseId);
		dr.deleteById(addedDepId);
	}

	@Test
	public void getEntrepriseByIdTest() {
		Entreprise ent = new Entreprise("dummy", "dummy");
		int a = es.ajouterEntreprise(ent);
		Optional<Entreprise> entOpt = er.findById(a);
		Entreprise entr = null;
		if (entOpt.isPresent()) {
			entr = entOpt.get();
		}
		assertEquals(entr.getName(), ent.getName());
		assertEquals(entr.getRaisonSocial(), ent.getRaisonSocial());
		// clear db
		er.deleteById(a);
	}

	@Test
	public void deleteEntrepriseByIdTest() {
		Entreprise ent = new Entreprise("dummy", "dummy");
		int a = es.ajouterEntreprise(ent);
		es.deleteEntrepriseById(a);
		Optional<Entreprise> mustBeNull = er.findById(a);
		assertFalse(mustBeNull.isPresent());
	}

	@Test
	public void deleteDepartementByIdTest() {
		Departement dep = new Departement("dummy");
		int a = es.ajouterDepartement(dep);
		es.deleteDepartementById(a);
		Optional<Departement> mustBeNull = dr.findById(a);
		assertFalse(mustBeNull.isPresent());
	}

}
