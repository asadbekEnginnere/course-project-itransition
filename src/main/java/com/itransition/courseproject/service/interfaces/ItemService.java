package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.CustomColumnDto;

import java.util.List;

public interface ItemService {
    List<CustomColumnDto> getCustomColumn(Integer id);
}
