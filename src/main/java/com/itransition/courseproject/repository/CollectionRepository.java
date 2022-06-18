package com.itransition.courseproject.repository;

import com.itransition.courseproject.dto.CollectionDto;
import com.itransition.courseproject.entity.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection,Integer> {

    @Query("select new com.itransition.courseproject.dto.CollectionDto(col.id,col.name,col.description,col.imageUrl) from com.itransition.courseproject.entity.collection.Collection col")
    List<CollectionDto> getAllCollection();

}
