package ru.otus.hw.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;

    private Integer month;

    private Integer week;

    private Float ratingIn;

    private Float ratingCurrent;

    private Float ratingOut;

    @ManyToOne
    private Player player;

    public Rating(Integer year, Integer month, Integer week, Player player) {
        this.year = year;
        this.month = month;
        this.week = week;
        this.setPlayer(player);
    }

    public Rating(Long id, Integer year, Integer month, Integer week, Player player) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.week = week;
        this.setPlayer(player);
    }

    @Override
    public String toString() {
        return "Rating id = %d, rating = %.2f, year = %d, month = %d, week = %d, player id = %d".formatted(id, ratingCurrent, year, month, week, player.getId());
    }

    @Override
    public boolean equals(Object ratingObject) {
        if (ratingObject == null)
            return false;
        if (!(ratingObject instanceof Rating)) {
            return false;
        }
        Rating ratingTo = (Rating) ratingObject;
        if (id != null && id.equals(ratingTo.getId()) &&
                (year != null && year.equals(ratingTo.getYear())) &&
                (month != null && month.equals(ratingTo.getMonth())) &&
                (week != null && week.equals(ratingTo.getWeek())) &&
//                (ratingIn != null && ratingIn.equals(ratingTo.getRatingIn())) &&
//                (ratingCurrent != null && ratingCurrent.equals(ratingTo.getRatingCurrent())) &&
//                (ratingOut != null && ratingOut.equals(ratingTo.getRatingOut())) &&
                (player != null && player.getId() != null && ratingTo.getPlayer() != null &&
                        player.getId().equals(ratingTo.getPlayer().getId()))) {
            return true;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 37;
        hash = hash + id.hashCode();
        hash = hash*17 + year.hashCode();
        hash = hash*17 + month.hashCode();
        hash = hash*17 + week.hashCode();
        hash = hash*17 + player.getId().hashCode();
//        hash = hash*17 + (ratingIn != null ? ratingIn.hashCode() : 0);
//        hash = hash*17 + (ratingCurrent != null ? ratingCurrent.hashCode() : 0);
//        hash = hash*17 + (ratingOut != null ? ratingOut.hashCode() : 0);
        return hash;
    }

}
