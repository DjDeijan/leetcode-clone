package com.leetcode.clone.leetcode_clone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.leetcode.clone.leetcode_clone.dto.Submissions.Judge0SubmissionDTO;
import com.leetcode.clone.leetcode_clone.dto.Submissions.UserSubmissionDTO;

import jakarta.transaction.Transactional;

@Service
public class Judge0Service {
    
   @Value("${judge0.ip}")
    private String judge0Ip;
    
    public String test(){
        return judge0Ip;
    }

    public String JudgeSubmission(UserSubmissionDTO userSubmission){

        return "Judging submission";
    }
    public String GetBatch(){
        return "Getting batch";
    }
    public String GetSingleSubmission(){
        return "Getting single submission";
    }

    private String JudgeBatch(List<Judge0SubmissionDTO> submissions){

        //buildvame submission kum judge za vseki testcase za taska i gi prashtame za ocenka



        return "Judging batch internal";
    }

}