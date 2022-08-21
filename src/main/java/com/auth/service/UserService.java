package com.auth.service;

import com.auth.dto.UserDto;
import com.auth.model.Role;
import com.auth.model.User;
import com.auth.repository.RoleRepository;
import com.auth.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @SneakyThrows(Exception.class)
    public User saveUser(User user) {

        log.info("saving new user {} to the database", user.getUsername());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @SneakyThrows(Exception.class)
    public Role saveRole(Role role) {

        log.info("saving new role {} to the database", role.getName());

        return roleRepository.save(role);
    }

    @SneakyThrows(Exception.class)
    public void addRoleToUser(String username, String roleName) {

        log.info("adding role {} to user {}", roleName, username);

        val user = userRepository.findByUsername(username);
        val role = roleRepository.findByName(roleName);

        user.getRoles().add(role);
    }

    @SneakyThrows(Exception.class)
    public User getUser(String username) {

        log.info("getting user{} from the database", username);

        return userRepository.findByUsername(username);
    }

    @SneakyThrows(Exception.class)
    public List<User> getUsers() {

        log.info("getting all users from the database");

        return userRepository.findAll();
    }

    @SneakyThrows(Exception.class)
    public User getDataUser() {

        log.info("getting all users from the database");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        return userRepository.findByUsername(username);
    }

    @SneakyThrows(Exception.class)
    public ResponseEntity<Object> createUser(UserDto.CreateUserDto dto){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setIsDeleted(0);
        userRepository.save(user);

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("code", HttpStatus.CREATED.value());
        res.put("message", "success");
        res.put("data", user);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(res);
    }

    @SneakyThrows(Exception.class)
    public ResponseEntity<Object> setRoleToUser(UserDto.SetRoleDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> res = new HashMap<String, Object>();

        val user = userRepository.findByUsername(dto.getUsername());
        val role = roleRepository.findByName(dto.getRolename());

        user.getRoles().add(role);

        res.put("code", HttpStatus.CREATED.value());
        res.put("message", "adding role success");
        res.put("data", user);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(res);
    }

    @SneakyThrows(Exception.class)
    public ResponseEntity<Object> getUserResponse(String username) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> res = new HashMap<String, Object>();

        val user = userRepository.findByUsername(username);

        if (Optional.ofNullable(user).isPresent()) {
            res.put("message", "success");
            res.put("data", user);
        } else {
            res.put("message", "failed");
            res.put("data", null);
        }

        res.put("code", HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(res);

    }
}
