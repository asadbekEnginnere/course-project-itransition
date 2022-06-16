package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.UserDto;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserDto> getAllUsers();

    Map<String, String> deleteUserById(Integer id);

    String saveUser(UserDto userDto, RedirectAttributes ra);

    String blockOrUnBlockUserById(Integer id, RedirectAttributes ra,boolean shouldBlockUser);

    UserDto getUserData();

    UserDto findById(Integer id);

    String editUser(UserDto userDto, RedirectAttributes ra);
}
