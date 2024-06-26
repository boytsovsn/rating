package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entities.TourneyPlayer;

import java.util.List;

public interface TourneyPlayerRepository extends CrudRepository<TourneyPlayer, Long> {

    List<TourneyPlayer> findAll();
}

