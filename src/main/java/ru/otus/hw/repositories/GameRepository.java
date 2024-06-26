package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entities.Game;

import java.util.List;


public interface GameRepository extends CrudRepository<Game, Long> {
    List<Game> findAll();
}
