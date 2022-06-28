package com.itransition.courseproject.controller;


// Asatbek Xalimjonov 6/14/22 10:06 AM

import com.itransition.courseproject.cloudinary.CloudForImage;
import com.itransition.courseproject.dto.CommentDto;
import com.itransition.courseproject.dto.ItemDetailDto;
import com.itransition.courseproject.entity.collection.Comment;
import com.itransition.courseproject.entity.collection.CustomColumn;
import com.itransition.courseproject.projection.CollectionProjection;
import com.itransition.courseproject.projection.CommentProjection;
import com.itransition.courseproject.projection.ItemProjection;
import com.itransition.courseproject.repository.CustomColumnRepository;
import com.itransition.courseproject.repository.TagRepository;
import com.itransition.courseproject.service.impl.CollectionServiceImpl;
import com.itransition.courseproject.service.impl.CommentServiceImpl;
import com.itransition.courseproject.service.impl.ItemServiceImpl;
import com.itransition.courseproject.service.impl.LikeDislikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {


    private final CustomColumnRepository customColumnRepository;
    private final TagRepository tagRepository;
    private final CollectionServiceImpl collectionService;
    private final ItemServiceImpl itemService;
    private final CommentServiceImpl commentService;
    private final LikeDislikeServiceImpl likeDislikeService;


    @GetMapping
    public String getHomePage(Model model) {
        model.addAttribute("items", itemService.getLatestItems());
        model.addAttribute("collections", collectionService.getTopFiveLargestCollection());
        model.addAttribute("tags", tagRepository.getAllTags());
        return "home";
    }

    @GetMapping("/collection")
    public String getCollectionPage(Model model) {
        model.addAttribute("collections", collectionService.getCollectionList());
        return "collection";
    }

    @GetMapping(value = {"/item", "/item/tag/{tagId}","/item/collection/{collectionId}"})
    public String getItemPage(Model model,
                              @PathVariable(required = false) Integer tagId,
                              @PathVariable(required = false) Integer collectionId) {

        List<ItemProjection> allItems = null;
        CollectionProjection byId=null;

        if (collectionId!=null){
            allItems=itemService.getAllItemsByCollectionId(collectionId);
            byId = collectionService.collectionGetById(collectionId);
        }else if (tagId!=null){
            allItems=itemService.getItemByTagId(tagId);
        }else{
            allItems=itemService.getAllItems();
        }

        model.addAttribute("collection",byId);
        model.addAttribute("items", allItems);
        return "item";
    }

    @GetMapping("/item/detail/{id}")
    public String itemDetailPage(Model model, @PathVariable Integer id) {
        ItemDetailDto itemById = itemService.getItemById(id);
        List<CommentProjection> commentsByItemId = commentService.getCommentsByItemId(id);
        int likes = likeDislikeService.likesCount(id);
        int dislikes = likeDislikeService.disLikesCount(id);
        model.addAttribute("comment", new Comment());
        model.addAttribute("likes", likes);
        model.addAttribute("dislikes", dislikes);
        model.addAttribute("comments", commentsByItemId);
        model.addAttribute("item", itemById);
        return "item/detail";
    }

}
