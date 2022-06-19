package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.entity.collection.Tag;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TagService {
    List<TagDto> getAllTags();

    String saveTag(HttpServletRequest request, TagDto tagDto, RedirectAttributes ra);

    String deleteById(Integer id,RedirectAttributes ra);

    TagDto findById(Integer id);

    String editTag(TagDto tagDto, RedirectAttributes ra);
}
