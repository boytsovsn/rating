package ru.otus.hw.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.BaseTest;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.models.entities.Rating;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ComponentScan({"ru.otus.hw.models", "ru.otus.hw.repositories"})
@DisplayName("JPA репозиторий для Rating")
class RatingRepositortyTest extends BaseTest {

    @Autowired
    private RatingRepository ratingRepository;

    private final int updatedRatingN = 1;

    private final int deletedRatingN = 2;

    private final int insertToPlayerN = 2;

    @BeforeEach
    public void setUp() {
        testN = 0;
        super.setUp(testN);
    }

    @DisplayName("Рэйтинг по id")
    @Test
    void findById() {
        List<Rating> ratingsList = convertToFlatDbRatings();
        for (Rating rating: ratingsList) {
                var fRating = ratingRepository.findById(rating.getId()).orElseThrow(() -> new EntityNotFoundException("Rating not found, id %s".formatted(rating.getId())));
                assertThat(fRating.getId()).isEqualTo(rating.getId());
                assertThat(fRating.getYear()).isEqualTo(rating.getYear());
                assertThat(fRating.getPlayer().getId()).isEqualTo(rating.getPlayer().getId());
        }
    }

    @DisplayName("Вставка рэйтинга")
    @Test
    void insertRating() {
        Player insertToPlayer = dbPlayers.get(insertToPlayerN-1);
        var newRating = new Rating(2024, 7, 3, insertToPlayer);
        var returnedRating = ratingRepository.save(newRating);
        newRating.setId(returnedRating.getId());
        assertThat(returnedRating).isNotNull()
                .matches(rating -> rating.getId() != null && rating.getId() > 0)
                .isEqualTo(newRating);
        Optional<Rating> checkRating = ratingRepository.findById(returnedRating.getId());
        assertThat(checkRating)
                .isPresent()
                .get()
                .isEqualTo(returnedRating);
        ratingRepository.deleteById(returnedRating.getId());
    }

    @DisplayName("Обновление рэйтинга")
    @Test
    void updateRating() {
        int forPlayerN = 2;
        Player forPlayer = dbPlayers.get(forPlayerN-1);
        Long updatedRatingId = convertToFlatDbRatings().get(updatedRatingN-1).getId();
        var expectedRating = new Rating(updatedRatingId, 2011, 1, 1, forPlayer);
        var fromBDRating = ratingRepository.findById(expectedRating.getId());
        assertThat(fromBDRating)
                .isPresent()
                .get()
                .isNotEqualTo(expectedRating);

        Rating returnedRating = ratingRepository.save(expectedRating);
        assertThat(returnedRating).isNotNull()
                .matches(rating -> rating.getId() != null && rating.getId() > 0);
        assertThat(returnedRating.getId())
                .isEqualTo(expectedRating.getId());

        Rating bdRating = ratingRepository.findById(returnedRating.getId()).get();
        assertThat(bdRating.getId())
                .isEqualTo(returnedRating.getId());
        assertThat(bdRating.getYear())
                .isEqualTo(returnedRating.getYear());
        assertThat(bdRating.getPlayer().getId())
                .isEqualTo(returnedRating.getPlayer().getId());
        ratingRepository.save(fromBDRating.get());
    }

    @DisplayName("Удаление рэйтинга")
    @Test
    void deleteById() {
        Player insertToPlayer = dbPlayers.get(insertToPlayerN-1);
        var returnedRating = ratingRepository.save(new Rating(2024, 3, 2, insertToPlayer));
        Long deletedRatingId = returnedRating.getId();
        assertThat(ratingRepository.findById(deletedRatingId)).isPresent();
        var rating = ratingRepository.findById(deletedRatingId);
        ratingRepository.deleteById(deletedRatingId);
        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            ratingRepository.findById(deletedRatingId).orElseThrow(() -> new EntityNotFoundException("Rating with id %s not found".formatted(deletedRatingId)));});
        assertEquals("Rating with id %s not found".formatted(deletedRatingId), exception.getMessage());
    }

}