package com.itransition.courseproject.controller.topic;


// Asatbek Xalimjonov 6/17/22 2:00 AM

import com.itransition.courseproject.dto.TopicDto;
import com.itransition.courseproject.service.impl.TopicServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicServiceImpl topicService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getAllTopics(Model model,
                              @RequestParam(required = false)String search,
                              @RequestParam(required = false,defaultValue = "1")Integer page,
                              @RequestParam(required = false,defaultValue = "4")Integer size){

        Page<TopicDto> topicPage = topicService.getAllDataByPage(page,size);
        List<TopicDto> topics = topicPage.getContent();
        model.addAttribute("topics",topics);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", topicPage.getTotalPages());
        model.addAttribute("totalItems", topicPage.getTotalElements());
        return "admin/topic/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getTopicCreatePage(Model model){
        model.addAttribute("topic",new TopicDto());
        return "admin/topic/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String saveTopic(TopicDto topicDto, RedirectAttributes ra){
        return topicService.saveData(topicDto,ra);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String getTopicEdit(Model model, @PathVariable Integer id){
        TopicDto topicDto = topicService.findById(id);
        if (topicDto!=null){
            model.addAttribute("topic",topicDto);
            return "admin/topic/edit";
        }
        return "redirect:/admin/topic";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String updateTopic(TopicDto topicDto, RedirectAttributes ra){
        return topicService.updateData(topicDto,ra);
    }



    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public String deleteTopicByUserId(RedirectAttributes ra, @PathVariable Integer id){
        return topicService.deleteById(id,ra);
    }




}
