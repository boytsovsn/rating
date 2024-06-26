package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entities.Tourney;

import java.util.List;


public interface TourneyRepository extends CrudRepository<Tourney, Long> {

    List<Tourney> findAll();
}
