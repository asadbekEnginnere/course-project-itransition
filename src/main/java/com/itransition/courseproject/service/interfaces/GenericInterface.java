package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.TopicDto;
import com.itransition.courseproject.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface GenericInterface<T,S,M> {

    List<T> getAllData();

    Page<T> getAllDataByPage(S page, S size);

    T findById(S id);

    M saveData(T t, RedirectAttributes ra);

    M updateData(T t, RedirectAttributes ra);

    M deleteById(S id,RedirectAttributes ra);



}
