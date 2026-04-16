package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
    public class DeviceController {

        private final PatientRepository patientRepository;
        private final DeviceRepository deviceRepository;
        private final ReservationRepository reservationRepository;

        public DeviceController(PatientRepository patientRepository, DeviceRepository deviceRepository, ReservationRepository reservationRepository)
        {
            this.patientRepository = patientRepository;
            this.deviceRepository = deviceRepository;
            this.reservationRepository = reservationRepository;
        }


        @GetMapping("/newPatient")
        public String newPatient(Model model)
        {
            model.addAttribute("patient", new Patient());
            return "newPatient";
        }

        @PostMapping("/newPatient")
        public String savePatient(@ModelAttribute Patient patient)
        {
            patientRepository.save(patient);
            return "redirect:/newReservation";
        }


        @GetMapping("/newReservation")
        public String newReservation(Model model)
        {
            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("devices", deviceRepository.findAll());
            model.addAttribute("reservation", new Reservation());
            return "newReservation";
        }

        @PostMapping("/newReservation")
        public String saveReservation(@ModelAttribute Reservation reservation)
        {
            reservationRepository.save(reservation);
            return "redirect:/deviceReservations";
        }


        @GetMapping("/deviceReservations")
        public String deviceReservations(Model model)
        {
            model.addAttribute("reservations", reservationRepository.findAll());
            return "deviceReservations";
        }
    }


