package com.itransition.courseproject.controller.collection;


// Asatbek Xalimjonov 6/18/22 12:52 AM

import com.itransition.courseproject.dto.CollectionDto;
import com.itransition.courseproject.projection.CollectionProjection;
import com.itransition.courseproject.projection.ItemProjection;
import com.itransition.courseproject.service.impl.CollectionServiceImpl;
import com.itransition.courseproject.service.impl.ItemServiceImpl;
import com.itransition.courseproject.service.impl.TopicServiceImpl;
import com.itransition.courseproject.service.impl.UserServiceImpl;
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
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getCollectionPage(Model model){
        model.addAttribute("currentUser",userService.currenUser());
        model.addAttribute("collections",collectionService.getAllCollectionByUserId());
        return "collection/index";
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String showCollectionPage(@PathVariable Integer id,Model model){
        CollectionProjection byId = collectionService.collectionGetById(id);
        List<ItemProjection> allItemsByCollectionId = itemService.getAllItemsByCollectionId(id);
        model.addAttribute("items",allItemsByCollectionId);
        model.addAttribute("collection",byId);
        return "collection/view";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getCollectionCreatePage(Model model){
        model.addAttribute("topics",topicService.getAllData());
        return "collection/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String saveCollection(MultipartFile image,HttpServletRequest request, RedirectAttributes ra){
        return collectionService.saveCollectionWithItemField(image,request,ra);
    }

    @GetMapping("/edit/{collectionId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getCollectionEditPage(Model model, @PathVariable Integer collectionId){
        model.addAttribute("collection",collectionService.getCollectionById(collectionId));
        model.addAttribute("topics",topicService.getAllData());
        return "collection/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String editCollection(MultipartFile file,CollectionDto collectionDto,RedirectAttributes ra){
        return collectionService.updateCollection(collectionDto,ra,file);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadError(RedirectAttributes ra)
    {
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Too large file max siz " + max_size);
        return "redirect:/user/collection";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String deleteCollectionById(@PathVariable Integer id,RedirectAttributes ra){
        return collectionService.deleteById(id,ra);
    }



}
