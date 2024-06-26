package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.Assert;
import ru.otus.hw.BaseTest;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.models.entities.Rating;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ComponentScan({"ru.otus.hw.models", "ru.otus.hw.repositories"})
@DisplayName("JPA репозиторий для Player")
class PlayerRepositoryTest extends BaseTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @BeforeEach
    public void setUp() {
        testN = 1;
        super.setUp(testN);
    }

    @DisplayName("Список всех игроков")
    @Test
    void findAll() {
        List<Player> players = playerRepository.findAll().stream().sorted(Comparator.comparingLong(Player::getId)).toList();
        List<Player> checkPlayers = dbPlayers;
        int k = 0;
        for (int i = testN * dbPlayers.size(); i < (testN + 1) * dbPlayers.size(); i++) {
            assertThat(players.get(i).getId()).isEqualTo(checkPlayers.get(k).getId());
            k++;
        }
    }

    @DisplayName("Загрузка книги по id")
    @Test
    void findById() {
        for (Player checkPlayer: dbPlayers) {
            var player = playerRepository.findById(checkPlayer.getId()).get();
            assertThat(player.getId()).isEqualTo(checkPlayer.getId());
        }
    }

    @DisplayName("Cохранение нового игрока")
    @Test
    void insertPlayer() {
        var newPlayer = new Player("PlayerTitle_10500");
        var returnedPlayer = playerRepository.save(newPlayer);
        newPlayer.setId(returnedPlayer.getId());
        assertThat(returnedPlayer).isNotNull()
                .matches(player -> player.getId() != null && player.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(newPlayer);
        Rating newRating = new Rating( 2024, 11, 4, returnedPlayer);
        var returnedRating = ratingRepository.save(newRating);
        returnedPlayer.setRating(Arrays.asList(returnedRating));
        playerRepository.save(returnedPlayer);
        Player checkPlayer = playerRepository.findById(returnedPlayer.getId()).get();
        assertThat(checkPlayer.getId()).isEqualTo(returnedPlayer.getId());
    }

    @DisplayName("Обновление игрока")
    @Test
    void updatedPlayer() {
        int updatedPlayerId = 1;
        int fromPlayerIndex = 2;
        List<Rating> expectedratings = dbRatings.get(updatedPlayerId-1);
        int i = 0;
        for (Rating exprating : expectedratings) {
            if (i >= dbRatings.get(fromPlayerIndex).size())
                break;
            exprating.setYear(dbRatings.get(fromPlayerIndex).get(i).getYear());
            i++;
        }
        var expectedPlayer = new Player(dbPlayers.get(updatedPlayerId-1).getId(), "PlayerTitle_1", expectedratings);

        assertThat(playerRepository.findById(expectedPlayer.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedPlayer);

        var returnedPlayer = playerRepository.save(expectedPlayer);
        returnedPlayer.setRating(expectedratings);
        assertThat(returnedPlayer).isNotNull()
                .matches(player -> player.getId() != null && player.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedPlayer);

        for (Rating rating : expectedratings) {
            var returnedrating = ratingRepository.save(rating);
        }
        Player actualPlayer = playerRepository.findById(returnedPlayer.getId()).get();
        assertThat(actualPlayer.getId()).isEqualTo(returnedPlayer.getId());
        assertThat(actualPlayer.getName()).isEqualTo(returnedPlayer.getName());
    }

    @DisplayName("Удаление игрока по id")
    @Test
    void deletePlayer() {
        Long deletedPlayerId = dbPlayers.get(2).getId();
        assertThat(playerRepository.findById(deletedPlayerId)).isPresent();

        playerRepository.deleteById(deletedPlayerId);
        Assert.isTrue(playerRepository.findById(deletedPlayerId).isEmpty(), "Player with id = %s is not deleted".formatted(deletedPlayerId));
    }
}