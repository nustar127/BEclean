package com.clean.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clean.demo.entity.Role;
import com.clean.demo.entity.User;
import com.clean.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User with that name alreaady exists");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with that email alreaady exists");
        }

        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User grantAdminRole(Long id) {
        User user = getById(id);

        if (user.getRoles().contains(Role.ROLE_ADMIN)) {
            return user;
        }

        var roles = new ArrayList<>(user.getRoles());
        roles.add(Role.ROLE_ADMIN);
        user.setRoles(roles);

        return save(user);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
