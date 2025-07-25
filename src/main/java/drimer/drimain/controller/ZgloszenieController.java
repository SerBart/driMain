package drimer.drimain.controller;
import org.springframework.beans.factory.annotation.Autowired;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.repository.*;
import drimer.drimain.service.RaportService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
@Controller
public class ZgloszenieController {


    @Autowired
    private ZgloszenieRepository zgloszenieRepository;


    @GetMapping("/zgloszenia")
    public String zgloszenia(Model model) {
        // Usunięto sprawdzanie sesji i isLoggedIn – zawsze dostępny
        boolean isAdmin = true; // Domyślnie admin, skoro bez logowania
        List<Zgloszenie> zgloszenia = zgloszenieRepository.findAll(); // Zawsze pokazuje wszystkie

        model.addAttribute("isLoggedIn", true);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("zgloszenia", zgloszenia);
        return "zgloszenia";
    }

    @PostMapping("/zgloszenia/add")
    public String addZgloszenie(@RequestParam String imie,
                                @RequestParam String nazwisko,
                                @RequestParam String typ,
                                @RequestParam String dataGodzina,
                                @RequestParam String opis) {
        // Usunięto sprawdzanie sesji
        Zgloszenie zgloszenie = new Zgloszenie();
        zgloszenie.setImie(imie);
        zgloszenie.setNazwisko(nazwisko);
        zgloszenie.setTyp(typ);
        zgloszenie.setDataGodzina(LocalDateTime.parse(dataGodzina));
        zgloszenie.setOpis(opis);

        zgloszenieRepository.save(zgloszenie);
        return "redirect:/zgloszenia";
    }
    // Edycja zgłoszenia (GET dla formularza w modalu)
    @GetMapping("/zgloszenia/edit/{id}")
    public String editZgloszenieForm(@PathVariable Long id, Model model) {
        Zgloszenie zgloszenie = zgloszenieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        model.addAttribute("zgloszenie", zgloszenie);
        return "zgloszenia :: editForm"; // Zwróć fragment Thymeleaf (dla modala)
    }

    // Zapisz edycję (POST)
    @PostMapping("/zgloszenia/edit")
    public String saveEditedZgloszenie(@RequestParam Long id,
                                       @RequestParam String imie,
                                       @RequestParam String nazwisko,
                                       @RequestParam String typ,
                                       @RequestParam String dataGodzina,
                                       @RequestParam String opis) {
        Zgloszenie zgloszenie = zgloszenieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        zgloszenie.setImie(imie);
        zgloszenie.setNazwisko(nazwisko);
        zgloszenie.setTyp(typ);
        zgloszenie.setDataGodzina(LocalDateTime.parse(dataGodzina));
        zgloszenie.setOpis(opis);
        zgloszenieRepository.save(zgloszenie);
        return "redirect:/zgloszenia";
    }

    // Usunięcie zgłoszenia (POST)
    @PostMapping("/zgloszenia/delete")
    public String deleteZgloszenie(@RequestParam Long id) {
        zgloszenieRepository.deleteById(id);
        return "redirect:/zgloszenia";
    }
}
