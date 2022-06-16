package com.itransition.courseproject.controller.admin;


// Asatbek Xalimjonov 6/15/22 10:59 AM

import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String getAdminPage(){
        return "admin/dashboard";
    }




}
