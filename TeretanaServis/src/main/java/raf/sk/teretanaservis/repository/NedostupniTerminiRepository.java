package raf.sk.teretanaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.sk.teretanaservis.domain.NedostupniTermini;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface NedostupniTerminiRepository extends JpaRepository<NedostupniTermini, Long> {
    Optional<NedostupniTermini> findNedostupniTerminiByTermin(LocalDateTime termin);
}
