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

}
