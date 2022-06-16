package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {
}
