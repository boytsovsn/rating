package ru.otus.hw.services;

import ru.otus.hw.models.entities.Users;

public interface UsersService {

    Users findUserByUsername(String userName);
}
