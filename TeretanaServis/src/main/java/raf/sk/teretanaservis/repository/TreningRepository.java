package raf.sk.teretanaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.sk.teretanaservis.domain.Teretana;
import raf.sk.teretanaservis.domain.Trening;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreningRepository extends JpaRepository<Trening, Long> {
    List<Trening> findAllByTeretana(Teretana teretana);
    Optional<Trening> findTreningById(Long id);
}
