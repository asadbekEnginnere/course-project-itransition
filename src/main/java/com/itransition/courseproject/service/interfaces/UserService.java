package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.dto.UserRegisterDto;
import com.itransition.courseproject.entity.user.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

public interface UserService {


    String blockOrUnBlockUserById(Integer id, RedirectAttributes ra,boolean shouldBlockUser);

    UserDto getUserData();

    List<UserDto> search(String search);

    String registerUser(UserRegisterDto userDto, RedirectAttributes ra);

    User currenUser();
}
