package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.entities.Game;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.models.entities.Tourney;
import ru.otus.hw.models.entities.TourneyPlayer;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {
    private Long id;

    private Integer tour;

    private Float rating1;

    private Float rating2;

    private GameResult result;    // 1 - выиграл первый, 2 - выиграл второй, 3 - не явился первый, 4 не явился второй, 5 не явились оба, 0 - пока нет результата

    private Long player1Id;

    private Long player2Id;

    private Long tourneyId;

    private Set<GameResult> results = Set.of(GameResult.values());

    private String player1Name;

    private String player2Name;

    public Game toDomainObject(){
        Tourney tourney = new Tourney(tourneyId, "");
        Integer resultOrd = 0;
        if (result != null) {
            resultOrd = result.ordinal();
        }
        return new Game(id, tour, rating1, rating2, resultOrd, new TourneyPlayer(player1Id), new TourneyPlayer(player1Id), tourney);
    }

    public static GameDto fromDomainObject(Game game) {
        return new GameDto(game.getId(), game.getTour(), game.getRating1()
                , game.getRating2(), GameResult.getValueById(game.getResult()), game.getTourneyPlayer1().getId()
                , game.getTourneyPlayer2().getId(), game.getTourney().getId(), Set.of(GameResult.values())
                , game.getTourneyPlayer1().getPlayer().getName(), game.getTourneyPlayer2().getPlayer().getName());
    }
}
