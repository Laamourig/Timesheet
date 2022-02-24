package tn.esprit.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.services.IEmployeService;

import java.sql.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeServiceImplTest {

    @Autowired
    IEmployeService employeService;

    @Test
    public void ajouterEmployeTest(){
        Employe employe = new Employe();
        employe.setPrenom("Fourat");
        employe.setNom("Nefoussi");
        employe.setEmail("fourat.nefoussi@esprit.tn");
        employe.setActif(true);
        employe.setRole(Role.INGENIEUR);
        employeService.ajouterEmploye(employe);
    }

    @Test
    public void mettreAjourEmailByEmployeIdTest(){
        String email = "Test@test.com";
        int employeId = 1;
        employeService.mettreAjourEmailByEmployeId(email,employeId);
    }

    @Test
    public void affecterEmployeADepartementTest(){
        employeService.affecterEmployeADepartement(1,1);
    }

    @Test
    public void desaffecterEmployeDuDepartementTest(){
        employeService.desaffecterEmployeDuDepartement(1,1);
    }

    @Test
    public void ajouterContratTest(){
        Contrat contrat = new Contrat();
        contrat.setDateDebut(Date.valueOf("2020-01-01"));
        contrat.setReference(20);
        contrat.setSalaire(1000);
        contrat.setTypeContrat("CDI");
        employeService.ajouterContrat(contrat);
    }

    @Test
    public void affecterContratAEmployeTest(){
        employeService.affecterContratAEmploye(1,1);
    }
}
