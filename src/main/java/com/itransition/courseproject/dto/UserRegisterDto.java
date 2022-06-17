package com.itransition.courseproject.dto;


// Asatbek Xalimjonov 6/17/22 3:54 PM

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;

}
