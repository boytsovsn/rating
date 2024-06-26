package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.models.entities.Rating;
import ru.otus.hw.repositories.PlayerRepository;
import ru.otus.hw.repositories.RatingRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    private final PlayerRepository playerRepository;

    @Override
    @Transactional(readOnly=true)
    public List<Rating> findByPlayerId(Long id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Player with id %s not found".formatted(id)));
        player.getRating().size();
        return player.getRating();
    }

    @Override
    @Transactional(readOnly=true)
    public Rating findLastByPlayerId(Long id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Player with id %d not found".formatted(id)));
        player.getRating().size();
        List<Rating> ratings = player.getRating();
        Comparator<Rating> compareByDate = Comparator
                .comparing(Rating::getYear)
                .thenComparing(Rating::getMonth)
                .thenComparing(Rating::getWeek);
        if (ratings != null && !ratings.isEmpty()) {
            ArrayList<Rating> sortedRating = ratings.stream().sorted(compareByDate).collect(Collectors.toCollection(ArrayList::new));
            Rating lastRating = sortedRating != null && !sortedRating.isEmpty() ? sortedRating.get(sortedRating.size()-1) : null;
            if (lastRating != null && lastRating.getRatingCurrent() != null) {
                return lastRating;
            }
        }
        throw new EntityNotFoundException("Rating for player with id %d not found!".formatted(id));
    }

    @Override
    public Optional<Rating> findById(Long id) {
        return ratingRepository.findById(id);
    }

    @Transactional
    @Override
    public Rating save(Long id, Integer year, Integer month, Integer week, Float ratingIn, Float ratingCurr, Float ratingOut, Long playerId) {
        Rating rating;
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new EntityNotFoundException("Rating with id %d not found".formatted(playerId)));
        if (id == null || id==0L)
            rating = new Rating(0L, year, month, week, ratingIn, ratingCurr, ratingOut, player);
        else {
            rating = ratingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Rating with id %d not found".formatted(id)));
            rating.setYear(year);
            rating.setMonth(month);
            rating.setWeek(week);
            rating.setPlayer(player);
            rating.setRatingIn(ratingIn);
            rating.setRatingCurrent(ratingCurr);
            rating.setRatingOut(ratingOut);
        }
        return ratingRepository.save(rating);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ratingRepository.deleteById(id);
    }
}
