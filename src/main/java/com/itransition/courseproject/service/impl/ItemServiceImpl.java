package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/18/22 6:39 PM


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itransition.courseproject.dto.CustomColumnDto;
import com.itransition.courseproject.dto.ItemDetailDto;
import com.itransition.courseproject.entity.collection.Collection;
import com.itransition.courseproject.entity.collection.*;
import com.itransition.courseproject.projection.ItemProjection;
import com.itransition.courseproject.repository.*;
import com.itransition.courseproject.service.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final CollectionItemColumnRepository collectionItemColumnRepository;
    private final CustomColumnRepository customColumnRepository;
    private final CollectionRepository collectionRepository;
    private final TagRepository tagRepository;
    private final Cloudinary cloudinary;
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

    public List<ItemProjection> getAllItemsByCollectionId(Integer collectionId) {
        return itemRepository.getAllItemsByCollectionId(collectionId);
    }

    @Override
    public String saveItem(MultipartHttpServletRequest file, HttpServletRequest request, Integer id, RedirectAttributes ra) {

        String status = "error";
        String message = "Creating error";
        String name = request.getParameter("name");
        String[] tagsIds = request.getParameterValues("tagsId");
        List<Integer> tagsID = new ArrayList<>();

        for (String s : tagsIds) tagsID.add(Integer.valueOf(s));

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


                String imageUrl = null;
                Iterator<String> iterator = file.getFileNames();
                MultiValueMap<String, MultipartFile> multiFileMap = file.getMultiFileMap();
                MultipartFile multipartFile = null;
                while (iterator.hasNext()) {
                    multipartFile = file.getFile(iterator.next());
                }

                if (multipartFile != null) {
                    File fileForServer = convertMultiPartToFile(multipartFile);
                    Map uploadResult = cloudinary.uploader().upload(fileForServer, ObjectUtils.emptyMap());
                    imageUrl = (String) uploadResult.get("url");
                    System.out.println("Url : " + imageUrl);

                    Iterator<String> mapIterator = multiFileMap.keySet().iterator();
                    String columnId = "";
                    while (mapIterator.hasNext()) {
                        columnId = mapIterator.next();
                    }

                    CustomColumn customColumn = customColumnRepository.findById(Integer.valueOf(columnId)).get();

                    if (customColumn != null) {

                        CustomValue customValue = new CustomValue(
                                imageUrl,
                                customColumn,
                                savedItem
                        );
                        customValueRepository.save(customValue);
                    }

                }

                Map<CustomColumn, String> customColumnStringMap = new HashMap<>();

                Map<String, String[]> parameterMap = request.getParameterMap();


                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

                    try {
                        if (!entry.getKey().equals("name") && !entry.getKey().equals("tagsId")) {
                            Integer columnId = Integer.valueOf(entry.getKey());
                            CustomColumn customColumn = customColumnRepository.findById(columnId).get();
                            if (customColumn != null) {
                                String value = "";
                                for (String s : entry.getValue()) {
                                    value = s;
                                }
                                customColumnStringMap.put(customColumn, value);
                            }
                        }
                    } catch (Exception e) {
                    }
                }


                for (Map.Entry<CustomColumn, String> entry : customColumnStringMap.entrySet()) {
                    CustomValue customValue = new CustomValue(
                            entry.getValue(),
                            entry.getKey(),
                            savedItem
                    );
                    customValueRepository.save(customValue);
                }

                status = "success";
                message = "Successfully Created";

            } catch (Exception e) {
            }
        }


        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message", message);
        return "redirect:/user/collection/view/" + id;
    }

    @Override
    public List<ItemProjection> getLatestItems() {
        return itemRepository.getLatestItems();
    }

    @Override
    public ItemDetailDto getItemById(Integer id) {
        ItemDetailDto itemDetail = new ItemDetailDto();
        String result = itemRepository.getItemById(id);

        ObjectMapper mapper = new ObjectMapper();
        try {
            ItemDetailDto itemDetailMapper = mapper.readValue(result, ItemDetailDto.class);
            log.error("item : ", itemDetailMapper);
            System.out.println(itemDetailMapper);
            return itemDetailMapper;
        } catch (Exception e) {
        }
        return itemDetail;
    }

    @Override
    public List<ItemProjection> getAllItems() {
        return itemRepository.getAllItems();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File("src/main/resources/file/" + file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}