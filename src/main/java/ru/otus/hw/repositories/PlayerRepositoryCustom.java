package ru.otus.hw.repositories;

import ru.otus.hw.models.entities.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerRepositoryCustom {

    List<Player> findAll();


    Optional<Player> findById(Long id);

    void resetSequence();
}
