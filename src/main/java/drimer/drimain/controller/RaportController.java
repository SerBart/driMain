package drimer.drimain.controller;

import drimer.drimain.service.RaportService; // Dodaj ten import!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RaportController {

    @Autowired
    private RaportService raportService; // Teraz powinno działać


    // Endpoint do usuwania
    @GetMapping("/raport/usun/{id}")
    public String usunRaport(@PathVariable Long id) {
        raportService.usun(id); // Wywołanie serwisu
        return "redirect:/"; // Przekierowanie na listę po usunięciu
    }

    // Inne endpointy, np. /raport/nowy, /raport/edytuj/{id}
}
