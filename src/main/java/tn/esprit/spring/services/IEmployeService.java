package tn.esprit.spring.services;

import tn.esprit.spring.entities.*;

import java.util.Date;
import java.util.List;


public interface IEmployeService {
	
	public int ajouterEmploye(Employe employe); //fourat
	public void mettreAjourEmailByEmployeId(String email, int employeId); //fourat
	public void affecterEmployeADepartement(int employeId, int depId); //fourat
	public void desaffecterEmployeDuDepartement(int employeId, int depId); //fourat
	public int ajouterContrat(Contrat contrat); //fourat
	public void affecterContratAEmploye(int contratId, int employeId); //fourat
	public String getEmployePrenomById(int employeId);
	public void deleteEmployeById(int employeId);
	public void deleteContratById(int contratId);
	public int getNombreEmployeJPQL();
	public List<String> getAllEmployeNamesJPQL();
	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise);
	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId);
	public void deleteAllContratJPQL();
	public float getSalaireByEmployeIdJPQL(int employeId);
	public Double getSalaireMoyenByDepartementId(int departementId);
	public List<Employe> getAllEmployes();
	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission,
                                                         Date dateDebut, Date dateFin);
	
	
	

	
}
