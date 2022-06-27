package com.itransition.courseproject.entity.collection;


// Asatbek Xalimjonov 6/14/22 1:08 AM


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itransition.courseproject.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @ManyToOne
    private User user;

    public Comment(String content, User user, Item item) {
        this.content = content;
        this.user = user;
        this.item = item;
    }


    @ManyToOne(targetEntity = Item.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Item item;

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp commentedAt;
}
