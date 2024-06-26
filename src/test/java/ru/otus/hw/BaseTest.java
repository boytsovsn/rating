package ru.otus.hw;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.models.AllEntitiesModel;
import ru.otus.hw.models.entities.Tourney;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.models.entities.Game;
import ru.otus.hw.models.entities.Rating;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseTest {

    @Autowired
    protected AllEntitiesModel allEntitiesModelImpl;

    protected List<Tourney> dbTourneys;

    protected List<Game> dbGames;

    protected List<List<Rating>> dbRatings;

    protected List<Player> dbPlayers;

    protected int testN;

    public void setUp(int testN) {
        allEntitiesModelImpl.init(testN);
        if (testN < allEntitiesModelImpl.getTestsCount()) {
//            dbTourneys = getDbTourneys(testN);
//            dbGames = getDbGames(testN);
            dbRatings = getDbRatings(testN, testN);
            dbPlayers = getDbPlayers(testN, dbTourneys, dbGames, dbRatings);
        }
    }

    protected List<Rating> convertToFlatDbRatings() {
        return dbRatings.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    protected List<Tourney> getDbTourneys(int t) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Tourney(allEntitiesModelImpl.getTourneys().get(t).get(id-1).getId(), "Tourney_" + id))
                .toList();
    }

//    protected List<Game> getDbGames(int t) {
//        return IntStream.range(1, 4).boxed()
//                .map(id -> new Game(allEntitiesModelImpl.getGames().get(t).get(id-1).getId(), id))
//                .toList();
//    }

    protected List<List<Rating>> getDbRatings(int t, int t1) {
        return IntStream.range(1, 4).boxed()
                .map(id -> IntStream.range(1, id+1).boxed()
                        .map(id1 -> new Rating(allEntitiesModelImpl.getRatings().get(t).get(id*(id - 1)/2 + id1 - 1).getId(), 2000+10*id+id1, id, id1, allEntitiesModelImpl.getPlayers().get(t1).get(id - 1))).toList())
                .toList();
    }

    protected List<Player> getDbPlayers(int t, List<Tourney> _dbTourneys, List<Game> _dbGames, List<List<Rating>> _dbratings) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Player(allEntitiesModelImpl.getPlayers().get(t).get(id-1).getId(), "PlayerTitle_" + id, _dbratings.get(id - 1)))
                .toList();
    }

}
