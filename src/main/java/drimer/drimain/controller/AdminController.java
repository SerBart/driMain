package drimer.drimain.controller;

import drimer.drimain.model.*;
import drimer.drimain.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private MaszynaRepository maszynaRepository;

    @Autowired
    private OsobaRepository osobaRepository;

    @Autowired
    private DzialRepository dzialRepository;

    @Autowired
    private RaportRepository raportRepository;

    @Autowired
    private ZgloszenieRepository zgloszenieRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Wstrzyknij enkoder
    // Usunięto /login i /logout – nie potrzebne bez logowania

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("osoba", new Osoba());
        return "register"; // Stwórz szablon Thymeleaf register.html
    }

    // Obsługa rejestracji (POST)
    @PostMapping("/register")
    public String register(@ModelAttribute Osoba osoba,
                           @RequestParam String hasloPotwierdz) { // Dodatkowe pole na potwierdzenie hasła
        if (!osoba.getHaslo().equals(hasloPotwierdz)) {
            // Błąd: hasła nie pasują – dodaj obsługę błędów
            return "register";
        }
        osoba.setHaslo(passwordEncoder.encode(osoba.getHaslo())); // Zahashuj
        osobaRepository.save(osoba);
        return "redirect:/admin"; // Przekieruj po sukcesie
    }

    @GetMapping("/admin")
    public String adminPanel(Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        // Usunięto sprawdzanie sesji – zawsze dostępny
        model.addAttribute("maszyny", maszynaRepository.findAll());
        model.addAttribute("osoby", osobaRepository.findAll());
        model.addAttribute("dzialy", dzialRepository.findAll());
        model.addAttribute("isLoggedIn", true); // Domyślnie true, jeśli potrzebne w template
        return "admin";
    }

    @PostMapping("/admin/dodaj-maszyna")
    public String dodajMaszyna(@RequestParam String nazwa, @RequestParam Long dzialId) {
        // Usunięto sprawdzanie sesji – zawsze dostępny
        Optional<Dzial> optionalDzial = dzialRepository.findById(dzialId);
        if (!optionalDzial.isPresent()) {
            throw new IllegalArgumentException("Department not found with ID: " + dzialId);
        }
        Dzial dzial = optionalDzial.get();

        Maszyna maszyna = new Maszyna();
        maszyna.setNazwa(nazwa);
        maszyna.setDzial(dzial);
        maszynaRepository.save(maszyna);
        return "redirect:/admin";
    }

    @PostMapping("/admin/dodaj-osoba")
    public String dodajOsoba(@RequestParam String imieNazwisko) {
        // Usunięto sprawdzanie sesji
        Osoba osoba = new Osoba();
        osoba.setImieNazwisko(imieNazwisko);
        osobaRepository.save(osoba);
        return "redirect:/admin";
    }



}
