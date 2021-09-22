package com.azizi.jpa;

import com.azizi.jpa.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringJpaExampleApplication implements CommandLineRunner {

    private final CommentService commentService;

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaExampleApplication.class, args);
    }

    @Override
    public void run(String... args) {
        commentService.getCommentsWithIssue();
        commentService.getCommentsWithOuterJoinFetch();
        commentService.getCommentsWithEntityGraphAndEntityManager();
        commentService.getCommentsWithEntityGraphAndSpringDataJpa();
        commentService.getCommentsWithNativeSqlQuery();
    }

}
