package com.itransition.courseproject.controller.user;


// Asatbek Xalimjonov 6/15/22 11:00 AM

import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getUserManagementPage(Model model,
                                        @RequestParam(required = false)String search,
                                        @RequestParam(required = false)Integer page,
                                        @RequestParam(required = false)Integer size){
        List<UserDto> allUsers = userService.getAllUsers();
        model.addAttribute("users",allUsers);
        return "admin/user/user";
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String addUser(Model model) {
        model.addAttribute("user",new UserDto());
        return "admin/user/create";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String deleteUserById(@PathVariable Integer id, RedirectAttributes ra){

        Map<String,String> messages = userService.deleteUserById(id);
        Map.Entry<String,String> entry = messages.entrySet().iterator().next();
        String key = entry.getKey();
        String value = entry.getValue();

        ra.addFlashAttribute("status", key);
        ra.addFlashAttribute("message", value);

        return "redirect:/admin/user";
    }

}
