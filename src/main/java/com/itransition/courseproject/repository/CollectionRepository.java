package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection,Integer> {
}
