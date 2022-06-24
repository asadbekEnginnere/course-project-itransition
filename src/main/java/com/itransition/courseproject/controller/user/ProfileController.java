package com.itransition.courseproject.controller.user;


// Asatbek Xalimjonov 6/17/22 6:29 PM

import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.service.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class ProfileController {

    private final UserServiceImpl userService;
    private final CollectionServiceImpl collectionService;
    private final ItemServiceImpl itemService;
    private final CommentServiceImpl commentService;
    private final LikeDislikeServiceImpl likeDislikeService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getProfilePage(Model model){
        model.addAttribute("user",userService.getUserData());
        model.addAttribute("totalCollections",collectionService.getTotalCollectionsByUserId());
        model.addAttribute("totalItems",itemService.getTotalItemsByUserId());
        model.addAttribute("totalComments",commentService.getTotalCommentsByUserId());
        model.addAttribute("totalLikes",likeDislikeService.getTotalLikesByUserId());
        model.addAttribute("totalDisLikes",likeDislikeService.getTotalDisLikesByUserId());
        return "user/profile";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getUserEditPage(Model model, @PathVariable Integer id) {
        if (userService.getUserData().getId().equals(id)) {
            UserDto userDto = userService.findById(id);
            model.addAttribute("user",userDto);
            return "user/edit";
        }
        return "redirect:/";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String updateUser(UserDto userDto, RedirectAttributes ra) {
        if (userService.getUserData().getId().equals(userDto.getId())) {
            return userService.updateData(userDto, ra);
        }
        return "redirect:/";
    }

}
