package ru.otus.hw.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="TourneyPlayers")
public class TourneyPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Tourney tourney;

    @ManyToOne()
    private Player player;

    private Float ratingIn;

    private Float ratingCurrent;

    private Float ratingOut;

    @OneToMany(mappedBy="tourneyPlayer1", targetEntity = Game.class, orphanRemoval = true)
    private List<Game> games1;

    @OneToMany(mappedBy="tourneyPlayer2", targetEntity = Game.class, orphanRemoval = true)
    private List<Game> games2;

    public TourneyPlayer(long id) {
        this.id = id;
    }
}
