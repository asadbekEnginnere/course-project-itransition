package com.itransition.courseproject.entity.collection;


// Asatbek Xalimjonov 6/14/22 1:10 AM


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
@Entity(name = "dislikes")
public class DisLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    public DisLike(User user, Item item) {
        this.user = user;
        this.item = item;
    }

    @ManyToOne
    private Item item;

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp dislikedAt;
}
