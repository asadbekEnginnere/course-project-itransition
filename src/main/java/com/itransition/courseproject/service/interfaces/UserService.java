package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserDto> getAllUsers();

    Map<String, String> deleteUserById(Integer id);
}
