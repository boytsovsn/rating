//package ru.otus.hw.services;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.ComponentScan;
//import ru.otus.hw.BaseTest;
//import ru.otus.hw.exceptions.EntityNotFoundException;
//import ru.otus.hw.models.entities.Player;
//import ru.otus.hw.models.entities.Rating;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@EnableConfigurationProperties
//@ComponentScan({"ru.otus.hw.models", "ru.otus.hw.repositories", "ru.otus.hw.services"})
//@DisplayName("Сервисы для Rating")
//class RatingServiceImplTest extends BaseTest {
//
//    @Autowired
//    private RatingService ratingServiceImpl;
//
//    private static Long insertedRatingId;
//
//    private static Long updatedRatingId;
//
//    private final int insertToPlayerN = 2;
//
//    private final int updatedRatingN = 1;
//
//    @BeforeEach
//    public void setUp() {
//        testN = 2;
//        super.setUp(testN);
//    }
//
//    @DisplayName("Рейтинг по id игрокам")
//    @Test
//    void findByPlayerId() {
//        for (int i = 0; i < dbRatings.size(); i++) {
//            List<Rating> actualRatings = ratingServiceImpl.findByPlayerId(dbRatings.get(i).get(0).getPlayer().getId());
//            List<Rating> expectedRatings = dbRatings.get(i);
//            assertThat(actualRatings.stream().map(x->x.getId()).toList()).isEqualTo(expectedRatings.stream().map(x->x.getId()).toList());
//        }
//    }
//
//    @DisplayName("Рейтинг по id")
//    @Test
//    void findById() {
//        List<Rating> ratings = convertToFlatDbRatings();
//        for (Rating expectedRating: ratings) {
//            var actualRating = ratingServiceImpl.findById(expectedRating.getId()).orElseThrow(() -> new EntityNotFoundException("Rating not found, id %s".formatted(expectedRating.getId())));
//                assertThat(actualRating.getId()).isEqualTo(expectedRating.getId());
//        }
//    }
//
//    @DisplayName("Вставка рейтинга")
//    @Test
//    void insert() {
//        Player insertToPlayer = dbPlayers.get(insertToPlayerN-1);
//        var newRating = new Rating(null, 2024, 11, 4, 1.2F, 1.3F, 1.5F, insertToPlayer);
//        var returnedRating = ratingServiceImpl.save(null, 2024, 11, 4, 1.2F, 1.3F, 1.5F, insertToPlayer.getId());
//        insertedRatingId = returnedRating.getId();
//        newRating.setId(returnedRating.getId());
//        assertThat(returnedRating).isNotNull()
//                .matches(rating -> rating.getId() != null && rating.getId() > 0)
//                .isEqualTo(newRating);
//        Optional<Rating> expectedRating = ratingServiceImpl.findById(returnedRating.getId());
//        assertThat(expectedRating)
//                .isPresent()
//                .get()
//                .isEqualTo(returnedRating);
//        ratingServiceImpl.deleteById(insertedRatingId);
//    }
//
//    @DisplayName("Обновление рейтинга")
//    @Test
//    void update() {
//        int forPlayerN = 2;
//        Player forPlayer = dbPlayers.get(forPlayerN-1);
//        updatedRatingId = convertToFlatDbRatings().get(updatedRatingN-1).getId();
//        var updatedRating = new Rating(updatedRatingId, 2012, 2, 2, 305.2F, 305.5F, 306.5F, forPlayer);
//        updatedRatingId = updatedRating.getId();
//        var fromBDRating = ratingServiceImpl.findById(updatedRating.getId());
//        assertThat(fromBDRating)
//                .isPresent()
//                .get()
//                .isNotEqualTo(updatedRating);
//
//        var returnedRating = ratingServiceImpl.save(updatedRatingId, 2012, 2, 2, 305.2F, 305.5F, 306.5F, forPlayer.getId());
//        assertThat(returnedRating).isNotNull()
//                .matches(rating -> rating.getId() != null && rating.getId() > 0)
//                .isEqualTo(updatedRating);
//
//        Rating expectedRating = ratingServiceImpl.findById(returnedRating.getId()).get();
//        assertThat(expectedRating.getId())
//                .isEqualTo(returnedRating.getId());
//        assertThat(expectedRating.getYear())
//                .isEqualTo(returnedRating.getYear());
//        assertThat(expectedRating.getPlayer().getId())
//                .isEqualTo(returnedRating.getPlayer().getId());
//        // Проверка, что рейтинг "перекочевал" из одной игрока к в другому
//        var playerRatingsFrom = ratingServiceImpl.findByPlayerId(fromBDRating.get().getPlayer().getId());
//        var playerRatingsTo   = ratingServiceImpl.findByPlayerId(forPlayer.getId());
//        int i = 0;
//        for (Rating rating : playerRatingsFrom) {
//            if (rating.getId().equals(updatedRatingId))
//                break;
//            i++;
//        }
//        // Рэйтинга нет у игрока, от которого рэйтинг переместился
//        assertThat(i).isEqualTo(playerRatingsFrom.size());
//
//        i = 0;
//        for (Rating rating : playerRatingsTo) {
//            if (rating.getId().equals(updatedRatingId))
//                break;
//            i++;
//        }
//        // Зато есть там, куда переместилось
//        assertThat(i).isLessThan(playerRatingsTo.size());
//        //Восстановление изменённого рейтинга
//        ratingServiceImpl.save(updatedRatingId, fromBDRating.get().getYear(), fromBDRating.get().getMonth(), fromBDRating.get().getWeek(), fromBDRating.get().getRatingIn(), fromBDRating.get().getRatingCurrent(), fromBDRating.get().getRatingOut(), fromBDRating.get().getPlayer().getId());
//    }
//
//    @DisplayName("Удаление замечания")
//    @Test
//    void deleteById() {
//        Player insertToPlayer = dbPlayers.get(insertToPlayerN-1);
//        var returnedRating = ratingServiceImpl.save(null, 2024, 11, 4, 413.0F, 412.3F, 400.1F, insertToPlayer.getId());
//        Long deletedRatingId = returnedRating.getId();
//        assertThat(ratingServiceImpl.findById(deletedRatingId)).isPresent();
//        var rating = ratingServiceImpl.findById(deletedRatingId);
//        ratingServiceImpl.deleteById(deletedRatingId);
//        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
//            ratingServiceImpl.findById(deletedRatingId).orElseThrow(() -> new EntityNotFoundException("Rating with id %s not found".formatted(deletedRatingId)));});
//        assertEquals("Rating with id %s not found".formatted(deletedRatingId), exception.getMessage());
//    }
//}