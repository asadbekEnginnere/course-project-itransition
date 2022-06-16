package com.itransition.courseproject.repository;

import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
    User findByEmail(String email);

    @Query("SELECT new com.itransition.courseproject.dto.UserDto(user.id, user.firstName,user.lastName,user.username,user.email,user.role,user.isBlocked,user.lastLoginTime) FROM com.itransition.courseproject.entity.user.User user")
    List<UserDto> getAllUsers();
}
