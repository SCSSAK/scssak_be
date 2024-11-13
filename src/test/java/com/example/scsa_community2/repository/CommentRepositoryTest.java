package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Comment;
import com.example.scsa_community2.entity.CommentId;
import com.example.scsa_community2.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void testCreateComment() {
        Comment comment = new Comment(1L, 1L, "댓글 내용", Date.valueOf(LocalDate.now()));
        commentRepository.save(comment);

        CommentId commentId = new CommentId(1L, 1L);
        Comment foundComment = commentRepository.findById(commentId).orElse(null);
        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getCommentContent()).isEqualTo("댓글 내용");
    }
}
