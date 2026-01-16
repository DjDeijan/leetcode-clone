package com.leetcode.clone.leetcode_clone.controller;

import java.io.Console;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leetcode.clone.leetcode_clone.service.Judge0Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/submissions")
@Validated
@Tag(name = "Submission Controller", description = "Submission related operations")
public class SubmissionController {
    private final Judge0Service judge0Service;
    @GetMapping("/test")
   public void test(){
       System.out.println("SubmissionController works");
   }
   @GetMapping("/testvalue")
   public void testvalue(){
     System.out.println(judge0Service.test());
   }
}
