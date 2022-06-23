package com.itransition.courseproject.dto;


// Asatbek Xalimjonov 6/18/22 10:34 AM

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CollectionDto {

    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private String creationTime;

    public CollectionDto(Integer id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
