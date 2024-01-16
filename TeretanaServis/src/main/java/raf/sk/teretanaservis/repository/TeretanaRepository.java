package raf.sk.teretanaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.sk.teretanaservis.domain.Teretana;

import java.util.Optional;

@Repository
public interface TeretanaRepository extends JpaRepository<Teretana, Long> {
    Optional<Teretana> findTeretanaById(Long id);
}
