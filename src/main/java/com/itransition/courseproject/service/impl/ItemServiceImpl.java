package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/18/22 6:39 PM


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itransition.courseproject.cloudinary.CloudForImage;
import com.itransition.courseproject.dto.CustomColumnDto;
import com.itransition.courseproject.dto.ItemDetailDto;
import com.itransition.courseproject.entity.collection.Collection;
import com.itransition.courseproject.entity.collection.*;
import com.itransition.courseproject.entity.enums.CustomColumnDataType;
import com.itransition.courseproject.entity.enums.Role;
import com.itransition.courseproject.entity.user.User;
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
    private final UserServiceImpl userService;
    private final CloudForImage cloudForImage;

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

    @Transactional
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

                Item item = new Item(name, collection, tags);

                Item savedItem = itemRepository.save(item);


                String imageUrl = null;
                imageUrl = saveItemImage(file, savedItem, imageUrl);

                Map<CustomColumn, String> customColumnStringMap = new HashMap<>();
                Map<String, String[]> parameterMap = request.getParameterMap();

                customItemColumn(customColumnStringMap, parameterMap);
                saveCustomColumnValue(savedItem, customColumnStringMap);

                status = "success";
                message = "Successfully Created";

            } catch (Exception e) {
            }
        }


        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message", message);
        User user = userService.currenUser();
        if (user.getRole().equals(Role.ROLE_ADMIN) || user.getRole().equals(Role.ROLE_SUPER_ADMIN))
            return "redirect:/admin/collection/view/" + id;
        return "redirect:/user/collection/view/" + id;
    }

    private void customItemColumn(Map<CustomColumn, String> customColumnStringMap, Map<String, String[]> parameterMap) {
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
    }

    private String saveItemImage(MultipartHttpServletRequest file, Item savedItem, String imageUrl) {
        Iterator<String> iterator = file.getFileNames();
        MultiValueMap<String, MultipartFile> multiFileMap = file.getMultiFileMap();
        MultipartFile multipartFile = null;
        while (iterator.hasNext()) {
            multipartFile = file.getFile(iterator.next());
        }

        if (multipartFile != null && !multipartFile.isEmpty()) {
            imageUrl = cloudForImage.uploadImageToCloud(multipartFile);
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
        return imageUrl;
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

    @Override
    public List<ItemProjection> getItemByTagId(Integer tagId) {
        return itemRepository.getAllItemByTagId(tagId);
    }

    @Override
    public int getTotalItemsByUserId() {
        User user = userService.currenUser();
        if (user != null) {
            return itemRepository.getTotalItemsByUserId(user.getId());
        }
        return 0;
    }

    @Override
    public Item findById(Integer itemId) {
        if (itemRepository.findById(itemId).isPresent()) {
            return itemRepository.findById(itemId).get();
        }
        return null;
    }

    @Override
    public List<CustomColumnDto> getCustomColumnWithValue(Integer collectionId, Integer itemId) {

        List<CustomColumnDto> columnDtoList = new ArrayList<>();
        Item item = itemRepository.findById(itemId).get();

        List<Integer> customColumns = collectionItemColumnRepository.collectCustomColumnId(collectionId);

        for (Integer customColumn : customColumns) {
            if (customColumnRepository.findById(customColumn).isPresent()) {
                CustomColumn customColumnFound = customColumnRepository.findById(customColumn).get();
                CustomValue customValue = customValueRepository.findByCustomColumnAndItem(customColumnFound, item);
                if (customValue != null) {
                    columnDtoList.add(new CustomColumnDto(
                            customColumnFound.getId(),
                            customColumnFound.getName(),
                            customColumnFound.getType().name(),
                            customValue.getValue()
                    ));
                }
            }
        }
        return columnDtoList;
    }

    @Transactional
    @Override
    public String editItem(MultipartHttpServletRequest file, HttpServletRequest request, Integer id, RedirectAttributes ra, Integer itemId) {
        String status = "error";
        String message = "Editing error";
        String name = request.getParameter("name");
        String[] tagsIds = request.getParameterValues("tagsId");

        List<Integer> tagsID = new ArrayList<>();

        for (String s : tagsIds) tagsID.add(Integer.valueOf(s));

        if (collectionRepository.findById(id).isPresent()) {
            try {
                Collection collection = collectionRepository.findById(id).get();
                List<Tag> tags = tagRepository.findAllById(tagsID);

                if (itemRepository.findById(itemId).isPresent()) {

                    Item item = itemRepository.findById(itemId).get();
                    item.setName(name);
                    item.setTags(tags);
                    Item savedItem = itemRepository.save(item);

                    String imageUrl = null;
                    imageUrl = itemImage(file, savedItem, imageUrl);

                    Map<CustomColumn, String> customColumnStringMap = new HashMap<>();

                    Map<String, String[]> parameterMap = request.getParameterMap();
                    List<CustomValue> customValueList = new ArrayList<>();

                    customColumnMap(savedItem, imageUrl, customColumnStringMap, parameterMap, customValueList);

                    deleteCustomColumnValue(customValueList);

                    saveCustomColumnValue(savedItem, customColumnStringMap);

                    status = "success";
                    message = "Successfully Updated";
                }

            } catch (Exception e) {
            }
        }


        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message", message);
        User user = userService.currenUser();
        if (user.getRole().equals(Role.ROLE_ADMIN) || user.getRole().equals(Role.ROLE_SUPER_ADMIN))
            return "redirect:/admin/collection/view/" + id;
        return "redirect:/user/collection/view/" + id;
    }

    private void deleteCustomColumnValue(List<CustomValue> customValueList) {
        for (CustomValue customValue : customValueList) {
            customValueRepository.delete(customValue);
        }
    }

    private void saveCustomColumnValue(Item savedItem, Map<CustomColumn, String> customColumnStringMap) {
        for (Map.Entry<CustomColumn, String> entry : customColumnStringMap.entrySet()) {
            CustomValue customValue = new CustomValue(
                    entry.getValue(),
                    entry.getKey(),
                    savedItem
            );
            customValueRepository.save(customValue);
        }
    }

    private String itemImage(MultipartHttpServletRequest file, Item savedItem, String imageUrl) {
        Iterator<String> iterator = file.getFileNames();
        MultiValueMap<String, MultipartFile> multiFileMap = file.getMultiFileMap();
        MultipartFile multipartFile = null;
        while (iterator.hasNext()) {
            multipartFile = file.getFile(iterator.next());
        }

        if (multipartFile != null && !multipartFile.isEmpty()) {
            imageUrl = cloudForImage.uploadImageToCloud(multipartFile);
            Iterator<String> mapIterator = multiFileMap.keySet().iterator();
            String columnId = "";
            while (mapIterator.hasNext()) {
                columnId = mapIterator.next();
            }

            CustomColumn customColumn = customColumnRepository.findById(Integer.valueOf(columnId)).get();
            if (customColumn != null && imageUrl != null) {
                CustomValue updateCustomValue = customValueRepository.findByCustomColumnAndItem(customColumn, savedItem);
                updateCustomValue.setValue(imageUrl);
                customValueRepository.save(updateCustomValue);
            }
        }
        return imageUrl;
    }

    private void customColumnMap(Item savedItem, String imageUrl, Map<CustomColumn, String> customColumnStringMap, Map<String, String[]> parameterMap, List<CustomValue> customValueList) {
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
                        CustomValue byCustomColumnAndItem = customValueRepository.findByCustomColumnAndItem(customColumn, savedItem);
                        if (imageUrl == null && customColumn.getType().equals(CustomColumnDataType.image)) {
                        } else {
                            customValueList.add(byCustomColumnAndItem);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Transactional
    @Override
    public String deleteItemById(Integer collectionId, Integer itemId, RedirectAttributes ra) {
        String status = "error";
        String message = "Deleting error";

        if (itemRepository.existsById(itemId)) {
            try {
                itemRepository.deleteById(itemId);
                status = "success";
                message = "Successfully Deleted";
            } catch (Exception e) {
            }
        }

        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message", message);

        User user = userService.currenUser();
        if (user.getRole().equals(Role.ROLE_ADMIN) || user.getRole().equals(Role.ROLE_SUPER_ADMIN))
            return "redirect:/admin/collection/view/" + collectionId;
        return "redirect:/user/collection/view/" + collectionId;
    }



}

