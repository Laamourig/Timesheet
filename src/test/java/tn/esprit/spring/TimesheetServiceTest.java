package tn.esprit.spring;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.ITimesheetService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimesheetServiceTest {

    @Autowired
    ITimesheetService timesheetService;
    @Autowired
    TimesheetRepository timesheetRepo;
    @Autowired
    MissionRepository missionRepo;
    @Autowired
    DepartementRepository departRepo;
    @Autowired
    EmployeRepository employeRepo;
    @Autowired
    IEmployeService employeService;

    private Mission mission;
    private Employe employe;
    private Departement departement;
    private int idMission;
    private int idDepartment;
    private int idEmploye;
    List<Employe> employeList;
    List<Mission> missionList;

    @Before
    public void init() {
        mission = new Mission("test","mission");
        idMission=timesheetService.ajouterMission(mission);
        employe = new Employe("Moujbani", "Amr","amr.moujbani@esprit.tn",true,Role.CHEF_DEPARTEMENT);
        employe.setId(idEmploye);
        idEmploye= employeService.ajouterEmploye(employe);
        employeList =timesheetService.getAllEmployeByMission(idMission);
        missionList= timesheetService.findAllMissionByEmployeJPQL(idEmploye);
    }

    /*@After
    public void restart() {
        missionRepo.delete(mission);
    }*/

    @Test
    public void testAjouterMission() {
        Assert.assertEquals(idMission, timesheetService.ajouterMission(mission));
    }

    @Test
    public void testAffecterMissionADepartement() {
        Mission mission = new Mission("mission","mission");
        Departement departement = new Departement("INFO");
        departRepo.save(departement);
        mission = missionRepo.save(mission);
        departement = departRepo.save(departement);
        timesheetService.affecterMissionADepartement(mission.getId(), departement.getId());
        int depId = Objects.requireNonNull(missionRepo.findById(mission.getId()).orElse(null)).getDepartement().getId();
        assertEquals(depId, departement.getId());
    }

    @Test
    public void testAjouterTimesheet(){
        TimesheetPK timesheetPK = new TimesheetPK(idMission,idEmploye, new Date(22, 01, 21), new Date(22, 01, 21));
        timesheetService.ajouterTimesheet(idMission, idEmploye, new Date(22, 01, 21), new Date(22, 01, 21));
        Timesheet timeSheetExpected =timesheetRepo.findBytimesheetPK(timesheetPK);
        assertEquals(timeSheetExpected.getTimesheetPK(),timesheetPK);
    }

    @Test
    public void testValiderTimesheet() {
        TimesheetPK timesheetPK = new TimesheetPK(idMission,idEmploye, new Date(22, 01, 21), new Date(22, 01, 21));
        timesheetService.ajouterTimesheet(idMission, idEmploye, new Date(22, 01, 21), new Date(22, 01, 21));
        Timesheet timeSheetExpected =timesheetRepo.findBytimesheetPK(timesheetPK);
        assertTrue(employeService.ajouterEmploye(employe) > 0);
        Date dateDebut;
        Date dateFin;
        try {
            dateDebut = new SimpleDateFormat("yyyy-MM-dd").parse("1922-01-21");
            dateFin = new SimpleDateFormat("yyyy-MM-dd").parse("1922-01-21");
            timesheetService.validerTimesheet(this.mission.getId(), this.employe.getId(), dateDebut, dateFin,
                    this.employe.getId());
            Iterable<Timesheet> timesheets = timesheetRepo.findAll();
            for (Timesheet ts : timesheets) {
                if ((ts.getMission().getId() == this.mission.getId())
                        && (ts.getEmploye().getId() == this.employe.getId())) {
                    List<Timesheet> timesheet = new ArrayList<>();
                    timesheet.add(ts);
                    this.mission.setTimesheets(timesheet);
                }
            }

            assertNotNull(this.mission.getTimesheets());

        } catch (ParseException ignored) {  }
    }

    @Test
    public void testgetAllEmployeByMission() {
        assertEquals(employeList , timesheetService.getAllEmployeByMission(idMission));
    }

    @Test
    public void testfindAllMissionByEmployeJPQL() {
        assertEquals(missionList , timesheetService.findAllMissionByEmployeJPQL(idEmploye));
    }


}