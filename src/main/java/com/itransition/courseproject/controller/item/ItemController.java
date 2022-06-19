package com.itransition.courseproject.controller.item;


// Asatbek Xalimjonov 6/18/22 3:50 PM

import com.itransition.courseproject.dto.CustomColumnDto;
import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.service.impl.CollectionServiceImpl;
import com.itransition.courseproject.service.impl.ItemServiceImpl;
import com.itransition.courseproject.service.impl.TagServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/collection/item")
public class ItemController {

    private final TagServiceImpl tagService;
    private final ItemServiceImpl itemService;
    private final CollectionServiceImpl collectionService;

    @GetMapping("/{id}/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String itemCreatePage(Model model, @PathVariable Integer id){
        List<CustomColumnDto> customColumnDos = itemService.getCustomColumn(id);
        List<TagDto> tags = tagService.getAllTags();
        model.addAttribute("collection",collectionService.findById(id));
        model.addAttribute("tags",tags);
        model.addAttribute("columns",customColumnDos);
        return "item/create";
    }

    @PostMapping("/{id}/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String itemSave(HttpServletRequest request, @PathVariable Integer id, RedirectAttributes ra){
        return itemService.saveItem(request,id,ra);
    }
}
