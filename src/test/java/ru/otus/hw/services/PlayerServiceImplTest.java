package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.BaseTest;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.models.entities.Rating;
import ru.otus.hw.repositories.PlayerRepository;
import ru.otus.hw.repositories.RatingRepository;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.models", "ru.otus.hw.repositories", "ru.otus.hw.services"})
@DisplayName("Сервисы для Player")
class PlayerServiceImplTest extends BaseTest {

    @Autowired
    PlayerService playerServiceImpl;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    PlayerRepository playerRepository;

    @BeforeEach
    public void setUp() {
        testN = 3;
        super.setUp(testN);
    }

    @DisplayName("Поиск игроков по id")
    @Test
    void findById() {
        for (Player checkPlayer: dbPlayers) {
            Player player = playerServiceImpl.findById(checkPlayer.getId());
            assertThat(player.getId())
                    .isEqualTo(checkPlayer.getId());
        }
    }

    @DisplayName("Список всех игроков")
    @Test
    void findAll() {
        // Список всех игроков из БД
        List<Player> players = playerServiceImpl.findAll().stream().sorted(Comparator.comparingLong(Player::getId)).toList();
        // Список тестовых игроков
        List<Player> checkPlayers = dbPlayers;
        assertThat(players.get(3).getId()).isEqualTo(checkPlayers.get(0).getId());
        assertThat(players.get(4).getId()).isEqualTo(checkPlayers.get(1).getId());
        assertThat(players.get(5).getId()).isEqualTo(checkPlayers.get(2).getId());
    }


    @DisplayName("Вставка игрока")
    @Test
    void insertForUser() {
        var newPlayer = new Player(4L,"PlayerTitle_10500", null);
        var returnedPlayer = playerServiceImpl.insert(newPlayer.getName(), null, null, null, null);
        newPlayer.setId(returnedPlayer.getId());
        assertThat(returnedPlayer).isNotNull()
                .matches(player -> player.getId() != null && player.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(newPlayer);
        Player checkPlayer = playerServiceImpl.findById(returnedPlayer.getId());
        assertThat(checkPlayer.getId())
                .isEqualTo(returnedPlayer.getId());
    }

    @DisplayName("Обновление игрока")
    @Test
    void updatePlayer() {
        int updatedPlayerN = 2;
        int fromPlayerN = 2;
        // dbratings - список - списков (список по игрокам списков рейтингов)
        List<Rating> expectedratings = dbRatings.get(updatedPlayerN-1);
        int i = 0;
        for (Rating exprating : expectedratings) {
            if (i >= dbRatings.get(fromPlayerN).size())
                break;
            exprating.setYear(dbRatings.get(fromPlayerN).get(i).getYear());
            i++;
        }
        var expectedPlayer = new Player(dbPlayers.get(updatedPlayerN-1).getId(), "PlayerTitle_10500",  null, null, null, null, expectedratings);

        assertThat(playerServiceImpl.findById(expectedPlayer.getId()))
                .isNotEqualTo(expectedPlayer);

        var returnedPlayer = playerServiceImpl.update(expectedPlayer);
        // Рейтинг игрока сохраняются в сервисе ratingService, поэтому мы здесь рейтинг просто
        // присваиваем, чтобы игроки стали равны
        returnedPlayer.setRating(expectedratings);
        assertThat(returnedPlayer).isNotNull()
                .matches(player -> player.getId() != null && player.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedPlayer);
        // Восстанавливаем изменённую игрока
        playerServiceImpl.update(expectedPlayer.getId(), dbPlayers.get(updatedPlayerN-1).getName(), null, null, null, null);
    }

    @DisplayName("Удаление игрока")
    @Test
    void deleteById() {
        Long deletedPlayerId = 1L;
        Player deletedPlayer = playerServiceImpl.findById(deletedPlayerId);
        assertThat(deletedPlayer != null && deletedPlayer.getId()!= null).isTrue();
        playerServiceImpl.delete(deletedPlayer);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            playerServiceImpl.findById(deletedPlayerId);
                }
        );
    }
}