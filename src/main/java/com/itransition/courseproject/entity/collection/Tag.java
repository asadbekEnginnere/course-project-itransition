package com.itransition.courseproject.entity.collection;


// Asatbek Xalimjonov 6/14/22 1:05 AM

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public Tag(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
}
