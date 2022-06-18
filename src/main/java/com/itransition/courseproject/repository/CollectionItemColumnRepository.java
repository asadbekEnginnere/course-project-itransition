package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.CollectionItemColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionItemColumnRepository extends JpaRepository<CollectionItemColumn,Integer> {


    @Query(nativeQuery = true,
    value = "select id \n" +
            "from collection_item_column \n" +
            "where collection_id= :id ")
    List<Integer> collectCollectionItemColumnId(int id);
}
