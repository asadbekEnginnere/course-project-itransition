package com.itransition.courseproject.service;


// Asatbek Xalimjonov 6/17/22 1:56 AM


import com.itransition.courseproject.repository.TopicRepository;
import com.itransition.courseproject.service.interfaces.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

}
