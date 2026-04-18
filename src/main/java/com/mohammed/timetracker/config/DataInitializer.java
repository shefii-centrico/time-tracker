package com.mohammed.timetracker.config;

import com.mohammed.timetracker.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        // All users are seeded via Flyway migration V3__seed_team_users.sql
        // This ensures user data lives in the database, not in application code.
        long count = userRepository.count();
        System.out.println("=== Time Tracker started — " + count + " users loaded from DB ===");
    }
}

