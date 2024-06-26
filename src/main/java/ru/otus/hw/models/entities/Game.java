package ru.otus.hw.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer tour;

    private Float rating1;

    private Float rating2;

    private Integer result;    // 1 - выиграл первый, 2 - выиграл второй, -1 - не явился первый, -2 не явился второй, -3 не явились оба, 0 - пока нет результата

    @ManyToOne
    private TourneyPlayer tourneyPlayer1;

    @ManyToOne
    private TourneyPlayer tourneyPlayer2;

    @ManyToOne
    private Tourney tourney;

    public Game(TourneyPlayer player1, TourneyPlayer player2, Float rating1, Float rating2, Tourney tourney, Integer tour) {
        this.tourneyPlayer1 = player1;
        this.tourneyPlayer2 = player2;
        this.rating1 = rating1;
        this.rating2 = rating2;
        this.tourney = tourney;
        this.tour = tour;
        this.result = 0;
    }
}
