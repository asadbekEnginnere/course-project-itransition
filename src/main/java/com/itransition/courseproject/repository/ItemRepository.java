package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Integer> {
}
