package ru.otus.hw.models;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entities.Tourney;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.models.entities.Game;
import ru.otus.hw.models.entities.Rating;
import ru.otus.hw.repositories.TourneyRepository;
import ru.otus.hw.repositories.PlayerRepository;
import ru.otus.hw.repositories.GameRepository;
import ru.otus.hw.repositories.RatingRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@ComponentScan({"ru.otus.hw.models", "ru.otus.hw.repositories"})
public class AllEntitiesModelImpl implements AllEntitiesModel {

    //Количество тестов и наборов данных для тестирования
    //в мапах под индексом testsCount, находятся предопределёные сущности БД
    //для тестирования Secutity ACL
    private final int testsCount = 4;

    //Начальное Id для сущностей тестовых наборов данных (нужно для того, чтобы не затереть предопределённые сущности в БД,
    //которые нужны для тестирования Security ACL и связаны с таблицами ACL в тестовой БД)
    private final int testsIdStarts = 10;

    private Map<Integer, List<Tourney>> tourneys;

    private Map<Integer, List<Game>> games;

    private Map<Integer, List<Rating>> ratings;

    private Map<Integer, List<Player>> players;

    @Autowired
    TourneyRepository tourneyRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    RatingRepository ratingRepository;


    @Override
    public void setTourneys(Map<Integer, List<Tourney>> tourneys) {
        this.tourneys = tourneys;
    }

    @Override
    public void setGames(Map<Integer, List<Game>> games) {
        this.games = games;
    }

    @Override
    public void setRatings(Map<Integer, List<Rating>> ratings) {
        this.ratings = ratings;
    }

    @Override
    public void setPlayers(Map<Integer, List<Player>> players) {
        this.players = players;
    }

    @Override
    public Map<Integer, List<Tourney>> getTourneys() {
        return tourneys;
    }

    @Override
    public Map<Integer, List<Game>> getGames() {
        return games;
    }

    @Override
    public Map<Integer, List<Rating>> getRatings() {
        return ratings;
    }

    @Override
    public Map<Integer, List<Player>> getPlayers() {
        return players;
    }

    public void init(int nTest) {
        deleteAllAndInitACLTestData();
        if (nTest<testsCount) {
//            initTourney(nTest);
//            initGame(nTest);
            initPlayers(nTest);
            initRatings(nTest);
            initPlayerRating(nTest);
        } else if (nTest==testsCount) {     //ACL Security test
            initRatings(nTest);
            initPlayerRating(nTest);
        }
    }

    //Удаление всех сущностей в БД, кроме предопределённых,
    //которые нужны для тестирования Security ACL и связаны с таблицами ACL в тестовой БД.
    //ACL тестовые данные записываются в мапы под индексом testsCount
    public void deleteAllAndInitACLTestData() {
        int endAclDataIndex=4;
        Map<Integer, List<Rating>> ratingsMap = new HashMap<Integer, List<Rating>>();
        setRatings(ratingsMap);
        ratingRepository.deleteAll();
        Map<Integer, List<Player>> playersMap = new HashMap<Integer, List<Player>>();
        setPlayers(playersMap);
        List<Player> players=playerRepository.findAll();
        List<Player> aclPlayers=players.stream().filter(x->x.getId()<endAclDataIndex).toList();
        getPlayers().put(getTestsCount(), aclPlayers);
        players = players.stream().filter(x->x.getId()>=endAclDataIndex).toList();
        playerRepository.deleteAll(players);
        Map<Integer, List<Tourney>> tourneysMap = new HashMap<Integer, List<Tourney>>();
        setTourneys(tourneysMap);
        List<Tourney> tourneys = tourneyRepository.findAll();
        List<Tourney> aclTourneys = tourneys.stream().filter(x->x.getId()<endAclDataIndex).toList();
        getTourneys().put(getTestsCount(), aclTourneys);
        tourneys = tourneys.stream().filter(x->x.getId()>=endAclDataIndex).toList();
        tourneyRepository.deleteAll(tourneys);
        Map<Integer, List<Game>> gamesMap = new HashMap<Integer, List<Game>>();
        setGames(gamesMap);
        List<Game> games=gameRepository.findAll();
        List<Game> aclGames=games.stream().filter(x->x.getId()<endAclDataIndex).toList();
        getGames().put(getTestsCount(), aclGames);
        games = games.stream().filter(x->x.getId()>=endAclDataIndex).toList();
        gameRepository.deleteAll(games);
        playerRepository.resetSequence();
    }

    public void initTourney(Integer i) {
        List<Tourney> tourneys = new ArrayList<>();
        tourneys.add(tourneyRepository.save(new Tourney(null,"Tourney_1")));
        tourneys.add(tourneyRepository.save(new Tourney(null,"Tourney_2")));
        tourneys.add(tourneyRepository.save(new Tourney(null,"Tourney_3")));
        getTourneys().put(i, tourneys);
    }

//    public void initGame(Integer i) {
//        List<Game> games = new ArrayList<>();
//        games.add(gameRepository.save(new Game(null,1)));
//        games.add(gameRepository.save(new Game(null,2)));
//        games.add(gameRepository.save(new Game(null,3)));
//        getGames().put(i, games);
//    }

    public void initPlayers(Integer i) {
        List<Player> players = new ArrayList<>();
        List<Tourney> tourneys = getTourneys().get(i);
        List<Game> games = getGames().get(i);
        players.add(playerRepository.save(new Player(null, "PlayerTitle_1", null)));
        players.add(playerRepository.save(new Player(null, "PlayerTitle_2", null)));
        players.add(playerRepository.save(new Player(null, "PlayerTitle_3", null)));
        getPlayers().put(i, players);
    }

    public void initRatings(Integer i) {
        List<Rating> ratings = new ArrayList<>();
        List<Player> players = getPlayers().get(i);
        ratings.add(ratingRepository.save(new Rating(null, 2011, 1, 1, players.get(0))));
        ratings.add(ratingRepository.save(new Rating(null, 2021, 2, 1, players.get(1))));
        ratings.add(ratingRepository.save(new Rating(null, 2022, 2, 2, players.get(1))));
        ratings.add(ratingRepository.save(new Rating(null, 2031, 3, 1, players.get(2))));
        ratings.add(ratingRepository.save(new Rating(null, 2032, 3, 2, players.get(2))));
        ratings.add(ratingRepository.save(new Rating(null, 2033, 3, 3, players.get(2))));
        getRatings().put(i, ratings);
    }

    public void initPlayerRating(Integer i) {
        List<Player> players = getPlayers().get(i);
        List<Rating> ratings = getRatings().get(i);
        for (Player player : players) {
            player.setRating(new ArrayList<Rating>());
        }
        players.get(0).getRating().add(ratings.get(0));
        players.get(1).getRating().add(ratings.get(1));
        players.get(1).getRating().add(ratings.get(2));
        players.get(2).getRating().add(ratings.get(3));
        players.get(2).getRating().add(ratings.get(4));
        players.get(2).getRating().add(ratings.get(5));
        getPlayers().replace(i, players);
        for (Player player : players) {
            playerRepository.save(player);
        }
    }

    public int getTestsCount() {
        return testsCount;
    }
}
