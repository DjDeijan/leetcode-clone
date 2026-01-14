package com.leetcode.clone.leetcode_clone.startup;

import com.leetcode.clone.leetcode_clone.model.User;
import com.leetcode.clone.leetcode_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("benchmark")
@Component
@RequiredArgsConstructor
public class InsertBenchmarkRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        int totalRecords = 10000;

        long start = System.currentTimeMillis();

        for (int i = 0; i < totalRecords; i++) {
            User emp = User.builder()
                    .username("Username" + i)
                    .build();

            userRepository.save(emp);
        }

        long end = System.currentTimeMillis();
        log.info("--- Inserted {} records in {}ms ---", totalRecords, (end - start));
    }

}
