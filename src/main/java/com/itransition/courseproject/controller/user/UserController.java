package com.itransition.courseproject.controller.user;


// Asatbek Xalimjonov 6/15/22 11:00 AM

import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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
                                        @RequestParam(required = false) String search,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size){
        List<UserDto> allUser = userService.getAllUsers();
        model.addAttribute("users",allUser);
        return "admin/user/user";
    }


    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String getUserCreatePage(Model model) {
        model.addAttribute("user",new UserDto());
        return "admin/user/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String saveUser(UserDto userDto,RedirectAttributes ra) {
        return userService.saveUser(userDto,ra);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String getUserEditPage(Model model, @PathVariable Integer id) {
        UserDto userDto = userService.findById(id);
        model.addAttribute("user",userDto);
        return "admin/user/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String updateUser(UserDto userDto, RedirectAttributes ra) {
        return userService.editUser(userDto,ra);
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

    @GetMapping("/block/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String blockUserById(@PathVariable Integer id,RedirectAttributes ra){
        return userService.blockOrUnBlockUserById(id,ra,true);
    }

    @GetMapping("/unblock/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String unBlockUserById(@PathVariable Integer id,RedirectAttributes ra){
        return userService.blockOrUnBlockUserById(id,ra,false);
    }



}