package com.itransition.courseproject.dto;


// Asatbek Xalimjonov 6/16/22 1:25 PM

import com.itransition.courseproject.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UserDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Role role;
    private boolean isBlocked;
    private LocalDateTime lastLoginTime;

    public UserDto(Integer id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }
}
