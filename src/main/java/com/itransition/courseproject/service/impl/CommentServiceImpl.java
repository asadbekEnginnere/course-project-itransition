package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/23/22 2:04 PM


import com.itransition.courseproject.entity.collection.Comment;
import com.itransition.courseproject.entity.collection.Item;
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
}