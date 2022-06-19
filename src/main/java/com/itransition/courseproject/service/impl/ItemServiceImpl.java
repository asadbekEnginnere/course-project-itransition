package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/18/22 6:39 PM


import com.itransition.courseproject.dto.CustomColumnDto;
import com.itransition.courseproject.entity.collection.*;
import com.itransition.courseproject.repository.*;
import com.itransition.courseproject.service.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final CollectionItemColumnRepository collectionItemColumnRepository;
    private final CustomColumnRepository customColumnRepository;
    private final CollectionRepository collectionRepository;
    private final TagRepository tagRepository;
    private final ItemRepository itemRepository;
    private final CustomValueRepository customValueRepository;

    @Override
    public List<CustomColumnDto> getCustomColumn(Integer id) {

        List<CustomColumnDto> columnDtoList = new ArrayList<>();

        List<Integer> customColumns = collectionItemColumnRepository.collectCustomColumnId(id);

        for (Integer customColumn : customColumns) {
            if (customColumnRepository.findById(customColumn).isPresent()) {
                CustomColumn customColumnFound = customColumnRepository.findById(customColumn).get();
                columnDtoList.add(new CustomColumnDto(
                        customColumnFound.getId(),
                        customColumnFound.getName(),
                        customColumnFound.getType().name()
                ));
            }
        }
        return columnDtoList;
    }

    @Override
    public String saveItem(HttpServletRequest request, Integer id, RedirectAttributes ra) {
        String status="error";
        String message="Creating error";
        String name = request.getParameter("name");
        String[] tagsIds = request.getParameterValues("tagsId");
        List<Integer> tagsID = new ArrayList<>();

        for(String s : tagsIds) tagsID.add(Integer.valueOf(s));

        if (collectionRepository.findById(id).isPresent()) {
            try {
                Collection collection = collectionRepository.findById(id).get();
                List<Tag> tags = tagRepository.findAllById(tagsID);

                Item item = new Item(
                        name,
                        collection,
                        tags
                );

                Item savedItem = itemRepository.save(item);

                Map<CustomColumn,String> customColumnStringMap = new HashMap<>();

                Map<String, String[]> parameterMap = request.getParameterMap();


                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

                    try {
                        if (!entry.getKey().equals("name") && !entry.getKey().equals("tagsId")) {
                            Integer columnId = Integer.valueOf(entry.getKey());
                            CustomColumn customColumn = customColumnRepository.findById(columnId).get();
                            if (customColumn!=null) {
                                String value = "";
                                for (String s : entry.getValue()) {
                                    value = s;
                                }
                                customColumnStringMap.put(customColumn, value);
                            }
                        }
                    }catch (Exception e){}
                }


                for (Map.Entry<CustomColumn, String> entry : customColumnStringMap.entrySet()) {
                    CustomValue customValue = new CustomValue(
                            entry.getValue(),
                            entry.getKey(),
                            savedItem
                    );
                    customValueRepository.save(customValue);
                }

                status="success";
                message="Successfully Created";

            }catch (Exception e){}
        }


        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message",message);
        return "redirect:/user/collection/view/"+id;
    }
}
