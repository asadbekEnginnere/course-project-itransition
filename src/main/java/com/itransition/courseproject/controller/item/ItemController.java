package com.itransition.courseproject.controller.item;


// Asatbek Xalimjonov 6/18/22 3:50 PM

import com.itransition.courseproject.dto.CustomColumnDto;
import com.itransition.courseproject.service.impl.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class ItemController {

    private final ItemServiceImpl itemService;

    @GetMapping("/{id}/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String itemCreatePage(Model model, @PathVariable Integer id){
        List<CustomColumnDto> customColumnDos = itemService.getCustomColumn(id);
        customColumnDos.forEach(System.out::println);
        model.addAttribute("columns",customColumnDos);
        return "item/create";
    }
}
