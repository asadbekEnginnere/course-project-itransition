package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic,Integer> {
}
