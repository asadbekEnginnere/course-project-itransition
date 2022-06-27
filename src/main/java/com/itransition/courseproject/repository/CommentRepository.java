package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Comment;
import com.itransition.courseproject.projection.CommentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    @Query(nativeQuery = true,
    value = "select comments.id,\n" +
            "       comments.content,\n" +
            "       concat(u.first_name,' ',u.last_name) as commentorFullName,\n" +
            "       comments.commented_at as commentedAt\n" +
            "from comments\n" +
            "join items i on i.id = comments.item_id\n" +
            "join users u on u.id = comments.user_id\n" +
            "where i.id= :id order by comments.commented_at desc ")
    List<CommentProjection> getAllCommentsByItemId(int id);

    @Query(nativeQuery = true,
    value = "select count(comments.*)\n" +
            "from comments\n" +
            "join items i on i.id = comments.item_id\n" +
            "join collections c on c.id = i.collection_id\n" +
            "join user_collection uc on uc.id = c.user_collection_id\n" +
            "join users u on u.id = uc.user_id\n" +
            "where u.id= :id ;")
    int getTotalCommentsByUserId(Integer id);
}
