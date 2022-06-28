package com.itransition.courseproject.controller.admin;


// Asatbek Xalimjonov 6/28/22 11:10 AM

import com.itransition.courseproject.dto.CustomColumnDto;
import com.itransition.courseproject.dto.ItemDetailDto;
import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.entity.collection.Comment;
import com.itransition.courseproject.entity.collection.Item;
import com.itransition.courseproject.projection.CommentProjection;
import com.itransition.courseproject.service.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/item")
@ControllerAdvice
@RequiredArgsConstructor
public class AdminItemController {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String max_size;

    private final ItemServiceImpl itemService;
    private final CommentServiceImpl commentService;
    private final LikeDislikeServiceImpl likeDislikeService;
    private final TagServiceImpl tagService;
    private final CollectionServiceImpl collectionService;


    @GetMapping("/{collectionId}/view/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public String getAdminItemViewPage(@PathVariable Integer collectionId, @PathVariable Integer itemId, Model model) {
        ItemDetailDto itemById = itemService.getItemById(itemId);
        List<CommentProjection> commentsByItemId = commentService.getCommentsByItemId(itemId);
        int likes = likeDislikeService.likesCount(itemId);
        int dislikes = likeDislikeService.disLikesCount(itemId);
        model.addAttribute("comment", new Comment());
        model.addAttribute("likes", likes);
        model.addAttribute("dislikes", dislikes);
        model.addAttribute("comments", commentsByItemId);
        model.addAttribute("item", itemById);
        return "admin/item/view";
    }

    @GetMapping("/{id}/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String itemCreatePage(Model model, @PathVariable Integer id) {
        List<CustomColumnDto> customColumnDos = itemService.getCustomColumn(id);
        List<TagDto> tags = tagService.getAllData();
        model.addAttribute("collection", collectionService.findById(id));
        model.addAttribute("tags", tags);
        model.addAttribute("columns", customColumnDos);
        return "admin/item/create";
    }

    @PostMapping("/{id}/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String itemSave(MultipartHttpServletRequest file,
                           @PathVariable Integer id,
                           HttpServletRequest request,
                           RedirectAttributes ra) {
        return itemService.saveItem(file, request, id, ra);
    }

    @GetMapping("/{id}/edit/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String itemEditPage(Model model, @PathVariable Integer id, @PathVariable Integer itemId) {
        List<CustomColumnDto> customColumnDos = itemService.getCustomColumnWithValue(id, itemId);
        Item item = itemService.findById(itemId);
        if (item != null) {
            List<TagDto> tags = tagService.getAllData();
            model.addAttribute("item", item);
            model.addAttribute("collection", collectionService.findById(id));
            model.addAttribute("tags", tags);
            model.addAttribute("columns", customColumnDos);
            return "admin/item/edit";
        }
        return "redirect:/admin/collection/view/" + id;
    }

    @PostMapping("/{id}/edit/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String itemEdit(MultipartHttpServletRequest file, @PathVariable Integer id, HttpServletRequest request, RedirectAttributes ra, @PathVariable Integer itemId) {
        return itemService.editItem(file, request, id, ra, itemId);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadError(RedirectAttributes ra) {
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message", "Too large file max siz " + max_size);
        return "redirect:/admin/collection";
    }

    @PostMapping("/{collectionId}/delete/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String deleteItemById(@PathVariable Integer collectionId, @PathVariable Integer itemId, RedirectAttributes redirectAttributes) {
        return itemService.deleteItemById(collectionId, itemId, redirectAttributes);
    }


}
