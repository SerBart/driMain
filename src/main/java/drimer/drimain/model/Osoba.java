package drimer.drimain.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Osoba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imieNazwisko;

    @OneToMany(mappedBy = "osoba")
    private List<Raport> raporty;

    // Gettery i settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImieNazwisko() { return imieNazwisko; }
    public void setImieNazwisko(String imieNazwisko) { this.imieNazwisko = imieNazwisko; }
    public List<Raport> getRaporty() { return raporty; }
    public void setRaporty(List<Raport> raporty) { this.raporty = raporty; }
}
