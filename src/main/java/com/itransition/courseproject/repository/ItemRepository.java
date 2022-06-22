package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Item;
import com.itransition.courseproject.projection.ItemProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Integer> {


    @Query(nativeQuery = true,
    value = "select i.id,\n" +
            "       i.name,\n" +
            "       concat(u.first_name,' ',u.last_name) as authorFullName,\n" +
            "        coalesce(cte.value,'null') as imageUrl\n" +
            "from  items i\n" +
            "join collections c on c.id = i.collection_id\n" +
            "join user_collection uc on uc.id = c.user_collection_id\n" +
            "join users u on uc.user_id = u.id\n" +
            "left join  (\n" +
            "    select cc.id,cv.item_id,cv.value,cc.type\n" +
            "    from custom_column cc\n" +
            "             join custom_value cv on cc.id = cv.custom_column_id\n" +
            "    where cc.type='image'\n" +
            ")cte on cte.item_id=i.id\n" +
            "where i.collection_id= :id ")
    List<ItemProjection> getAllItemsByCollectionId(int id);


    @Query(nativeQuery = true,
    value = "select i.id,\n" +
            "       i.name,\n" +
            "       concat(u.first_name,' ',u.last_name) as authorFullName,\n" +
            "       c.name as collection,\n" +
            "       coalesce(cte.value,'null') as imageUrl\n" +
            "from  items i\n" +
            "          join collections c on c.id = i.collection_id\n" +
            "          join user_collection uc on uc.id = c.user_collection_id\n" +
            "          join users u on uc.user_id = u.id\n" +
            "          left join  (\n" +
            "    select cc.id,cv.item_id,cv.value,cc.type\n" +
            "    from custom_column cc\n" +
            "             join custom_value cv on cc.id = cv.custom_column_id\n" +
            "    where cc.type='image'\n" +
            ")cte on cte.item_id=i.id\n" +
            "order by i.created_at desc\n" +
            "limit 4 ")
    List<ItemProjection> getLatestItems();

}
