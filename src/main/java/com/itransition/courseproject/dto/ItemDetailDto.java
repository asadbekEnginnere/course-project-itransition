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
    private List<TagDto> itemTag;
    private List<ItemCustomColumnDto> itemCustomColumn;
    private List<CommentDto> comments;
    private String dateTime;
}
