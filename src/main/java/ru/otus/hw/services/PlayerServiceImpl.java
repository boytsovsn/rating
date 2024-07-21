package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.models.entities.Rating;
import ru.otus.hw.repositories.PlayerRepository;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final RatingService ratingService;

    @Override
    public Player findById(Long id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Player with id %s not found".formatted(id)));;
        return player;
    }

    @Override
    public List<Player> findAll() {
        List<Player> players = playerRepository.findAll();
        for (Player player: players) {
            List<Rating> ratings = ratingService.findByPlayerId(player.getId());
            Comparator<Rating> compareByDate = Comparator
                    .comparing(Rating::getYear)
                    .thenComparing(Rating::getMonth)
                    .thenComparing(Rating::getWeek);
            if (ratings != null && !ratings.isEmpty()) {
                ArrayList<Rating> sortedRating = ratings.stream().sorted(compareByDate).collect(Collectors.toCollection(ArrayList::new));
                Rating lastRating = sortedRating != null && !sortedRating.isEmpty() ? sortedRating.get(sortedRating.size()-1) : null;
                if (lastRating != null && lastRating.getRatingCurrent() != null) {
                    player.setRatingCurrent(lastRating.getRatingCurrent());
                }
            }
        }
        Comparator<Player> compareByRating = Comparator.comparing(Player::getRatingCurrent).reversed();
        ArrayList<Player> sortedPlayer = players.stream().sorted(compareByRating).collect(Collectors.toCollection(ArrayList::new));
        return sortedPlayer;
    }

    @Transactional
    @Override
    public Player insert(String name, String birthPlace, Date birthDate, String location, Boolean gender) {
        return save(null, name, birthPlace, birthDate, location, gender);
    }

    @Transactional
    @Override
    public Player update(Long id, String name, String birthPlace, Date birthDate, String location, Boolean gender) {
        return save(id, name, birthPlace, birthDate, location, gender);
    }

    @Override
    public Player update(Player player) {
        var ratings = ratingService.findByPlayerId(player.getId());
        player = new Player(player.getId(), player.getName(), player.getBirthPlace(), player.getBirthDate(),  player.getLocation(),  player.getGender(), ratings);
        return playerRepository.save(player);
    }

    @Override
    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public void delete(Player player) {
        deleteById(player.getId());
    }

    private Player save(Long id, String name, String birthPlace, Date birthDate, String location, Boolean gender) {
        Player player;
        if (id!=null && !id.equals(0L)) {
            var ratings = ratingService.findByPlayerId(id);
            player = new Player(id, name, birthPlace, birthDate, location, gender, ratings);
        } else {
           player = new Player(name, birthPlace, birthDate, location, gender);
        }
        Player savedPlayer = playerRepository.save(player);
        if (id==null || id.equals(0L)) {
            Calendar calendar = GregorianCalendar.getInstance();

            Date date = new Date(calendar.getTimeInMillis());
            Rating rating = new Rating();
            ratingService.save(0L, date.getYear()+1900, date.getMonth()+1, calendar.get(Calendar.WEEK_OF_MONTH), 300F, 300F, 0F, savedPlayer.getId());
        }
        return savedPlayer;
    }
}
