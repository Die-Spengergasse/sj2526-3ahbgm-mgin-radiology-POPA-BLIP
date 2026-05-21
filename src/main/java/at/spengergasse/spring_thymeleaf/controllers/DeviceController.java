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
public class DeviceController
{
    private final PatientRepository patientRepository;
    private final DeviceRepository deviceRepository;
    private final ReservationRepository reservationRepository;

    public DeviceController(PatientRepository patientRepository, DeviceRepository deviceRepository, ReservationRepository reservationRepository)
    {
        this.patientRepository = patientRepository;
        this.deviceRepository = deviceRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/device/new")
    public String newDevice(Model model)
    {        model.addAttribute("device", new Device());
        return "newDevice";
    }

    @PostMapping("/device/new")
    public String saveDevice(@ModelAttribute Device device, Model model)
    {
        if (device.getName() == null || device.getName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Gerätename ist erforderlich.");
            model.addAttribute("device", device);
            return "newDevice";
        }
        if (device.getType() == null || device.getType().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Gerätetyp ist erforderlich.");
            model.addAttribute("device", device);
            return "newDevice";
        }
        deviceRepository.save(device);
        return "redirect:/reservation/new";
    }



}




