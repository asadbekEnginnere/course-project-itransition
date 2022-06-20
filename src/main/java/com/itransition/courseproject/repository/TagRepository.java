package com.itransition.courseproject.repository;

import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.entity.collection.Tag;
import com.itransition.courseproject.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Integer> {

    @Query("select new com.itransition.courseproject.dto.TagDto(t.id,t.name) from com.itransition.courseproject.entity.collection.Tag t")
    List<TagDto> getAllTags();

    Tag findByName(String name);

    Page<Tag> findAll(@NotNull Pageable pageable);

}
