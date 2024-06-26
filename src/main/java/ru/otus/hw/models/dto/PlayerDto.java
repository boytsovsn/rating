package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.entities.Player;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {

    private Long id;

    @NotBlank(message = "{field-should-not-be-blank}")
    @Size(min = 2, max = 50, message = "{string-field-should-has-expected-size}")
    private String name;

    private String birthPlace;

    private Date birthDate;

    private String location;

    private Boolean gender;

    private Float ratingCurrent = 0.0F;

    private String checkedPlayer;

    public PlayerDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player toDomainObject(){
        return new Player(id, name, birthPlace, birthDate, location, gender, ratingCurrent, null);
    }

    public static PlayerDto fromDomainObject(Player player) {
        return new PlayerDto(player.getId(), player.getName(), player.getBirthPlace(), player.getBirthDate(), player.getLocation(), player.getGender(), player.getRatingCurrent(), "on");
    }

}
