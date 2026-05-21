package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.Patient;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
public class PatientController {
    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/patient/new";
    }

    @GetMapping("/patient/new")
    public String newPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "newPatient";
    }

    @PostMapping("/patient/new")
    public String savePatient(@ModelAttribute Patient patient, Model model) {
        if (patient.getBirthDate() != null && patient.getBirthDate().isAfter(LocalDate.now())) {
            model.addAttribute("errorMessage", "Geburtsdatum darf nicht in der Zukunft liegen.");
            model.addAttribute("patient", patient);
            return "newPatient";
        }
        if (patient.getSocialSecurityNumber() == null || patient.getSocialSecurityNumber().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Sozialversicherungsnummer ist erforderlich.");
            model.addAttribute("patient", patient);
            return "newPatient";
        }
        if (patient.getSocialSecurityNumber().length() < 10 || patient.getSocialSecurityNumber().length() > 12) {
            model.addAttribute("errorMessage", "Ungültige Sozialversicherungsnummer (10–12 Zeichen).");
            model.addAttribute("patient", patient);
            return "newPatient";
        }
        patientRepository.save(patient);
        return "redirect:/reservation/new";
    }
}