package com.itransition.courseproject.controller.item;


// Asatbek Xalimjonov 6/18/22 3:50 PM

import com.itransition.courseproject.dto.CustomColumnDto;
import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.entity.collection.Item;
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
    public String itemSave(MultipartHttpServletRequest file,
                           @PathVariable Integer id,
                           HttpServletRequest request,
                           RedirectAttributes ra){
        return itemService.saveItem(file,request,id,ra);
    }


    @GetMapping("/{id}/edit/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String itemEditPage(Model model, @PathVariable Integer id, @PathVariable Integer itemId){
        List<CustomColumnDto> customColumnDos = itemService.getCustomColumnWithValue(id,itemId);
        Item item = itemService.findById(itemId);
        if (item!=null) {
            List<TagDto> tags = tagService.getAllData();
            model.addAttribute("item", item);
            model.addAttribute("collection", collectionService.findById(id));
            model.addAttribute("tags", tags);
            model.addAttribute("columns", customColumnDos);
            return "item/edit";
        }
        return "redirect:/user/collection/view/"+id;
    }

    @PostMapping("/{id}/edit/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String itemEdit(MultipartHttpServletRequest file, @PathVariable Integer id, HttpServletRequest request, RedirectAttributes ra, @PathVariable Integer itemId){
        return itemService.editItem(file,request,id,ra,itemId);
    }

    @PostMapping("/{collectionId}/delete/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String deleteItemById(@PathVariable Integer collectionId,@PathVariable Integer itemId, RedirectAttributes redirectAttributes){
        return itemService.deleteItemById(collectionId,itemId,redirectAttributes);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadError(RedirectAttributes ra)
    {
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Too large file max siz " + max_size);
        return "redirect:/user/profile";
    }


}
