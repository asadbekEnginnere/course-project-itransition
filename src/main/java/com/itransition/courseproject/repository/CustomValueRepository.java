package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.CustomColumn;
import com.itransition.courseproject.entity.collection.CustomValue;
import com.itransition.courseproject.entity.collection.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomValueRepository extends JpaRepository<CustomValue,Integer> {

    CustomValue findByCustomColumnAndItem(CustomColumn customColumn, Item item);

    CustomValue findByItemAndValue(Item item , String value);

}
