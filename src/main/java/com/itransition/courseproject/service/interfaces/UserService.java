package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.dto.UserRegisterDto;
import com.itransition.courseproject.entity.user.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserDto> getAllUsers();
    List<UserDto> getAllUsers(int pageId,int total);

    Map<String, String> deleteUserById(Integer id);

    String saveUser(UserDto userDto, RedirectAttributes ra);

    String blockOrUnBlockUserById(Integer id, RedirectAttributes ra,boolean shouldBlockUser);

    UserDto getUserData();

    UserDto findById(Integer id);

    String editUser(UserDto userDto, RedirectAttributes ra);

    List<UserDto> search(String search);

    String registerUser(UserRegisterDto userDto, RedirectAttributes ra);

    User currenUser();
}
