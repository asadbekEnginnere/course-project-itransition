package com.itransition.courseproject.controller.likeDislike;


// Asatbek Xalimjonov 6/23/22 6:09 PM


import com.itransition.courseproject.service.impl.LikeDislikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/likeDislike")
public class LikeDislikeController {

    private final LikeDislikeServiceImpl likeDislikeService;

    @GetMapping("/like/add/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String addLikeToItem(@PathVariable Integer itemId, RedirectAttributes ra){
        return likeDislikeService.addLikeDislike(true,itemId,ra);
    }

    @GetMapping("/dislike/add/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String addDisLikeToItem(@PathVariable Integer itemId, RedirectAttributes ra){
        return likeDislikeService.addLikeDislike(false,itemId,ra);
    }

}
