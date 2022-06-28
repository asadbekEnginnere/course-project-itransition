package com.itransition.courseproject.dto;


// Asatbek Xalimjonov 6/18/22 10:34 AM

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CollectionDto {


    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private String creationTime;
    private Integer topicId;
    private List<CustomColumnDto> customColumns;
    private String topic;

    @Transient
    private MultipartFile file;

    public CollectionDto(Integer id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public CollectionDto(Integer id, String name, String description, String imageUrl, String creationTime, Integer topicId, List<CustomColumnDto> customColumns, String topic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.creationTime = creationTime;
        this.topicId = topicId;
        this.customColumns = customColumns;
        this.topic = topic;
    }
}
