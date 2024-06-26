package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.models.entities.Tourney;
import ru.otus.hw.models.entities.TourneyPlayer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourneyPlayerDto {

    private Long id;

    private Long tourneyId;

    private Long playerId;

    private Float ratingIn;

    private Float ratingCurrent;

    private Float ratingOut;

    private String playerName;

    private String tourneyTitle;

    public TourneyPlayer toDomainObject(){
        return new TourneyPlayer(id, new Tourney(tourneyId, ""), new Player(playerId, playerName, null), ratingIn, ratingCurrent, ratingOut, null, null);
    }

    public static TourneyPlayerDto fromDomainObject(TourneyPlayer tourneyPlayer) {
        return new TourneyPlayerDto(tourneyPlayer.getId(), tourneyPlayer.getTourney().getId(), tourneyPlayer.getPlayer().getId()
                , tourneyPlayer.getRatingIn(), tourneyPlayer.getRatingCurrent(), tourneyPlayer.getRatingOut()
                , tourneyPlayer.getPlayer().getName(), tourneyPlayer.getTourney().getTitle());
    }
}
