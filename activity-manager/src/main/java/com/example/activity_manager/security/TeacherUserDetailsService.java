package com.example.activity_manager.security;

import com.example.activity_manager.model.User;
import com.example.activity_manager.model.UserRole;
import com.example.activity_manager.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class
TeacherUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public TeacherUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Only teachers can log in to UI
        if (u.getUserRole() != UserRole.TEACHER) {
            throw new UsernameNotFoundException("Only teachers can log in");
        }

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getUserPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_TEACHER"))
        );
    }
}
