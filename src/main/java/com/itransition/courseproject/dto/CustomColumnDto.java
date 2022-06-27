package com.itransition.courseproject.dto;


// Asatbek Xalimjonov 6/18/22 6:31 PM

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CustomColumnDto {
    private Integer id;
    private String name;
    private String type;
    private String value;

    public CustomColumnDto(Integer id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
