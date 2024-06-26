package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.entities.Competition;
import ru.otus.hw.models.entities.Player;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionDto {

    private Long id;

    @NotBlank(message = "{field-should-not-be-blank}")
    @Size(min = 3, max = 200, message = "{string-field-should-has-expected-size}")
    private String title;

    private Date date;

    @NotBlank(message = "{field-should-not-be-blank}")
    @Size(min = 2, max = 100, message = "{string-field-should-has-expected-size}")
    private String location;

    public Competition toDomainObject(){
        return new Competition(id, title, date, location);
    }

    public static CompetitionDto fromDomainObject(Competition competition) {
        return new CompetitionDto(competition.getId(), competition.getTitle(), competition.getDate(), competition.getLocation());
    }
}
