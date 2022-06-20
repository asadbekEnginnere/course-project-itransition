package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.CollectionDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CollectionService {

    String saveCollectionWithItemField(MultipartFile file, HttpServletRequest request, RedirectAttributes ra);
}
