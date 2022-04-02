package com.bootcamp.ondemandreservation.ServiceImplementation;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Repository.DoctorRepository;
import com.bootcamp.ondemandreservation.Service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DoctorServiceImplementation implements DoctorService {

    private DoctorRepository doctorRepository;


    public DoctorServiceImplementation(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    /**
     * Save doctor method
     * @param doctor doctor to save
     * @return Doctor as saved in the DB, nul;l if error
     */
    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findDoctorById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return doctor.get();
    }

    /**
     * @param id passed from Controller to find specific doctor.
     *  To delete a Doctor object, doctor can not have any relations to other tables.
     *  This method removes relations to any Appointment list that a doctor might have relations to.
     *  Deletes doctor.
     */

    @Override
    public void deleteDoctor(Long id) {
        Doctor currentDoctor = findDoctorById(id);
        List<Appointment> currentList = currentDoctor.getAppointmentList();
        for (Appointment appointment: currentList) {
            appointment.removeDoctor();
        }
        doctorRepository.deleteById(id);
    }

    @Override
    public List<Appointment> getAllAppointments(Long id) {
        Doctor doctor = findDoctorById(id);
        return doctor.getAppointmentList();
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        doctor.setId(id);
        return saveDoctor(doctor);
    }
}
