package com.itransition.courseproject.entity.collection;


// Asatbek Xalimjonov 6/14/22 1:06 AM

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    private Collection collection;

    @ManyToMany
    @JoinTable(name = "item_tags",
            joinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags;


    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedAt;

    public Item(String name, Collection collection, List<Tag> tags) {
        this.name = name;
        this.collection = collection;
        this.tags = tags;
    }


    //cascade type

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Comment> comment;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<CustomValue> customValueList;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<DisLike> dislikes;


}
