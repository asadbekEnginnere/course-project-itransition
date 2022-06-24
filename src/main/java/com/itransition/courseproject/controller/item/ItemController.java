package com.itransition.courseproject.controller.item;


// Asatbek Xalimjonov 6/18/22 3:50 PM

import com.itransition.courseproject.dto.CustomColumnDto;
import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.service.impl.CollectionServiceImpl;
import com.itransition.courseproject.service.impl.ItemServiceImpl;
import com.itransition.courseproject.service.impl.TagServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
@RequestMapping("/user/collection/item")
public class ItemController {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String max_size;

    private final TagServiceImpl tagService;
    private final ItemServiceImpl itemService;
    private final CollectionServiceImpl collectionService;

    @GetMapping("/{id}/create")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String itemCreatePage(Model model, @PathVariable Integer id){
        List<CustomColumnDto> customColumnDos = itemService.getCustomColumn(id);
        List<TagDto> tags = tagService.getAllData();
        model.addAttribute("collection",collectionService.findById(id));
        model.addAttribute("tags",tags);
        model.addAttribute("columns",customColumnDos);
        return "item/create";
    }

    @PostMapping("/{id}/create")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String itemSave(MultipartHttpServletRequest file, @PathVariable Integer id, HttpServletRequest request, RedirectAttributes ra){
        return itemService.saveItem(file,request,id,ra);
    }


    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String deleteItemById(@PathVariable Integer id){
        return null;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadError(RedirectAttributes ra)
    {
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Too large file max siz " + max_size);
        return "redirect:/user/profile";
    }


}
