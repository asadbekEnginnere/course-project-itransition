package com.itransition.courseproject.entity.collection;


// Asatbek Xalimjonov 6/14/22 3:06 PM

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "custom_value")
public class CustomValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String value;

    @ManyToOne
    private CustomColumn customColumn;

    @ManyToOne(targetEntity = Item.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Item item;

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedAt;

    public CustomValue(String value, CustomColumn customColumn, Item item) {
        this.value = value;
        this.customColumn = customColumn;
        this.item = item;
    }
}
