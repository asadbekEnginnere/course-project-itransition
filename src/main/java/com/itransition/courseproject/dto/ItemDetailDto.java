package com.itransition.courseproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ItemDetailDto {
    private Integer id;
    private String itemName;
    private String itemCollection;
    private String authorFullName;
    private String itemImageUrl;
    private List<TagProjection> itemTag;
    private List<ItemCustomColumnProjection> itemCustomColumn;
    private String dateTime;
}
