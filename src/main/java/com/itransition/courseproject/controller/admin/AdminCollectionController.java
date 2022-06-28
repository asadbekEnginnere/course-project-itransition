package com.itransition.courseproject.controller.admin;


// Asatbek Xalimjonov 6/28/22 10:31 AM

import com.itransition.courseproject.dto.CollectionDto;
import com.itransition.courseproject.service.impl.CollectionServiceImpl;
import com.itransition.courseproject.service.impl.ItemServiceImpl;
import com.itransition.courseproject.service.impl.TopicServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/collection")
@ControllerAdvice
@RequiredArgsConstructor
public class AdminCollectionController {


    @Value("${spring.servlet.multipart.max-file-size}")
    private String max_size;

    private final CollectionServiceImpl collectionService;
    private final ItemServiceImpl itemService;
    private final TopicServiceImpl topicService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getAdminCollectionPage(Model model) {
        model.addAttribute("collections", collectionService.getCollectionList());
        return "admin/collection/index";
    }

    @GetMapping("/view/{collectionId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getAdminCollectionViewPage(Model model, @PathVariable Integer collectionId) {
        model.addAttribute("collection", collectionService.collectionGetById(collectionId));
        model.addAttribute("items", itemService.getAllItemsByCollectionId(collectionId));
        return "admin/collection/view";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public String getAdminCollectionCreatePage(Model model) {
        model.addAttribute("topics", topicService.getAllData());
        return "admin/collection/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String saveCollection(MultipartFile image, HttpServletRequest request, RedirectAttributes ra) {
        return collectionService.saveCollectionWithItemField(image, request, ra);
    }

    @GetMapping("/edit/{collectionId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getCollectionEditPage(Model model, @PathVariable Integer collectionId) {
        model.addAttribute("collection", collectionService.getCollectionById(collectionId));
        model.addAttribute("topics", topicService.getAllData());
        return "admin/collection/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String editCollection(MultipartFile file, CollectionDto collectionDto, RedirectAttributes ra) {
        return collectionService.updateCollection(collectionDto, ra, file);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadError(RedirectAttributes ra) {
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message", "Too large file max siz " + max_size);
        return "redirect:/admin/collection";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String deleteCollectionById(@PathVariable Integer id, RedirectAttributes ra) {
        return collectionService.deleteById(id, ra);
    }

}
