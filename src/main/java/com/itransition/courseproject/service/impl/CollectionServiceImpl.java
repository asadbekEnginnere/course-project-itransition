package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/18/22 12:53 AM


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.itransition.courseproject.dto.CollectionDto;
import com.itransition.courseproject.entity.collection.Collection;
import com.itransition.courseproject.entity.collection.CollectionItemColumn;
import com.itransition.courseproject.entity.collection.CustomColumn;
import com.itransition.courseproject.entity.collection.Topic;
import com.itransition.courseproject.entity.enums.CustomColumnDataType;
import com.itransition.courseproject.entity.user.UserCollection;
import com.itransition.courseproject.repository.*;
import com.itransition.courseproject.service.interfaces.CollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionItemColumnRepository collectionItemColumn;
    private final CustomColumnRepository customColumnRepository;
    private final Cloudinary cloudinary;
    private final TopicRepository topicRepository;
    private final UserCollectionRepository userCollectionRepository;
    private final UserServiceImpl userService;

    @Override
    public List<CollectionDto> getAllCollection() {
        return collectionRepository.getAllCollection();
    }

    @Override
    public String saveCollectionWithItemField(MultipartFile file, HttpServletRequest request, RedirectAttributes ra) {

        String message="Collection creating error";
        String status="error";

        try {

            String name = request.getParameter("name");
            String description = request.getParameter("description");
            Integer topicId = Integer.valueOf(request.getParameter("topic"));

            UserCollection userCollection=null;
            if (userCollectionRepository.findByUser(userService.currenUser())!=null) {
                userCollection=userCollectionRepository.findByUser(userService.currenUser());
            }else{
                userCollection=userCollectionRepository.save(new UserCollection(userService.currenUser()));
            }

            if (topicRepository.findById(topicId).isPresent()) {

                File fileForServer = convertMultiPartToFile(file);
                Map uploadResult = cloudinary.uploader().upload(fileForServer, ObjectUtils.emptyMap());

                String imageUrl = (String) uploadResult.get("url");
                System.out.println("Url : "+imageUrl);

                Topic topic = topicRepository.findById(topicId).get();
                Collection collection = new Collection(
                        name,
                        description,
                        imageUrl,
                        userCollection,
                        topic
                );

                Collection savedCollection = collectionRepository.save(collection);

                Map<String, String[]> columnAndType = request.getParameterMap();
                List<String> col = new ArrayList<>(Arrays.asList("name","description","topic"));

                Map<String[],String[]> columnType = new HashMap<>();

                List<String> keys = new ArrayList<>(columnAndType.keySet());
                keys.removeAll(col);
                for (int i = 0; i < keys.size()-1; i++) {
                    columnType.put(columnAndType.get(keys.get(i)),columnAndType.get(keys.get(i+1)));
                    i++;
                }

                List<CustomColumn> customColumns = new ArrayList<>();

                for(Map.Entry<String[], String[]> entry:columnType.entrySet()) {
                    String column = "";
                    for (String s : entry.getKey()) {
                        column+=s;
                    }

                    String type = "";
                    for (String s : entry.getValue()) {
                        type+=s;
                    }

                    if (customColumnRepository.findByName(column)!=null && customColumnRepository.findByName(column).getType().equals(type)) {
                        customColumns.add(customColumnRepository.findByName(name));
                    }else{
                        CustomColumn savedColumn = customColumnRepository.save(new CustomColumn(column, CustomColumnDataType.valueOf(type)));
                        customColumns.add(savedColumn);
                    }
                    log.info("key: "+entry.getValue()+" value: "+entry.getValue());
                }



                for (CustomColumn customColumn : customColumns) {
                    collectionItemColumn.save(new CollectionItemColumn(savedCollection,customColumn));
                }
                message="Successfully created";
                status="success";
            }

        }catch (Exception e){}

        ra.addFlashAttribute("status", status);
        ra.addFlashAttribute("message",message);
        return "redirect:/user/collection";
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File("src/main/resources/file/"+file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
