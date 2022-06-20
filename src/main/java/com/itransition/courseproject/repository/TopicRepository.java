package com.itransition.courseproject.repository;

import com.itransition.courseproject.dto.TopicDto;
import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.entity.collection.Topic;
import com.itransition.courseproject.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Integer> {

    @Query("SELECT new com.itransition.courseproject.dto.TopicDto(topic.id, topic.name) FROM com.itransition.courseproject.entity.collection.Topic topic")
    List<TopicDto> getAllTopic();

    Topic findByName(String name);

    Page<Topic> findAll(@NotNull Pageable pageable);
}
