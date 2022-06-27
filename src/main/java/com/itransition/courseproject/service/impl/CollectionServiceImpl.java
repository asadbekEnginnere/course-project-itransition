package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/18/22 12:53 AM


import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itransition.courseproject.cloudinary.CloudForImage;
import com.itransition.courseproject.dto.CollectionDto;
import com.itransition.courseproject.entity.collection.Collection;
import com.itransition.courseproject.entity.collection.CollectionItemColumn;
import com.itransition.courseproject.entity.collection.CustomColumn;
import com.itransition.courseproject.entity.collection.Topic;
import com.itransition.courseproject.entity.enums.CustomColumnDataType;
import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.entity.user.UserCollection;
import com.itransition.courseproject.projection.CollectionProjection;
import com.itransition.courseproject.repository.*;
import com.itransition.courseproject.service.interfaces.CollectionService;
import com.itransition.courseproject.service.interfaces.GenericInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CollectionServiceImpl implements CollectionService, GenericInterface<CollectionDto, Integer, String> {

    private final CollectionRepository collectionRepository;
    private final CollectionItemColumnRepository collectionItemColumn;
    private final CustomColumnRepository customColumnRepository;
    private final Cloudinary cloudinary;
    private final TopicRepository topicRepository;
    private final UserCollectionRepository userCollectionRepository;
    private final UserServiceImpl userService;
    private final CloudForImage cloudForImage;

    @Transactional
    @Override
    public String saveCollectionWithItemField(MultipartFile file, HttpServletRequest request, RedirectAttributes ra) {

        String message = "Collection creating error";
        String status = "error";

        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            Integer topicId = Integer.valueOf(request.getParameter("topic"));

            UserCollection userCollection = null;
            if (userCollectionRepository.findByUser(userService.currenUser()) != null) {
                userCollection = userCollectionRepository.findByUser(userService.currenUser());
            } else {
                userCollection = userCollectionRepository.save(new UserCollection(userService.currenUser()));
            }

            if (topicRepository.findById(topicId).isPresent()) {

                String imageUrl = null;
                if (file!=null && !file.isEmpty()) {
                    imageUrl = cloudForImage.uploadImageToCloud(file);
                }

                Topic topic = topicRepository.findById(topicId).get();
                Collection savedCollection = saveCollection(name, description, userCollection, imageUrl, topic);

                Map<String, String[]> columnAndType = request.getParameterMap();
                List<String> col = new ArrayList<>(Arrays.asList("name", "description", "topic"));

                Map<String[], String[]> columnType = addColumnKeyValue(columnAndType, col);
                List<CustomColumn> customColumns = new ArrayList<>();

                customColumn(columnType, customColumns);
                saveCustomColumn(savedCollection, customColumns);

                message = "Successfully created";
                status = "success";
            }

        } catch (Exception e) {
        }

        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message", message);
        return "redirect:/user/collection";
    }

    private Map<String[], String[]> addColumnKeyValue(Map<String, String[]> columnAndType, List<String> col) {
        Map<String[], String[]> columnType = new HashMap<>();

        List<String> keys = new ArrayList<>(columnAndType.keySet());
        keys.removeAll(col);
        for (int i = 0; i < keys.size() - 1; i++) {
            columnType.put(columnAndType.get(keys.get(i)), columnAndType.get(keys.get(i + 1)));
            i++;
        }
        return columnType;
    }

    private void saveCustomColumn(Collection savedCollection, List<CustomColumn> customColumns) {
        for (CustomColumn customColumn : customColumns) {
            collectionItemColumn.save(new CollectionItemColumn(savedCollection, customColumn));
        }
    }

    private Collection saveCollection(String name, String description, UserCollection userCollection, String imageUrl, Topic topic) {
        Collection collection = new Collection(
                name,
                description,
                imageUrl,
                userCollection,
                topic
        );

        Collection savedCollection = collectionRepository.save(collection);
        return savedCollection;
    }

    private void customColumn(Map<String[], String[]> columnType, List<CustomColumn> customColumns) {
        for (Map.Entry<String[], String[]> entry : columnType.entrySet()) {
            String column = "";
            for (String s : entry.getKey()) {
                column = s;
            }
            column = column.trim();

            String type = "";
            for (String s : entry.getValue()) {
                type = s;
            }

            CustomColumn byName = customColumnRepository.findByName(column);

            if (byName != null && byName.getType().name().equals(type)) {
                log.error("Found column : " + byName.toString());
                customColumns.add(byName);
            } else {
                log.error("Column not found : ");
                CustomColumn savedColumn = customColumnRepository.save(new CustomColumn(column, CustomColumnDataType.valueOf(type)));
                customColumns.add(savedColumn);
            }
        }
    }

    @Override
    public List<CollectionProjection> getTopFiveLargestCollection() {
        return collectionRepository.getTopFiveLargestCollection();
    }

    @Override
    public List<CollectionProjection> getCollectionList() {
        return collectionRepository.getCollectionList();
    }

    @Override
    public CollectionDto getCollectionById(int id) {
        String result = collectionRepository.collectionWithCustomColumns(id);

        ObjectMapper mapper = new ObjectMapper();
        try {
            CollectionDto collectionDto = mapper.readValue(result, CollectionDto.class);
            System.out.println("collection : " + collectionDto);
            return collectionDto;
        } catch (Exception e) {
        }

        return new CollectionDto();
    }

    @Transactional
    @Override
    public String updateCollection(CollectionDto collectionDto, RedirectAttributes ra, MultipartFile file) {

        String status = "error";
        String message = "Updating error";


        if (collectionRepository.findById(collectionDto.getId()).isPresent() && topicRepository.findById(collectionDto.getTopicId()).isPresent()) {
            try{
                Topic topic = topicRepository.findById(collectionDto.getTopicId()).get();
                String imageUrl = null;
                if (file!=null && !file.isEmpty()) {
                    imageUrl = cloudForImage.uploadImageToCloud(file);
                }
                Collection collection = collectionRepository.findById(collectionDto.getId()).get();
                if (imageUrl!=null) collection.setImageUrl(imageUrl);
                collection.setName(collectionDto.getName());
                collection.setDescription(collection.getDescription());
                collection.setTopic(topic);
                collectionRepository.save(collection);
                status="success";
                message="Successfully Updated";
            }catch (Exception e){}
        }

        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message", message);
        return "redirect:/user/collection";
    }

    @Override
    public List<CollectionDto> getAllData() {
        Integer id = userService.currenUser().getId();
        List<String> allCollection = collectionRepository.getAllCollection(id);
        List<CollectionDto> collectionDtoList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            for (String s : allCollection) {
                CollectionDto collectionDto = mapper.readValue(s, CollectionDto.class);
                System.out.println(collectionDto);
                collectionDtoList.add(collectionDto);
            }
        } catch (Exception e) {
        }
        return collectionDtoList;
    }

    @Override
    public Page<CollectionDto> getAllDataByPage(Integer page, Integer size) {
        // TODO: 6/21/22
        return null;
    }

    @Override
    public CollectionDto findById(Integer id) {
        return collectionRepository.getCollection(id);
    }

    public CollectionProjection collectionGetById(Integer id) {
        return collectionRepository.collectionById(id);
    }

    @Override
    public String saveData(CollectionDto collectionDto, RedirectAttributes ra) {
        // TODO: 6/21/22 Done
        return null;
    }

    @Override
    public String updateData(CollectionDto collectionDto, RedirectAttributes ra) {
        return null;
    }

    @Transactional
    @Override
    public String deleteById(Integer id, RedirectAttributes ra) {
        String status = "error";
        String message = "Deleting error";

        if (collectionRepository.existsById(id)) {
            try {
                collectionRepository.deleteById(id);
                status = "success";
                message = "Successfully Deleted";
            } catch (Exception e) {
            }
        }

        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message", message);
        return "redirect:/user/collection";
    }

    public int getTotalCollectionsByUserId() {
        User user = userService.currenUser();
        if (user != null) {
            return collectionRepository.getTotalCollectionsByUserId(user.getId());
        }
        return 0;
    }
}
