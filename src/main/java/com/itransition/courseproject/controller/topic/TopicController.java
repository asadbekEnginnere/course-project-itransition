package com.itransition.courseproject.controller.topic;


// Asatbek Xalimjonov 6/17/22 2:00 AM

import com.itransition.courseproject.service.TopicServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.plaf.PanelUI;

@Controller
@RequestMapping("/admin/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicServiceImpl topicService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getAllTopic(Model model,
                              @RequestParam(required = false)String search,
                              @RequestParam(required = false)Integer page,
                              @RequestParam(required = false)Integer size){
        return "admin/topic/index";
    }


}
