package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.CustomColumnDto;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ItemService {

    List<CustomColumnDto> getCustomColumn(Integer id);

    String saveItem(HttpServletRequest request, Integer id, RedirectAttributes ra);
}
