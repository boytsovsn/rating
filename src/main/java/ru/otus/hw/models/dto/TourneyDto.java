package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourneyDto {

    private Long id;

    @NotBlank(message = "{field-should-not-be-blank}")
    @Size(min = 3, max = 200, message = "{string-field-should-has-expected-size}")
    private String title;

    private TourneyType type;   // сетка - 1 или в круг - 0

    private Rank rank;          // 1, 2, 3 финалы (0, 1, 2)

    private Stage stage;        // подгруппы - 0, финалы - 1

    @NotNull(message = "{field-should-not-be-blank}")
    private Long competitionId;

    private Set<TourneyType> types = Set.of(TourneyType.values());

    private Set<Rank> ranks = Set.of(Rank.values());

    private Set<Stage> stages = Set.of(Stage.values());

    private List<CompetitionDto> competitions = new ArrayList<>();

    public CompetitionDto getCompetitionDto() {
        for (CompetitionDto compDto : competitions) {
            if (compDto.getId().equals(competitionId)) {
                return compDto;
            }
        }
        throw new EntityNotFoundException("Competition not found");
    }

    public TourneyDto(Long id, String title, TourneyType tourneyType, Rank rank, Stage stage, Long competitionId) {
        this.id = id;
        this.title = title;
        this.type = tourneyType;
        this.rank = rank;
        this.stage = stage;
        this.competitionId = competitionId;
    }

    public Tourney toDomainObject(){
        Integer typeOrd = 0;
        Integer rankOrd = 0;
        Integer stageOrd = 0;
        if (type != null) {
            typeOrd = type.ordinal();
        }
        if (rank != null) {
            rankOrd = rank.ordinal();
        }
        if (stage != null) {
            stageOrd = stage.ordinal();
        }
        return new Tourney(id, title, typeOrd, rankOrd, stageOrd, new Competition(competitionId, null, new Date(90, 00, 01), null));
    }

    public static TourneyDto fromDomainObject(Tourney tourney) {
        return new TourneyDto(tourney.getId(), tourney.getTitle(), TourneyType.getValueById(tourney.getType()), Rank.getValueById(tourney.getRank()), Stage.getValueById(tourney.getStage()), tourney.getCompetition().getId());
    }
}
