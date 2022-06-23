package com.itransition.courseproject.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ItemCustomColumnDto {
    private Integer id;
    private String  name;
    private String value;
    private String itemid;
    private String type;

    public ItemCustomColumnDto(Integer id, String name, String value, String type) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.type = type;
    }
}
