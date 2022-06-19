package com.itransition.courseproject.controller.tag;


// Asatbek Xalimjonov 6/19/22 9:54 PM


import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/tag")
@RequiredArgsConstructor
public class TagController {


    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String tagCreatePage(){

        return "tag/create";
    }
}
