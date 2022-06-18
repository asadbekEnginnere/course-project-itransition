package com.itransition.courseproject.entity.collection;


// Asatbek Xalimjonov 6/14/22 1:03 AM

import com.itransition.courseproject.entity.user.UserCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "collections")
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private String imageUrl;

    @ManyToOne
    private UserCollection userCollection;

    @ManyToOne
    private Topic topic;

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    public Collection(String name, String description, String imageUrl, UserCollection userCollection, Topic topic) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.userCollection = userCollection;
        this.topic = topic;
    }

}
