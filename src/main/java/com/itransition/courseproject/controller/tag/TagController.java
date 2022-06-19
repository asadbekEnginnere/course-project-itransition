package com.itransition.courseproject.controller.tag;


// Asatbek Xalimjonov 6/19/22 9:54 PM


import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.service.impl.TagServiceImpl;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagServiceImpl tagService;

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String tagCreatePage(Model model){
        model.addAttribute("tag",new TagDto());
        return "tag/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String saveTag(HttpServletRequest request,TagDto tagDto, RedirectAttributes ra){
        return tagService.saveTag(request,tagDto,ra);
    }

}
