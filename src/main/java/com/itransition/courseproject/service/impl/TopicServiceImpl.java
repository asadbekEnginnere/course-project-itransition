package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/17/22 1:56 AM


import com.itransition.courseproject.dto.TopicDto;
import com.itransition.courseproject.entity.collection.Topic;
import com.itransition.courseproject.repository.TopicRepository;
import com.itransition.courseproject.service.interfaces.GenericInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicServiceImpl implements  GenericInterface<TopicDto,Integer,String> {

    private final TopicRepository topicRepository;

    @Override
    public List<TopicDto> getAllData() {
        return topicRepository.getAllTopic();
    }

    @Override
    public Page<TopicDto> getAllDataByPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
                page-1,
                size,
                Sort.by("id")
        );

        Page<Topic> topicPage = topicRepository.findAll(pageable);
        int totalElements = (int) topicPage.getTotalElements();
        return new PageImpl<TopicDto>(topicPage.getContent()
                .stream()
                .map(topic -> new TopicDto(
                                topic.getId(),
                                topic.getName()
                        )
                )
                .collect(Collectors.toList()), pageable, totalElements);
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
    public String saveData(TopicDto topicDto, RedirectAttributes ra) {

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
    public String updateData(TopicDto topicDto, RedirectAttributes ra) {

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

    @Override
    public String deleteById(Integer id, RedirectAttributes ra) {
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


}
