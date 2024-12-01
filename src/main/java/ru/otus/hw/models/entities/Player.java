package ru.otus.hw.models.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String birthPlace;

    private Date birthDate;

    private String location;

    private Integer gender;

    @Transient
    private Float ratingCurrent = 0.0F;

    @OneToMany(mappedBy="player", targetEntity = Rating.class, orphanRemoval = true)
    private List<Rating> rating;

    public Player(String name, Rating... ratings) {
        this.name = name;
        this.rating = Arrays.asList(ratings);
    }

    public Player(Long id, String name, List<Rating> ratings) {
        this.id = id;
        this.name = name;
        this.rating = ratings;
    }

    public Player(String name, String birthPlace, Date birthDate, String location, Integer gender, List<Rating> ratings) {
        this.name = name;
        this.birthPlace = birthPlace;
        this.birthDate = birthDate;
        this.location = location;
        this.gender = gender;
        this.rating = ratings;
    }

    public Player(Long id, String name, String birthPlace, Date birthDate, String location, Integer gender, List<Rating> ratings) {
        this.id = id;
        this.name = name;
        this.birthPlace = birthPlace;
        this.birthDate = birthDate;
        this.location = location;
        this.gender = gender;
        this.rating = ratings;
    }

    public Player(String name, String birthPlace, Date birthDate, String location, Integer gender) {
        this.name = name;
        this.birthPlace = birthPlace;
        this.birthDate = birthDate;
        this.location = location;
        this.gender = gender;
    }

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[Id: %d, Name: %s, Birth Place: %s, BirthDate: %4$td %4$tB %4$tY, Gender: %b, Location: %s]".formatted(
                this.getId(),
                this.getName(),
                this.getBirthPlace(),
                this.getBirthDate(),
                this.getGender(),
                this.getLocation()
                );
    }

}
