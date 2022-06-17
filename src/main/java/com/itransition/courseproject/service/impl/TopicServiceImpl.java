package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/17/22 1:56 AM


import com.itransition.courseproject.dto.TopicDto;
import com.itransition.courseproject.entity.collection.Topic;
import com.itransition.courseproject.repository.TopicRepository;
import com.itransition.courseproject.service.interfaces.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    public List<TopicDto> getAllTopic() {
        return topicRepository.getAllTopic();
    }

    @Override
    public String saveTopic(TopicDto topicDto, RedirectAttributes ra) {

        if (topicRepository.findByName(topicDto.getName())==null) {
            try {
                Topic topic = new Topic();
                topic.setName(topicDto.getName());
                topicRepository.save(topic);
                ra.addFlashAttribute("status", "success");
                ra.addFlashAttribute("message", "Successfully created");
                return "redirect:/admin/topic";
            }catch (Exception e){
            }
        }

        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Topic Already exist with this data");
        return "redirect:/admin/topic/create";
    }

    @Override
    public String deleteTopicByUserId(Integer id, RedirectAttributes ra) {
        if (topicRepository.existsById(id)) {
            try {
                topicRepository.deleteById(id);
                ra.addFlashAttribute("status", "success");
                ra.addFlashAttribute("message", "Successfully deleted");
                return "redirect:/admin/topic";
            }catch (Exception e){
            }
        }
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Deleting Error");
        return "redirect:/admin/topic";
    }

    @Override
    public TopicDto findById(Integer id) {
        if (topicRepository.existsById(id)) {
            Topic topic = topicRepository.findById(id).get();
            return new TopicDto(topic.getId(),topic.getName());
        }
        return null;
    }

    @Override
    public String updateTopic(TopicDto topicDto, RedirectAttributes ra) {
        if (topicRepository.existsById(topicDto.getId())) {
            try {
                if (topicDto.getName().length()>0) {
                    Topic topic = new Topic(topicDto.getId(), topicDto.getName());
                    topicRepository.save(topic);
                    ra.addFlashAttribute("status", "success");
                    ra.addFlashAttribute("message", "Successfully Updated");
                    return "redirect:/admin/topic";
                }
            }catch (Exception e){
            }
        }
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Updating Error");
        return "redirect:/admin/topic/edit/"+topicDto.getId();
    }
}
