package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
