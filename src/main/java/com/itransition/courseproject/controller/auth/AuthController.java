package com.itransition.courseproject.controller.auth;


// Asatbek Xalimjonov 6/15/22 9:46 AM

import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {


    @GetMapping("/signing")
    public String getSigningPage(Model model){
        return "signing";
    }

    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup";
    }
}
