package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.TopicDto;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface TopicService {
    List<TopicDto> getAllTopic();

    String saveTopic(TopicDto topicDto, RedirectAttributes ra);

    String deleteTopicByUserId(Integer id, RedirectAttributes ra);

    TopicDto findById(Integer id);

    String updateTopic(TopicDto topicDto, RedirectAttributes ra);
}
