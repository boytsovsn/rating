package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entities.Competition;

import java.util.List;

public interface CompetitionRepository extends CrudRepository<Competition, Long> {
    List<Competition> findAll();
}
