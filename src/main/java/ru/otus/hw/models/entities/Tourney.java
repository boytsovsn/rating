package ru.otus.hw.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.dto.CompetitionDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tourneys")
public class Tourney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Integer type;   // сетка - 1 или в круг - 0

    private Integer rank;      // 1, 2, 3 финалы (0, 1, 2)

    private Integer stage;     // подгруппы - 0, финалы - 1

    @ManyToOne
    private Competition competition;

    public Tourney(String name) {
        title = name;
    }

    public Tourney(Long id, String name) {
        this.id = id;
        this.title = name;
        this.type =  0;
        this.rank = 0;
        this.stage = 0;
    }

}
