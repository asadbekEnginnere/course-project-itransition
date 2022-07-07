package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/23/22 2:04 PM


import com.itransition.courseproject.entity.collection.Comment;
import com.itransition.courseproject.entity.collection.Item;
import com.itransition.courseproject.entity.enums.Role;
import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.projection.CommentProjection;
import com.itransition.courseproject.repository.CommentRepository;
import com.itransition.courseproject.repository.ItemRepository;
import com.itransition.courseproject.service.interfaces.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserServiceImpl userService;
    private final ItemRepository itemRepository;

    @Override
    public String saveData(Comment commentData, RedirectAttributes ra, Integer itemId) {

        String message = "Commenting error";
        String status = "error";

        User user = userService.currenUser();

        if (user != null && itemRepository.findById(itemId).isPresent()) {

            try {
                Item item = itemRepository.findById(itemId).get();
                Comment comment = new Comment(
                        commentData.getContent(),
                        user,
                        item
                );
                commentRepository.save(comment);
                status = "success";
                message = "Successfully commented";
            } catch (Exception exception) {
            }
        }

        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message", message);

        return "redirect:/item/detail/" + itemId;
    }

    @Override
    public List<CommentProjection> getCommentsByItemId(Integer id) {
        return commentRepository.getAllCommentsByItemId(id);
    }

    @Override
    public int getTotalCommentsByUserId() {
        User user = userService.currenUser();
        if (user != null) {
            return commentRepository.getTotalCommentsByUserId(user.getId());
        }
        return 0;
    }

    @Override
    public String deleteCommentById(Integer deleteId) {

        Integer itemId = null;
        try {

            if (commentRepository.existsById(deleteId)) {
                Comment comment = commentRepository.findById(deleteId).get();
                itemId = comment.getItem().getId();
                commentRepository.deleteById(deleteId);
            }
        } catch (Exception exception) {
        }

        if (itemId != null) return "redirect:/item/detail/" + itemId;
        return "redirect:/";
    }

    @Override
    public String updateComment(String commentContent, Integer commentId, RedirectAttributes ra, Integer itemId) {

        User user = userService.currenUser();

        if (user != null && itemRepository.findById(itemId).isPresent()) {
            try {
                if (commentRepository.findById(commentId).isPresent()) {
                    Comment comment = commentRepository.findById(commentId).get();
                    if (comment.getUser().equals(user) || user.getRole().equals(Role.ROLE_ADMIN) || user.getRole().equals(Role.ROLE_SUPER_ADMIN)) {
                        comment.setContent(commentContent);
                        commentRepository.save(comment);
                    }
                }
            } catch (Exception exception) {
            }
        }

        return "redirect:/item/detail/" + itemId;
    }
}