package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    private final PatientRepository patientRepository;
    private final DeviceRepository deviceRepository;
    private final ReservationRepository reservationRepository;

    public ReservationController(PatientRepository patientRepository,
                                 DeviceRepository deviceRepository,
                                 ReservationRepository reservationRepository) {
        this.patientRepository = patientRepository;
        this.deviceRepository = deviceRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/new")
    public String newReservation(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll()); // ← das hat gefehlt
        return "newReservation";
    }

    @PostMapping("/new")
    public String saveReservation(@ModelAttribute Reservation reservation, Model model) {
        LocalDate date  = reservation.getDate();
        String timeFrom = reservation.getTimeFrom();
        String timeTo   = reservation.getTimeTo();
        Patient patient = reservation.getPatient();
        Device device   = reservation.getDevice();

        if (date != null && date.isBefore(LocalDate.now())) {
            model.addAttribute("errorMessage", "Datum darf nicht in der Vergangenheit liegen.");
            return prepareForm(model, reservation);
        }
        if (timeFrom == null || timeTo == null || timeFrom.isBlank() || timeTo.isBlank()) {
            model.addAttribute("errorMessage", "Bitte gültige Zeiten angeben.");
            return prepareForm(model, reservation);
        }
        if (device != null) {
            List<Reservation> existing = reservationRepository.findByDeviceIdAndDate(device.getId(), date);
            if (overlapsTime(existing, timeFrom, timeTo)) {
                model.addAttribute("errorMessage", "Gerät ist in diesem Zeitraum bereits reserviert.");
                return prepareForm(model, reservation);
            }
        }
        if (patient != null) {
            List<Reservation> existing = reservationRepository.findByPatientIdAndDate(patient.getId(), date);
            if (overlapsTime(existing, timeFrom, timeTo)) {
                model.addAttribute("errorMessage", "Patient hat in diesem Zeitraum bereits einen Termin.");
                return prepareForm(model, reservation);
            }
        }

        try {
            reservationRepository.save(reservation);
        } catch (DataAccessException e) {
            model.addAttribute("errorMessage", "Datenbankfehler: Verbindung fehlgeschlagen.");
            return prepareForm(model, reservation);
        }
        return "redirect:/reservation/list";
    }

    @GetMapping("/list")
    public String listReservations(@RequestParam(required = false) Long deviceId, Model model) {
        model.addAttribute("devices", deviceRepository.findAll());
        if (deviceId != null) {
            model.addAttribute("reservations", reservationRepository.findByDeviceId(deviceId));
            model.addAttribute("selectedDeviceId", deviceId);
        } else {
            model.addAttribute("reservations", reservationRepository.findAll());
        }
        return "reservation_list";
    }

    private String prepareForm(Model model, Reservation reservation) {
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        model.addAttribute("reservation", reservation);
        return "newReservation";
    }

    private boolean overlapsTime(List<Reservation> existing, String timeFrom, String timeTo) {
        for (Reservation r : existing) {
            if (timeFrom.compareTo(r.getTimeTo()) < 0 && timeTo.compareTo(r.getTimeFrom()) > 0) {
                return true;
            }
        }
        return false;
    }
}