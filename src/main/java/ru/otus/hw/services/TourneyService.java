package ru.otus.hw.services;

import ru.otus.hw.models.entities.Tourney;

import java.util.List;
import java.util.Optional;

public interface TourneyService {
    List<Tourney> findAll();

    Optional<Tourney> findById(Long id);
}
