package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Item;
import com.itransition.courseproject.entity.collection.Like;
import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.projection.LikeDisLikeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    @Query(nativeQuery = true,
            value = "select coalesce(count(likes.user_id),0)\n" +
                    "from likes\n" +
                    "join users u on u.id = likes.user_id\n" +
                    "where u.id= :userId and likes.item_id= :itemId ")
    int isLikedByUser(int userId,int itemId);

    Like findByUserAndItem(User user, Item item);

    @Query(nativeQuery = true,
            value = "select coalesce(count(likes.*),0)\n" +
                    "from likes\n" +
                    "where item_id= :id")
    int likeCountByItemId(int id);


    @Query(nativeQuery = true,
            value = "select concat(u.first_name,' ',u.last_name) as userFullName,\n" +
                    "       likes.liked_at as likedDislikedAt\n" +
                    "from likes \n" +
                    "join users u on u.id = likes.user_id\n" +
                    "where item_id= :id")
    List<LikeDisLikeProjection> getLikeDislikeDetail(int id);

    @Query(nativeQuery = true,
    value = "select count(likes.*) as likes\n" +
            "from likes\n" +
            "join items i on i.id = likes.item_id\n" +
            "join collections c on c.id = i.collection_id\n" +
            "join user_collection uc on uc.id = c.user_collection_id\n" +
            "join users u on u.id = uc.user_id\n" +
            "where u.id= :id ;")
    int getTotalLikesByUserId(Integer id);

}
