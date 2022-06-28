package com.itransition.courseproject.controller.user;


// Asatbek Xalimjonov 6/15/22 11:00 AM

import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
                                        @RequestParam(required = false) String search,
                                        @RequestParam(required = false,defaultValue = "1") Integer page,
                                        @RequestParam(required = false,defaultValue = "4") Integer size){

        Page<UserDto> usersPage = userService.getAllDataByPage(page,size);
        List<UserDto> users = usersPage.getContent();
        model.addAttribute("users",users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("totalItems", usersPage.getTotalElements());
        return "admin/user/user";
    }

    @GetMapping("/view/{userId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String getAdminUserViewPage(Model model, @PathVariable Integer userId) {
        UserDto userDto = userService.findById(userId);
        model.addAttribute("user",userDto);
        return "admin/user/view";
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
        return userService.saveData(userDto,ra);
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
        return userService.updateData(userDto,ra);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String deleteUserById(@PathVariable Integer id, RedirectAttributes ra){
        return userService.deleteById(id,ra);
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
