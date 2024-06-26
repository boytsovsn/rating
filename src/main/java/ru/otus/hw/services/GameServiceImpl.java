package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.calculation.TourGameGenerator;
import ru.otus.hw.models.dto.TourneyType;
import ru.otus.hw.models.entities.Game;
import ru.otus.hw.models.entities.Tourney;
import ru.otus.hw.models.entities.TourneyPlayer;
import ru.otus.hw.repositories.GameRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    private final TourGameGenerator tourGameGeneratorCircle;

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }
    @Override
    public Optional<Game> findById(Long id) {
        return gameRepository.findById(id);
    }

    @Override
    public List<Game> findByTourneyPlayers(List<TourneyPlayer> players, Integer tour, List<Game> previousTourGames) {
        List<Game> tourGames = new ArrayList<>();
        if (players != null && !players.isEmpty()) {
            if (players.get(0) != null && players.get(0).getTourney() != null) {
                Tourney tourney = players.get(0).getTourney();
                tourGames = em.createQuery("select g from Game g left join fetch g.tourney t " +
                                "left join fetch g.tourneyPlayer1 p1 left join fetch g.tourneyPlayer2 p2 " +
                                "where t.id = :tid and g.tour = :tour")
                        .setParameter("tid", tourney.getId())
                        .setParameter("tour", tour)
                        .getResultList();
            }
        }
        return tourGames;
    }

    @Override
    public boolean checkTourRange(Integer tour, Integer range) {
        if (range % 2 == 0) {
            return tour <  range;
        } else {
            return tour <= range;
        }
    }

    @Override
    public List<Game> generateByTourneyPlayers(List<TourneyPlayer> players, Integer tour, List<Game> previousTourGames) {
        List<Game> savedTourGames = new ArrayList<>();
        if (tour >= 1 && checkTourRange(tour, players.size())) {
            if (players != null && !players.isEmpty()) {
                if (players.get(0) != null && players.get(0).getTourney() != null &&
                        players.get(0).getTourney().getType() == TourneyType.Circle.ordinal()) {
                    // игра вкруг
                    List<Game> currentGames = findByTourneyPlayers(players, tour, previousTourGames);
                    if (currentGames.isEmpty()) {
                        List<Game> tourGames = tourGameGeneratorCircle.generateTourGames(players, tour, previousTourGames);
                        savedTourGames = (List<Game>) gameRepository.saveAll(tourGames);
                    } else if (players.size() >= 2*currentGames.size()) {
                        savedTourGames = currentGames;
                    }
                } else if (players.get(0) != null && players.get(0).getTourney() != null &&
                        players.get(0).getTourney().getType() == TourneyType.Net.ordinal()) {
                    if (tour >= 1 && tour <= Math.log(players.size()) / Math.log(2)) {
                        //TODO: игра по сетке
                    }
                }
            }
        }
        return savedTourGames;
    }
}
