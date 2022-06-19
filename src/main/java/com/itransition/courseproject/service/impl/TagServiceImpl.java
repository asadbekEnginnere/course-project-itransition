package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/18/22 9:53 PM


import com.itransition.courseproject.dto.TagDto;
import com.itransition.courseproject.entity.collection.Tag;
import com.itransition.courseproject.repository.TagRepository;
import com.itransition.courseproject.service.interfaces.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public String saveTag(HttpServletRequest request, TagDto tagDto, RedirectAttributes ra) {
        String status="error";
        String message="Creating error";
        try{
            if (tagRepository.findByName(tagDto.getName())!=null){
            }else {
                Tag tag = new Tag(tagDto.getName());
                tagRepository.save(tag);
                status="success";
                message="Successfully created Tag";
            }
        }catch (Exception e){
        }

        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message",message);

        try {
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }catch (Exception e){}

        return "redirect:/admin/tag";

    }

    @Override
    public String deleteById(Integer id,RedirectAttributes ra) {
        if (tagRepository.existsById(id)) {
            try {
                tagRepository.deleteById(id);
                ra.addFlashAttribute("status", "success");
                ra.addFlashAttribute("message", "Successfully deleted");
                return "redirect:/admin/tag";
            }catch (Exception e){
            }
        }
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Deleting Error");
        return "redirect:/admin/tag";
    }

    @Override
    public TagDto findById(Integer id) {
        if (tagRepository.existsById(id)) {
            Tag tag = tagRepository.findById(id).get();
            return new TagDto(tag.getId(),tag.getName());
        }
        return null;
    }

    @Override
    public String editTag(TagDto tagDto, RedirectAttributes ra) {
        if (tagRepository.existsById(tagDto.getId())) {
            try {
                if (tagDto.getName().length()>0) {
                    Tag tag = new Tag(tagDto.getId(), tagDto.getName());
                    tagRepository.save(tag);
                    ra.addFlashAttribute("status", "success");
                    ra.addFlashAttribute("message", "Successfully Updated");
                    return "redirect:/admin/tag";
                }
            }catch (Exception e){
            }
        }
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Updating Error");
        return "redirect:/admin/tag/edit/"+tagDto.getId();
    }
}
