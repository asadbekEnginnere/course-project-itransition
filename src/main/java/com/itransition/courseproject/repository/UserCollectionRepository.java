package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.user.UserCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCollectionRepository extends JpaRepository<UserCollection,Integer> {
}
