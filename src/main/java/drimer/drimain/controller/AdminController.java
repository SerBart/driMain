package drimer.drimain.controller;

import drimer.drimain.model.Dzial;
import drimer.drimain.model.Maszyna;
import drimer.drimain.model.Osoba;
import drimer.drimain.model.Raport;
import drimer.drimain.repository.DzialRepository;
import drimer.drimain.repository.MaszynaRepository;
import drimer.drimain.repository.OsobaRepository;
import drimer.drimain.repository.RaportRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("adminLoggedIn") == null) {
            return "redirect:/login";  // jeśli nie zalogowany, do logowania
        }
        model.addAttribute("raporty", raportRepository.findAll());
        model.addAttribute("isAdmin", true); // teraz zawsze admin
        return "index"; // strona z listą raportów
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/admin/dodaj-dzial")
    public String dodajDzial(@RequestParam String nazwa, HttpSession session) {
        if (session.getAttribute("adminLoggedIn") == null) {
            return "redirect:/login";
        }
        Dzial dzial = new Dzial();
        dzial.setNazwa(nazwa);
        dzialRepository.save(dzial);
        return "redirect:/admin";
    }

    @PostMapping("/login")
    public String login(@RequestParam String haslo, HttpSession session) {
        if ("admin123".equals(haslo)) {
            session.setAttribute("adminLoggedIn", true);
            // Zamiast do /admin, przekieruj na główną
            return "redirect:/";
        }
        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String adminPanel(Model model, HttpSession session) {
        if (session.getAttribute("adminLoggedIn") == null) {
            return "redirect:/login";
        }
        model.addAttribute("maszyny", maszynaRepository.findAll());
        model.addAttribute("osoby", osobaRepository.findAll());
        model.addAttribute("dzialy", dzialRepository.findAll());
        return "admin";
    }

    @PostMapping("/admin/dodaj-maszyna")
    public String dodajMaszyna(@RequestParam String nazwa, @RequestParam Long dzialId, HttpSession session) {
        if (session.getAttribute("adminLoggedIn") == null) {
            return "redirect:/login";
        }
        // Znajdź dział po ID
        Optional<Dzial> optionalDzial = dzialRepository.findById(dzialId);
        if (!optionalDzial.isPresent()) {
            // Możesz tu dodać błąd lub przekierowanie z komunikatem
            throw new IllegalArgumentException("Nie znaleziono działu o ID: " + dzialId);
        }
        Dzial dzial = optionalDzial.get();

        // Utwórz maszynę i ustaw relację
        Maszyna maszyna = new Maszyna();
        maszyna.setNazwa(nazwa);
        maszyna.setDzial(dzial);  // Ustaw powiązanie z działem
        maszynaRepository.save(maszyna);
        return "redirect:/admin";
    }

    @PostMapping("/admin/dodaj-osoba")
    public String dodajOsoba(@RequestParam String imieNazwisko, HttpSession session) {
        if (session.getAttribute("adminLoggedIn") == null) {
            return "redirect:/login";
        }
        Osoba osoba = new Osoba();
        osoba.setImieNazwisko(imieNazwisko);
        osobaRepository.save(osoba);
        return "redirect:/admin";
    }

    @GetMapping("/raport/nowy")
    public String nowyRaportForm(Model model) {
        model.addAttribute("raport", new Raport());
        model.addAttribute("dzialy", dzialRepository.findAll());
        model.addAttribute("maszyny", maszynaRepository.findAll());
        model.addAttribute("osoby", osobaRepository.findAll());
        return "raport_form";
    }

    @GetMapping("/raport/edytuj/{id}")
    public String edytujRaportForm(@PathVariable Long id, Model model) {
        Raport raport = raportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        model.addAttribute("raport", raport);
        model.addAttribute("maszyny", maszynaRepository.findAll());
        model.addAttribute("osoby", osobaRepository.findAll());
        model.addAttribute("dzialy", dzialRepository.findAll()); // dodaj tę linię!
        return "raport_form"; // albo nazwa Twojego szablonu
    }

    @PostMapping("/raport/zapisz")
    public String zapiszRaport(@RequestParam(required = false) Long id,
                               @RequestParam Long maszynaId,
                               @RequestParam String typNaprawy,
                               @RequestParam String opis,
                               @RequestParam Long osobaId,
                               @RequestParam String status,
                               @RequestParam String dataNaprawy,
                               @RequestParam String czasOd,
                               @RequestParam String czasDo) {
        Raport raport = id != null ? raportRepository.findById(id).orElse(new Raport()) : new Raport();
        raport.setMaszyna(maszynaRepository.findById(maszynaId).orElse(null));
        raport.setTypNaprawy(typNaprawy);
        raport.setOpis(opis);
        raport.setOsoba(osobaRepository.findById(osobaId).orElse(null));
        raport.setStatus(status);
        raport.setDataNaprawy(LocalDate.parse(dataNaprawy));
        raport.setCzasOd(LocalTime.parse(czasOd));
        raport.setCzasDo(LocalTime.parse(czasDo));
        raportRepository.save(raport);
        return "redirect:/";
    }

}
