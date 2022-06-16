package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.CustomValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomValueRepository extends JpaRepository<CustomValue,Integer> {
}
