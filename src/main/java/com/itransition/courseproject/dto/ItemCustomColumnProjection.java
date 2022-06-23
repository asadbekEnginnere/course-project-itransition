package com.itransition.courseproject.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ItemCustomColumnProjection {
    private Integer id;
    private String  name;
    private String value;
    private String itemid;
    private String type;
}
