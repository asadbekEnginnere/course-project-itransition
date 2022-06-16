package com.itransition.courseproject.controller.admin;


// Asatbek Xalimjonov 6/15/22 10:59 AM

import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.List;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getAdminPage(){
        return "admin/dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getProfilePage(Model model){
        UserDto userDto = userService.getUserData();
        model.addAttribute("user",userDto);
        return "admin/user/profile";
    }


}
