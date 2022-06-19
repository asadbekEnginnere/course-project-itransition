package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/18/22 9:53 PM


import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.repository.TagRepository;
import com.itransition.courseproject.service.interfaces.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TagServiceImpl implements TagService {


    private final TagRepository tagRepository;

    @Override
    public List<TagDto> getAllTags() {
        return tagRepository.getAllTags();
    }
}
