package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Item;
import com.itransition.courseproject.entity.collection.Like;
import com.itransition.courseproject.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like,Integer> {

    @Query(nativeQuery = true,
    value = "select coalesce(count(likes.user_id),0)\n" +
            "from likes\n" +
            "join users u on u.id = likes.user_id\n" +
            "where u.id= :userId ")
    int isLikedByUser(int userId);

    Like findByUserAndItem(User user, Item item);

    @Query(nativeQuery = true,
    value = "select coalesce(count(likes.*),0)\n" +
            "from likes\n" +
            "where item_id= :id")
    int likeCountByItemId(int id);

}
