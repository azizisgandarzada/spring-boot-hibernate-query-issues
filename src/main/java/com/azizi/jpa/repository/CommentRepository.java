package com.azizi.jpa.repository;

import com.azizi.jpa.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // you can write attributes this way directly
    @EntityGraph(attributePaths = "user", type = EntityGraph.EntityGraphType.FETCH)
    List<Comment> findAllByOrderById();


    @EntityGraph(value = Comment.WITH_USER_GRAPH, type = EntityGraph.EntityGraphType.FETCH)
    List<Comment> findAllByOrderByIdDesc();

}
