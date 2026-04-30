package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DeviceController {

    private final PatientRepository patientRepository;
    private final DeviceRepository deviceRepository;
    private final ReservationRepository reservationRepository;

    public DeviceController(
            PatientRepository patientRepository,
            DeviceRepository deviceRepository,
            ReservationRepository reservationRepository) {
        this.patientRepository = patientRepository;
        this.deviceRepository = deviceRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/newPatient")
    public String newPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "newPatient";
    }

    @PostMapping("/newPatient")
    public String savePatient(@ModelAttribute Patient patient, Model model) {
        LocalDate birthDate = patient.getBirthDate();
        String socialSecurityNumber = patient.getSocialSecurityNumber();

        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            model.addAttribute("errorMessage", "Geburtsdatum darf nicht in der Zukunft liegen.");
            model.addAttribute("patient", patient);
            return "newPatient";
        }

        if (socialSecurityNumber == null || socialSecurityNumber.trim().isEmpty())
        {
            model.addAttribute("errorMessage", "Sozialversicherungsnummer ist erforderlich.");
            model.addAttribute("patient", patient);
            return "newPatient";
        }
        if (socialSecurityNumber.length() < 10 || socialSecurityNumber.length() > 12)
        {
            model.addAttribute("errorMessage", "Ungültige Sozialversicherungsnummer (10–12 Zeichen).");
            model.addAttribute("patient", patient);
            return "newPatient";
        }

        patientRepository.save(patient);
        return "redirect:/newReservation";
    }

    @GetMapping("/newReservation")
    public String newReservation(Model model) {
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        model.addAttribute("reservation", new Reservation());
        return "newReservation";
    }

    @PostMapping("/newReservation")
    public String saveReservation(@ModelAttribute Reservation reservation, Model model)
    {
        LocalDate date = reservation.getDate();
        String timeFrom = reservation.getTimeFrom();
        String timeTo   = reservation.getTimeTo();
        Patient patient = reservation.getPatient();
        Device  device  = reservation.getDevice();

        if (date != null && date.isBefore(LocalDate.now()))
        {
            model.addAttribute("errorMessage", "Reservierung darf nicht für einen vergangenen Zeitpunkt erfolgen.");
            return prepareNewReservation(model, reservation);
        }

        if (device != null)
        {
            String deviceId = device.getId();
            List<Reservation> existingDevice = reservationRepository.findByDeviceIdAndDate(deviceId, date);
            if (overlapsTime(existingDevice, timeFrom, timeTo))
            {
                model.addAttribute("errorMessage", "Das Gerät ist in diesem Zeitraum bereits reserviert.");
                return prepareNewReservation(model, reservation);
            }
        }

        if (patient != null)
        {
            Integer patientId = patient.getid();
            List<Reservation> existingPatient = reservationRepository.findByPatientIdAndDate(patientId, date);
            if (overlapsTime(existingPatient, timeFrom, timeTo))
            {
                model.addAttribute("errorMessage", "Der Patient hat in diesem Zeitraum bereits einen Termin.");
                return prepareNewReservation(model, reservation);
            }
        }
        reservationRepository.save(reservation);
        return "redirect:/deviceReservations";
    }

    private String prepareNewReservation(Model model, Reservation reservation)
    {
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        model.addAttribute("reservation", reservation);
        return "newReservation";
    }

    private boolean overlapsTime(List<Reservation> existing, String timeFrom, String timeTo)
    {
        for (Reservation r : existing)
        {
            String existingFrom = r.getTimeFrom();
            String existingTo   = r.getTimeTo();
            if (timeFrom.compareTo(existingTo) < 0 && timeTo.compareTo(existingFrom) > 0)
            {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/deviceReservations")
    public String deviceReservations(Model model) {
        model.addAttribute("reservations", reservationRepository.findAll());
        return "deviceReservations";
    }
}