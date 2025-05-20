package com.av.sharebook.DataPopulator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.av.sharebook.data.UserRepository;
import com.av.sharebook.model.User;

@Component
public class DataPopulator implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .build();

        userRepository.save(user);
        User user2 = User.builder()
                .username("user2")
                .password(passwordEncoder.encode("user"))
                .build();

        userRepository.save(user2);

        User admin =  User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .build();

        admin.getUserRoles().add("ADMIN");

        userRepository.save(admin);


    }
}