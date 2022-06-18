package com.itransition.courseproject.controller;


// Asatbek Xalimjonov 6/14/22 10:06 AM

import com.itransition.courseproject.cloudinary.CloudForImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {


    @GetMapping
    public String getHomePage(Model model){
        return "home";
    }

    @PostMapping
    public void testPage(HttpServletRequest request) throws ServletException, IOException {

        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] topics = request.getParameterValues("topics");
        Collection<Part> parts = request.getParts();
    }
}
