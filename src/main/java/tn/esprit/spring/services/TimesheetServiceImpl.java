package tn.esprit.spring.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.entities.TimesheetPK;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Slf4j
@Service
public class TimesheetServiceImpl implements ITimesheetService {

	@Autowired
	MissionRepository missionRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;
	@Autowired
	EmployeRepository employeRepository;

	public int ajouterMission(Mission mission) {
		log.info("In ajouterMission");
		try {
			missionRepository.save(mission);
			log.info("Out of ajouterMission without errors");
		}catch (Exception e){
			log.error(e.getMessage());
		}
		return mission.getId();
	}
    
	public void affecterMissionADepartement(int missionId, int depId) {
		log.info("In affecterMissionADepartement()");
		Mission mission = missionRepository.findById(missionId).orElse(null);
		Departement dep = deptRepoistory.findById(depId).orElse(null);
		try {
			assert mission != null;
			mission.setDepartement(dep);
			missionRepository.save(mission);
		log.info("Out of affecterMissionADepartement() without errors");
		}catch (NullPointerException e){
			log.error(e.getMessage());
		}
	}

	public void ajouterTimesheet(int missionId, int employeId, Date dateDebut, Date dateFin) {
		log.info("In ajouterTimesheet()");
		TimesheetPK timesheetPK = new TimesheetPK();
		Timesheet timesheet = new Timesheet();
		try {
			timesheetPK.setDateDebut(dateDebut);
			timesheetPK.setDateFin(dateFin);
			timesheetPK.setIdEmploye(employeId);
			timesheetPK.setIdMission(missionId);

			timesheet.setTimesheetPK(timesheetPK);
			timesheet.setValide(false); //par defaut non valide
			timesheetRepository.save(timesheet);
		}catch (Exception e){
			log.error(e.getMessage());
		}

		log.info("Out of ajouterTimesheet() without errors");
	}

	
	public void validerTimesheet(int missionId, int employeId, Date dateDebut, Date dateFin, int validateurId) {
		log.info("In validerTimesheet()");
		Employe validateur = employeRepository.findById(validateurId).orElse(null);
		Mission mission = missionRepository.findById(missionId).orElse(null);

		if(validateur!=null && !validateur.getRole().equals(Role.CHEF_DEPARTEMENT)){
			log.error("l'employe doit etre chef de departement pour valider une feuille de temps !");
			return;
		}
		boolean chefDeLaMission = false;
		assert validateur != null;
		for(Departement dep : validateur.getDepartements()){
			assert mission != null;
			if(dep.getId() == mission.getDepartement().getId()){
				chefDeLaMission = true;
				break;
			}
		}
		if(!chefDeLaMission){
			log.info("l'employe doit etre chef de departement de la mission en question");
			return;
		}

		TimesheetPK timesheetPK = new TimesheetPK(missionId, employeId, dateDebut, dateFin);
		Timesheet timesheet =timesheetRepository.findBytimesheetPK(timesheetPK);
		timesheet.setValide(true);
		timesheetRepository.save(timesheet);
		//Comment Lire une date de la base de données
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		log.info("dateDebut : " + dateFormat.format(timesheet.getTimesheetPK().getDateDebut()));
		
	}

	
	public List<Mission> findAllMissionByEmployeJPQL(int employeId) {
		List<Mission> missionList= timesheetRepository.findAllMissionByEmployeJPQL(employeId);
		for (Mission m: missionList){
			log.info("Mission: "+m);
		}
		return missionList;
	}

	
	public List<Employe> getAllEmployeByMission(int missionId) {
		List<Employe> employeList= timesheetRepository.getAllEmployeByMission(missionId);
		for (Employe employe: employeList){
			log.info("Employee: "+employe.getId());
		}
		return employeList;
	}

}
