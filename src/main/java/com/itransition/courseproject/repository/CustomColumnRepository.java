package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.CustomColumn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomColumnRepository extends JpaRepository<CustomColumn,Integer> {
}
