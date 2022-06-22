package com.itransition.courseproject.repository;

import com.itransition.courseproject.entity.collection.Item;
import com.itransition.courseproject.projection.ItemDetailProjection;
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


    @Query(nativeQuery = true,
    value = "select i.id,\n" +
            "       i.name as itemName,\n" +
            "       c.name as itemCollection,\n" +
            "       concat(u.first_name,' ',u.last_name) as authorFullName,\n" +
            "       coalesce(cte.value,'null') as itemImageUrl,\n" +
            "       json_agg(itemTags.*) as itemTag,\n" +
            "       json_agg(distinct customColumn.*) as itemCustomColumn\n" +
            "from items i\n" +
            "join collections c on c.id = i.collection_id\n" +
            "join user_collection uc on uc.id = c.user_collection_id\n" +
            "join users u on u.id = uc.user_id\n" +
            "left join  (\n" +
            "    select cc.id,cv.item_id,cv.value,cc.type\n" +
            "    from custom_column cc\n" +
            "             join custom_value cv on cc.id = cv.custom_column_id\n" +
            "    where cc.type='image'\n" +
            ")cte on cte.item_id=i.id\n" +
            "join (\n" +
            "    select tags.id,tags.name,it.item_id as itemId\n" +
            "    from tags\n" +
            "    join item_tags it on tags.id = it.tag_id\n" +
            ")itemTags on itemTags.itemId=i.id\n" +
            "left join(\n" +
            "    select cv.id,cc2.name,cv.value,cv.item_id as itemId\n" +
            "    from custom_value cv\n" +
            "    join custom_column cc2 on cv.custom_column_id = cc2.id\n" +
            "    where cc2.type<>'image'\n" +
            ")customColumn on customColumn.itemId=i.id\n" +
            "where i.id= :id \n" +
            "group by i.id, i.name, c.name, concat(u.first_name,' ',u.last_name), coalesce(cte.value,'null');")
    List<ItemDetailProjection> getItemById(int id);

}
