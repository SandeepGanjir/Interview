package lld;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * ######### REQUIREMENTS #########
 * 1. People should be able to:
 *    a. enroll for vaccination on specific days.
 *    b. check booked day.
 *    c. check previous vaccination details. (date/type/whichDose)
 * 2. Doctor should be able to:
 *    a. check patients' previous vaccination details.
 *    b. verify if a patient is booked for the day and get details.
 *    c. administer vaccine to patient.
 *    d. <Optional> get patient details.
 * 3. Staff should be able to:
 *    a. check all booked patients list for current day.
 *    b. check number of patients booked on any specific day for any vaccine type.
 *    c. manage vaccine stock.
 *    d. build vaccination certificate for given patient.
 *
 * ######### ACTORS & ACTIONS #########
 * 1. Patient
 *    - getPreviousVaccinationDetails(): List<VaccinationDetails>
 *    - registerNextDose(VaccineType, LocalDateTime/Slot): <LocalDateTime/Slot + Details>
 *    - getUpcomingVaccinationDate(): <LocalDateTime/Slot + Details>
 * 2. Doctor
 *    - getPatientVaccinationDetails(PatientId): List<VaccinationDetails>
 *    - getPatientBooking(PatientId): <LocalDateTime/Slot + Details>
 *    - administerVaccine(VaccineType): VaccinationDetails
 * 3. Staff
 *    - getTodaysPatients(): List<Patient>
 *    - getRequiredVaccineDose(VaccineType, LocalDate): int
 *    - addVaccineStock(VaccineType, numberOfDoses): int
 *    - buildVaccineCertificate(PatientId): Document
 *
 * ######### ENTITY #########
 * 1. Enum VaccineType
 * 3. class System(VaccineCentreManagement)
 * 4. class Patient
 * 5. class Doctor
 * 6. class Staff
 * 7. class VaccinationDetail
 * 8. class RegistrationDetail
 * 9. class VaccinationCentre
 */

enum VaccineType{
    COVISHIELD,
    COVAXIN,
    ASTRAZENECA
}

class VaccinationDetail {
    private final VaccineType vaccineType;
    private final int doseNumber;
    private final int administeringDoctorId;
    private final LocalDateTime timeOfVaccination;
    private final int vaccinationCentreId;

    VaccinationDetail(VaccineType vaccineType, int doseNumber, int administeringDoctorId, LocalDateTime timeOfVaccination, int vaccinationCentreId) {
        this.vaccineType = vaccineType;
        this.doseNumber = doseNumber;
        this.administeringDoctorId = administeringDoctorId;
        this.timeOfVaccination = timeOfVaccination;
        this.vaccinationCentreId = vaccinationCentreId;
    }

    public int getDoseNumber() {
        return doseNumber;
    }
}

class RegistrationDetail {
    private final VaccineType vaccineType;
    private final LocalDateTime scheduledTime;
    private final int vaccinationCentreId;

    RegistrationDetail(VaccineType vaccineType, LocalDateTime scheduledTime, int vaccinationCentreId) {
        this.vaccineType = vaccineType;
        this.scheduledTime = scheduledTime;
        this.vaccinationCentreId = vaccinationCentreId;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public VaccineType getVaccineType() {
        return vaccineType;
    }
}

class VaccinationCentre {
    private final int id;
    private final String address;
    private final int zipCode;
    private final Map<VaccineType, Integer> inventory = new HashMap<>();

    VaccinationCentre(int id, String address, int zipCode) {
        this.id = id;
        this.address = address;
        this.zipCode = zipCode;
    }

    public Map<VaccineType, Integer> getInventory() {
        return inventory;
    }

    public int addToInventory(VaccineType vaccineType, int numberOfDoses) {
        int curStock = inventory.getOrDefault(vaccineType, 0);
        inventory.put(vaccineType, curStock + numberOfDoses);
        return inventory.get(vaccineType);
    }
}

class Patient {
    private final int id;
    private final List<VaccinationDetail> vaccinationList = new ArrayList<>();
    private RegistrationDetail registrationDetail;

    Patient(int id) {
        this.id = id;
    }

    public List<VaccinationDetail> getPreviousVaccinationDetails() {
        return vaccinationList;
    }

    public RegistrationDetail getUpcomingVaccinationDate() {
        return registrationDetail;
    }

    public RegistrationDetail registerNextDose(VaccineType vaccineType, LocalDateTime slot, int vaccinationCentreId) {
        this.registrationDetail = new RegistrationDetail(vaccineType, slot, vaccinationCentreId);
        return registrationDetail;
    }
}

class Doctor {
    private final int id;
    private int vaccinationCentreId;

    Doctor(int id) {
        this.id = id;
    }

    private Patient _getPatientById(int patientId) {
        Patient patient = VaccineCentreManagement.getInstance().getPatient(patientId);
        return patient;
    }

    public List<VaccinationDetail> getPatientVaccinationDetails(int patientId) {
        Patient patient = _getPatientById(patientId);
        return patient.getPreviousVaccinationDetails();
    }

    public RegistrationDetail getPatientBooking(int patientId) {
        Patient patient = _getPatientById(patientId);
        return patient.getUpcomingVaccinationDate();
    }

    public VaccinationDetail administerVaccine(VaccineType vaccineType, int patientId) {
        List<VaccinationDetail> vaccinationDetails = getPatientVaccinationDetails(patientId);
        VaccinationDetail lastVaccine = vaccinationDetails.get(vaccinationDetails.size()-1);
        VaccinationDetail vaccinationDetail = new VaccinationDetail(vaccineType, lastVaccine.getDoseNumber()+1, id, LocalDateTime.now(), vaccinationCentreId);
        vaccinationDetails.add(vaccinationDetail);
        return vaccinationDetail;
    }
}

class Staff {
    private final int id;
    private int vaccinationCentreId;

    Staff(int id) {
        this.id = id;
    }

    public int addVaccineStock(VaccineType vaccineType, int numberOfDoses) {
        VaccinationCentre vaccinationCentre = VaccineCentreManagement.getInstance().getVaccinationCentre(vaccinationCentreId);
        return vaccinationCentre.addToInventory(vaccineType, numberOfDoses);
    }

    public List<Patient> getTodaysPatients() {
        List<Patient> patientList = VaccineCentreManagement.getInstance().getRegisteredPatients(LocalDate.now(), null);
        return patientList;
    }

    public int getRequiredVaccineDose(VaccineType vaccineType, LocalDate date) {
        List<Patient> patientList = VaccineCentreManagement.getInstance().getRegisteredPatients(date, vaccineType);
        return patientList.size();
    }

    public String buildVaccineCertificate(int patientId) {
        Patient patient = VaccineCentreManagement.getInstance().getPatient(patientId);
        List<VaccinationDetail> vaccinationDetails= patient.getPreviousVaccinationDetails();
        Gson gson = new Gson();
        return gson.toJson(vaccinationDetails);
    }
}

public class VaccineCentreManagement {
    private final Map<Integer, VaccinationCentre> vaccinationCentres = new HashMap<>();
    private final Map<Integer, Patient> patients = new HashMap<>();

    private VaccineCentreManagement(){}

    private static class VaccineCentreManagementHelper {
        private final static VaccineCentreManagement instance = new VaccineCentreManagement();
    }

    public static VaccineCentreManagement getInstance() {
        return VaccineCentreManagementHelper.instance;
    }

    public Patient getPatient(int patientId) {
        return patients.get(patientId);
    }

    public VaccinationCentre getVaccinationCentre(int vaccinationCentreId) {
        return vaccinationCentres.get(vaccinationCentreId);
    }

    public List<Patient> getRegisteredPatients(LocalDate date, VaccineType vaccineType) {
        List<Patient> patientList = new ArrayList<>();
        for (Patient p: patients.values()) {
            RegistrationDetail registrationDetail = p.getUpcomingVaccinationDate();
            if (registrationDetail != null) {
                LocalDate scheduledDate = registrationDetail.getScheduledTime().toLocalDate();
                if (date.equals(scheduledDate)) {
                    if (vaccineType==null || vaccineType.equals(registrationDetail.getVaccineType()))
                        patientList.add(p);
                }
            }
        }
        return patientList;
    }
}
