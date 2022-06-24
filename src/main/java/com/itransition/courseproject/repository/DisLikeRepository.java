package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.DisLike;
import com.itransition.courseproject.entity.collection.Item;
import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.projection.LikeDisLikeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DisLikeRepository extends JpaRepository<DisLike, Integer> {

    @Query(nativeQuery = true,
            value = "select coalesce(count(dislikes.user_id),0)\n" +
                    "from dislikes\n" +
                    "join users u on u.id = dislikes.user_id\n" +
                    "where u.id= :userId ")
    int isDisliked(int userId);

    DisLike findByUserAndItem(User user, Item item);

    @Query(nativeQuery = true,
            value = "select coalesce(count(dislikes.*),0)\n" +
                    "from dislikes\n" +
                    "where item_id= :id ")
    int disLikeCountByItemId(Integer id);

    @Query(nativeQuery = true,
            value = "select concat(u.first_name,' ',u.last_name) as userFullName,\n" +
                    "       dislikes.disliked_at as likedDislikedAt\n" +
                    "from dislikes\n" +
                    "join users u on u.id = dislikes.user_id\n" +
                    "where item_id= :id")
    List<LikeDisLikeProjection> getLikeDislikeDetail(int id);

}
