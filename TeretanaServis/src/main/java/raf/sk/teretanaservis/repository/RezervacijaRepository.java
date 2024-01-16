package raf.sk.teretanaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.sk.teretanaservis.domain.Rezervacija;
import raf.sk.teretanaservis.domain.Trening;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {
    Optional<Rezervacija> findRezervacijaByTermin(LocalDateTime termin);
    List<Rezervacija> findAllByUserId(Long id);
    List<Rezervacija> findAllByTrening(Trening t);

    List<Rezervacija> findAllByTreningAndTermin(Trening trening, LocalDateTime termin);
}
