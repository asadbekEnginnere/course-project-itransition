package com.itransition.courseproject.projection;


// Asatbek Xalimjonov 6/23/22 2:35 PM


import java.sql.Timestamp;

public interface CommentProjection {
    Integer getId();
    String getContent();
    String getCommentorFullName();
    Timestamp getCommentedAt();
    Integer getUserId();
}
