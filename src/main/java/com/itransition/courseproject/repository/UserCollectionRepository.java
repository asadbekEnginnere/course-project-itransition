package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.entity.user.UserCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCollectionRepository extends JpaRepository<UserCollection,Integer> {

    UserCollection findByUser(User user);
}
