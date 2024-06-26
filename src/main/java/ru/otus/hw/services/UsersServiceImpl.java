package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entities.Users;
import ru.otus.hw.repositories.UsersRepository;

@RequiredArgsConstructor
@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    public Users findUserByUsername(String userName) {
        return usersRepository.findAll().stream().filter(u->u.getLogin().equals(userName)).findFirst().orElseThrow(()->new UsernameNotFoundException("User not found!"));
    }
}
