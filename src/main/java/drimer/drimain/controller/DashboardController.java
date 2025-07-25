package drimer.drimain.controller;  // Dostosuj do swojego pakietu (musi być skanowany przez Spring)

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Twój szablon z kafelkami
    }

}
