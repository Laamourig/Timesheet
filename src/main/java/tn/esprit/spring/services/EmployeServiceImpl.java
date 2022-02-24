package tn.esprit.spring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class EmployeServiceImpl implements IEmployeService {

	@Autowired
    EmployeRepository employeRepository;
	@Autowired
    DepartementRepository deptRepoistory;
	@Autowired
    ContratRepository contratRepoistory;
	@Autowired
    TimesheetRepository timesheetRepository;

	public int ajouterEmploye(Employe employe) {
		log.info("Adding employee ...");
		try {
			employeRepository.save(employe);
		} catch (Exception e){
			log.error("An exception thrown when adding employee " + e.getStackTrace());
		}
		log.info("Employee "+employe.getPrenom()+" added successfully!");
		return employe.getId();
	}

	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		log.info("Updating emplyee " + employeId + " by email : " + email);
		Employe employe = employeRepository.findById(employeId).orElse(null);
		log.debug("mettreAjourEmailByEmployeId " + employe);
		if (employe != null){
			employe.setEmail(email);
			try {
				employeRepository.save(employe);
			} catch (Exception e){
				log.error("An exception thrown when updating employee " + e.getStackTrace());
			}
			log.info("Employee " + employe.getPrenom() + " updated successfully!");
		} else {
			log.info("Employee with id " + employeId + " not found");
		}
	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		log.info("Affecting employe to departement ...");
		Departement depManagedEntity = deptRepoistory.findById(depId).orElse(null);
		Employe employeManagedEntity = employeRepository.findById(employeId).orElse(null);

		log.debug("affecterEmployeADepartement");
		log.debug("employeID : " + employeId + " depId : " + depId);
		if (employeManagedEntity != null && depManagedEntity != null){
			if(depManagedEntity.getEmployes() == null){

				List<Employe> employes = new ArrayList<>();
				employes.add(employeManagedEntity);
				depManagedEntity.setEmployes(employes);
				log.info("Employe affected to departement!");
			}else{
				depManagedEntity.getEmployes().add(employeManagedEntity);
				log.info("Employe affected to departement!");
			}
		}

	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		log.info("Desaffecting employe from departement ...");
		Departement dep = deptRepoistory.findById(depId).orElse(null);

		log.debug("desaffecterEmployeDuDepartement");
		if (dep != null){
			int employeNb = dep.getEmployes().size();
			log.debug("employeNb : " + employeNb);
			for(int index = 0; index < employeNb; index++){
				log.debug("index : " + index);
				log.debug("employeId : " + employeId);
				log.debug("dep.getEmployes().get(index).getId() : " + dep.getEmployes().get(index).getId());
				if(dep.getEmployes().get(index).getId() == employeId){
					dep.getEmployes().remove(index);
					log.info("Employe desaffected from departement!");
					break;//a revoir
				}
			}
		}
	}

	public int ajouterContrat(Contrat contrat) {
		log.info("Adding contrat ...");
		try {
			contratRepoistory.save(contrat);
		} catch (Exception e){
			log.error("An exception thrown when adding an contrat " + e.getStackTrace());
		}
		log.info("Contrat "+contrat.getReference()+" added successfully!");
		return contrat.getReference();
	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		log.info("Affecting contrat with id : " + contratId + " to the employe with id : " + employeId);
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).orElse(null);
		Employe employeManagedEntity = employeRepository.findById(employeId).orElse(null);
		log.debug("affecterContratAEmploye, contratID: " + contratId + "employeeId : " + employeId);

		if (contratManagedEntity != null && employeManagedEntity != null){
			contratManagedEntity.setEmploye(employeManagedEntity);
			try {
				contratRepoistory.save(contratManagedEntity);
			} catch (Exception e){
				log.error("An exception thrown affecting contrat to employee " + e.getStackTrace());
			}
			log.info("Contrat is affected to employee");
		} else {
			log.info("Contrat or employee not found");
		}
	}

	public String getEmployePrenomById(int employeId) {
		Employe employeManagedEntity = employeRepository.findById(employeId).get();
		return employeManagedEntity.getPrenom();
	}
	public void deleteEmployeById(int employeId)
	{
		Employe employe = employeRepository.findById(employeId).get();

		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		for(Departement dep : employe.getDepartements()){
			dep.getEmployes().remove(employe);
		}

		employeRepository.delete(employe);
	}

	public void deleteContratById(int contratId) {
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		contratRepoistory.delete(contratManagedEntity);

	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}
	
	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}
	
	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
         employeRepository.deleteAllContratJPQL();
	}
	
	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}
	
	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
                                                         Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
				return (List<Employe>) employeRepository.findAll();
	}

}
