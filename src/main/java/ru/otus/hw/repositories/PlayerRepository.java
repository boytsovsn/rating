package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.entities.Player;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface PlayerRepository extends PlayerRepositoryCustom, CrudRepository<Player, Long> {

    List<Player> findAll();

    Optional<Player> findById(Long id);

    Player save(Player player);

    void resetSequence();
}
