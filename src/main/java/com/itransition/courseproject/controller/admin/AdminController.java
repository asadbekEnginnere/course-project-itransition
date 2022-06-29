package com.itransition.courseproject.controller.admin;


// Asatbek Xalimjonov 6/15/22 10:59 AM

import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.dto.TopicDto;
import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.entity.collection.Tag;
import com.itransition.courseproject.repository.*;
import com.itransition.courseproject.service.impl.TagServiceImpl;
import com.itransition.courseproject.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;
    private final TagServiceImpl tagService;
    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final DisLikeRepository disLikeRepository;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getAdminPage(Model model){
        model.addAttribute("users",userRepository.findAll().size());
        model.addAttribute("collections",collectionRepository.findAll().size());
        model.addAttribute("items",itemRepository.findAll().size());
        model.addAttribute("comments",commentRepository.findAll().size());
        model.addAttribute("likes",likeRepository.findAll().size());
        model.addAttribute("dislikes",disLikeRepository.findAll().size());
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
    public String getAllTags(Model model,
                             @RequestParam(required = false)String search,
                             @RequestParam(required = false,defaultValue = "1")Integer page,
                             @RequestParam(required = false,defaultValue = "4")Integer size){

        Page<TagDto> tagPage = tagService.getAllDataByPage(page,size);
        List<TagDto> tags = tagPage.getContent();
        model.addAttribute("tags",tags);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tagPage.getTotalPages());
        model.addAttribute("totalItems", tagPage.getTotalElements());
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
        return tagService.saveData(tagDto,ra);
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
        return tagService.updateData(tagDto,ra);
    }

}
