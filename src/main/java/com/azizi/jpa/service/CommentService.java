package com.azizi.jpa.service;

import com.azizi.jpa.entity.Comment;
import com.azizi.jpa.entity.User;
import com.azizi.jpa.repository.CommentRepository;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private Faker faker;

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        this.faker = new Faker();
        List<Comment> comments = IntStream.range(0, 10).mapToObj(i -> {
            User user = User.builder()
                    .name(this.faker.name().fullName())
                    .build();
            return Comment.builder()
                    .text(this.faker.lorem().sentence(4))
                    .user(user)
                    .build();
        }).collect(Collectors.toList());
        commentRepository.saveAll(comments);
        getCommentsWithIssue();
        getCommentsWithOuterJoinFetch();
        getCommentsWithEntityGraphAndEntityManager();
        getCommentsWithEntityGraphAndSpringDataJpa();
        getCommentsWithNativeSqlQuery();
    }

    public void getCommentsWithIssue() {
        System.out.println("\nGetting comments and users with n+1 problem\n");
        List<Comment> comments = commentRepository.findAll();
        comments.forEach(System.out::println);
    }

    public void getCommentsWithOuterJoinFetch() {
        System.out.println("\nGetting comments and users with outer join fetch\n");
        List<Comment> comments = entityManager.createQuery("select c from Comment c left join fetch c.user u",
                        Comment.class)
                .getResultList();
        comments.forEach(System.out::println);
    }

    public void getCommentsWithEntityGraphAndEntityManager() {
        System.out.println("\nGetting comments and users with entity graph and entity manager\n");
        EntityGraph<?> entityGraph = entityManager.createEntityGraph(Comment.WITH_USER_GRAPH);
        List<Comment> comments = entityManager.createQuery("select c from Comment c", Comment.class)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
        comments.forEach(System.out::println);
    }

    public void getCommentsWithEntityGraphAndSpringDataJpa() {
        System.out.println("\nGetting comments and users with entity graph and spring data jpa\n");
        List<Comment> comments = commentRepository.findAllByOrderById();
        comments.forEach(System.out::println);

        //  List<Comment> comments = commentRepository.findAllByOrderByIdDesc();  you can use this method as well
    }

    public void getCommentsWithNativeSqlQuery() {
        System.out.println("\nGetting comments and users with native sql query\n");
        List<Tuple> tuples = entityManager.createNativeQuery("select c.id id, c.text, c.user_id userId, " +
                                "u.name userName from comments c left outer join users u on c.user_id=u.id",
                        Tuple.class)
                .getResultList();
        for (Tuple tuple : tuples) {
            Integer commentId = tuple.get("id", Integer.class);
            Integer userId = tuple.get("userId", Integer.class);
            String text = tuple.get("text", String.class);
            String userName = tuple.get("userName", String.class);
            User user = User.builder()
                    .id(userId)
                    .name(userName)
                    .build();
            Comment comment = Comment.builder()
                    .id(commentId)
                    .text(text)
                    .user(user)
                    .build();
            System.out.println(comment);
        }
    }

}
