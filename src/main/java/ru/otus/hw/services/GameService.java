package ru.otus.hw.services;

import ru.otus.hw.models.entities.Competition;
import ru.otus.hw.models.entities.Game;
import ru.otus.hw.models.entities.Tourney;
import ru.otus.hw.models.entities.TourneyPlayer;

import java.util.List;
import java.util.Optional;

public interface GameService {

    List<Game> findAll();

    Optional<Game> findById(Long id);

    boolean checkTourRange(Integer tour, Integer range);

    List<Game> findByTourneyPlayers(List<TourneyPlayer> players, Integer tour, List<Game> previousTourGames);

    List<Game> generateByTourneyPlayers(List<TourneyPlayer> players, Integer tour, List<Game> previousTourGames);
}
