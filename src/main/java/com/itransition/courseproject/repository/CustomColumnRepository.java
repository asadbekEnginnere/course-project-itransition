package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.CustomColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomColumnRepository extends JpaRepository<CustomColumn,Integer> {

    CustomColumn findByName(String variable);

}
