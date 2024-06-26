package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entities.Users;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PostgresAuthUserDetailService implements UserDetailsService {

    private final UsersService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users user = userService.findUserByUsername(userName);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getAuthorities()
                .forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority())));
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .builder().username(user.getUsername()).password(user.getPassword()).roles(user.getRole())
                .authorities(grantedAuthorities)
                .accountLocked(!user.isAccountNonLocked())
                .build();
        return userDetails;
    }
}
