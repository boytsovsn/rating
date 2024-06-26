package ru.otus.hw.services;

import ru.otus.hw.models.entities.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingService {

    List<Rating> findByPlayerId(Long id);

    Rating findLastByPlayerId(Long id);

    Optional<Rating> findById(Long id);

    Rating save(Long id, Integer year, Integer month, Integer week, Float ratingIn, Float ratingCurr, Float ratingOut, Long playerId);

    void deleteById(Long id);
}
