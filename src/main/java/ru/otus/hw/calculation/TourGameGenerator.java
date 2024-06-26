package ru.otus.hw.calculation;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.entities.Game;
import ru.otus.hw.models.entities.TourneyPlayer;

import java.util.List;


public interface TourGameGenerator {

    List<Game> generateTourGames(List<TourneyPlayer> players, Integer tour, List<Game> previousTourGames);

}
