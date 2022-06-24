package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.CustomColumnDto;
import com.itransition.courseproject.dto.ItemDetailDto;
import com.itransition.courseproject.projection.ItemProjection;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ItemService {

    List<CustomColumnDto> getCustomColumn(Integer id);

    String saveItem(MultipartHttpServletRequest file, HttpServletRequest request, Integer id, RedirectAttributes ra);

    List<ItemProjection> getLatestItems();

    ItemDetailDto getItemById(Integer id);

    List<ItemProjection> getAllItems();

    List<ItemProjection> getItemByTagId(Integer tagId);

    int getTotalItemsByUserId();
}
