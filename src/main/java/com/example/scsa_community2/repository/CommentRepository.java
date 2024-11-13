package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

}
