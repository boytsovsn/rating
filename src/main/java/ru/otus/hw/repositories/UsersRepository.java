package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entities.Users;

import java.util.List;

public interface UsersRepository extends CrudRepository<Users, Long> {
    List<Users> findAll();
}
