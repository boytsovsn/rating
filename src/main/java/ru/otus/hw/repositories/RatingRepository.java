package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entities.Rating;

public interface RatingRepository extends CrudRepository<Rating, Long> {

}
