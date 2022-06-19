package com.itransition.courseproject.controller.admin;


// Asatbek Xalimjonov 6/15/22 10:59 AM

import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.entity.collection.Tag;
import com.itransition.courseproject.service.impl.TagServiceImpl;
import com.itransition.courseproject.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;
    private final TagServiceImpl tagService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getAdminPage(){
        return "admin/dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getProfilePage(Model model){
        UserDto userDto = userService.getUserData();
        model.addAttribute("user",userDto);
        return "admin/user/profile";
    }


    //TAG CREATE , EDIT , DELETE , LIST BY ADMIN, SUPER ADMIN PAGE


    @GetMapping("/tag")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getTagPage(Model model){
        model.addAttribute("tags",tagService.getAllTags());
        return "admin/tag/index";
    }

    @PostMapping("/tag/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String deleteTagById(@PathVariable Integer id, RedirectAttributes ra){
        return tagService.deleteById(id,ra);
    }

    @GetMapping("/tag/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getAdminTageCreatePage(Model model){
        model.addAttribute("tag",new TagDto());
        return "admin/tag/create";
    }

    @PostMapping("/tag/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getAdminTageCreatePage(RedirectAttributes ra,TagDto tagDto){
        return tagService.saveTag(null,tagDto,ra);
    }

    @GetMapping("/tag/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getAdminTagEditPage(Model model, @PathVariable Integer id){
        TagDto tag = tagService.findById(id);
        if (tag!=null) {
            model.addAttribute("tag", tag);
            return "admin/tag/edit";
        }
        return "redirect:/admin/tag";
    }

    @PostMapping("/tag/edit")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String updateTag(RedirectAttributes ra,TagDto tagDto){
        return tagService.editTag(tagDto,ra);
    }

}
