package ru.otus.hw.services;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.hw.models.entities.Player;

import java.sql.Date;
import java.util.List;

public interface PlayerService {

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    Player findById(Long id);

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Player> findAll();

    Player insert(String name, String birthPlace, Date birthDate, String location, Integer gender);

    Player update(Long id, String name, String birthPlace, Date birthDate, String location, Integer gender);

    @PreAuthorize("hasPermission(#player, 'WRITE')")
    Player update(@Param("player") Player player);

    @PreAuthorize("hasPermission(#player, 'DELETE')")
    void delete(Player player);

    void deleteById(Long id);
}
