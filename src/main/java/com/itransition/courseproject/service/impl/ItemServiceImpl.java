package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/18/22 6:39 PM


import com.itransition.courseproject.dto.CustomColumnDto;
import com.itransition.courseproject.entity.collection.CustomColumn;
import com.itransition.courseproject.repository.CollectionItemColumnRepository;
import com.itransition.courseproject.repository.CustomColumnRepository;
import com.itransition.courseproject.service.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final CollectionItemColumnRepository collectionItemColumnRepository;
    private final CustomColumnRepository customColumnRepository;

    @Override
    public List<CustomColumnDto> getCustomColumn(Integer id) {

        List<CustomColumnDto> columnDtoList = new ArrayList<>();

        List<Integer> customColumns = collectionItemColumnRepository.collectCustomColumnId(id);

        for (Integer customColumn : customColumns) {
            if (customColumnRepository.findById(customColumn).isPresent()) {
                CustomColumn customColumnFound = customColumnRepository.findById(customColumn).get();
                columnDtoList.add(new CustomColumnDto(
                        customColumnFound.getName(),
                        customColumnFound.getType().name()
                ));
            }
        }
        return columnDtoList;
    }
}
