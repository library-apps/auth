package com.auth.service;

import com.auth.repository.RoleRepository;
import com.auth.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @SneakyThrows(UsernameNotFoundException.class)
    public UserDetails loadUserByUsername(String username) {

        val user = userRepository.findByUsername(username);

        if (Optional.ofNullable(user).isPresent()) {

            log.info("user {} is available in the database", username);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            user.getRoles().forEach(role -> {

                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    authorities);

        } else {

            log.info("user {} is not available in the database", username);
            throw new UsernameNotFoundException("user " + username + " is not available in the database");
        }
    }
}
