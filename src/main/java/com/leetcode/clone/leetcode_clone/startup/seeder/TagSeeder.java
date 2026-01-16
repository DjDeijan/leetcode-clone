package com.leetcode.clone.leetcode_clone.startup.seeder;

import com.leetcode.clone.leetcode_clone.model.Tag;
import com.leetcode.clone.leetcode_clone.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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