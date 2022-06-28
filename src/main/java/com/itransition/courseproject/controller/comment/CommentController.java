package com.itransition.courseproject.controller.comment;


// Asatbek Xalimjonov 6/23/22 1:59 PM

import com.itransition.courseproject.entity.collection.Comment;
import com.itransition.courseproject.service.impl.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentServiceImpl commentService;


    @PostMapping("/add/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    public String saveComment(Comment comment, RedirectAttributes ra,
                              @PathVariable Integer itemId,
                              @RequestParam(required = false) Integer commentId
    ) {
        if (commentId!=null)return commentService.updateComment(comment.getContent(),commentId,ra,itemId);
        return commentService.saveData(comment, ra, itemId);
    }

    @GetMapping("/delete/{deleteId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    public String deleteComment(@PathVariable Integer deleteId) {
        return commentService.deleteCommentById(deleteId);
    }


}
