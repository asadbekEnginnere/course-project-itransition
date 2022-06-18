package com.itransition.courseproject.controller.collection;


// Asatbek Xalimjonov 6/18/22 12:52 AM

import com.itransition.courseproject.dto.CollectionDto;
import com.itransition.courseproject.service.impl.CollectionServiceImpl;
import com.itransition.courseproject.service.impl.TopicServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@ControllerAdvice
@RequestMapping("/user/collection")
@RequiredArgsConstructor
@Slf4j
public class CollectionController {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String max_size;

    private final CollectionServiceImpl collectionService;
    private final TopicServiceImpl topicService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCollectionPage(Model model){
        model.addAttribute("collections",collectionService.getAllCollection());
        return "collection/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCollectionCreatePage(Model model){
        model.addAttribute("topics",topicService.getAllTopic());
        return "collection/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String saveCollection(MultipartFile image,HttpServletRequest request, RedirectAttributes ra){
        return collectionService.saveCollectionWithItemField(image,request,ra);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadError(RedirectAttributes ra)
    {
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Too large file max siz " + max_size);
        return "redirect:/user/collection";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteCollectionById(@PathVariable Integer id,RedirectAttributes ra){
        return collectionService.deleteCollectionById(id,ra);
    }



}
