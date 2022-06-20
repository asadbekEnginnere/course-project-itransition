package com.itransition.courseproject.repository;

import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
    User findByEmail(String email);

    @Query("SELECT new com.itransition.courseproject.dto.UserDto(user.id, user.firstName,user.lastName,user.username,user.email,user.role,user.isBlocked,user.lastLoginTime) FROM com.itransition.courseproject.entity.user.User user where user.role<>'ROLE_SUPER_ADMIN'")
    List<UserDto> getAllUsers();

    Page<User> findAll(@NotNull Pageable pageable);
}
