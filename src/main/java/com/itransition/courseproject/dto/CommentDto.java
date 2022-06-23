package com.itransition.courseproject.dto;


// Asatbek Xalimjonov 6/23/22 2:34 PM


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CommentDto {

    private Integer commentId;
    private String content;
    private String commentsFullName;
    private String commentedAt;
}
