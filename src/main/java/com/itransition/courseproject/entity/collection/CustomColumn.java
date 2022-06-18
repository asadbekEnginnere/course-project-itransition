package com.itransition.courseproject.entity.collection;


// Asatbek Xalimjonov 6/14/22 3:06 PM

import com.itransition.courseproject.entity.enums.CustomColumnDataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "custom_column")
public class CustomColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CustomColumnDataType type;

    public CustomColumn(String name, CustomColumnDataType type) {
        this.name = name;
        this.type = type;
    }

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedAt;
}
