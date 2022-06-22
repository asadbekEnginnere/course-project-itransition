package com.itransition.courseproject.controller;


// Asatbek Xalimjonov 6/14/22 10:06 AM

import com.itransition.courseproject.cloudinary.CloudForImage;
import com.itransition.courseproject.entity.collection.CustomColumn;
import com.itransition.courseproject.repository.CustomColumnRepository;
import com.itransition.courseproject.repository.TagRepository;
import com.itransition.courseproject.service.impl.CollectionServiceImpl;
import com.itransition.courseproject.service.impl.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {


    private final CustomColumnRepository customColumnRepository;
    private final TagRepository tagRepository;
    private final CollectionServiceImpl collectionService;
    private final ItemServiceImpl itemService;


    @GetMapping
    public String getHomePage(Model model){
        model.addAttribute("items",itemService.getLatestItems());
        model.addAttribute("collections",collectionService.getTopFiveLargestCollection());
        model.addAttribute("tags",tagRepository.getAllTags());
        return "home";
    }

    @GetMapping("/collection")
    public String getCollectionPage(Model model){
        model.addAttribute("collections",collectionService.getCollectionList());
        return "collection";
    }

    @GetMapping("/item")
    public String getItemPage(Model model){
        return "item";
    }

}
