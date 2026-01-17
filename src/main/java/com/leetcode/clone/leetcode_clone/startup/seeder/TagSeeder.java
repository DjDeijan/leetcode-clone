package com.leetcode.clone.leetcode_clone.startup.seeder;

import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Profile("dev")
@Order(2)
@Component
@RequiredArgsConstructor
public class TagSeeder implements CommandLineRunner {

    private final TagRepository tagRepository;

    @Override
    public void run(String... args) {
        if (tagRepository.count() == 0) {
            List<Tag> defaultTags = List.of(
                    Tag.builder().name("Array").build(),
                    Tag.builder().name("String").build(),
                    Tag.builder().name("Hash Table").build(),
                    Tag.builder().name("Dynamic Programming").build(),
                    Tag.builder().name("Math").build()
            );
            tagRepository.saveAll(defaultTags);
        }
    }
}