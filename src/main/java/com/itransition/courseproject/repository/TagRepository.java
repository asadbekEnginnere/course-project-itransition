package com.itransition.courseproject.repository;

import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.entity.collection.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Integer> {

    @Query("select new com.itransition.courseproject.dto.TagDto(t.id,t.name) from com.itransition.courseproject.entity.collection.Tag t")
    List<TagDto> getAllTags();

    Tag findByName(String name);

}
